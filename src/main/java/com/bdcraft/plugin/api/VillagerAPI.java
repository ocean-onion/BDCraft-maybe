package com.bdcraft.plugin.api;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

/**
 * API for accessing villager data.
 */
public interface VillagerAPI {
    
    /**
     * Gets a player's reputation with a villager.
     *
     * @param player The player
     * @param villager The villager
     * @return The reputation
     */
    int getReputation(Player player, Villager villager);
    
    /**
     * Changes a player's reputation with a villager.
     *
     * @param player The player
     * @param villager The villager
     * @param change The change
     * @return The new reputation
     */
    int changeReputation(Player player, Villager villager, int change);
    
    /**
     * Sets a player's reputation with a villager.
     *
     * @param player The player
     * @param villager The villager
     * @param reputation The reputation
     */
    void setReputation(Player player, Villager villager, int reputation);
    
    /**
     * Gets the maximum reputation a player can have with a villager.
     *
     * @return The maximum reputation
     */
    int getMaxReputation();
    
    /**
     * Gets the minimum reputation a player can have with a villager.
     *
     * @return The minimum reputation
     */
    int getMinReputation();
    
    /**
     * Checks if a villager is a BDVillager.
     *
     * @param villager The villager to check
     * @return True if the villager is a BDVillager
     */
    boolean isBDVillager(Villager villager);
    
    /**
     * Gets the type of a BDVillager.
     *
     * @param villager The villager
     * @return The villager type, or null if not a BDVillager
     */
    String getBDVillagerType(Villager villager);
}