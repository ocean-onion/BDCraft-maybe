package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a player-owned market in the BDCraft economy system.
 */
public class Market {
    private final UUID id;
    private final UUID ownerId;
    private final String ownerName;
    private String name;
    private final int centerX;
    private final int centerY;
    private final int centerZ;
    private final String worldName;
    private int level;
    private int totalSales;
    private final List<UUID> associates;
    
    /**
     * Creates a new market.
     * 
     * @param id The market ID
     * @param ownerId The owner's UUID
     * @param ownerName The owner's name
     * @param name The market name
     * @param centerX The center X coordinate
     * @param centerY The center Y coordinate
     * @param centerZ The center Z coordinate
     * @param worldName The world name
     */
    public Market(UUID id, UUID ownerId, String ownerName, String name, 
            int centerX, int centerY, int centerZ, String worldName) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.name = name;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.worldName = worldName;
        this.level = 1;
        this.totalSales = 0;
        this.associates = new ArrayList<>();
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
     * Gets the owner's UUID.
     * 
     * @return The owner's UUID
     */
    public UUID getOwnerId() {
        return ownerId;
    }
    
    /**
     * Gets the owner's name.
     * 
     * @return The owner's name
     */
    public String getOwnerName() {
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
     * Gets the center X coordinate.
     * 
     * @return The X coordinate
     */
    public int getCenterX() {
        return centerX;
    }
    
    /**
     * Gets the center Y coordinate.
     * 
     * @return The Y coordinate
     */
    public int getCenterY() {
        return centerY;
    }
    
    /**
     * Gets the center Z coordinate.
     * 
     * @return The Z coordinate
     */
    public int getCenterZ() {
        return centerZ;
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
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Gets the total sales.
     * 
     * @return The total sales
     */
    public int getTotalSales() {
        return totalSales;
    }
    
    /**
     * Increments the total sales.
     * 
     * @param amount The amount to add
     */
    public void incrementTotalSales(int amount) {
        this.totalSales += amount;
    }
    
    /**
     * Gets the market associates.
     * 
     * @return The associates
     */
    public List<UUID> getAssociates() {
        return new ArrayList<>(associates);
    }
    
    /**
     * Adds an associate to the market.
     * 
     * @param associateId The associate's UUID
     * @return True if added successfully
     */
    public boolean addAssociate(UUID associateId) {
        if (associates.size() >= 5) {
            return false; // Maximum 5 associates per market
        }
        
        if (!associates.contains(associateId)) {
            associates.add(associateId);
            return true;
        }
        
        return false;
    }
    
    /**
     * Removes an associate from the market.
     * 
     * @param associateId The associate's UUID
     * @return True if removed successfully
     */
    public boolean removeAssociate(UUID associateId) {
        return associates.remove(associateId);
    }
    
    /**
     * Checks if a player is an associate of the market.
     * 
     * @param playerId The player's UUID
     * @return True if the player is an associate
     */
    public boolean isAssociate(UUID playerId) {
        return associates.contains(playerId);
    }
    
    /**
     * Gets the market radius.
     * 
     * @return The radius in blocks
     */
    public int getRadius() {
        return 24; // 49x49 area as per documentation
    }
    
    /**
     * Gets the market center location.
     * 
     * @param world The world
     * @return The center location
     */
    public Location getCenterLocation(World world) {
        return new Location(world, centerX, centerY, centerZ);
    }
    
    /**
     * Checks if a location is within the market boundaries.
     * 
     * @param location The location to check
     * @return True if within boundaries
     */
    public boolean isInBounds(Location location) {
        if (!location.getWorld().getName().equals(worldName)) {
            return false;
        }
        
        int radius = getRadius();
        int minX = centerX - radius;
        int maxX = centerX + radius;
        int minZ = centerZ - radius;
        int maxZ = centerZ + radius;
        
        int x = location.getBlockX();
        int z = location.getBlockZ();
        
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }
}