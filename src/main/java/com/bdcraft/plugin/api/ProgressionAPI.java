package com.bdcraft.plugin.api;

import java.util.UUID;

/**
 * API for BD progression, ranks, and rebirth mechanics.
 */
public interface ProgressionAPI {
    /**
     * Gets a player's rank.
     * @param uuid The player's UUID
     * @return The player's rank
     */
    int getPlayerRank(UUID uuid);
    
    /**
     * Checks if a player has a minimum required rank.
     * @param uuid The player's UUID
     * @param requiredRank The minimum required rank
     * @return Whether the player has the required rank
     */
    boolean hasRank(UUID uuid, int requiredRank);
    
    /**
     * Adds experience to a player.
     * @param uuid The player's UUID
     * @param amount The amount of experience to add
     */
    void addExperience(UUID uuid, int amount);
    
    /**
     * Gets a player's experience multiplier.
     * @param uuid The player's UUID
     * @return The experience multiplier
     */
    double getExperienceMultiplier(UUID uuid);
    
    /**
     * Gets a player's rebirth count.
     * @param uuid The player's UUID
     * @return The rebirth count
     */
    int getRebirths(UUID uuid);
    
    /**
     * Gets a player's rank name.
     * @param uuid The player's UUID
     * @return The rank name
     */
    String getRankName(UUID uuid);
}