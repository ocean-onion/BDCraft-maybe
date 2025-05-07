package com.bdcraft.plugin.modules.economy.villager;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a BD villager entity.
 */
public abstract class BDVillager {
    protected final Villager entity;
    protected final String type;
    protected final Map<UUID, Integer> reputations = new HashMap<>();
    
    /**
     * Creates a new BD villager.
     * @param entity The villager entity
     * @param type The BD villager type
     */
    public BDVillager(Villager entity, String type) {
        this.entity = entity;
        this.type = type;
    }
    
    /**
     * Gets the villager entity.
     * @return The villager entity
     */
    public Villager getEntity() {
        return entity;
    }
    
    /**
     * Gets the BD villager type.
     * @return The BD villager type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Gets the villager ID.
     * @return The villager ID
     */
    public UUID getId() {
        return entity.getUniqueId();
    }
    
    /**
     * Gets the villager location.
     * @return The villager location
     */
    public Location getLocation() {
        return entity.getLocation();
    }
    
    /**
     * Gets a player's reputation with this villager.
     * @param playerUuid The player UUID
     * @return The player's reputation
     */
    public int getReputation(UUID playerUuid) {
        return reputations.getOrDefault(playerUuid, 0);
    }
    
    /**
     * Changes a player's reputation with this villager.
     * @param playerUuid The player UUID
     * @param amount The amount to change
     * @return The new reputation
     */
    public int changeReputation(UUID playerUuid, int amount) {
        int current = getReputation(playerUuid);
        int newRep = Math.max(current + amount, -100);
        reputations.put(playerUuid, newRep);
        return newRep;
    }
    
    /**
     * Sets a player's reputation with this villager.
     * @param playerUuid The player UUID
     * @param amount The new reputation amount
     */
    public void setReputation(UUID playerUuid, int amount) {
        reputations.put(playerUuid, amount);
    }
    
    /**
     * Gets all player reputations with this villager.
     * @return The map of player UUIDs to reputation values
     */
    public Map<UUID, Integer> getAllReputations() {
        return new HashMap<>(reputations);
    }
    
    /**
     * Sets all player reputations with this villager.
     * @param reputations The map of player UUIDs to reputation values
     */
    public void setAllReputations(Map<UUID, Integer> reputations) {
        this.reputations.clear();
        this.reputations.putAll(reputations);
    }
    
    /**
     * Gets the price modifier based on a player's reputation.
     * @param playerUuid The player UUID
     * @return The price modifier (1.0 = normal, < 1.0 = discount, > 1.0 = markup)
     */
    public double getPriceModifier(UUID playerUuid) {
        int rep = getReputation(playerUuid);
        if (rep <= 0) {
            return 1.0;
        }
        
        // Every 10 reputation points gives a 1% discount, max 30%
        double discount = Math.min(0.3, rep / 1000.0);
        return 1.0 - discount;
    }
}