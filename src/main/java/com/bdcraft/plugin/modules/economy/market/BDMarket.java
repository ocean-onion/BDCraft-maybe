package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a BD market.
 */
public class BDMarket {
    private final UUID id;
    private final Location center;
    private final UUID founderId;
    private final String founderName;
    private int level;
    private double priceModifier;
    
    // Maps trader UUIDs to their types
    private final Map<UUID, String> traders;
    
    /**
     * Creates a new BD market.
     * @param id The market ID
     * @param center The center of the market
     * @param founderId The founder's ID
     * @param founderName The founder's name
     */
    public BDMarket(UUID id, Location center, UUID founderId, String founderName) {
        this.id = id;
        this.center = center;
        this.founderId = founderId;
        this.founderName = founderName;
        this.level = 1; // Start at level 1
        this.priceModifier = 1.0; // Default price modifier
        this.traders = new HashMap<>();
    }
    
    /**
     * Gets the market ID.
     * @return The market ID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the center of the market.
     * @return The center
     */
    public Location getCenter() {
        return center;
    }
    
    /**
     * Gets the founder's ID.
     * @return The founder's ID
     */
    public UUID getFounderId() {
        return founderId;
    }
    
    /**
     * Gets the founder's name.
     * @return The founder's name
     */
    public String getFounderName() {
        return founderName;
    }
    
    /**
     * Gets the market level.
     * @return The level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Sets the market level.
     * @param level The new level
     */
    public void setLevel(int level) {
        if (level < 1) {
            level = 1;
        } else if (level > 4) {
            level = 4;
        }
        
        this.level = level;
        
        // Update price modifier with level
        this.priceModifier = 1.0 + (0.1 * (level - 1)); // 10% increase per level
    }
    
    /**
     * Gets the price modifier.
     * @return The price modifier
     */
    public double getPriceModifier() {
        return priceModifier;
    }
    
    /**
     * Sets the price modifier.
     * @param priceModifier The new price modifier
     */
    public void setPriceModifier(double priceModifier) {
        if (priceModifier < 0.5) {
            priceModifier = 0.5;
        } else if (priceModifier > 2.0) {
            priceModifier = 2.0;
        }
        
        this.priceModifier = priceModifier;
    }
    
    /**
     * Adds a trader to the market.
     * @param traderId The trader's ID
     * @param traderType The trader's type
     */
    public void addTrader(UUID traderId, String traderType) {
        traders.put(traderId, traderType);
    }
    
    /**
     * Removes a trader from the market.
     * @param traderId The trader's ID
     */
    public void removeTrader(UUID traderId) {
        traders.remove(traderId);
    }
    
    /**
     * Gets all trader IDs in the market.
     * @return The trader IDs
     */
    public List<UUID> getTraders() {
        return new ArrayList<>(traders.keySet());
    }
    
    /**
     * Gets the type of a trader.
     * @param traderId The trader's ID
     * @return The trader's type, or null if not found
     */
    public String getTraderType(UUID traderId) {
        return traders.get(traderId);
    }
    
    /**
     * Gets the number of traders of a specific type.
     * @param traderType The trader type
     * @return The number of traders
     */
    public int getTraderCount(String traderType) {
        int count = 0;
        
        for (String type : traders.values()) {
            if (type.equalsIgnoreCase(traderType)) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Gets the total number of traders.
     * @return The total number of traders
     */
    public int getTotalTraderCount() {
        return traders.size();
    }
    
    /**
     * Gets the radius of the market.
     * @return The radius
     */
    public int getRadius() {
        return 20 + (level * 5); // Base radius + 5 per level
    }
    
    /**
     * Checks if a location is within the market radius.
     * @param location The location to check
     * @return Whether the location is within the radius
     */
    public boolean isInRadius(Location location) {
        if (!center.getWorld().equals(location.getWorld())) {
            return false;
        }
        
        double distance = center.distance(location);
        return distance <= getRadius();
    }
}