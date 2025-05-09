package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.progression.rebirth.BDRebirthManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Module for player progression including ranks and rebirth.
 */
public class BDProgressionModule extends BDModule {
    private final BDCraft plugin;
    private File dataFile;
    private FileConfiguration data;
    
    // Player ranks and rebirth data
    private final Map<UUID, Integer> playerRanks;
    private final Map<UUID, Integer> playerRebirthLevels;
    
    // Rebirth manager
    private BDRebirthManager rebirthManager;
    
    // Rank names
    private final String[] rankNames = {
        "Newcomer",
        "Farmer",
        "Expert Farmer",
        "Master Farmer",
        "Agricultural Expert"
    };
    
    /**
     * Creates a new progression module.
     *
     * @param plugin The plugin instance
     */
    public BDProgressionModule(BDCraft plugin) {
        super(plugin, "progression");
        this.plugin = plugin;
        this.playerRanks = new HashMap<>();
        this.playerRebirthLevels = new HashMap<>();
    }
    
    @Override
    public void onEnable() {
        // Load data
        loadData();
        
        // Initialize rebirth manager
        rebirthManager = new BDRebirthManager(plugin);
        
        // Register commands
        // Will be implemented in the future
        
        // Register events
        // Will be implemented in the future
        
        plugin.getLogger().info("Progression module enabled.");
    }
    
    @Override
    public void onDisable() {
        // Save data
        saveData();
        
        plugin.getLogger().info("Progression module disabled.");
    }
    
    /**
     * Loads player progression data.
     */
    private void loadData() {
        // Create data directory if it doesn't exist
        File dataDir = new File(plugin.getDataFolder(), "data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Create or load data file
        dataFile = new File(dataDir, "progression.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create progression data file", e);
            }
        }
        
        // Load data
        data = YamlConfiguration.loadConfiguration(dataFile);
        
