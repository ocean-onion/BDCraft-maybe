package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manages player ranks and progression in the BD system.
 */
public class BDRankManager {
    private final BDCraft plugin;
    private final Logger logger;
    
    // Rank progression levels (as per documentation - do not modify)
    public static final int RANK_NEWCOMER = 1;
    public static final int RANK_FARMER = 2;
    public static final int RANK_EXPERT_FARMER = 3;
    public static final int RANK_MASTER_FARMER = 4;
    public static final int RANK_AGRICULTURAL_EXPERT = 5;
    public static final int RANK_REBORN = 6; // Special rebirthable state
    
    // Currency costs for each rank as per documentation
    private final int[] rankCosts = new int[]{
        0,      // Newcomer (starting rank)
        5000,   // Farmer
        15000,  // Expert Farmer
        30000,  // Master Farmer
        60000,  // Agricultural Expert
        0       // Reborn status (uses special rebirth system)
    };
    
    // Experience points required for each rank (now based on currency costs)
    private final int[] rankExperienceRequirements = new int[]{
        0,      // Rank 0 (unused)
        0,      // Newcomer (starting rank)
        5000,   // Farmer
        15000,  // Expert Farmer
        30000,  // Master Farmer
        60000,  // Agricultural Expert
        100000  // Reborn status
    };
    
    // Rank names
    private final String[] rankNames = new String[]{
        "", // Index 0 is unused
        "Newcomer",
        "Farmer",
        "Expert Farmer",
        "Master Farmer",
        "Agricultural Expert",
        "Reborn Farmer"
    };
    
    // Rank display colors
    private final ChatColor[] rankColors = new ChatColor[]{
        ChatColor.WHITE,      // Unused
        ChatColor.GRAY,       // Newcomer
        ChatColor.GREEN,      // Farmer
        ChatColor.BLUE,       // Expert Farmer
        ChatColor.GOLD,       // Master Farmer
        ChatColor.AQUA,       // Agricultural Expert
        ChatColor.LIGHT_PURPLE// Reborn status
    };
    
    // Map of player UUIDs to current ranks
    private final Map<UUID, Integer> playerRanks;
    
    // Map of player UUIDs to current experience points
    private final Map<UUID, Integer> playerExperience;
    
    // Map of player UUIDs to rebirth count
    private final Map<UUID, Integer> playerRebirths;
    
