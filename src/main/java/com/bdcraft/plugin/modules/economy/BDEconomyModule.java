package com.bdcraft.plugin.modules.economy;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.EconomyAPI;
import com.bdcraft.plugin.api.VillagerAPI;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.items.BDRecipeManager;
import com.bdcraft.plugin.modules.economy.listeners.TokenPlacementListener;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import com.bdcraft.plugin.modules.economy.villager.BDVillagerManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Module that provides economy functionality.
 */
public class BDEconomyModule implements BDModule, EconomyAPI {

    private final BDCraft plugin;
    private final Logger logger;
    private ConfigurationSection config;
    private Map<UUID, Double> balances;
    private String currencyName;
    private String currencySymbol;
    private int decimalPlaces;
    
    // Economy sub-systems
    private BDVillagerManager villagerManager;
    private BDItemManager itemManager;
    private BDRecipeManager recipeManager;
    private MarketManager marketManager;
    
    /**
     * Creates a new economy module.
     * @param plugin The plugin instance
     */
    public BDEconomyModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.balances = new HashMap<>();
    }
    
    @Override
    public void onEnable() {
        logger.info("Enabling Economy Module...");
        
        // Load configuration
        config = plugin.getConfigManager().getModuleConfig("economy");
        
        // Load economy settings
        loadEconomySettings();
        
        // Initialize sub-systems
        initializeSubSystems();
        
        // Initialize dependencies
        marketManager.initialize(villagerManager, itemManager);
        
        // Register API interfaces
        plugin.setEconomyAPI(this);
        plugin.setVillagerAPI(villagerManager);
        
        // Register commands
        registerCommands();
        
        // Register event listeners
        registerListeners();
        
        logger.info("Economy Module enabled!");
    }
    
    @Override
    public void onDisable() {
        logger.info("Disabling Economy Module...");
        
        // Unregister recipes
        if (recipeManager != null) {
            recipeManager.unregisterRecipes();
        }
        
        // Unregister event listeners
        HandlerList.unregisterAll(plugin);
        
        // Save data
        // saveData();
        
        logger.info("Economy Module disabled!");
    }
    
    @Override
    public void onReload() {
        logger.info("Reloading Economy Module...");
        
        // Reload configuration
        config = plugin.getConfigManager().getModuleConfig("economy");
        
        // Reload economy settings
        loadEconomySettings();
        
        logger.info("Economy Module reloaded!");
    }
    
    @Override
    public String getName() {
        return "economy";
    }
    
    @Override
    public List<String> getDependencies() {
        // No dependencies
        return Collections.emptyList();
    }
    
    /**
     * Loads economy settings from configuration.
     */
    private void loadEconomySettings() {
        currencyName = config.getString("currency.name", "BD");
        currencySymbol = config.getString("currency.symbol", "₿");
        decimalPlaces = config.getInt("currency.decimalPlaces", 2);
    }
    
    /**
     * Initializes the sub-systems of the economy module.
     */
    private void initializeSubSystems() {
        // Initialize villager manager
        villagerManager = new BDVillagerManager(plugin);
        
        // Initialize item manager
        itemManager = new BDItemManager(plugin);
        
        // Initialize recipe manager (depends on item manager)
        recipeManager = new BDRecipeManager(plugin, itemManager);
        
        // Initialize market manager
        marketManager = new MarketManager(plugin);
    }
    
    /**
     * Registers commands for this module.
     */
    private void registerCommands() {
        new CommandBase(plugin, "bdcurrency", "bdcraft.currency") {
            {
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "balance";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Shows your balance";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "[player]";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.currency.balance";
                    }
                    
                    @Override
                    public List<String> getAliases() {
                        return Arrays.asList("bal", "money");
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        if (args.length == 0) {
                            // Show own balance
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                                return true;
                            }
                            
                            Player player = (Player) sender;
                            double balance = getBalance(player.getUniqueId());
                            
                            String formattedBalance = formatCurrency(balance);
                            sender.sendMessage(ChatColor.GREEN + "Your balance: " + formattedBalance);
                            return true;
                        } else {
                            // Show other player's balance
                            if (!sender.hasPermission("bdcraft.currency.balance.others")) {
                                sender.sendMessage(ChatColor.RED + "You don't have permission to view other players' balances.");
                                return true;
                            }
                            
                            String playerName = args[0];
                            Player target = plugin.getServer().getPlayer(playerName);
                            
                            if (target == null) {
                                sender.sendMessage(ChatColor.RED + "Player not found.");
                                return true;
                            }
                            
                            double balance = getBalance(target.getUniqueId());
                            String formattedBalance = formatCurrency(balance);
                            
                            sender.sendMessage(ChatColor.GREEN + target.getName() + "'s balance: " + formattedBalance);
                            return true;
                        }
                    }
                });
                
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "pay";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Pays another player";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "<player> <amount>";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.currency.pay";
                    }
                    
                    @Override
                    public boolean isPlayerOnly() {
                        return true;
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "Usage: /bdcurrency pay <player> <amount>");
                            return true;
                        }
                        
                        Player player = (Player) sender;
                        String targetName = args[0];
                        Player target = plugin.getServer().getPlayer(targetName);
                        
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Player not found.");
                            return true;
                        }
                        
                        if (target.equals(player)) {
                            sender.sendMessage(ChatColor.RED + "You can't pay yourself.");
                            return true;
                        }
                        
                        double amount;
                        try {
                            amount = Double.parseDouble(args[1]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid amount.");
                            return true;
                        }
                        
                        // Check if amount is valid
                        double minTransaction = config.getDouble("currency.minTransaction", 0.01);
                        if (amount < minTransaction) {
                            sender.sendMessage(ChatColor.RED + "Amount must be at least " + formatCurrency(minTransaction));
                            return true;
                        }
                        
                        double balance = getBalance(player.getUniqueId());
                        if (balance < amount) {
                            sender.sendMessage(ChatColor.RED + "You don't have enough money.");
                            return true;
                        }
                        
                        // Process the payment
                        withdrawMoney(player.getUniqueId(), amount);
                        depositMoney(target.getUniqueId(), amount);
                        
                        String formattedAmount = formatCurrency(amount);
                        sender.sendMessage(ChatColor.GREEN + "You sent " + formattedAmount + " to " + target.getName() + ".");
                        target.sendMessage(ChatColor.GREEN + "You received " + formattedAmount + " from " + player.getName() + ".");
                        
                        return true;
                    }
                });
                
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "top";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Shows the richest players";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "[page]";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.currency.top";
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        // In a real implementation, this would load from database
                        // and show a paginated list of top balances
                        sender.sendMessage(ChatColor.YELLOW + "Top balances feature not yet implemented.");
                        return true;
                    }
                });
            }
            
            @Override
            protected void showHelp(CommandSender sender) {
                sender.sendMessage(ChatColor.GOLD + "=== " + ChatColor.YELLOW + "BDCurrency Help" + 
                        ChatColor.GOLD + " ===");
                
                sender.sendMessage(ChatColor.YELLOW + "/bdcurrency balance [player]" + ChatColor.GRAY + " - Shows balance");
                sender.sendMessage(ChatColor.YELLOW + "/bdcurrency pay <player> <amount>" + ChatColor.GRAY + " - Pays a player");
                sender.sendMessage(ChatColor.YELLOW + "/bdcurrency top [page]" + ChatColor.GRAY + " - Shows richest players");
            }
        };
    }
    
    // EconomyAPI Implementation
    
    @Override
    public double getBalance(UUID uuid) {
        return balances.getOrDefault(uuid, config.getDouble("currency.startingBalance", 0.0));
    }
    
    @Override
    public void setBalance(UUID uuid, double amount) {
        double maxBalance = config.getDouble("currency.maxBalance", 1000000000.0);
        amount = Math.min(amount, maxBalance);
        amount = Math.max(amount, 0);
        balances.put(uuid, amount);
    }
    
    @Override
    public boolean depositMoney(UUID uuid, double amount) {
        if (amount < 0) {
            return false;
        }
        
        double balance = getBalance(uuid);
        setBalance(uuid, balance + amount);
        return true;
    }
    
    @Override
    public boolean withdrawMoney(UUID uuid, double amount) {
        if (amount < 0) {
            return false;
        }
        
        double balance = getBalance(uuid);
        if (balance < amount) {
            return false;
        }
        
        setBalance(uuid, balance - amount);
        return true;
    }
    
    @Override
    public boolean hasEnough(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }
    
    @Override
    public String formatCurrency(double amount) {
        String format = "%." + decimalPlaces + "f";
        return currencySymbol + String.format(format, amount);
    }
    
    @Override
    public String getCurrencyName() {
        return currencyName;
    }
    
    @Override
    public String getCurrencyNamePlural() {
        return currencyName + "s";
    }
    
    @Override
    public String getCurrencySymbol() {
        return currencySymbol;
    }
    
    /**
     * Gets the villager manager.
     * @return The villager manager
     */
    public BDVillagerManager getVillagerManager() {
        return villagerManager;
    }
    
    /**
     * Gets the item manager.
     * @return The item manager
     */
    public BDItemManager getItemManager() {
        return itemManager;
    }
    
    /**
     * Gets the market manager.
     * @return The market manager
     */
    public MarketManager getMarketManager() {
        return marketManager;
    }
    
    /**
     * Gets the recipe manager.
     * @return The recipe manager
     */
    public BDRecipeManager getRecipeManager() {
        return recipeManager;
    }
    
    /**
     * Registers event listeners for this module.
     */
    private void registerListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();
        
        // Register the token placement listener
        pm.registerEvents(new TokenPlacementListener(plugin, this), plugin);
        
        logger.info("Economy event listeners registered");
    }
}