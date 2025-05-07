package com.bdcraft.plugin.modules.economy.villager;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base class for BD Villagers.
 */
public abstract class BDVillager {
    protected final Villager entity;
    protected final String type;
    protected final UUID uuid;
    protected final Map<UUID, Integer> playerReputations;
    
    /**
     * Creates a new BD Villager.
     * @param entity The villager entity
     * @param type The villager type
     */
    public BDVillager(Villager entity, String type) {
        this.entity = entity;
        this.type = type;
        this.uuid = entity.getUniqueId();
        this.playerReputations = new HashMap<>();
        
        // Mark this entity as a BD Villager
        markAsBDVillager();
    }
    
    /**
     * Marks this entity as a BD Villager in its persistent data.
     */
    private void markAsBDVillager() {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        NamespacedKey bdVillagerKey = new NamespacedKey("bdcraft", "bd_villager");
        NamespacedKey bdVillagerTypeKey = new NamespacedKey("bdcraft", "bd_villager_type");
        
        pdc.set(bdVillagerKey, PersistentDataType.BYTE, (byte) 1);
        pdc.set(bdVillagerTypeKey, PersistentDataType.STRING, type);
    }
    
    /**
     * Gets the UUID of this villager.
     * @return The UUID
     */
    public UUID getUUID() {
        return uuid;
    }
    
    /**
     * Gets the type of this villager.
     * @return The type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Gets the villager entity.
     * @return The entity
     */
    public Villager getEntity() {
        return entity;
    }
    
    /**
     * Gets a player's reputation with this villager.
     * @param playerUuid The player's UUID
     * @return The reputation value
     */
    public int getReputation(UUID playerUuid) {
        return playerReputations.getOrDefault(playerUuid, 0);
    }
    
    /**
     * Changes a player's reputation with this villager.
     * @param playerUuid The player's UUID
     * @param amount The amount to change (positive or negative)
     * @return The new reputation value
     */
    public int changeReputation(UUID playerUuid, int amount) {
        int currentRep = getReputation(playerUuid);
        int newRep = currentRep + amount;
        
        playerReputations.put(playerUuid, newRep);
        
        return newRep;
    }
    
    /**
     * Gets the price modifier for a player based on reputation.
     * @param playerUuid The player's UUID
     * @return The price modifier (1.0 = normal price)
     */
    public double getPriceModifier(UUID playerUuid) {
        // Default implementation - override in subclasses
        int rep = getReputation(playerUuid);
        
        if (rep <= -50) {
            return 1.5; // 50% higher prices
        } else if (rep <= -20) {
            return 1.2; // 20% higher prices
        } else if (rep >= 50) {
            return 0.85; // 15% discount
        } else if (rep >= 20) {
            return 0.95; // 5% discount
        }
        
        return 1.0; // Normal price
    }
    
    /**
     * Removes this villager.
     */
    public void remove() {
        entity.remove();
    }
}