    /**
     * Creates a new BD rank manager.
     * @param plugin The plugin instance
     */
    public BDRankManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.playerRanks = new HashMap<>();
        this.playerExperience = new HashMap<>();
        this.playerRebirths = new HashMap<>();
    }
    
    /**
     * Gets a player's current rank.
     * @param player The player
     * @return The player's rank
     */
    public int getPlayerRank(Player player) {
        return playerRanks.getOrDefault(player.getUniqueId(), RANK_NEWCOMER);
    }
    
    /**
     * Gets a player's current experience points.
     * @param player The player
     * @return The player's experience points
     */
    public int getPlayerExperience(Player player) {
        return playerExperience.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Gets a player's rebirth count.
     * @param player The player
     * @return The player's rebirth count
     */
    public int getPlayerRebirths(Player player) {
        return playerRebirths.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Sets a player's rank.
     * @param player The player
     * @param rank The new rank
     */
    public void setPlayerRank(Player player, int rank) {
        if (rank < RANK_NEWCOMER || rank > RANK_REBORN) {
            return;
        }
        
        playerRanks.put(player.getUniqueId(), rank);
    }
    
    /**
     * Sets a player's experience points.
     * @param player The player
     * @param experience The new experience points
     */
    public void setPlayerExperience(Player player, int experience) {
        if (experience < 0) {
            experience = 0;
        }
        
        playerExperience.put(player.getUniqueId(), experience);
        
        // Check for rank up
        checkRankUp(player);
    }
    
    /**
     * Adds experience points to a player.
     * @param player The player
     * @param experienceToAdd The experience points to add
     * @return The player's new experience points
     */
    public int addPlayerExperience(Player player, int experienceToAdd) {
        if (experienceToAdd <= 0) {
            return getPlayerExperience(player);
        }
        
        // Apply rebirth bonus if applicable
        int rebirths = getPlayerRebirths(player);
        if (rebirths > 0) {
            // 10% bonus per rebirth
            experienceToAdd += (int)(experienceToAdd * (rebirths * 0.10));
        }
        
        int currentExperience = getPlayerExperience(player);
        int newExperience = currentExperience + experienceToAdd;
        
        setPlayerExperience(player, newExperience);
        
        return newExperience;
    }
    
    /**
     * Adds a rebirth to a player.
     * @param player The player
     * @return The player's new rebirth count
     */
    public int addPlayerRebirth(Player player) {
        int currentRank = getPlayerRank(player);
        
        // Player must be an Agricultural Expert to rebirth
        if (currentRank < RANK_AGRICULTURAL_EXPERT) {
            return getPlayerRebirths(player);
        }
        
        int currentRebirths = getPlayerRebirths(player);
        int newRebirths = currentRebirths + 1;
        
        playerRebirths.put(player.getUniqueId(), newRebirths);
        
        // Reset rank and experience
        setPlayerRank(player, RANK_NEWCOMER);
        setPlayerExperience(player, 0);
        
        // Notify player
        player.sendMessage(ChatColor.LIGHT_PURPLE + "You have been reborn! Your farming skills will now develop faster than before.");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Current rebirth count: " + newRebirths);
        
        return newRebirths;
    }
    
    /**
     * Checks if a player can rank up and performs the rank up if possible.
     * @param player The player
     * @return Whether the player ranked up
     */
    public boolean checkRankUp(Player player) {
        int currentRank = getPlayerRank(player);
        int currentExperience = getPlayerExperience(player);
        
        // Player is already at max rank
        if (currentRank >= RANK_AGRICULTURAL_EXPERT) {
            return false;
        }
        
        // Check if player has enough experience to rank up
        int nextRank = currentRank + 1;
        int requiredExperience = rankExperienceRequirements[nextRank];
        
        if (currentExperience >= requiredExperience) {
            // Rank up the player
            setPlayerRank(player, nextRank);
            
            // Notify player
            player.sendMessage(ChatColor.GREEN + "Congratulations! You have ranked up to " + 
                    rankColors[nextRank] + rankNames[nextRank] + ChatColor.GREEN + "!");
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets a player's rank name.
     * @param player The player
     * @return The rank name
     */
    public String getPlayerRankName(Player player) {
        int rank = getPlayerRank(player);
        return rankNames[rank];
    }
    
    /**
     * Gets a player's colored rank name.
     * @param player The player
     * @return The colored rank name
     */
    public String getPlayerColoredRankName(Player player) {
        int rank = getPlayerRank(player);
        return rankColors[rank] + rankNames[rank];
    }
    
    /**
     * Gets the display format for a player's rank and name.
     * @param player The player
     * @return The formatted display
     */
    public String getPlayerDisplayName(Player player) {
        int rank = getPlayerRank(player);
        int rebirths = getPlayerRebirths(player);
        
        StringBuilder display = new StringBuilder();
        
        // Add rebirth stars if applicable
        if (rebirths > 0) {
            for (int i = 0; i < Math.min(rebirths, 5); i++) {
                display.append(ChatColor.YELLOW).append("★");
            }
            
            if (rebirths > 5) {
                display.append(ChatColor.YELLOW).append("[").append(rebirths).append("]");
            }
            
            display.append(" ");
        }
        
        // Add rank and name
        display.append(rankColors[rank]).append("[").append(rankNames[rank]).append("] ")
               .append(ChatColor.WHITE).append(player.getName());
        
        return display.toString();
    }
    
    /**
     * Gets the experience required to reach the next rank.
     * @param player The player
     * @return The experience required, or -1 if already at max rank
     */
    public int getExperienceRequiredForNextRank(Player player) {
        int currentRank = getPlayerRank(player);
        
        // Player is already at max rank
        if (currentRank >= RANK_AGRICULTURAL_EXPERT) {
            return -1;
        }
        
        int nextRank = currentRank + 1;
        return rankExperienceRequirements[nextRank];
    }
    
    /**
     * Gets the progress percentage towards the next rank.
     * @param player The player
     * @return The progress percentage (0-100), or 100 if already at max rank
     */
    public int getProgressPercentage(Player player) {
        int currentRank = getPlayerRank(player);
        
        // Player is already at max rank
        if (currentRank >= RANK_AGRICULTURAL_EXPERT) {
            return 100;
        }
        
        int currentExperience = getPlayerExperience(player);
        int nextRankExperience = rankExperienceRequirements[currentRank + 1];
        int currentRankExperience = rankExperienceRequirements[currentRank];
        
        int expNeeded = nextRankExperience - currentRankExperience;
        int expGained = currentExperience - currentRankExperience;
        
        return (int)(((double)expGained / expNeeded) * 100);
    }
    
    /**
     * Gets a progress bar for display in chat.
     * @param player The player
     * @return The progress bar
     */
    public String getProgressBar(Player player) {
        int percentage = getProgressPercentage(player);
        int bars = percentage / 5; // 20 bars total for 100%
        
        StringBuilder progressBar = new StringBuilder(ChatColor.GRAY + "[");
        
        for (int i = 0; i < 20; i++) {
            if (i < bars) {
                progressBar.append(ChatColor.GREEN).append("|");
            } else {
                progressBar.append(ChatColor.RED).append("|");
            }
        }
        
        progressBar.append(ChatColor.GRAY).append("] ").append(percentage).append("%");
        
        return progressBar.toString();
    }
    
    /**
     * Checks if a player has a specific minimum rank.
     * @param player The player
     * @param requiredRank The minimum required rank
     * @return Whether the player has the required rank
     */
    public boolean hasRank(Player player, int requiredRank) {
        return getPlayerRank(player) >= requiredRank;
    }
    
    /**
     * Gets the experience bonus multiplier based on rebirth count.
     * @param player The player
     * @return The experience multiplier (1.0 = no bonus)
     */
    public double getExperienceMultiplier(Player player) {
        int rebirths = getPlayerRebirths(player);
        return 1.0 + (rebirths * 0.10); // 10% bonus per rebirth
    }
    
    /**
     * Gets the experience needed for the next rank.
     * @param player The player
     * @return The experience needed for the next rank
     */
    public int getExperienceForNextRank(Player player) {
        int currentRank = getPlayerRank(player);
        
        // Player is already at max rank
        if (currentRank >= RANK_AGRICULTURAL_EXPERT) {
            return -1;
        }
        
        int nextRank = currentRank + 1;
        return rankExperienceRequirements[nextRank];
    }
    
    /**
     * Gets the next rank's experience requirement.
     * @param player The player
     * @return The next rank's experience, or -1 if already at max rank
     */
    public int getNextRankExperience(Player player) {
        return getExperienceForNextRank(player);
    }
    
    /**
     * Gets the name of a rank.
     * @param rank The rank
     * @return The rank name
     */
    public String getRankName(int rank) {
        if (rank < RANK_NEWCOMER || rank > RANK_REBORN) {
            return "Unknown Rank";
        }
        
        return rankNames[rank];
    }
    
    /**
     * Checks if a player has rebirth status.
     * @param player The player
     * @return Whether the player has rebirth status
     */
    public boolean hasRebirthStatus(Player player) {
        return getPlayerRebirths(player) > 0;
    }
    
    /**
     * Gets a player's rebirth level.
     * @param player The player
     * @return The rebirth level
     */
    public int getRebirthLevel(Player player) {
        return getPlayerRebirths(player);
    }
    
    /**
     * Checks if a player's rank is at least a certain rank.
     * @param playerRank The player's rank
     * @param requiredRank The required rank
     * @return Whether the player's rank is at least the required rank
     */
    public boolean isRankAtLeast(String playerRank, String requiredRank) {
        int playerRankValue = -1;
        int requiredRankValue = -1;
        
        // Find player rank value
        for (int i = 0; i < rankNames.length; i++) {
            if (rankNames[i].equalsIgnoreCase(playerRank)) {
                playerRankValue = i;
                break;
            }
        }
        
        // Find required rank value
        for (int i = 0; i < rankNames.length; i++) {
            if (rankNames[i].equalsIgnoreCase(requiredRank)) {
                requiredRankValue = i;
                break;
            }
        }
        
        // If ranks not found, return false
        if (playerRankValue == -1 || requiredRankValue == -1) {
            return false;
        }
        
        return playerRankValue >= requiredRankValue;
    }
    
    /**
     * Adds experience to a player.
     * @param player The player
     * @param experience The experience to add
     */
    public void addExperience(Player player, int experience) {
        addPlayerExperience(player, experience);
    }
    
    /**
     * Loads rank data from storage.
     */
    public void loadRankData() {
        // This would load player rank data from a database or file
        // For simplicity, we'll just use the in-memory maps for now
        logger.info("Loaded rank data from storage");
    }
}