package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a player-owned market in the game.
 */
public class Market {
    private final UUID id;
    private final UUID ownerId;
    private final String ownerName;
    private String name;
    private final String worldName;
    private final int centerX;
    private final int centerY;
    private final int centerZ;
    private int level;
    private int radius;
    private final Set<UUID> associates;
    private int totalSales;
    
    /**
     * Creates a new market.
     * 
     * @param id The market ID
     * @param owner The owner
     * @param name The market name
     * @param center The center location
     * @param radius The market radius
     */
    public Market(UUID id, Player owner, String name, Location center, int radius) {
        this.id = id;
        this.ownerId = owner.getUniqueId();
        this.ownerName = owner.getName();
        this.name = name;
        this.worldName = center.getWorld().getName();
        this.centerX = center.getBlockX();
        this.centerY = center.getBlockY();
        this.centerZ = center.getBlockZ();
        this.level = 1;
        this.radius = radius;
        this.associates = new HashSet<>();
        this.totalSales = 0;
    }
    
    /**
     * Gets the market ID.
     * 
     * @return The ID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the owner ID.
     * 
     * @return The owner ID
     */
    public UUID getOwnerId() {
        return ownerId;
    }
    
    /**
     * Gets the founder ID (same as owner ID for backward compatibility).
     * 
     * @return The founder ID
     */
    public UUID getFounderId() {
        return ownerId;
    }
    
    /**
     * Gets the owner name.
     * 
     * @return The owner name
     */
    public String getOwnerName() {
        return ownerName;
    }
    
    /**
     * Gets the founder name (same as owner name for backward compatibility).
     * 
     * @return The founder name
     */
    public String getFounderName() {
        return ownerName;
    }
    
    /**
     * Gets the market name.
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the market name.
     * 
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the world name.
     * 
     * @return The world name
     */
    public String getWorldName() {
        return worldName;
    }
    
    /**
     * Gets the center X coordinate.
     * 
     * @return The center X
     */
    public int getCenterX() {
        return centerX;
    }
    
    /**
     * Gets the center Y coordinate.
     * 
     * @return The center Y
     */
    public int getCenterY() {
        return centerY;
    }
    
    /**
     * Gets the center Z coordinate.
     * 
     * @return The center Z
     */
    public int getCenterZ() {
        return centerZ;
    }
    
    /**
     * Gets the market level.
     * 
     * @return The level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Sets the market level.
     * 
     * @param level The new level
     * @return True if set successfully
     */
    public boolean setLevel(int level) {
        if (level < 1 || level > 4) {
            return false;
        }
        this.level = level;
        return true;
    }
    
    /**
     * Gets the market radius.
     * 
     * @return The radius
     */
    public int getRadius() {
        return radius;
    }
    
    /**
     * Sets the market radius.
     * 
     * @param radius The new radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    /**
     * Checks if a location is within this market.
     * 
     * @param location The location
     * @return True if within the market
     */
    public boolean isInMarket(Location location) {
        if (!location.getWorld().getName().equals(worldName)) {
            return false;
        }
        
        int dx = location.getBlockX() - centerX;
        int dz = location.getBlockZ() - centerZ;
        
        return Math.abs(dx) <= radius && Math.abs(dz) <= radius;
    }
    
    /**
     * Checks if a player is the owner of this market.
     * 
     * @param playerId The player ID
     * @return True if owner
     */
    public boolean isOwner(UUID playerId) {
        return ownerId.equals(playerId);
    }
    
    /**
     * Checks if a player is an associate of this market.
     * 
     * @param playerId The player ID
     * @return True if associate
     */
    public boolean isAssociate(UUID playerId) {
        return associates.contains(playerId);
    }
    
    /**
     * Gets the market associates.
     * 
     * @return The associates
     */
    public Set<UUID> getAssociates() {
        return new HashSet<>(associates);
    }
    
    /**
     * Adds an associate to the market.
     * 
     * @param playerId The player ID
     * @return True if added
     */
    public boolean addAssociate(UUID playerId) {
        return associates.add(playerId);
    }
    
    /**
     * Removes an associate from the market.
     * 
     * @param playerId The player ID
     * @return True if removed
     */
    public boolean removeAssociate(UUID playerId) {
        return associates.remove(playerId);
    }
    
    /**
     * Gets the total sales count for this market.
     * 
     * @return The total sales
     */
    public int getTotalSales() {
        return totalSales;
    }
    
    /**
     * Increments the total sales count.
     */
    public void incrementTotalSales() {
        this.totalSales++;
    }
    
    /**
     * Adds to the total sales count.
     * 
     * @param count The count to add
     */
    public void addTotalSales(int count) {
        this.totalSales += count;
    }
}