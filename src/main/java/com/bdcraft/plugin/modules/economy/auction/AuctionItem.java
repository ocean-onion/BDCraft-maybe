package com.bdcraft.plugin.modules.economy.auction;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Represents an item listed in the auction house.
 */
public class AuctionItem {
    private final UUID id;
    private final UUID sellerId;
    private final String sellerName;
    private final ItemStack item;
    private final double price;
    private final long listTime;
    private final long expireTime;
    
    /**
     * Creates a new auction item.
     * 
     * @param id The auction ID
     * @param sellerId The seller's ID
     * @param sellerName The seller's name
     * @param item The item being sold
     * @param price The price
     * @param listTime The time when the auction was listed
     * @param expireTime The time when the auction expires
     */
    public AuctionItem(UUID id, UUID sellerId, String sellerName, ItemStack item, double price, long listTime, long expireTime) {
        this.id = id;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.item = item;
        this.price = price;
        this.listTime = listTime;
        this.expireTime = expireTime;
    }
    
    /**
     * Gets the auction ID.
     * 
     * @return The ID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the seller's ID.
     * 
     * @return The seller's ID
     */
    public UUID getSellerId() {
        return sellerId;
    }
    
    /**
     * Gets the seller's name.
     * 
     * @return The seller's name
     */
    public String getSellerName() {
        return sellerName;
    }
    
    /**
     * Gets the item being sold.
     * 
     * @return The item
     */
    public ItemStack getItem() {
        return item.clone();
    }
    
    /**
     * Gets the price.
     * 
     * @return The price
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Gets the time when the auction was listed.
     * 
     * @return The list time
     */
    public long getListTime() {
        return listTime;
    }
    
    /**
     * Gets the time when the auction was listed (as Date).
     * 
     * @return The list date
     */
    public java.util.Date getListedDate() {
        return new java.util.Date(listTime);
    }
    
    /**
     * Gets the time when the auction expires.
     * 
     * @return The expire time
     */
    public long getExpireTime() {
        return expireTime;
    }
    
    /**
     * Checks if the auction is expired.
     * 
     * @return True if expired
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }
    
    /**
     * Gets the remaining time in milliseconds.
     * 
     * @return The remaining time
     */
    public long getRemainingTime() {
        long now = System.currentTimeMillis();
        if (now > expireTime) {
            return 0;
        }
        return expireTime - now;
    }
    
    /**
     * Gets a formatted string of the remaining time.
     * 
     * @return The formatted remaining time
     */
    public String getFormattedRemainingTime() {
        long remaining = getRemainingTime();
        
        if (remaining <= 0) {
            return "Expired";
        }
        
        long seconds = remaining / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        hours %= 24;
        minutes %= 60;
        seconds %= 60;
        
        StringBuilder sb = new StringBuilder();
        
        if (days > 0) {
            sb.append(days).append("d ");
        }
        
        if (hours > 0 || days > 0) {
            sb.append(hours).append("h ");
        }
        
        if (minutes > 0 || hours > 0 || days > 0) {
            sb.append(minutes).append("m ");
        }
        
        sb.append(seconds).append("s");
        
        return sb.toString();
    }
    
    // Fields for sale tracking
    private boolean sold = false;
    private UUID buyerId = null;
    private String buyerName = null;
    private long soldTime = 0;
    
    /**
     * Checks if the auction item has been sold.
     * 
     * @return True if sold
     */
    public boolean isSold() {
        return sold;
    }
    
    /**
     * Marks the item as sold.
     * 
     * @param buyerId The buyer's ID
     * @param buyerName The buyer's name
     */
    public void markAsSold(UUID buyerId, String buyerName) {
        this.sold = true;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.soldTime = System.currentTimeMillis();
    }
    
    /**
     * Gets the buyer's ID.
     * 
     * @return The buyer's ID, or null if not sold
     */
    public UUID getBuyerId() {
        return buyerId;
    }
    
    /**
     * Gets the buyer's name.
     * 
     * @return The buyer's name, or null if not sold
     */
    public String getBuyerName() {
        return buyerName;
    }
    
    /**
     * Gets the time when the item was sold.
     * 
     * @return The time when sold, or 0 if not sold
     */
    public long getSoldTime() {
        return soldTime;
    }
    
    /**
     * Gets the date when the item was sold.
     * 
     * @return The date when sold, or null if not sold
     */
    public java.util.Date getSoldDate() {
        return soldTime > 0 ? new java.util.Date(soldTime) : null;
    }
}