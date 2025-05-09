package com.bdcraft.plugin.modules.economy.market;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Extended Market class with BD-specific features.
 */
public class BDMarket {
    private final Market market;
    private boolean openBreak;
    private boolean hasParticles;
    private boolean hasSounds;
    private int weeklyTradeCount;
    private double priceModifier;
    private boolean removed;
    
    /**
     * Creates a new BD market wrapper around a base market.
     * 
     * @param market The base market
     */
    public BDMarket(Market market) {
        this.market = market;
        this.openBreak = false;
        this.hasParticles = true;
        this.hasSounds = true;
        this.weeklyTradeCount = 0;
        this.priceModifier = 1.0;
        this.removed = false;
    }
    
    /**
     * Gets the wrapped market.
     * 
     * @return The market
     */
    public Market getMarket() {
        return market;
    }
    
    /**
     * Gets the market ID.
     * 
     * @return The ID
     */
    public UUID getId() {
        return market.getId();
    }
    
    /**
     * Gets the market name.
     * 
     * @return The name
     */
    public String getName() {
        return market.getName();
    }
    
    /**
     * Gets the owner ID.
     * 
     * @return The owner ID
     */
    public UUID getOwnerId() {
        return market.getOwnerId();
    }
    
    /**
     * Gets the owner name.
     * 
     * @return The owner name
     */
    public String getOwnerName() {
        return market.getOwnerName();
    }
    
    /**
     * Gets the founder name (same as owner name for backward compatibility).
     * 
     * @return The founder name
     */
    public String getFounderName() {
        return market.getFounderName();
    }
    
    /**
     * Gets the founder ID (same as owner ID for backward compatibility).
     * 
     * @return The founder ID
     */
    public UUID getFounderId() {
        return market.getFounderId();
    }
    
    /**
     * Gets the world name.
     * 
     * @return The world name
     */
    public String getWorldName() {
        return market.getWorldName();
    }
    
    /**
     * Gets the center X coordinate.
     * 
     * @return The center X
     */
    public int getCenterX() {
        return market.getCenterX();
    }
    
    /**
     * Gets the center Y coordinate.
     * 
     * @return The center Y
     */
    public int getCenterY() {
        return market.getCenterY();
    }
    
    /**
     * Gets the center Z coordinate.
     * 
     * @return The center Z
     */
    public int getCenterZ() {
        return market.getCenterZ();
    }
    
    /**
     * Gets the center location.
     * 
     * @return The center location
     */
    public Location getCenter() {
        World world = org.bukkit.Bukkit.getWorld(getWorldName());
        if (world != null) {
            return new Location(world, getCenterX(), getCenterY(), getCenterZ());
        }
        return null;
    }
    
    /**
     * Gets the market level.
     * 
     * @return The level
     */
    public int getLevel() {
        return market.getLevel();
    }
    
    /**
     * Gets the market radius.
     * 
     * @return The radius
     */
    public int getRadius() {
        return market.getRadius();
    }
    
    /**
     * Checks if a player is the owner of this market.
     * 
     * @param playerId The player ID
     * @return True if owner
     */
    public boolean isOwner(UUID playerId) {
        return market.isOwner(playerId);
    }
    
    /**
     * Checks if a player is an associate of this market.
     * 
     * @param playerId The player ID
     * @return True if associate
     */
    public boolean isAssociate(UUID playerId) {
        return market.isAssociate(playerId);
    }
    
    /**
     * Gets the market associates.
     * 
     * @return The associates
     */
    public Set<UUID> getAssociates() {
        return market.getAssociates();
    }
    
    /**
     * Adds an associate to the market.
     * 
     * @param playerId The player ID
     * @return True if added
     */
    public boolean addAssociate(UUID playerId) {
        return market.addAssociate(playerId);
    }
    
    /**
     * Removes an associate from the market.
     * 
     * @param playerId The player ID
     * @return True if removed
     */
    public boolean removeAssociate(UUID playerId) {
        return market.removeAssociate(playerId);
    }
    
    /**
     * Checks if the market allows open breaking.
     * 
     * @return True if open breaking is allowed
     */
    public boolean isOpenBreak() {
        return openBreak;
    }
    
    /**
     * Sets whether the market allows open breaking.
     * 
     * @param openBreak Whether to allow open breaking
     */
    public void setOpenBreak(boolean openBreak) {
        this.openBreak = openBreak;
    }
    
    /**
     * Checks if the market has particles enabled.
     * 
     * @return True if particles are enabled
     */
    public boolean hasParticles() {
        return hasParticles;
    }
    
    /**
     * Sets whether the market has particles enabled.
     * 
     * @param hasParticles Whether to enable particles
     */
    public void setParticles(boolean hasParticles) {
        this.hasParticles = hasParticles;
    }
    
    /**
     * Checks if the market has sounds enabled.
     * 
     * @return True if sounds are enabled
     */
    public boolean hasSounds() {
        return hasSounds;
    }
    
    /**
     * Sets whether the market has sounds enabled.
     * 
     * @param hasSounds Whether to enable sounds
     */
    public void setSounds(boolean hasSounds) {
        this.hasSounds = hasSounds;
    }
    
    /**
     * Gets the weekly trade count.
     * 
     * @return The weekly trade count
     */
    public int getWeeklyTradeCount() {
        return weeklyTradeCount;
    }
    
    /**
     * Sets the weekly trade count.
     * 
     * @param weeklyTradeCount The weekly trade count
     */
    public void setWeeklyTradeCount(int weeklyTradeCount) {
        this.weeklyTradeCount = weeklyTradeCount;
    }
    
    /**
     * Increments the weekly trade count.
     */
    public void incrementWeeklyTradeCount() {
        this.weeklyTradeCount++;
    }
    
    /**
     * Gets the total sales for this market.
     * 
     * @return The total sales
     */
    public int getTotalSales() {
        return market.getTotalSales();
    }
    
    /**
     * Increments the total sales count.
     */
    public void incrementTotalSales() {
        market.incrementTotalSales();
    }
    
    /**
     * Gets the price modifier.
     * 
     * @return The price modifier
     */
    public double getPriceModifier() {
        return priceModifier;
    }
    
    /**
     * Sets the price modifier.
     * 
     * @param priceModifier The price modifier
     */
    public void setPriceModifier(double priceModifier) {
        this.priceModifier = priceModifier;
    }
    
    /**
     * Checks if the market is removed.
     * 
     * @return True if removed
     */
    public boolean isRemoved() {
        return removed;
    }
    
    /**
     * Sets whether the market is removed.
     * 
     * @param removed Whether the market is removed
     */
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
    
    /**
     * Gets the count of a specific trader type.
     * 
     * @param traderType The trader type
     * @return The count
     */
    public int getTraderCount(String traderType) {
        // Implementation would need to track traders
        return 0;
    }
    
    /**
     * Upgrades the market to the next level.
     * 
     * @return True if upgraded successfully
     */
    public boolean upgrade() {
        boolean upgraded = market.setLevel(market.getLevel() + 1);
        
        if (upgraded) {
            // Adjust price modifier based on new level
            switch (market.getLevel()) {
                case 2:
                    setPriceModifier(1.05); // 5% better prices
                    break;
                case 3:
                    setPriceModifier(1.10); // 10% better prices
                    break;
                case 4:
                    setPriceModifier(1.15); // 15% better prices
                    break;
            }
        }
        
        return upgraded;
    }
}