        // Load player ranks
        if (data.contains("ranks")) {
            for (String uuidString : data.getConfigurationSection("ranks").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    int rank = data.getInt("ranks." + uuidString);
                    playerRanks.put(uuid, rank);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in progression data: " + uuidString);
                }
            }
        }
        
        // Load player rebirth levels
        if (data.contains("rebirth")) {
            for (String uuidString : data.getConfigurationSection("rebirth").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    int level = data.getInt("rebirth." + uuidString);
                    playerRebirthLevels.put(uuid, level);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in progression data: " + uuidString);
                }
            }
        }
        
        plugin.getLogger().info("Loaded progression data for " + playerRanks.size() + " players.");
    }
    
    /**
     * Saves player progression data.
     */
    private void saveData() {
        if (data == null || dataFile == null) {
            return;
        }
        
        // Save player ranks
        for (Map.Entry<UUID, Integer> entry : playerRanks.entrySet()) {
            data.set("ranks." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save player rebirth levels
        for (Map.Entry<UUID, Integer> entry : playerRebirthLevels.entrySet()) {
            data.set("rebirth." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save data file
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save progression data", e);
        }
    }
    
    /**
     * Gets a player's rank.
     *
     * @param player The player
     * @return The player's rank (0-4)
     */
    public int getPlayerRank(Player player) {
        return playerRanks.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Gets a player's rank name.
     *
     * @param player The player
     * @return The player's rank name
     */
    public String getPlayerRankName(Player player) {
        int rank = getPlayerRank(player);
        if (rank >= 0 && rank < rankNames.length) {
            return rankNames[rank];
        }
        return "Unknown";
    }
    
    /**
     * Sets a player's rank.
     *
     * @param player The player
     * @param rank The rank (0-4)
     */
    public void setPlayerRank(Player player, int rank) {
        if (rank < 0 || rank >= rankNames.length) {
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
        
        playerRanks.put(player.getUniqueId(), rank);
        player.sendMessage(ChatColor.GREEN + "Your rank is now: " + ChatColor.GOLD + rankNames[rank]);
        
        // Save data
        saveData();
    }
    
    /**
     * Gets a player's rebirth level.
     *
     * @param player The player
     * @return The player's rebirth level
     */
    public int getPlayerRebirthLevel(Player player) {
        return playerRebirthLevels.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Sets a player's rebirth level.
     *
     * @param player The player
     * @param level The rebirth level
     */
    public void setPlayerRebirthLevel(Player player, int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Invalid rebirth level: " + level);
        }
        
        playerRebirthLevels.put(player.getUniqueId(), level);
        
        if (level > 0) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Your rebirth level is now: " + level);
            Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + 
                    ChatColor.LIGHT_PURPLE + " has reached rebirth level " + level + "!");
        }
        
        // Save data
        saveData();
    }
    
    /**
     * Checks if a player meets the requirements for rebirth.
     *
     * @param player The player
     * @return True if the player meets the requirements
     */
    public boolean canRebirth(Player player) {
        // Player must be at the highest rank
        if (getPlayerRank(player) < rankNames.length - 1) {
            return false;
        }
        
        // Additional requirements can be checked here, such as:
        // - Having enough currency
        // - Having completed a certain number of trades
        // - Having reached a certain level or stat
        
        return true;
    }
    
    /**
     * Performs rebirth for a player.
     *
     * @param player The player
     * @return True if rebirth was successful
     */
    public boolean rebirth(Player player) {
        if (!canRebirth(player)) {
            player.sendMessage(ChatColor.RED + "You do not meet the requirements for rebirth!");
            return false;
        }
        
        // Get current rebirth level
        int currentLevel = getPlayerRebirthLevel(player);
        
        // Increase rebirth level
        setPlayerRebirthLevel(player, currentLevel + 1);
        
        // Reset rank to Newcomer
        setPlayerRank(player, 0);
        
        // Additional rebirth effects can be applied here
        
        // Broadcast rebirth message
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + 
                ChatColor.LIGHT_PURPLE + " has been reborn! They are now at rebirth level " + 
                (currentLevel + 1) + "!");
        
        return true;
    }
    
    /**
     * Reloads progression data.
     */
    public void reloadData() {
        // Clear current data
        playerRanks.clear();
        playerRebirthLevels.clear();
        
        // Load data
        loadData();
        
        // Reload rebirth manager
        if (rebirthManager != null) {
            rebirthManager.reloadData();
        }
        
        plugin.getLogger().info("Reloaded progression data.");
    }
    
    /**
     * Gets the rebirth manager.
     *
     * @return The rebirth manager
     */
    public BDRebirthManager getRebirthManager() {
        return rebirthManager;
    }
    
    /**
     * Gets the rank manager.
     *
     * @return The rank manager
     */
    public BDRankManager getRankManager() {
        return rankManager;
    }
    
    /**
     * Performs a rebirth for the player.
     * 
     * @param player The player to rebirth
     * @return True if successful, false otherwise
     */
    public boolean performRebirth(Player player) {
        if (player == null) {
            return false;
        }
        
        // Check if player meets rebirth requirements
        if (!canPlayerRebirth(player)) {
            return false;
        }
        
        // Reset player rank to 1 (Newcomer)
        setPlayerRank(player, 1);
        
        // Increment rebirth level
        int currentRebirthLevel = getPlayerRebirthLevel(player);
        setPlayerRebirthLevel(player, currentRebirthLevel + 1);
        
        // Clear player stats
        resetPlayerStats(player);
        
        // Apply rebirth bonuses
        applyRebirthBonuses(player);
        
        // Fire event
        PlayerRebirthEvent event = new PlayerRebirthEvent(player, currentRebirthLevel + 1);
        Bukkit.getPluginManager().callEvent(event);
        
        // Save data
        saveData();
        
        return true;
    }
    
    /**
     * Checks if a player can rebirth.
     * 
     * @param player The player to check
     * @return True if the player can rebirth, false otherwise
     */
    private boolean canPlayerRebirth(Player player) {
        if (player == null) {
            return false;
        }
        
        // Check if player is at maximum rank (Agricultural Expert, rank 5)
        if (getPlayerRank(player) != 5) {
            return false;
        }
        
        // Check if player has enough currency (100,000)
        if (!plugin.getEconomyModule().hasCurrency(player.getUniqueId(), 100000)) {
            return false;
        }
        
        // Check if player has completed enough trades (500)
        int completedTrades = plugin.getEconomyModule().getCompletedTradesCount(player.getUniqueId());
        if (completedTrades < 500) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Resets player stats after rebirth.
     * 
     * @param player The player
     */
    private void resetPlayerStats(Player player) {
        if (player == null) {
            return;
        }
        
        // Reset player's currency (but preserve a small amount as a bonus)
        UUID playerId = player.getUniqueId();
        int currentCurrency = plugin.getEconomyModule().getCurrency(playerId);
        int preservedAmount = Math.min(currentCurrency, 1000);
        
        plugin.getEconomyModule().setCurrency(playerId, preservedAmount);
        
        // Reset tracked statistics
        plugin.getEconomyModule().resetTradeCount(playerId);
        
        // Other stat resets as needed
    }
    
    /**
     * Applies rebirth bonuses to a player.
     * 
     * @param player The player
     */
    private void applyRebirthBonuses(Player player) {
        if (player == null) {
            return;
        }
        
        int rebirthLevel = getPlayerRebirthLevel(player);
        
        // Unlock rebirth perks
        if (rebirthLevel >= 1) {
            // Unlock aura command
            if (!player.hasPermission("bdcraft.rebirth.aura")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), 
                        "lp user " + player.getName() + " permission set bdcraft.rebirth.aura true");
            }
        }
        
        if (rebirthLevel >= 2) {
            // Unlock blessing command
            if (!player.hasPermission("bdcraft.rebirth.bless")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), 
                        "lp user " + player.getName() + " permission set bdcraft.rebirth.bless true");
            }
        }
        
        if (rebirthLevel >= 3) {
            // Unlock touch command
            if (!player.hasPermission("bdcraft.rebirth.touch")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), 
                        "lp user " + player.getName() + " permission set bdcraft.rebirth.touch true");
            }
        }
        
        // Apply temporary rewards
        double bonusMultiplier = 1.0 + (rebirthLevel * 0.05); // 5% per rebirth level
        long duration = 24 * 60 * 60 * 1000; // 24 hours
        
        rebirthManager.setPlayerTradeBonus(player.getUniqueId(), bonusMultiplier, duration);
        
        // Send message
        player.sendMessage(ChatColor.LIGHT_PURPLE + "You have been reborn! You are now at rebirth level " + rebirthLevel + ".");
        player.sendMessage(ChatColor.YELLOW + "You have received special bonuses and abilities!");
        
        // Play effects
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.7f, 0.8f);
        
        // Spawn particles
        player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation().add(0, 1, 0), 
                100, 0.5, 1.0, 0.5, 0.2);
    }
}