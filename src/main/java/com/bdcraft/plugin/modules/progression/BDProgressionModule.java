package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.ProgressionAPI;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.progression.rebirth.BDRebirthManager;
import com.bdcraft.plugin.modules.progression.BDRankManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.bdcraft.plugin.events.market.PlayerRebirthEvent;
import com.bdcraft.plugin.util.ParticleHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Module for player progression including ranks and rebirth.
 */
public class BDProgressionModule extends BDModule implements ProgressionAPI {
    private final BDCraft plugin;
    private File dataFile;
    private FileConfiguration data;
    
    // Player ranks and rebirth data
    private final Map<UUID, Integer> playerRanks;
    private final Map<UUID, Integer> playerRebirthLevels;
    private final Map<UUID, Integer> playerExperience;
    
    // Managers
    private BDRebirthManager rebirthManager;
    private BDRankManager rankManager;
    
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
        this.playerExperience = new HashMap<>();
    }
    
    @Override
    public void onEnable() {
        // Load data
        loadData();
        
        // Initialize managers
        rebirthManager = new BDRebirthManager(plugin);
        rankManager = new BDRankManager(plugin);
        
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
     * Implements ProgressionAPI.getPlayerRank
     */
    @Override
    public Rank getPlayerRank(UUID playerUuid) {
        int rankValue = playerRanks.getOrDefault(playerUuid, 0);
        switch (rankValue) {
            case 0: return Rank.NEWCOMER;
            case 1: return Rank.FARMER;
            case 2: return Rank.EXPERT_FARMER;
            case 3: return Rank.MASTER_FARMER;
            case 4: return Rank.AGRICULTURAL_EXPERT;
            default: return Rank.NEWCOMER;
        }
    }
    
    /**
     * Implements ProgressionAPI.getRankDisplayName
     */
    @Override
    public String getRankDisplayName(Rank rank) {
        String[] rankNames = {
            "Newcomer",
            "Farmer",
            "Expert Farmer",
            "Master Farmer",
            "Agricultural Expert"
        };
        return rankNames[rank.ordinal()];
    }
    
    /**
     * Implements ProgressionAPI.getPlayerRebirthLevel
     */
    @Override
    public int getPlayerRebirthLevel(UUID playerUuid) {
        return playerRebirthLevels.getOrDefault(playerUuid, 0);
    }
    
    /**
     * Implements ProgressionAPI.getPlayerExperience
     */
    @Override
    public int getPlayerExperience(UUID playerUuid) {
        return playerExperience.getOrDefault(playerUuid, 0);
    }
    
    /**
     * Implements ProgressionAPI.addPlayerExperience
     */
    @Override
    public int addPlayerExperience(UUID playerUuid, int amount) {
        int current = getPlayerExperience(playerUuid);
        int newTotal = current + amount;
        playerExperience.put(playerUuid, newTotal);
        
        // Check for rank up
        checkForRankUp(playerUuid);
        
        // Save data
        saveData();
        
        return newTotal;
    }
    
    /**
     * Implements ProgressionAPI.getNextRank
     */
    @Override
    public Rank getNextRank(UUID playerUuid) {
        int currentRank = playerRanks.getOrDefault(playerUuid, 0);
        
        // If player is at max rank, return null
        if (currentRank >= Rank.values().length - 1) {
            return null;
        }
        
        // Return the next rank
        return Rank.values()[currentRank + 1];
    }
    
    /**
     * Implements ProgressionAPI.getProgressPercentage
     */
    @Override
    public double getProgressPercentage(UUID playerUuid) {
        int exp = getPlayerExperience(playerUuid);
        int currentRank = playerRanks.getOrDefault(playerUuid, 0);
        
        // Experience thresholds for each rank
        int[] rankThresholds = {0, 100, 500, 2000, 10000};
        
        // If player is at max rank, return 100%
        if (currentRank >= rankThresholds.length - 1) {
            return 100.0;
        }
        
        // Calculate next rank threshold
        int nextRankThreshold = rankThresholds[currentRank + 1];
        int currentRankThreshold = rankThresholds[currentRank];
        
        // Calculate progress percentage
        double expNeeded = nextRankThreshold - currentRankThreshold;
        double expProgress = exp - currentRankThreshold;
        
        return Math.min(100.0, Math.max(0.0, (expProgress / expNeeded) * 100.0));
    }
    
    /**
     * Checks if a player has enough experience to rank up.
     *
     * @param playerUuid The player's UUID
     */
    private void checkForRankUp(UUID playerUuid) {
        int exp = getPlayerExperience(playerUuid);
        int currentRank = playerRanks.getOrDefault(playerUuid, 0);
        
        // Experience thresholds for each rank
        int[] rankThresholds = {0, 100, 500, 2000, 10000};
        
        // Check if player meets requirements for next rank
        for (int rank = rankThresholds.length - 1; rank > currentRank; rank--) {
            if (exp >= rankThresholds[rank]) {
                // Rank up the player
                playerRanks.put(playerUuid, rank);
                
                // Notify online player if possible
                Player player = Bukkit.getServer().getPlayer(playerUuid);
        if (player != null && !player.isOnline()) {
            player = null; // Ensure consistency with old behavior
        }
                if (player != null && player.isOnline()) {
                    player.sendMessage(ChatColor.GREEN + "Congratulations! You've ranked up to " + 
                                      ChatColor.GOLD + getRankDisplayName(getPlayerRank(playerUuid)) + 
                                      ChatColor.GREEN + "!");
                }
                
                break;
            }
        }
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
        
        // Get config values for rebirth requirements
        int requiredRank = plugin.getConfig().getInt("progression.rebirth.rank-requirement", 5);
        int requiredCurrency = plugin.getConfig().getInt("progression.rebirth.currency-requirement", 100000);
        int requiredTrades = plugin.getConfig().getInt("progression.rebirth.trades-requirement", 500);
        
        // Check if player is at required rank
        if (getPlayerRank(player) < requiredRank) {
            return false;
        }
        
        // Check if player has enough currency
        if (!plugin.getEconomyModule().hasCurrency(player.getUniqueId(), requiredCurrency)) {
            return false;
        }
        
        // Check if player has completed enough trades
        int completedTrades = plugin.getEconomyModule().getCompletedTradesCount(player.getUniqueId());
        if (completedTrades < requiredTrades) {
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
        
        // Play effects - using sounds that are available in all Minecraft versions
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.5f);
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.7f, 0.8f);
        
        // Spawn particles
        ParticleHelper.spawnTotemParticles(player.getLocation().add(0, 1, 0), 100, 0.5, 1.0, 0.5, 0.2);
    }
}