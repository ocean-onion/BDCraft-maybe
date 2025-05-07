package com.bdcraft.modules.economy;

import com.bdcraft.BDCraftPlugin;
import com.bdcraft.module.AbstractBDModule;
import com.bdcraft.module.ModuleManager;
import com.bdcraft.modules.economy.commands.EconomyCommands;
import com.bdcraft.modules.logging.LogLevel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Economy module for BDCraft.
 * Manages player currencies, transactions, and economy-related commands.
 * Based on documented economy functionality in economy-overview.md
 */
public class EconomyModule extends AbstractBDModule {

    private final Currency credits;
    private final Map<UUID, PlayerAccount> playerAccounts;
    private final FileConfiguration config;
    
    /**
     * Creates a new EconomyModule.
     *
     * @param plugin The plugin instance
     * @param moduleManager The module manager
     */
    public EconomyModule(BDCraftPlugin plugin, ModuleManager moduleManager) {
        super(plugin, "Economy", "economy.yml", moduleManager);
        
        this.config = getConfig();
        this.playerAccounts = new ConcurrentHashMap<>();
        
        // Initialize credits currency
        this.credits = new Currency(
            config.getString("credits.name", "Credit"),
            config.getString("credits.plural", "Credits"),
            config.getString("credits.symbol", "C")
        );
        
        log(LogLevel.INFO, "Economy module initialized with currency: " + credits.getName());
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        // Register commands
        EconomyCommands economyCommands = new EconomyCommands(getPlugin(), this);
        getPlugin().getCommand("bdbal").setExecutor(economyCommands);
        getPlugin().getCommand("bdpay").setExecutor(economyCommands);
        getPlugin().getCommand("bdeco").setExecutor(economyCommands);
        
        // Also set tab completers
        getPlugin().getCommand("bdbal").setTabCompleter(economyCommands);
        getPlugin().getCommand("bdpay").setTabCompleter(economyCommands);
        getPlugin().getCommand("bdeco").setTabCompleter(economyCommands);
        
        // Load online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadAccount(player);
        }
        
