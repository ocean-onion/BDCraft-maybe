package com.bdcraft.plugin.api;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * API for player progression and rank systems.
 * This implementation handles player progression from Newcomer through all ranks to Reborn.
 */
public interface ProgressionAPI {
    /**
     * The available ranks in progression order.
     */
    enum Rank {
        NEWCOMER,
        FARMER,
        EXPERT_FARMER,
        MASTER_FARMER,
        AGRICULTURAL_EXPERT,
        REBORN
    }
    
    /**
     * Gets a player's rank.
     * @param uuid The player's UUID
     * @return The player's rank
     */
    Rank getPlayerRank(UUID uuid);
    
    /**
     * Sets a player's rank.
     * @param uuid The player's UUID
     * @param rank The new rank
     * @return Whether the operation was successful
     */
    boolean setPlayerRank(UUID uuid, Rank rank);
    
    /**
     * Gets a player's experience points.
     * @param uuid The player's UUID
     * @return The player's experience points
     */
    int getPlayerExperience(UUID uuid);
    
    /**
     * Adds experience points to a player.
     * @param uuid The player's UUID
     * @param amount The amount of experience to add
     * @return The new total experience
     */
    int addPlayerExperience(UUID uuid, int amount);
    
    /**
     * Gets the experience required for a rank.
     * @param rank The rank
     * @return The required experience
     */
    int getRequiredExperience(Rank rank);
    
    /**
     * Checks if a player can progress to the next rank.
     * @param uuid The player's UUID
     * @return Whether the player can progress
     */
    boolean canProgress(UUID uuid);
    
    /**
     * Progresses a player to the next rank if possible.
     * @param uuid The player's UUID
     * @return Whether the player was successfully progressed
     */
    boolean progressPlayer(UUID uuid);
    
    /**
     * Gets the rebirth count of a player.
     * @param uuid The player's UUID
     * @return The player's rebirth count
     */
    int getRebirthCount(UUID uuid);
    
    /**
     * Performs a rebirth on a player.
     * @param player The player
     * @return Whether the rebirth was successful
     */
    boolean performRebirth(Player player);
    
    /**
     * Gets the rebirth bonuses for a player.
     * @param uuid The player's UUID
     * @return Map of bonus types to values
     */
    Map<String, Double> getRebirthBonuses(UUID uuid);
    
    /**
     * Gets top players by experience.
     * @param limit The number of players to get
     * @return List of top player UUIDs
     */
    List<UUID> getTopPlayers(int limit);
    
    /**
     * Gets all players with a specific rank.
     * @param rank The rank
     * @return List of player UUIDs
     */
    List<UUID> getPlayersByRank(Rank rank);
    
    /**
     * Gets the rank name for display.
     * @param rank The rank
     * @return The formatted rank name
     */
    String getRankDisplayName(Rank rank);
    
    /**
     * Gets the rank prefix for chat.
     * @param rank The rank
     * @return The rank prefix
     */
    String getRankPrefix(Rank rank);
    
    /**
     * Gets the next rank for a player.
     * @param uuid The player's UUID
     * @return The next rank, or null if at maximum rank
     */
    Rank getNextRank(UUID uuid);
    
    /**
     * Gets the progress percentage to the next rank.
     * @param uuid The player's UUID
     * @return The progress percentage (0-100)
     */
    double getProgressPercentage(UUID uuid);
}