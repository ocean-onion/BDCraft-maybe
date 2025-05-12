package com.bdcraft.plugin.modules.progression;

import java.util.UUID;

/**
 * Interface for progression management.
 */
public interface ProgressionManager {
    
    /**
     * Gets a player's rank.
     * 
     * @param playerId The player's UUID
     * @return The player's rank
     */
    Rank getPlayerRank(UUID playerId);
    
    /**
     * Gets a player's rebirth level.
     * 
     * @param playerId The player's UUID
     * @return The player's rebirth level
     */
    int getRebirthLevel(UUID playerId);
    
    /**
     * Performs a rebirth for a player.
     * 
     * @param playerId The player's UUID
     * @return Whether the rebirth was successful
     */
    boolean performRebirth(UUID playerId);
    
    /**
     * Sets a player's rank.
     * 
     * @param playerId The player's UUID
     * @param rank The new rank
     * @return Whether the rank change was successful
     */
    boolean setRank(UUID playerId, Rank rank);
}