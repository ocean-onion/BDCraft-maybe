package com.bdcraft.plugin.modules.economy.auction;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.Date;

/**
 * Represents an item listed in the BD Auction House.
 */
public class AuctionItem {
    private final UUID id;
    private final UUID sellerUUID;
    private final String sellerName;
    private final ItemStack item;
    private final int price;
    private final Date listedDate;
    private boolean sold;
    private Date soldDate;
    private UUID buyerUUID;
    private String buyerName;
    
    /**
     * Creates a new auction item.
     * 
     * @param seller The player who listed the item
     * @param item The item stack being listed
     * @param price The listing price in BD coins
     */
    public AuctionItem(Player seller, ItemStack item, int price) {
        this.id = UUID.randomUUID();
        this.sellerUUID = seller.getUniqueId();
        this.sellerName = seller.getName();
        this.item = item.clone(); // Clone to avoid modifications
        this.price = price;
        this.listedDate = new Date();
        this.sold = false;
        this.soldDate = null;
        this.buyerUUID = null;
        this.buyerName = null;
    }
    
    /**
     * Creates an auction item from serialized data.
     * 
     * @param id The item ID
     * @param sellerUUID The seller's UUID
     * @param sellerName The seller's name
     * @param item The item stack
     * @param price The price
     * @param listedDate The date listed
     * @param sold Whether the item is sold
     * @param soldDate The date sold, or null
     * @param buyerUUID The buyer's UUID, or null
     * @param buyerName The buyer's name, or null
     */
    public AuctionItem(UUID id, UUID sellerUUID, String sellerName, ItemStack item, 
            int price, Date listedDate, boolean sold, Date soldDate, 
            UUID buyerUUID, String buyerName) {
        this.id = id;
        this.sellerUUID = sellerUUID;
        this.sellerName = sellerName;
        this.item = item;
        this.price = price;
        this.listedDate = listedDate;
        this.sold = sold;
        this.soldDate = soldDate;
        this.buyerUUID = buyerUUID;
        this.buyerName = buyerName;
    }
    
    /**
     * Gets the ID of the auction item.
     * 
     * @return The UUID
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Gets the UUID of the seller.
     * 
     * @return The seller's UUID
     */
    public UUID getSellerUUID() {
        return sellerUUID;
    }
    
    /**
     * Gets the name of the seller.
     * 
     * @return The seller's name
     */
    public String getSellerName() {
        return sellerName;
    }
    
    /**
     * Gets the listed item.
     * 
     * @return The item stack
     */
    public ItemStack getItem() {
        return item.clone(); // Return a clone to prevent modifications
    }
    
    /**
     * Gets the price of the item.
     * 
     * @return The price in BD coins
     */
    public int getPrice() {
        return price;
    }
    
    /**
     * Gets the date the item was listed.
     * 
     * @return The listed date
     */
    public Date getListedDate() {
        return listedDate;
    }
    
    /**
     * Checks if the item is sold.
     * 
     * @return True if sold
     */
    public boolean isSold() {
        return sold;
    }
    
    /**
     * Gets the date the item was sold.
     * 
     * @return The sold date, or null if not sold
     */
    public Date getSoldDate() {
        return soldDate;
    }
    
    /**
     * Gets the UUID of the buyer.
     * 
     * @return The buyer's UUID, or null if not sold
     */
    public UUID getBuyerUUID() {
        return buyerUUID;
    }
    
    /**
     * Gets the name of the buyer.
     * 
     * @return The buyer's name, or null if not sold
     */
    public String getBuyerName() {
        return buyerName;
    }
    
    /**
     * Marks the item as sold.
     * 
     * @param buyer The buyer
     */
    public void markAsSold(Player buyer) {
        this.sold = true;
        this.soldDate = new Date();
        this.buyerUUID = buyer.getUniqueId();
        this.buyerName = buyer.getName();
    }
    
    /**
     * Creates a formatted display of the auction item.
     * 
     * @return The formatted display
     */
    public String getFormattedDisplay() {
        StringBuilder display = new StringBuilder();
        String itemName = item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? 
                item.getItemMeta().getDisplayName() : 
                item.getType().toString().toLowerCase().replace('_', ' ');
        
        display.append(ChatColor.GOLD).append("Item: ").append(ChatColor.WHITE).append(itemName).append("\n");
        display.append(ChatColor.GOLD).append("Seller: ").append(ChatColor.WHITE).append(sellerName).append("\n");
        display.append(ChatColor.GOLD).append("Price: ").append(ChatColor.GREEN).append(price).append(" BD coins").append("\n");
        
        if (sold) {
            display.append(ChatColor.RED).append("SOLD to ").append(ChatColor.WHITE).append(buyerName);
        } else {
            display.append(ChatColor.GREEN).append("Available for purchase");
        }
        
        return display.toString();
    }
    
    @Override
    public String toString() {
        return "AuctionItem{" +
                "id=" + id +
                ", seller=" + sellerName +
                ", item=" + item.getType() +
                ", price=" + price +
                ", sold=" + sold +
                '}';
    }
}