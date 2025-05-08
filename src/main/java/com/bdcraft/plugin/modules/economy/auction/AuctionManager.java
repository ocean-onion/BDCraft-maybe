package com.bdcraft.plugin.modules.economy.auction;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Manages the BD Auction House.
 */
public class AuctionManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final BDEconomyModule economyModule;
    private final Map<UUID, AuctionItem> auctionItems;
    private final File auctionFile;
    private FileConfiguration auctionConfig;
    
    // Auction settings
    private static final int MAX_LISTINGS_PER_PLAYER = 5;
    private static final int MAX_LISTING_DAYS = 7;
    private static final double LISTING_FEE_PERCENTAGE = 0.05; // 5% listing fee
    private static final double SALE_FEE_PERCENTAGE = 0.10; // 10% sale fee
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Creates a new auction manager.
     * 
     * @param plugin The plugin instance
     */
    public AuctionManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.economyModule = plugin.getEconomyModule();
        this.auctionItems = new HashMap<>();
        this.auctionFile = new File(plugin.getDataFolder(), "auction.yml");
        
        // Ensure file exists
        if (!auctionFile.exists()) {
            try {
                if (auctionFile.createNewFile()) {
                    logger.info("Created auction.yml file");
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not create auction.yml file", e);
            }
        }
        
        // Load config
        this.auctionConfig = YamlConfiguration.loadConfiguration(auctionFile);
        
        // Load auction items
        loadAuctionItems();
        
        // Schedule cleanup task
        Bukkit.getScheduler().runTaskTimer(plugin, this::cleanupExpiredListings, 20L * 60 * 60, 20L * 60 * 60); // Run every hour
    }
    
    /**
     * Lists an item in the auction house.
     * 
     * @param player The player listing the item
     * @param item The item to list
     * @param price The price in BD coins
     * @return True if the listing was successful
     */
    public boolean listItem(Player player, ItemStack item, int price) {
        if (price <= 0) {
            player.sendMessage(ChatColor.RED + "Price must be greater than 0.");
            return false;
        }
        
        // Check if player has too many active listings
        long activeListings = auctionItems.values().stream()
                .filter(auctionItem -> auctionItem.getSellerUUID().equals(player.getUniqueId()) && !auctionItem.isSold())
                .count();
        
        if (activeListings >= MAX_LISTINGS_PER_PLAYER) {
            player.sendMessage(ChatColor.RED + "You can only have " + MAX_LISTINGS_PER_PLAYER + 
                    " active listings at once. Please wait for some to sell or expire.");
            return false;
        }
        
        // Calculate listing fee
        int listingFee = (int) Math.ceil(price * LISTING_FEE_PERCENTAGE);
        
        // Check if player can afford listing fee
        if (economyModule.getPlayerBalance(player) < listingFee) {
            player.sendMessage(ChatColor.RED + "You need " + listingFee + 
                    " BD coins to list this item (5% listing fee).");
            return false;
        }
        
        // Deduct listing fee
        economyModule.removePlayerBalance(player, listingFee);
        
        // Create auction item
        AuctionItem auctionItem = new AuctionItem(player, item, price);
        auctionItems.put(auctionItem.getId(), auctionItem);
        
        // Remove the item from player's inventory
        player.getInventory().removeItem(item);
        
        // Save auction items
        saveAuctionItems();
        
        player.sendMessage(ChatColor.GREEN + "You've listed your item in the Auction House for " + 
                price + " BD coins. Listing fee: " + listingFee + " BD coins.");
        
        return true;
    }
    
    /**
     * Buys an item from the auction house.
     * 
     * @param player The player buying the item
     * @param itemId The ID of the item to buy
     * @return True if the purchase was successful
     */
    public boolean buyItem(Player player, UUID itemId) {
        AuctionItem auctionItem = auctionItems.get(itemId);
        
        if (auctionItem == null) {
            player.sendMessage(ChatColor.RED + "Invalid auction item.");
            return false;
        }
        
        if (auctionItem.isSold()) {
            player.sendMessage(ChatColor.RED + "This item has already been sold.");
            return false;
        }
        
        if (auctionItem.getSellerUUID().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You cannot buy your own auction listings.");
            return false;
        }
        
        int price = auctionItem.getPrice();
        
        // Check if player can afford the item
        if (economyModule.getPlayerBalance(player) < price) {
            player.sendMessage(ChatColor.RED + "You don't have enough BD coins to buy this item.");
            return false;
        }
        
        // Check if player has space in inventory
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED + "Your inventory is full. Make space before buying.");
            return false;
        }
        
        // Deduct coins from buyer
        economyModule.removePlayerBalance(player, price);
        
        // Calculate sale fee
        int saleFee = (int) Math.ceil(price * SALE_FEE_PERCENTAGE);
        int sellerProfit = price - saleFee;
        
        // Add coins to seller (minus fee)
        Player seller = Bukkit.getPlayer(auctionItem.getSellerUUID());
        if (seller != null && seller.isOnline()) {
            economyModule.addPlayerBalance(seller, sellerProfit);
            seller.sendMessage(ChatColor.GREEN + "Your auction item was sold to " + 
                    player.getName() + " for " + price + " BD coins. " +
                    "You received " + sellerProfit + " BD coins (10% sales fee applied).");
        } else {
            // Store the profit for when the seller logs in
            auctionConfig.set("pending_payments." + auctionItem.getSellerUUID().toString(), 
                    auctionConfig.getInt("pending_payments." + auctionItem.getSellerUUID().toString(), 0) + sellerProfit);
            saveAuctionConfig();
        }
        
        // Mark as sold
        auctionItem.markAsSold(player);
        
        // Give item to buyer
        player.getInventory().addItem(auctionItem.getItem());
        
        // Save auction items
        saveAuctionItems();
        
        player.sendMessage(ChatColor.GREEN + "You've successfully purchased " + 
                (auctionItem.getItem().hasItemMeta() && auctionItem.getItem().getItemMeta().hasDisplayName() ? 
                auctionItem.getItem().getItemMeta().getDisplayName() : 
                auctionItem.getItem().getType().toString().toLowerCase().replace('_', ' ')) + 
                " for " + price + " BD coins.");
        
        return true;
    }
    
    /**
     * Cancels a listing in the auction house.
     * 
     * @param player The player cancelling the listing
     * @param itemId The ID of the item to cancel
     * @return True if the cancellation was successful
     */
    public boolean cancelListing(Player player, UUID itemId) {
        AuctionItem auctionItem = auctionItems.get(itemId);
        
        if (auctionItem == null) {
            player.sendMessage(ChatColor.RED + "Invalid auction item.");
            return false;
        }
        
        if (auctionItem.isSold()) {
            player.sendMessage(ChatColor.RED + "This item has already been sold and cannot be cancelled.");
            return false;
        }
        
        if (!auctionItem.getSellerUUID().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You can only cancel your own listings.");
            return false;
        }
        
        // Check if player has space in inventory
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED + "Your inventory is full. Make space before cancelling.");
            return false;
        }
        
        // Remove from auction house
        auctionItems.remove(itemId);
        
        // Return item to player
        player.getInventory().addItem(auctionItem.getItem());
        
        // Save auction items
        saveAuctionItems();
        
        player.sendMessage(ChatColor.YELLOW + "You've cancelled your auction listing and the item has been returned to your inventory.");
        
        return true;
    }
    
    /**
     * Gets all active auction listings.
     * 
     * @return The list of active auction items
     */
    public List<AuctionItem> getActiveListings() {
        return auctionItems.values().stream()
                .filter(item -> !item.isSold())
                .collect(Collectors.toList());
    }
    
    /**
     * Gets auction listings by a specific seller.
     * 
     * @param sellerUUID The UUID of the seller
     * @param includeExpired Whether to include expired listings
     * @return The list of auction items by the seller
     */
    public List<AuctionItem> getListingsBySeller(UUID sellerUUID, boolean includeExpired) {
        return auctionItems.values().stream()
                .filter(item -> item.getSellerUUID().equals(sellerUUID))
                .filter(item -> includeExpired || !isListingExpired(item))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets a player's pending auction payment.
     * 
     * @param player The player
     * @return The pending payment amount
     */
    public int getPendingPayment(Player player) {
        return auctionConfig.getInt("pending_payments." + player.getUniqueId().toString(), 0);
    }
    
    /**
     * Collects a player's pending auction payment.
     * 
     * @param player The player
     * @return The amount collected
     */
    public int collectPendingPayment(Player player) {
        int pendingAmount = getPendingPayment(player);
        
        if (pendingAmount > 0) {
            economyModule.addPlayerBalance(player, pendingAmount);
            auctionConfig.set("pending_payments." + player.getUniqueId().toString(), null);
            saveAuctionConfig();
            
            player.sendMessage(ChatColor.GREEN + "You've collected " + pendingAmount + 
                    " BD coins from your auction sales.");
        }
        
        return pendingAmount;
    }
    
    /**
     * Loads auction items from the configuration.
     */
    private void loadAuctionItems() {
        if (!auctionConfig.contains("items")) {
            return;
        }
        
        ConfigurationSection itemsSection = auctionConfig.getConfigurationSection("items");
        if (itemsSection == null) {
            return;
        }
        
        for (String idStr : itemsSection.getKeys(false)) {
            try {
                UUID id = UUID.fromString(idStr);
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(idStr);
                
                if (itemSection == null) {
                    continue;
                }
                
                UUID sellerUUID = UUID.fromString(itemSection.getString("seller_uuid", ""));
                String sellerName = itemSection.getString("seller_name", "Unknown");
                ItemStack item = itemSection.getItemStack("item");
                int price = itemSection.getInt("price");
                Date listedDate = DATE_FORMAT.parse(itemSection.getString("listed_date"));
                boolean sold = itemSection.getBoolean("sold");
                
                Date soldDate = null;
                UUID buyerUUID = null;
                String buyerName = null;
                
                if (sold) {
                    soldDate = DATE_FORMAT.parse(itemSection.getString("sold_date"));
                    buyerUUID = UUID.fromString(itemSection.getString("buyer_uuid", ""));
                    buyerName = itemSection.getString("buyer_name", "Unknown");
                }
                
                AuctionItem auctionItem = new AuctionItem(id, sellerUUID, sellerName, item, 
                        price, listedDate, sold, soldDate, buyerUUID, buyerName);
                
                auctionItems.put(id, auctionItem);
            } catch (Exception e) {
                logger.warning("Error loading auction item " + idStr + ": " + e.getMessage());
            }
        }
        
        logger.info("Loaded " + auctionItems.size() + " auction items from storage");
    }
    
    /**
     * Saves auction items to the configuration.
     */
    public void saveAuctionItems() {
        // Clear existing items
        auctionConfig.set("items", null);
        
        // Save items
        for (AuctionItem item : auctionItems.values()) {
            String idStr = item.getId().toString();
            String path = "items." + idStr + ".";
            
            auctionConfig.set(path + "seller_uuid", item.getSellerUUID().toString());
            auctionConfig.set(path + "seller_name", item.getSellerName());
            auctionConfig.set(path + "item", item.getItem());
            auctionConfig.set(path + "price", item.getPrice());
            auctionConfig.set(path + "listed_date", DATE_FORMAT.format(item.getListedDate()));
            auctionConfig.set(path + "sold", item.isSold());
            
            if (item.isSold()) {
                auctionConfig.set(path + "sold_date", DATE_FORMAT.format(item.getSoldDate()));
                auctionConfig.set(path + "buyer_uuid", item.getBuyerUUID().toString());
                auctionConfig.set(path + "buyer_name", item.getBuyerName());
            }
        }
        
        saveAuctionConfig();
    }
    
    /**
     * Saves the auction configuration.
     */
    private void saveAuctionConfig() {
        try {
            auctionConfig.save(auctionFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save auction.yml file", e);
        }
    }
    
    /**
     * Checks if a listing is expired.
     * 
     * @param item The auction item
     * @return True if the listing is expired
     */
    private boolean isListingExpired(AuctionItem item) {
        if (item.isSold()) {
            return false; // Sold items don't expire
        }
        
        long diffInMillis = new Date().getTime() - item.getListedDate().getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        
        return diffInDays >= MAX_LISTING_DAYS;
    }
    
    /**
     * Cleans up expired listings.
     * This is run periodically to remove old listings.
     */
    private void cleanupExpiredListings() {
        List<AuctionItem> expiredItems = new ArrayList<>();
        
        for (AuctionItem item : auctionItems.values()) {
            if (isListingExpired(item)) {
                expiredItems.add(item);
            }
        }
        
        for (AuctionItem item : expiredItems) {
            // Notify seller if online
            Player seller = Bukkit.getPlayer(item.getSellerUUID());
            if (seller != null && seller.isOnline()) {
                seller.sendMessage(ChatColor.YELLOW + "Your auction listing for " + 
                        (item.getItem().hasItemMeta() && item.getItem().getItemMeta().hasDisplayName() ? 
                        item.getItem().getItemMeta().getDisplayName() : 
                        item.getItem().getType().toString().toLowerCase().replace('_', ' ')) + 
                        " has expired. The item has been sent to your pending returns.");
            }
            
            // Store the item for when the seller logs in
            String pendingReturnsPath = "pending_returns." + item.getSellerUUID().toString();
            List<ItemStack> pendingItems = new ArrayList<>();
            
            if (auctionConfig.contains(pendingReturnsPath)) {
                pendingItems = (List<ItemStack>) auctionConfig.getList(pendingReturnsPath);
            }
            
            if (pendingItems == null) {
                pendingItems = new ArrayList<>();
            }
            
            pendingItems.add(item.getItem());
            auctionConfig.set(pendingReturnsPath, pendingItems);
            
            // Remove from auction items
            auctionItems.remove(item.getId());
        }
        
        if (!expiredItems.isEmpty()) {
            logger.info("Cleaned up " + expiredItems.size() + " expired auction listings");
            saveAuctionConfig();
        }
    }
    
    /**
     * Gets a specific auction item by ID.
     * 
     * @param id The auction item ID
     * @return The auction item, or null if not found
     */
    public AuctionItem getAuctionItem(UUID id) {
        return auctionItems.get(id);
    }
    
    /**
     * Checks if a player has any pending auction returns.
     * 
     * @param player The player
     * @return True if the player has pending returns
     */
    public boolean hasPendingReturns(Player player) {
        return auctionConfig.contains("pending_returns." + player.getUniqueId().toString());
    }
    
    /**
     * Collects a player's pending auction returns.
     * 
     * @param player The player
     * @return True if returns were collected
     */
    public boolean collectPendingReturns(Player player) {
        String path = "pending_returns." + player.getUniqueId().toString();
        
        if (!auctionConfig.contains(path)) {
            return false;
        }
        
        @SuppressWarnings("unchecked")
        List<ItemStack> pendingItems = (List<ItemStack>) auctionConfig.getList(path);
        
        if (pendingItems == null || pendingItems.isEmpty()) {
            return false;
        }
        
        // Count how many items were returned
        int returnedCount = 0;
        
        for (ItemStack item : pendingItems) {
            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);
            
            if (leftover.isEmpty()) {
                returnedCount++;
            } else {
                // Drop the item at the player's location if inventory is full
                for (ItemStack drop : leftover.values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), drop);
                }
                returnedCount++;
            }
        }
        
        // Clear the pending returns
        auctionConfig.set(path, null);
        saveAuctionConfig();
        
        player.sendMessage(ChatColor.GREEN + "You've collected " + returnedCount + 
                " expired auction items. Items that didn't fit in your inventory were dropped at your feet.");
        
        return true;
    }
}