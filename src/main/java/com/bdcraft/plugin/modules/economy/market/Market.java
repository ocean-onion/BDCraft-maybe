package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a player-owned market in the game.
 */
public class Market {
    private final UUID id;
    private final UUID ownerId;
    private String name;
    private final String worldName;
    private final int centerX;
    private final int centerY;
    private final int centerZ;
    private int level;
    private int radius;
    private final Set<UUID> associates;
    private int totalSales;
    private int collectorsCount;
    private boolean unlimitedCollectors;
    
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
        this.name = name;
        this.worldName = center.getWorld().getName();
        this.centerX = center.getBlockX();
        this.centerY = center.getBlockY();
        this.centerZ = center.getBlockZ();
        this.level = 1;
        this.radius = radius;
        this.associates = new HashSet<>();
        this.totalSales = 0;
        this.collectorsCount = 0;
        this.unlimitedCollectors = false;
    }
    
    /**
     * Creates a new market with specified owner UUID.
     *
     * @param id The market ID
     * @param ownerUuid The UUID of the owner
     * @param name The market name
     * @param center The center location
     */
    public Market(UUID id, UUID ownerUuid, String name, Location center) {
        this.id = id;
        this.ownerId = ownerUuid;
        this.name = name;
        this.worldName = center.getWorld().getName();
        this.centerX = center.getBlockX();
        this.centerY = center.getBlockY();
        this.centerZ = center.getBlockZ();
        this.level = 1;
        this.radius = getRadiusForLevel(1);
        this.associates = new HashSet<>();
        this.totalSales = 0;
        this.collectorsCount = 0;
        this.unlimitedCollectors = false;
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
     * Gets the owner UUID (alias for getOwnerId).
     *
     * @return The owner UUID
     */
    public UUID getOwnerUuid() {
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
     * First attempts to get the name from an online player,
     * then falls back to offline player data if necessary.
     * 
     * @return The owner name, or "Unknown" if not retrievable
     */
    public String getOwnerName() {
        // Use PlayerExact for exact UUID matching to avoid ambiguity
        Player owner = Bukkit.getServer().getPlayerExact(ownerId.toString());
        if (owner != null && owner.isOnline()) {
            return owner.getName();
        }
        
        // Fall back to offline player data
        String ownerName = Bukkit.getOfflinePlayer(ownerId).getName();
        return ownerName != null ? ownerName : "Unknown";
    }
    
    /**
     * Gets the founder name (same as owner name for backward compatibility).
     * 
     * @return The founder name
     */
    public String getFounderName() {
        return getOwnerName();
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
     * Gets the center location of the market.
     *
     * @return The center location
     */
    public Location getCenterLocation() {
        return new Location(
            Bukkit.getWorld(worldName),
            centerX,
            centerY,
            centerZ
        );
    }
    
    /**
     * Gets the center location alias for compatibility with BDMarket.
     *
     * @return The center location
     */
    public Location getCenter() {
        return getCenterLocation();
    }
    
    /**
     * Checks if the market has been removed for compatibility with BDMarket.
     * Markets are permanently stored until explicitly removed by an admin.
     *
     * @return True if removed, false otherwise
     */
    public boolean isRemoved() {
        return false;
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
     * Upgrades the market by one level.
     * Capped at level 4.
     * 
     * @return True if the market was upgraded
     */
    public boolean upgrade() {
        if (level >= 4) {
            return false;
        }
        
        level++;
        // Update radius based on new level
        radius = getRadiusForLevel(level);
        return true;
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
        // Update radius based on new level
        this.radius = getRadiusForLevel(level);
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
     * Gets the radius for a specific level.
     *
     * @param level The level
     * @return The radius for that level
     */
    private int getRadiusForLevel(int level) {
        switch (level) {
            case 1:
                return 32;
            case 2:
                return 48;
            case 3:
                return 64;
            case 4:
                return 80;
            default:
                return 32;
        }
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
     * Checks if a player is associated with the market.
     *
     * @param uuid The player UUID
     * @return True if the player is the owner or an associate
     */
    public boolean isAssociated(UUID uuid) {
        return uuid.equals(ownerId) || associates.contains(uuid);
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
     * Gets the market associates as a list.
     * 
     * @return The associates as a list
     */
    public List<UUID> getAssociatesList() {
        return new ArrayList<>(associates);
    }
    
    /**
     * Adds an associate to the market.
     * 
     * @param playerId The player ID
     * @return True if added
     */
    public boolean addAssociate(UUID playerId) {
        if (associates.contains(playerId) || playerId.equals(ownerId)) {
            return false;
        }
        
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
    
    /**
     * Gets the current collector count.
     *
     * @return The number of collectors in this market
     */
    public int getCollectorsCount() {
        return collectorsCount;
    }
    
    /**
     * Increments the collector count.
     *
     * @return True if a new collector can be added
     */
    public boolean incrementCollectorsCount() {
        if (collectorsCount >= getMaxCollectors()) {
            return false;
        }
        
        collectorsCount++;
        return true;
    }
    
    /**
     * Decrements the collector count.
     */
    public void decrementCollectorsCount() {
        if (collectorsCount > 0) {
            collectorsCount--;
        }
    }
    
    /**
     * Gets the maximum number of collectors allowed in this market.
     *
     * @return The maximum number of collectors
     */
    public int getMaxCollectors() {
        if (unlimitedCollectors) {
            return Integer.MAX_VALUE;
        }
        
        switch (level) {
            case 1:
                return 3;
            case 2:
                return 5;
            case 3:
                return 7;
            case 4:
                return 10;
            default:
                return 0;
        }
    }
    
    /**
     * Gets the price multiplier for this market.
     *
     * @return The price multiplier
     */
    public double getPriceMultiplier() {
        switch (level) {
            case 2:
                return 1.05;  // 5% better prices
            case 3:
                return 1.10;  // 10% better prices
            case 4:
                return 1.15;  // 15% better prices
            default:
                return 1.0;
        }
    }
    
    /**
     * Sets whether this market has unlimited collectors.
     *
     * @param unlimited True if the market should have unlimited collectors
     */
    public void setUnlimitedCollectors(boolean unlimited) {
        this.unlimitedCollectors = unlimited;
    }
    
    /**
     * Checks if this market has unlimited collectors.
     *
     * @return True if the market has unlimited collectors
     */
    public boolean hasUnlimitedCollectors() {
        return unlimitedCollectors;
    }
    
    /**
     * Serializes the market to a string for storage.
     *
     * @return The serialized market data
     */
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(id.toString()).append(";");
        sb.append(name).append(";");
        sb.append(ownerId.toString()).append(";");
        sb.append(worldName).append(",");
        sb.append(centerX).append(",");
        sb.append(centerY).append(",");
        sb.append(centerZ).append(";");
        sb.append(level).append(";");
        sb.append(collectorsCount).append(";");
        sb.append(unlimitedCollectors ? "1" : "0").append(";");
        sb.append(totalSales).append(";");
        
        // Serialize associates
        if (associates.isEmpty()) {
            sb.append("-");
        } else {
            for (int i = 0; i < associates.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(new ArrayList<>(associates).get(i).toString());
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Deserializes a market from a string.
     *
     * @param data The serialized market data
     * @return The deserialized market, or null if invalid
     */
    public static Market deserialize(String data) {
        try {
            String[] parts = data.split(";");
            
            if (parts.length < 7) {
                return null;
            }
            
            UUID id = UUID.fromString(parts[0]);
            String name = parts[1];
            UUID ownerUuid = UUID.fromString(parts[2]);
            
            // Parse location
            String[] locParts = parts[3].split(",");
            if (locParts.length != 4) {
                return null;
            }
            
            String worldName = locParts[0];
            double x = Double.parseDouble(locParts[1]);
            double y = Double.parseDouble(locParts[2]);
            double z = Double.parseDouble(locParts[3]);
            
            Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
            
            // Create market
            Market market = new Market(id, ownerUuid, name, location);
            
            // Set level
            market.setLevel(Integer.parseInt(parts[4]));
            
            // Set collectors count if available
            if (parts.length > 5) {
                market.collectorsCount = Integer.parseInt(parts[5]);
            }
            
            // Set unlimited collectors if available
            if (parts.length > 6) {
                market.unlimitedCollectors = parts[6].equals("1");
            }
            
            // Set total sales if available
            if (parts.length > 7) {
                market.totalSales = Integer.parseInt(parts[7]);
            }
            
            // Parse associates
            if (parts.length > 8 && !parts[8].equals("-")) {
                String[] associateParts = parts[8].split(",");
                for (String associatePart : associateParts) {
                    if (!associatePart.isEmpty()) {
                        market.addAssociate(UUID.fromString(associatePart));
                    }
                }
            }
            
            return market;
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to deserialize market: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Checks if this market has sound effects enabled.
     *
     * @return True if sound effects are enabled
     */
    public boolean hasSounds() {
        // Return true by default or based on market level
        return level > 1;
    }
    
    /**
     * Checks if this market has particle effects enabled.
     *
     * @return True if particle effects are enabled
     */
    public boolean hasParticles() {
        // Return true by default or based on market level
        return level > 2;
    }
}