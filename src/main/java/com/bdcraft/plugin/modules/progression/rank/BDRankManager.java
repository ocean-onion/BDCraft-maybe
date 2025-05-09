package com.bdcraft.plugin.modules.progression.rank;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player ranks and their progression.
 */
public class BDRankManager {
    private final BDCraft plugin;
    private final Map<UUID, Integer> playerRebirths;
    private final Map<UUID, Double> playerProgress;
    private File dataFile;
    private FileConfiguration data;
    
    // Rank names
    private static final String[] RANK_NAMES = {
        "Newcomer",         // Rank 1
        "Farmer",           // Rank 2
        "Expert Farmer",    // Rank 3
        "Master Farmer",    // Rank 4
        "Agricultural Expert" // Rank 5
    };
    
    // Rank requirements - how much BD currency required to reach each rank
    private static final int[] RANK_REQUIREMENTS = {
        0,       // Rank 1 (Newcomer) - starting rank
        2500,    // Rank 2 (Farmer)
        10000,   // Rank 3 (Expert Farmer)
        35000,   // Rank 4 (Master Farmer)
        75000    // Rank 5 (Agricultural Expert)
    };
    
    /**
     * Creates a new rank manager.
     *
     * @param plugin The plugin instance
     */
    public BDRankManager(BDCraft plugin) {
        this.plugin = plugin;
        this.playerRebirths = new HashMap<>();
        this.playerProgress = new HashMap<>();
        
        loadData();
    }
    
    /**
     * Loads rank data from file.
     */
    public void loadData() {
        dataFile = new File(plugin.getDataFolder(), "data/ranks.yml");
        
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            plugin.saveResource("data/ranks.yml", false);
        }
        
        data = YamlConfiguration.loadConfiguration(dataFile);
        
        // Load player rebirths
        if (data.contains("rebirths")) {
            for (String key : data.getConfigurationSection("rebirths").getKeys(false)) {
                try {
                    UUID playerId = UUID.fromString(key);
                    int rebirthLevel = data.getInt("rebirths." + key);
                    playerRebirths.put(playerId, rebirthLevel);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in ranks.yml: " + key);
                }
            }
        }
        
