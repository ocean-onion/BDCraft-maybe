package com.bdcraft.plugin.api;

import java.util.UUID;

/**
 * API for accessing progression data.
 */
public interface ProgressionAPI {
    
    /**
     * Gets a player's rank.
     *
     * @param playerUuid The player UUID
     * @return The rank
     */
    Rank getPlayerRank(UUID playerUuid);
    
    /**
     * Gets a display name for a rank.
     *
     * @param rank The rank
     * @return The display name
     */
    String getRankDisplayName(Rank rank);
    
    /**
     * Gets a player's rebirth level.
     *
     * @param playerUuid The player UUID
     * @return The rebirth level
     */
    int getPlayerRebirthLevel(UUID playerUuid);
    
    /**
     * Gets a player's experience points.
     *
     * @param playerUuid The player UUID
     * @return The experience points
     */
    int getPlayerExperience(UUID playerUuid);
    
    /**
     * Adds experience points to a player.
     *
     * @param playerUuid The player UUID
     * @param amount The amount
     * @return The new experience points total
     */
    int addPlayerExperience(UUID playerUuid, int amount);
    
    /**
     * Gets the player's progress percentage to the next rank.
     *
     * @param playerUuid The player UUID
     * @return The progress percentage (0-100)
     */
    double getProgressPercentage(UUID playerUuid);
    
    /**
     * Gets the next rank for a player.
     *
     * @param playerUuid The player UUID
     * @return The next rank, or null if at max rank
     */
    Rank getNextRank(UUID playerUuid);
    
    /**
     * Gets the rank enum values.
     */
    enum Rank {
        NEWCOMER,
        FARMER,
        EXPERT_FARMER,
        MASTER_FARMER,
        AGRICULTURAL_EXPERT
    }
}