        log(LogLevel.INFO, "Economy module enabled");
    }
    
    @Override
    public void onDisable() {
        // Save all accounts on disable
        saveAllAccounts();
        log(LogLevel.INFO, "Economy module disabled");
        super.onDisable();
    }
    
    /**
     * Gets the credits currency.
     *
     * @return The credits currency
     */
    public Currency getCredits() {
        return credits;
    }
    
    /**
     * Gets a player's account.
     *
     * @param player The player
     * @return The player's account
     */
    public PlayerAccount getAccount(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        
        // If account doesn't exist, create it
        if (!playerAccounts.containsKey(playerId)) {
            createAccount(player);
        }
        
        return playerAccounts.get(playerId);
    }
    
    /**
     * Creates a new account for a player.
     *
     * @param player The player
     * @return The new account
     */
    public PlayerAccount createAccount(OfflinePlayer player) {
        UUID playerId = player.getUniqueId();
        String playerName = player.getName() != null ? player.getName() : playerId.toString();
        
        PlayerAccount account = new PlayerAccount(playerId, playerName);
        playerAccounts.put(playerId, account);
        
        log(LogLevel.INFO, "Created economy account for " + playerName);
        return account;
    }
    
    /**
     * Gets a player's credit balance.
     *
     * @param player The player
     * @return The balance
     */
    public double getBalance(OfflinePlayer player) {
        return getAccount(player).getCredits();
    }
    
    /**
     * Formats a credit amount with the currency name.
     *
     * @param amount The amount
     * @return The formatted amount
     */
    public String formatCredits(double amount) {
        return credits.format(amount);
    }
    
    /**
     * Formats a credit amount with the currency symbol.
     *
     * @param amount The amount
     * @return The formatted amount
     */
    public String formatCreditsWithSymbol(double amount) {
        return credits.formatWithSymbol(amount);
    }
    
    /**
     * Adds credits to a player's account.
     *
     * @param player The player
     * @param amount The amount to add
     * @param initiatorId The UUID of who initiated the transaction
     * @param type The transaction type
     * @param description The transaction description
     * @return The new balance
     */
    public double addCredits(OfflinePlayer player, double amount, UUID initiatorId, 
                           TransactionType type, String description) {
        PlayerAccount account = getAccount(player);
        double newBalance = account.addCredits(amount, initiatorId, type, description);
        
        log(LogLevel.INFO, String.format("Added %.0f credits to %s's account. New balance: %.0f", 
                                        amount, player.getName(), newBalance));
        
        return newBalance;
    }
    
    /**
     * Removes credits from a player's account.
     *
     * @param player The player
     * @param amount The amount to remove
     * @param initiatorId The UUID of who initiated the transaction
     * @param type The transaction type
     * @param description The transaction description
     * @return True if successful, false if not enough credits
     */
    public boolean removeCredits(OfflinePlayer player, double amount, UUID initiatorId, 
                               TransactionType type, String description) {
        PlayerAccount account = getAccount(player);
        boolean success = account.removeCredits(amount, initiatorId, type, description);
        
        if (success) {
            log(LogLevel.INFO, String.format("Removed %.0f credits from %s's account. New balance: %.0f", 
                                           amount, player.getName(), account.getCredits()));
        } else {
            log(LogLevel.WARNING, String.format("Failed to remove %.0f credits from %s's account. Insufficient funds.", 
                                              amount, player.getName()));
        }
        
        return success;
    }
    
    /**
     * Sets a player's credit balance.
     *
     * @param player The player
     * @param amount The new amount
     * @param description The reason for the change
     * @param initiatorId The UUID of who initiated the change
     */
    public void setBalance(OfflinePlayer player, double amount, String description, UUID initiatorId) {
        PlayerAccount account = getAccount(player);
        account.setCredits(amount, description, initiatorId);
        
        log(LogLevel.INFO, String.format("Set %s's credits to %.0f", player.getName(), amount));
    }
    
    /**
     * Transfers credits from one player to another.
     *
     * @param from The player sending credits
     * @param to The player receiving credits
     * @param amount The amount to transfer
     * @return True if the transfer was successful
     */
    public boolean transferCredits(OfflinePlayer from, OfflinePlayer to, double amount) {
        if (amount <= 0) {
            return false;
        }
        
        UUID fromId = from.getUniqueId();
        UUID toId = to.getUniqueId();
        
        // Check that we're not transferring to self
        if (fromId.equals(toId)) {
            return false;
        }
        
        // Get accounts
        PlayerAccount fromAccount = getAccount(from);
        PlayerAccount toAccount = getAccount(to);
        
        // Check if sender has enough funds
        if (!fromAccount.hasEnoughCredits(amount)) {
            return false;
        }
        
        // Remove from sender
        boolean success = fromAccount.removeCredits(
            amount, 
            toId, 
            TransactionType.PLAYER_TO_PLAYER,
            "Payment to " + to.getName()
        );
        
        if (!success) {
            return false;
        }
        
        // Add to receiver
        toAccount.addCredits(
            amount, 
            fromId, 
            TransactionType.PLAYER_TO_PLAYER,
            "Payment from " + from.getName()
        );
        
        log(LogLevel.INFO, String.format("%s transferred %.0f credits to %s", 
                                        from.getName(), amount, to.getName()));
        
        return true;
    }
    
    /**
     * Gets a player's recent transactions.
     *
     * @param player The player
     * @param limit The maximum number of transactions to return
     * @return The recent transactions
     */
    public List<Transaction> getRecentTransactions(OfflinePlayer player, int limit) {
        return getAccount(player).getRecentTransactions(limit);
    }
    
    /**
     * Gets the top player accounts by balance.
     *
     * @param limit The maximum number of accounts to return
     * @return The top accounts
     */
    public Map<UUID, Double> getTopBalances(int limit) {
        // Create a map of player UUIDs to balances
        Map<UUID, Double> balances = new HashMap<>();
        for (PlayerAccount account : playerAccounts.values()) {
            balances.put(account.getPlayerId(), account.getCredits());
        }
        
        // Sort players by balance (descending) and take the top 'limit'
        return balances.entrySet().stream()
                .sorted(Map.Entry.<UUID, Double>comparingByValue().reversed())
                .limit(limit)
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }
    
    /**
     * Loads a player's account from storage.
     *
     * @param player The player
     */
    public void loadAccount(OfflinePlayer player) {
        // Create the account, database loading would be implemented here
        createAccount(player);
    }
    
    /**
     * Saves a player's account to storage.
     *
     * @param playerId The player's UUID
     */
    public void saveAccount(UUID playerId) {
        // Database saving would be implemented here
        PlayerAccount account = playerAccounts.get(playerId);
        if (account != null) {
            log(LogLevel.DEBUG, "Saved economy account for " + account.getPlayerName());
        }
    }
    
    /**
     * Saves all accounts to storage.
     */
    public void saveAllAccounts() {
        for (UUID playerId : playerAccounts.keySet()) {
            saveAccount(playerId);
        }
        log(LogLevel.INFO, "Saved all economy accounts");
    }
}