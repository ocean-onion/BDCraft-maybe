package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a BD market.
 */
public class BDMarket {
    private final UUID id;
    private final String name;
    private final UUID ownerId;
    private final String ownerName;
    private final String worldName;
    private final double centerX;
    private final double centerY;
    private final double centerZ;
    private final int level;
    private final long creationTime;
    private final int radius;
    private final List<UUID> members;
    private boolean active;
    
    /**
     * Creates a new BD market.
     * 
     * @param id The market ID
     * @param name The market name
     * @param ownerId The owner UUID
     * @param ownerName The owner name
     * @param worldName The world name
     * @param centerX The center X coordinate
     * @param centerY The center Y coordinate
     * @param centerZ The center Z coordinate
     * @param level The market level
     * @param radius The market radius
     */
    public BDMarket(UUID id, String name, UUID ownerId, String ownerName, String worldName,
            double centerX, double centerY, double centerZ, int level, int radius) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.worldName = worldName;
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.level = level;
        this.radius = radius;
        this.creationTime = System.currentTimeMillis();
        this.members = new ArrayList<>();
        this.active = true;
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
     * Gets the market name.
     * 
     * @return The name
     */
    public String getName() {
        return name;
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
     * Gets the owner name.
     * 
     * @return The owner name
     */
    public String getOwnerName() {
        return ownerName;
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
    public double getCenterX() {
        return centerX;
    }
    
    /**
     * Gets the center Y coordinate.
     * 
     * @return The center Y
     */
    public double getCenterY() {
        return centerY;
    }
    
    /**
     * Gets the center Z coordinate.
     * 
     * @return The center Z
     */
    public double getCenterZ() {
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
     * Gets the creation time.
     * 
     * @return The creation time
     */
    public long getCreationTime() {
        return creationTime;
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
     * Gets the market members.
     * 
     * @return The members
     */
    public List<UUID> getMembers() {
        return new ArrayList<>(members);
    }
    
    /**
     * Checks if the market is active.
     * 
     * @return True if active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Sets whether the market is active.
     * 
     * @param active True if active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Adds a member to the market.
     * 
     * @param memberId The member ID
     * @return True if added successfully
     */
    public boolean addMember(UUID memberId) {
        if (!members.contains(memberId)) {
            members.add(memberId);
            return true;
        }
        return false;
    }
    
    /**
     * Removes a member from the market.
     * 
     * @param memberId The member ID
     * @return True if removed successfully
     */
    public boolean removeMember(UUID memberId) {
        return members.remove(memberId);
    }
    
    /**
     * Checks if a player is a member.
     * 
     * @param playerId The player ID
     * @return True if a member
     */
    public boolean isMember(UUID playerId) {
        return members.contains(playerId) || ownerId.equals(playerId);
    }
    
    /**
     * Checks if a location is within the market area.
     * 
     * @param location The location
     * @return True if within the market
     */
    public boolean isWithinMarket(Location location) {
        if (!location.getWorld().getName().equals(worldName)) {
            return false;
        }
        
        double dx = location.getX() - centerX;
        double dz = location.getZ() - centerZ;
        
        return Math.sqrt(dx * dx + dz * dz) <= radius;
    }
    
    /**
     * Gets the center location.
     * 
     * @param plugin The plugin instance
     * @return The center location
     */
    public Location getCenterLocation(BDCraft plugin) {
        World world = plugin.getServer().getWorld(worldName);
        if (world != null) {
            return new Location(world, centerX, centerY, centerZ);
        }
        return null;
    }
    
    /**
     * Checks if a player is the owner.
     * 
     * @param player The player
     * @return True if the owner
     */
    public boolean isOwner(Player player) {
        return player.getUniqueId().equals(ownerId);
    }
    
    /**
     * Gets the founder ID (same as owner ID for backward compatibility).
     * 
     * @return The founder UUID
     */
    public UUID getFounderId() {
        return ownerId;
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
     * Gets the net worth of the market.
     * This is a calculated value based on market level, size, and member count.
     * 
     * @return The net worth
     */
    public double getNetWorth() {
        // Base value from level
        double netWorth = level * 500.0;
        
        // Add value for members
        netWorth += members.size() * 100.0;
        
        // Add value based on radius
        netWorth += radius * 10.0;
        
        return netWorth;
    }
}