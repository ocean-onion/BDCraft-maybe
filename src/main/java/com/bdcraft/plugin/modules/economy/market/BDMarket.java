package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a player-owned market in the BDCraft economy system.
 */
public class BDMarket {
    private final UUID id;
    private final String name;
    private final UUID ownerUuid;
    private final List<UUID> associates;
    private final Location centerLocation;
    private int level;
    private int collectorsCount;
    private boolean unlimitedCollectors;
    
    /**
     * Creates a new market.
     *
     * @param id The market ID
     * @param name The market name
     * @param ownerUuid The UUID of the market owner
     * @param centerLocation The center location of the market
     */
    public BDMarket(UUID id, String name, UUID ownerUuid, Location centerLocation) {
        this.id = id;
        this.name = name;
        this.ownerUuid = ownerUuid;
        this.centerLocation = centerLocation;
        this.associates = new ArrayList<>();
        this.level = 1;
        this.collectorsCount = 0;
        this.unlimitedCollectors = false;
    }
    
    /**
     * Gets the market ID.
     *
     * @return The market ID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the market name.
     *
     * @return The market name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the UUID of the market owner.
     *
     * @return The owner UUID
     */
    public UUID getOwnerUuid() {
        return ownerUuid;
    }
    
    /**
     * Gets the owner name.
     *
     * @return The owner name, or "Unknown" if offline
     */
    public String getOwnerName() {
        Player owner = Bukkit.getPlayer(ownerUuid);
        if (owner != null) {
            return owner.getName();
        }
        
        String name = Bukkit.getOfflinePlayer(ownerUuid).getName();
        return name != null ? name : "Unknown";
    }
    
    /**
     * Gets the center location of the market.
     *
     * @return The center location
     */
    public Location getCenterLocation() {
        return centerLocation.clone();
    }
    
    /**
     * Gets the market level.
     *
     * @return The market level (1-4)
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Gets the list of associates.
     *
     * @return The list of associate UUIDs
     */
    public List<UUID> getAssociates() {
        return new ArrayList<>(associates);
    }
    
    /**
     * Adds an associate to the market.
     *
     * @param uuid The associate UUID
     * @return True if the associate was added
     */
    public boolean addAssociate(UUID uuid) {
        if (associates.contains(uuid) || uuid.equals(ownerUuid)) {
            return false;
        }
        
        associates.add(uuid);
        return true;
    }
    
    /**
     * Removes an associate from the market.
     *
     * @param uuid The associate UUID
     * @return True if the associate was removed
     */
    public boolean removeAssociate(UUID uuid) {
        return associates.remove(uuid);
    }
    
    /**
     * Checks if a player is associated with the market.
     *
     * @param uuid The player UUID
     * @return True if the player is the owner or an associate
     */
    public boolean isAssociated(UUID uuid) {
        return uuid.equals(ownerUuid) || associates.contains(uuid);
    }
    
    /**
     * Upgrades the market to the next level.
     *
     * @return True if the market was upgraded
     */
    public boolean upgrade() {
        if (level >= 4) {
            return false;
        }
        
        level++;
        return true;
    }
    
    /**
     * Sets the market level.
     *
     * @param level The market level
     */
    public void setLevel(int level) {
        if (level < 1 || level > 4) {
            throw new IllegalArgumentException("Invalid market level: " + level);
        }
        
        this.level = level;
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
        sb.append(ownerUuid.toString()).append(";");
        sb.append(centerLocation.getWorld().getName()).append(",");
        sb.append(centerLocation.getX()).append(",");
        sb.append(centerLocation.getY()).append(",");
        sb.append(centerLocation.getZ()).append(";");
        sb.append(level).append(";");
        sb.append(collectorsCount).append(";");
        sb.append(unlimitedCollectors ? "1" : "0").append(";");
        
        // Serialize associates
        if (associates.isEmpty()) {
            sb.append("-");
        } else {
            for (int i = 0; i < associates.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(associates.get(i).toString());
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
    public static BDMarket deserialize(String data) {
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
            BDMarket market = new BDMarket(id, name, ownerUuid, location);
            
            // Set level and collectors count
            market.setLevel(Integer.parseInt(parts[4]));
            market.collectorsCount = Integer.parseInt(parts[5]);
            market.unlimitedCollectors = parts[6].equals("1");
            
            // Parse associates
            if (parts.length > 7 && !parts[7].equals("-")) {
                String[] associateParts = parts[7].split(",");
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
}