package com.bdcraft.plugin.modules.economy.modules.market;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a market in the economy system where players can trade.
 */
public class Market {
    private final UUID id;
    private String name;
    private UUID owner;
    private Location center;
    private List<MarketStall> stalls = new ArrayList<>();
    
    /**
     * Creates a new market.
     * 
     * @param id The unique identifier for this market
     * @param name The name of the market
     * @param owner The UUID of the player who owns this market
     * @param center The center location of the market
     */
    public Market(UUID id, String name, UUID owner, Location center) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.center = center;
    }
    
    /**
     * Gets the unique ID of this market.
     * 
     * @return The market's UUID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the name of this market.
     * 
     * @return The market's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of this market.
     * 
     * @param name The new name for the market
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the UUID of the player who owns this market.
     * 
     * @return The owner's UUID
     */
    public UUID getOwner() {
        return owner;
    }
    
    /**
     * Sets the owner of this market.
     * 
     * @param owner The UUID of the new owner
     */
    public void setOwner(UUID owner) {
        this.owner = owner;
    }
    
    /**
     * Gets the center location of this market.
     * 
     * @return The center location
     */
    public Location getCenter() {
        return center;
    }
    
    /**
     * Sets the center location of this market.
     * 
     * @param center The new center location
     */
    public void setCenter(Location center) {
        this.center = center;
    }
    
    /**
     * Gets the list of stalls in this market.
     * 
     * @return The list of stalls
     */
    public List<MarketStall> getStalls() {
        return new ArrayList<>(stalls);
    }
    
    /**
     * Adds a stall to this market.
     * 
     * @param stall The stall to add
     */
    public void addStall(MarketStall stall) {
        stalls.add(stall);
    }
    
    /**
     * Removes a stall from this market.
     * 
     * @param stall The stall to remove
     * @return True if the stall was removed, false if it wasn't found
     */
    public boolean removeStall(MarketStall stall) {
        return stalls.remove(stall);
    }
    
    /**
     * Gets a stall by its ID.
     * 
     * @param stallId The ID of the stall to find
     * @return The stall, or null if not found
     */
    public MarketStall getStall(UUID stallId) {
        for (MarketStall stall : stalls) {
            if (stall.getId().equals(stallId)) {
                return stall;
            }
        }
        return null;
    }
    
    /**
     * Checks if a player owns this market.
     * 
     * @param playerId The UUID of the player to check
     * @return True if the player owns this market
     */
    public boolean isOwner(UUID playerId) {
        return owner.equals(playerId);
    }
}