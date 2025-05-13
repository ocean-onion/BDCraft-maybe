package com.bdcraft.plugin.modules.economy.modules.market;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a stall within a market where players can sell items.
 */
public class MarketStall {
    private final UUID id;
    private final UUID owner;
    private String name;
    private Location location;
    private List<StallItem> items = new ArrayList<>();
    private boolean active = true;
    
    /**
     * Creates a new market stall.
     * 
     * @param id The unique identifier for this stall
     * @param owner The UUID of the player who owns this stall
     * @param name The name of the stall
     * @param location The location of the stall
     */
    public MarketStall(UUID id, UUID owner, String name, Location location) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.location = location;
    }
    
    /**
     * Gets the unique ID of this stall.
     * 
     * @return The stall's UUID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the UUID of the player who owns this stall.
     * 
     * @return The owner's UUID
     */
    public UUID getOwner() {
        return owner;
    }
    
    /**
     * Gets the name of this stall.
     * 
     * @return The stall's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of this stall.
     * 
     * @param name The new name for the stall
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the location of this stall.
     * 
     * @return The stall's location
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Sets the location of this stall.
     * 
     * @param location The new location for the stall
     */
    public void setLocation(Location location) {
        this.location = location;
    }
    
    /**
     * Gets the list of items for sale in this stall.
     * 
     * @return The list of items for sale
     */
    public List<StallItem> getItems() {
        return new ArrayList<>(items);
    }
    
    /**
     * Adds an item for sale to this stall.
     * 
     * @param item The item to add
     */
    public void addItem(StallItem item) {
        items.add(item);
    }
    
    /**
     * Removes an item from this stall.
     * 
     * @param item The item to remove
     * @return True if the item was removed, false if it wasn't found
     */
    public boolean removeItem(StallItem item) {
        return items.remove(item);
    }
    
    /**
     * Gets an item by its ID.
     * 
     * @param itemId The ID of the item to find
     * @return The item, or null if not found
     */
    public StallItem getItem(UUID itemId) {
        for (StallItem item : items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Checks if this stall is active.
     * 
     * @return True if the stall is active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Sets whether this stall is active.
     * 
     * @param active The new active status
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Checks if a player owns this stall.
     * 
     * @param playerId The UUID of the player to check
     * @return True if the player owns this stall
     */
    public boolean isOwner(UUID playerId) {
        return owner.equals(playerId);
    }
    
    /**
     * Inner class representing an item for sale in a stall.
     */
    public static class StallItem {
        private final UUID id;
        private final ItemStack item;
        private int price;
        private int quantity;
        
        /**
         * Creates a new stall item.
         * 
         * @param id The unique identifier for this item
         * @param item The item stack that is for sale
         * @param price The price in emeralds
         * @param quantity The quantity available
         */
        public StallItem(UUID id, ItemStack item, int price, int quantity) {
            this.id = id;
            this.item = item.clone(); // Clone to prevent modification
            this.price = price;
            this.quantity = quantity;
        }
        
        /**
         * Gets the unique ID of this item.
         * 
         * @return The item's UUID
         */
        public UUID getId() {
            return id;
        }
        
        /**
         * Gets the item stack that is for sale.
         * 
         * @return A copy of the item stack
         */
        public ItemStack getItem() {
            return item.clone(); // Clone to prevent modification
        }
        
        /**
         * Gets the price in emeralds.
         * 
         * @return The price
         */
        public int getPrice() {
            return price;
        }
        
        /**
         * Sets the price in emeralds.
         * 
         * @param price The new price
         */
        public void setPrice(int price) {
            this.price = price;
        }
        
        /**
         * Gets the quantity available.
         * 
         * @return The quantity
         */
        public int getQuantity() {
            return quantity;
        }
        
        /**
         * Sets the quantity available.
         * 
         * @param quantity The new quantity
         */
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
        /**
         * Decreases the quantity by the specified amount.
         * 
         * @param amount The amount to decrease by
         * @return True if there was enough quantity, false otherwise
         */
        public boolean decreaseQuantity(int amount) {
            if (quantity < amount) {
                return false;
            }
            
            quantity -= amount;
            return true;
        }
    }
}