        // Load player progress
        if (data.contains("progress")) {
            for (String key : data.getConfigurationSection("progress").getKeys(false)) {
                try {
                    UUID playerId = UUID.fromString(key);
                    double progress = data.getDouble("progress." + key);
                    playerProgress.put(playerId, progress);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in ranks.yml: " + key);
                }
            }
        }
    }
    
    /**
     * Saves rank data to file.
     */
    public void saveData() {
        if (data == null || dataFile == null) {
            return;
        }
        
        // Save player rebirths
        for (Map.Entry<UUID, Integer> entry : playerRebirths.entrySet()) {
            data.set("rebirths." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save player progress
        for (Map.Entry<UUID, Double> entry : playerProgress.entrySet()) {
            data.set("progress." + entry.getKey().toString(), entry.getValue());
        }
        
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save ranks data: " + e.getMessage());
        }
    }
    
    /**
     * Gets a player's rebirth level.
     *
     * @param player The player
     * @return The player's rebirth level (0 if not reborn)
     */
    public int getPlayerRebirths(Player player) {
        if (player == null) {
            return 0;
        }
        
        return getPlayerRebirths(player.getUniqueId());
    }
    
    /**
     * Gets a player's rebirth level.
     *
     * @param playerId The player's UUID
     * @return The player's rebirth level (0 if not reborn)
     */
    public int getPlayerRebirths(UUID playerId) {
        if (playerId == null) {
            return 0;
        }
        
        return playerRebirths.getOrDefault(playerId, 0);
    }
    
    /**
     * Sets a player's rebirth level.
     *
     * @param playerId The player's UUID
     * @param level The rebirth level
     */
    public void setPlayerRebirths(UUID playerId, int level) {
        if (playerId == null) {
            return;
        }
        
        if (level < 0) {
            level = 0;
        }
        
        playerRebirths.put(playerId, level);
        saveData();
    }
    
    /**
     * Gets a player's progress toward the next rank.
     *
     * @param playerId The player's UUID
     * @return The player's progress (0.0 to 1.0)
     */
    public double getPlayerProgress(UUID playerId) {
        if (playerId == null) {
            return 0.0;
        }
        
        return playerProgress.getOrDefault(playerId, 0.0);
    }
    
    /**
     * Sets a player's progress toward the next rank.
     *
     * @param playerId The player's UUID
     * @param progress The progress value (0.0 to 1.0)
     */
    public void setPlayerProgress(UUID playerId, double progress) {
        if (playerId == null) {
            return;
        }
        
        if (progress < 0.0) {
            progress = 0.0;
        } else if (progress > 1.0) {
            progress = 1.0;
        }
        
        playerProgress.put(playerId, progress);
        saveData();
    }
    
    /**
     * Gets the rank name for a specific rank.
     *
     * @param rank The rank number (1-5)
     * @return The rank name
     */
    public String getRankName(int rank) {
        if (rank < 1) {
            rank = 1;
        } else if (rank > 5) {
            rank = 5;
        }
        
        return RANK_NAMES[rank - 1];
    }
    
    /**
     * Gets the requirement to reach a specific rank.
     *
     * @param rank The rank number (1-5)
     * @return The BD currency required
     */
    public int getRankRequirement(int rank) {
        if (rank < 1) {
            rank = 1;
        } else if (rank > 5) {
            rank = 5;
        }
        
        return RANK_REQUIREMENTS[rank - 1];
    }
    
    /**
     * Calculates the progress toward the next rank based on current currency.
     *
     * @param currentRank The current rank (1-5)
     * @param currency The current BD currency amount
     * @return The progress toward the next rank (0.0 to 1.0)
     */
    public double calculateRankProgress(int currentRank, int currency) {
        if (currentRank >= 5) {
            return 1.0; // Already at max rank
        }
        
        int currentRequirement = getRankRequirement(currentRank);
        int nextRequirement = getRankRequirement(currentRank + 1);
        
        if (nextRequirement <= currentRequirement) {
            return 0.0;
        }
        
        double progress = (double)(currency - currentRequirement) / (nextRequirement - currentRequirement);
        
        if (progress < 0.0) {
            progress = 0.0;
        } else if (progress > 1.0) {
            progress = 1.0;
        }
        
        return progress;
    }
    
    /**
     * Updates a player's rank based on their current currency.
     *
     * @param player The player
     * @param currentCurrency The player's current BD currency
     * @return True if the player ranked up, false otherwise
     */
    public boolean updatePlayerRank(Player player, int currentCurrency) {
        if (player == null) {
            return false;
        }
        
        UUID playerId = player.getUniqueId();
        int currentRank = plugin.getProgressionModule().getPlayerRank(player);
        
        // Don't check for rank-up if already at max rank
        if (currentRank >= 5) {
            return false;
        }
        
        // Check if player meets requirements for the next rank
        int nextRank = currentRank + 1;
        int requirement = getRankRequirement(nextRank);
        
        if (currentCurrency >= requirement) {
            // Player meets requirements for rank up
            plugin.getProgressionModule().setPlayerRank(player, nextRank);
            
            // Update progress
            setPlayerProgress(playerId, 0.0); // Reset progress for the new rank
            
            // Announce rank up
            String newRankName = getRankName(nextRank);
            player.sendMessage(ChatColor.GREEN + "Congratulations! You have ranked up to " + 
                    ChatColor.GOLD + newRankName + ChatColor.GREEN + "!");
            
            // Play sound effect
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            
            return true;
        } else {
            // Update progress toward next rank
            double progress = calculateRankProgress(currentRank, currentCurrency);
            setPlayerProgress(playerId, progress);
            return false;
        }
    }
    
    /**
     * Reloads rank data.
     */
    public void reloadData() {
        playerRebirths.clear();
        playerProgress.clear();
        loadData();
    }
}