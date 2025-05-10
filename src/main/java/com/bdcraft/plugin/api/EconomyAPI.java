package com.bdcraft.plugin.api;

import java.util.UUID;

/**
 * API for accessing economy data.
 */
public interface EconomyAPI {
    
    /**
     * Gets a player's balance.
     *
     * @param playerUuid The player UUID
     * @return The balance
     */
    double getPlayerBalance(UUID playerUuid);
    
    /**
     * Deposits money into a player's account.
     *
     * @param playerUuid The player UUID
     * @param amount The amount
     * @return The new balance
     */
    double depositPlayer(UUID playerUuid, double amount);
    
    /**
     * Withdraws money from a player's account.
     *
     * @param playerUuid The player UUID
     * @param amount The amount
     * @return The new balance, or -1 if the player doesn't have enough money
     */
    double withdrawPlayer(UUID playerUuid, double amount);
    
    /**
     * Checks if a player has enough money.
     *
     * @param playerUuid The player UUID
     * @param amount The amount
     * @return Whether the player has enough money
     */
    boolean hasEnough(UUID playerUuid, double amount);
}