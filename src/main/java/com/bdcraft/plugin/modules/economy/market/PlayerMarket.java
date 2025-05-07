package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Location;

import java.util.UUID;

/**
 * Represents a player-created market.
 */
public class PlayerMarket {
    private final String id;
    private final UUID ownerUUID;
    private final Location center;
    private final int radius;
    private int level;
    private int collectorCount;
    
    /**
     * Creates a new player market.
     * @param id The market ID
     * @param ownerUUID The owner's UUID
     * @param center The center location
     * @param radius The market radius
     * @param level The market level
     */
    public PlayerMarket(String id, UUID ownerUUID, Location center, int radius, int level) {
        this.id = id;
        this.ownerUUID = ownerUUID;
        this.center = center;
        this.radius = radius;
        this.level = level;
        this.collectorCount = 0;
    }
    
    /**
     * Gets the market ID.
     * @return The market ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the owner's UUID.
     * @return The owner's UUID
     */
    public UUID getOwnerUUID() {
        return ownerUUID;
    }
    
    /**
     * Gets the center location.
     * @return The center location
     */
    public Location getCenter() {
        return center;
    }
    
    /**
     * Gets the market radius.
     * @return The market radius
     */
    public int getRadius() {
        return radius;
    }
    
    /**
     * Gets the market level.
     * @return The market level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Sets the market level.
     * @param level The new level
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Gets the collector count.
     * @return The collector count
     */
    public int getCollectorCount() {
        return collectorCount;
    }
    
    /**
     * Sets the collector count.
     * @param collectorCount The new collector count
     */
    public void setCollectorCount(int collectorCount) {
        this.collectorCount = collectorCount;
    }
    
    /**
     * Increments the collector count.
     */
    public void incrementCollectorCount() {
        this.collectorCount++;
    }
}