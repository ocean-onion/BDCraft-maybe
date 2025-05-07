package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.villager.BDVillager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a BD Market in the game world.
 */
public class BDMarket {
    private final BDCraft plugin;
    private final UUID id;
    private final Location center;
    private final String founderName;
    private final UUID founderUUID;
    private int level;
    private final Map<UUID, BDVillager> traders;
    private final Map<String, List<UUID>> tradersByType;
    
    /**
     * Creates a new BD Market.
     * @param plugin The plugin instance
     * @param center The center location of the market
     * @param founder The founder player
     */
    public BDMarket(BDCraft plugin, Location center, Player founder) {
        this.plugin = plugin;
        this.id = UUID.randomUUID();
        this.center = center;
        this.founderName = founder.getName();
        this.founderUUID = founder.getUniqueId();
        this.level = 1;
        this.traders = new HashMap<>();
        this.tradersByType = new HashMap<>();
    }
    
    /**
     * Gets the unique ID of this market.
     * @return The market ID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the center location of this market.
     * @return The center location
     */
    public Location getCenter() {
        return center;
    }
    
    /**
     * Gets the name of the player who founded this market.
     * @return The founder's name
     */
    public String getFounderName() {
        return founderName;
    }
    
    /**
     * Gets the UUID of the player who founded this market.
     * @return The founder's UUID
     */
    public UUID getFounderUUID() {
        return founderUUID;
    }
    
    /**
     * Gets the level of this market.
     * @return The market level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Sets the level of this market.
     * @param level The new market level
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Adds a trader to this market.
     * @param villager The trader to add
     * @return Whether the trader was added
     */
    public boolean addTrader(BDVillager villager) {
        UUID traderUUID = villager.getUUID();
        String traderType = villager.getType();
        
        // Add to traders map
        traders.put(traderUUID, villager);
        
        // Add to traders by type map
        if (!tradersByType.containsKey(traderType)) {
            tradersByType.put(traderType, new ArrayList<>());
        }
        
        tradersByType.get(traderType).add(traderUUID);
        
        return true;
    }
    
    /**
     * Removes a trader from this market.
     * @param traderUUID The UUID of the trader to remove
     * @return Whether the trader was removed
     */
    public boolean removeTrader(UUID traderUUID) {
        if (!traders.containsKey(traderUUID)) {
            return false;
        }
        
        BDVillager villager = traders.get(traderUUID);
        String traderType = villager.getType();
        
        // Remove from traders map
        traders.remove(traderUUID);
        
        // Remove from traders by type map
        if (tradersByType.containsKey(traderType)) {
            tradersByType.get(traderType).remove(traderUUID);
        }
        
        return true;
    }
    
    /**
     * Gets all traders in this market.
     * @return The traders
     */
    public Map<UUID, BDVillager> getTraders() {
        return traders;
    }
    
    /**
     * Gets all traders of a specific type in this market.
     * @param type The trader type
     * @return The traders of that type
     */
    public List<BDVillager> getTradersByType(String type) {
        if (!tradersByType.containsKey(type)) {
            return new ArrayList<>();
        }
        
        List<BDVillager> result = new ArrayList<>();
        for (UUID traderUUID : tradersByType.get(type)) {
            if (traders.containsKey(traderUUID)) {
                result.add(traders.get(traderUUID));
            }
        }
        
        return result;
    }
    
    /**
     * Gets the count of traders of a specific type in this market.
     * @param type The trader type
     * @return The number of traders of that type
     */
    public int getTraderCount(String type) {
        if (!tradersByType.containsKey(type)) {
            return 0;
        }
        
        return tradersByType.get(type).size();
    }
    
    /**
     * Checks if a location is within this market's bounds.
     * @param location The location to check
     * @return Whether the location is within this market
     */
    public boolean isInMarket(Location location) {
        if (!center.getWorld().equals(location.getWorld())) {
            return false;
        }
        
        // Market radius is 24 blocks (49x49 area)
        double distance = center.distance(location);
        return distance <= 24;
    }
    
    /**
     * Gets the market radius.
     * @return The market radius
     */
    public int getRadius() {
        return 24; // 49x49 area
    }
    
    /**
     * Gets the price modifier for this market based on its level.
     * @return The price modifier
     */
    public double getPriceModifier() {
        // Each level increases prices by 5%
        return 1.0 + ((level - 1) * 0.05);
    }
}