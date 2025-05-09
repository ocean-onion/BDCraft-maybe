package com.bdcraft.plugin.modules.economy.auction;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manager for the BD auction house.
 */
public class AuctionManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<UUID, List<AuctionItem>> playerAuctions;
    private final List<AuctionItem> activeAuctions;
    
    /**
     * Creates a new auction manager.
     * 
     * @param plugin The plugin instance
     */
    public AuctionManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.playerAuctions = new HashMap<>();
        this.activeAuctions = new ArrayList<>();
    }
    
    /**
     * Lists an item for auction.
     * 
     * @param player The player
     * @param item The item
     * @param price The price
     * @param duration The duration in milliseconds
     * @return The created auction or null if failed
     */
    public AuctionItem listItem(Player player, ItemStack item, double price, long duration) {
        if (item == null || item.getType().isAir()) {
            return null;
        }
        
        // Create the auction
        AuctionItem auction = new AuctionItem(
                UUID.randomUUID(),
                player.getUniqueId(),
                player.getName(),
                item.clone(),
                price,
                System.currentTimeMillis(),
                System.currentTimeMillis() + duration
        );
        
        // Add to active auctions
        activeAuctions.add(auction);
        
        // Add to player auctions
        List<AuctionItem> auctions = playerAuctions.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        auctions.add(auction);
        
        return auction;
    }
    
    /**
     * Gets active auctions.
     * 
     * @return The active auctions
     */
    public List<AuctionItem> getActiveAuctions() {
        return new ArrayList<>(activeAuctions);
    }
    
    /**
     * Gets active listings.
     * 
     * @return The active listings
     */
    public List<AuctionItem> getActiveListings() {
        return getActiveAuctions();
    }
    
    /**
     * Gets a player's auctions.
     * 
     * @param playerId The player ID
     * @return The player's auctions
     */
    public List<AuctionItem> getPlayerAuctions(UUID playerId) {
        return playerAuctions.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Gets listings by seller.
     * 
     * @param playerId The player ID
     * @param includeExpired Whether to include expired listings
     * @return The player's listings
     */
    public List<AuctionItem> getListingsBySeller(UUID playerId, boolean includeExpired) {
        List<AuctionItem> listings = new ArrayList<>();
        
        for (AuctionItem item : getPlayerAuctions(playerId)) {
            if (includeExpired || !item.isExpired()) {
                listings.add(item);
            }
        }
        
        return listings;
    }
    
    /**
     * Checks if a player has pending returns.
     * 
     * @param player The player
     * @return True if the player has pending returns
     */
    public boolean hasPendingReturns(Player player) {
        // For now, just return false as we don't track returns
        return false;
    }
    
    /**
     * Gets a player's pending payments.
     * 
     * @param player The player
     * @return The pending payment amount
     */
    public int getPendingPayment(Player player) {
        // For now, just return 0 as we don't track pending payments
        return 0;
    }
    
    /**
     * Gets an auction by ID.
     * 
     * @param id The auction ID
     * @return The auction, or null if not found
     */
    public AuctionItem getAuction(UUID id) {
        for (AuctionItem auction : activeAuctions) {
            if (auction.getId().equals(id)) {
                return auction;
            }
        }
        
        return null;
    }
    
    /**
     * Gets an auction item by ID (alias for getAuction).
     * 
     * @param id The auction ID
     * @return The auction, or null if not found
     */
    public AuctionItem getAuctionItem(UUID id) {
        return getAuction(id);
    }
    
    /**
     * Buys an auction.
     * 
     * @param auction The auction
     * @param buyer The buyer
     * @return True if bought successfully
     */
    public boolean buyAuction(AuctionItem auction, Player buyer) {
        if (auction == null || buyer == null) {
            return false;
        }
        
        // Check if auction is expired
        if (auction.isExpired()) {
            return false;
        }
        
        // Check if buyer has enough money
        if (!plugin.getEconomyAPI().hasMoney(buyer.getUniqueId(), auction.getPrice())) {
            return false;
        }
        
        // Get seller if online
        UUID sellerId = auction.getSellerId();
        
        // Transfer money
        plugin.getEconomyAPI().transferMoney(buyer.getUniqueId(), sellerId, auction.getPrice());
        
        // Remove from active auctions
        activeAuctions.remove(auction);
        
        // Remove from seller's auctions
        List<AuctionItem> sellerAuctions = playerAuctions.get(sellerId);
        if (sellerAuctions != null) {
            sellerAuctions.remove(auction);
        }
        
        return true;
    }
    
    /**
     * Cancels an auction.
     * 
     * @param auction The auction
     * @param player The player
     * @return True if cancelled successfully
     */
    public boolean cancelAuction(AuctionItem auction, Player player) {
        if (auction == null || player == null) {
            return false;
        }
        
        // Check if player is the seller
        if (!auction.getSellerId().equals(player.getUniqueId())) {
            return false;
        }
        
        // Remove from active auctions
        activeAuctions.remove(auction);
        
        // Remove from player's auctions
        List<AuctionItem> auctions = playerAuctions.get(player.getUniqueId());
        if (auctions != null) {
            auctions.remove(auction);
        }
        
        return true;
    }
    
    /**
     * Cleans up expired auctions.
     * 
     * @return The number of expired auctions removed
     */
    public int cleanupExpiredAuctions() {
        int count = 0;
        
        List<AuctionItem> expired = new ArrayList<>();
        
        // Find expired auctions
        for (AuctionItem auction : activeAuctions) {
            if (auction.isExpired()) {
                expired.add(auction);
                count++;
            }
        }
        
        // Remove expired auctions
        activeAuctions.removeAll(expired);
        
        // Remove from player auctions
        for (AuctionItem auction : expired) {
            List<AuctionItem> auctions = playerAuctions.get(auction.getSellerId());
            if (auctions != null) {
                auctions.remove(auction);
            }
        }
        
        return count;
    }
    
    /**
     * Collects pending payments for a player.
     * 
     * @param player The player
     * @return The amount collected, or 0 if none
     */
    public int collectPendingPayment(Player player) {
        // For now, this is a stub since we don't track pending payments
        return 0;
    }
    
    /**
     * Collects pending returns for a player.
     * 
     * @param player The player
     * @return The number of items returned, or 0 if none
     */
    public int collectPendingReturns(Player player) {
        // For now, this is a stub since we don't track pending returns
        return 0;
    }
}