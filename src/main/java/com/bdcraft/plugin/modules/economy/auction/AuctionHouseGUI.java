package com.bdcraft.plugin.modules.economy.auction;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GUI for interacting with the BD Auction House.
 */
public class AuctionHouseGUI {
    private final BDCraft plugin;
    private final AuctionManager auctionManager;
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    private static final int ITEMS_PER_PAGE = 45; // 5 rows of 9 slots for items
    
    // Map to track which auction items are in which inventory slots
    private final Map<UUID, Map<Integer, UUID>> playerPageItems = new HashMap<>();
    private final Map<UUID, Integer> playerCurrentPage = new HashMap<>();
    
    /**
     * Creates a new auction house GUI.
     * 
     * @param plugin The plugin instance
     * @param auctionManager The auction manager
     */
    public AuctionHouseGUI(BDCraft plugin, AuctionManager auctionManager) {
        this.plugin = plugin;
        this.auctionManager = auctionManager;
    }
    
    /**
     * Opens the main auction house menu for a player.
     * 
     * @param player The player
     */
    public void openMainMenu(Player player) {
        Inventory menu = Bukkit.createInventory(player, 27, "BD Auction House");
        
        // Browse listings button
        ItemStack browseButton = createGuiItem(Material.CHEST, 
                ChatColor.GREEN + "Browse Listings", 
                ChatColor.GRAY + "View all available items for sale");
        menu.setItem(10, browseButton);
        
        // My listings button
        ItemStack myListingsButton = createGuiItem(Material.WRITABLE_BOOK, 
                ChatColor.YELLOW + "My Listings", 
                ChatColor.GRAY + "Manage your auction listings");
        menu.setItem(13, myListingsButton);
        
        // Create listing button
        ItemStack createListingButton = createGuiItem(Material.GOLD_INGOT, 
                ChatColor.GOLD + "Create Listing", 
                ChatColor.GRAY + "Sell an item in the auction house");
        menu.setItem(16, createListingButton);
        
        // Notifications section
        List<String> notificationLore = new ArrayList<>();
        
        // Check for pending payments
        int pendingPayment = auctionManager.getPendingPayment(player);
        if (pendingPayment > 0) {
            notificationLore.add(ChatColor.GREEN + "You have " + pendingPayment + " BD coins in pending payments!");
            notificationLore.add(ChatColor.GREEN + "Click to collect");
        }
        
        // Check for pending returns
        if (auctionManager.hasPendingReturns(player)) {
            notificationLore.add(ChatColor.YELLOW + "You have expired auction items waiting to be collected!");
            notificationLore.add(ChatColor.YELLOW + "Click to collect");
        }
        
        if (notificationLore.isEmpty()) {
            notificationLore.add(ChatColor.GRAY + "No notifications");
        }
        
        ItemStack notificationsButton = createGuiItem(Material.BELL, 
                ChatColor.LIGHT_PURPLE + "Notifications", 
                notificationLore.toArray(new String[0]));
        menu.setItem(22, notificationsButton);
        
        player.openInventory(menu);
    }
    
    /**
     * Opens the browse listings page for a player.
     * 
     * @param player The player
     * @param page The page number (1-based)
     */
    public void openBrowseListings(Player player, int page) {
        List<AuctionItem> activeListings = auctionManager.getActiveListings();
        
        // Sort by most recently listed
        activeListings.sort(Comparator.comparing(AuctionItem::getListedDate).reversed());
        
        int maxPages = (int) Math.ceil((double) activeListings.size() / ITEMS_PER_PAGE);
        
        if (maxPages == 0) {
            maxPages = 1; // At least one page, even if empty
        }
        
        if (page < 1) {
            page = 1;
        } else if (page > maxPages) {
            page = maxPages;
        }
        
        playerCurrentPage.put(player.getUniqueId(), page);
        
        Inventory inventory = Bukkit.createInventory(player, 54, "BD Auction House - Page " + page + "/" + maxPages);
        
        // Store which auction items are in which slots
        Map<Integer, UUID> slotToItemId = new HashMap<>();
        playerPageItems.put(player.getUniqueId(), slotToItemId);
        
        // Add items for this page
        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, activeListings.size());
        
        for (int i = startIndex; i < endIndex; i++) {
            AuctionItem item = activeListings.get(i);
            int slot = i - startIndex;
            
            ItemStack displayItem = item.getItem().clone();
            ItemMeta meta = displayItem.getItemMeta();
            
            if (meta != null) {
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                if (lore == null) lore = new ArrayList<>();
                
                lore.add("");
                lore.add(ChatColor.GOLD + "Price: " + ChatColor.GREEN + item.getPrice() + " BD coins");
                lore.add(ChatColor.GOLD + "Seller: " + ChatColor.WHITE + item.getSellerName());
                lore.add(ChatColor.GOLD + "Listed: " + ChatColor.WHITE + DATE_FORMAT.format(item.getListedDate()));
                lore.add("");
                lore.add(ChatColor.YELLOW + "Click to purchase");
                
                meta.setLore(lore);
                displayItem.setItemMeta(meta);
            }
            
            inventory.setItem(slot, displayItem);
            slotToItemId.put(slot, item.getId());
        }
        
        // Add navigation buttons
        addNavigationButtons(inventory, page, maxPages);
        
        // Back button
        ItemStack backButton = createGuiItem(Material.ARROW, 
                ChatColor.RED + "Back to Main Menu", 
                ChatColor.GRAY + "Return to the main auction house menu");
        inventory.setItem(49, backButton);
        
        player.openInventory(inventory);
    }
    
    /**
     * Opens the my listings page for a player.
     * 
     * @param player The player
     * @param page The page number (1-based)
     */
    public void openMyListings(Player player, int page) {
        List<AuctionItem> myListings = auctionManager.getListingsBySeller(player.getUniqueId(), true);
        
        // Sort by most recently listed
        myListings.sort(Comparator.comparing(AuctionItem::getListedDate).reversed());
        
        int maxPages = (int) Math.ceil((double) myListings.size() / ITEMS_PER_PAGE);
        
        if (maxPages == 0) {
            maxPages = 1; // At least one page, even if empty
        }
        
        if (page < 1) {
            page = 1;
        } else if (page > maxPages) {
            page = maxPages;
        }
        
        playerCurrentPage.put(player.getUniqueId(), page);
        
        Inventory inventory = Bukkit.createInventory(player, 54, "BD Auction House - My Listings " + page + "/" + maxPages);
        
        // Store which auction items are in which slots
        Map<Integer, UUID> slotToItemId = new HashMap<>();
        playerPageItems.put(player.getUniqueId(), slotToItemId);
        
        // Add items for this page
        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, myListings.size());
        
        for (int i = startIndex; i < endIndex; i++) {
            AuctionItem item = myListings.get(i);
            int slot = i - startIndex;
            
            ItemStack displayItem = item.getItem().clone();
            ItemMeta meta = displayItem.getItemMeta();
            
            if (meta != null) {
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                if (lore == null) lore = new ArrayList<>();
                
                lore.add("");
                lore.add(ChatColor.GOLD + "Price: " + ChatColor.GREEN + item.getPrice() + " BD coins");
                lore.add(ChatColor.GOLD + "Listed: " + ChatColor.WHITE + DATE_FORMAT.format(item.getListedDate()));
                
                if (item.isSold()) {
                    lore.add(ChatColor.RED + "SOLD to " + ChatColor.WHITE + item.getBuyerName());
                    lore.add(ChatColor.RED + "on " + ChatColor.WHITE + DATE_FORMAT.format(item.getSoldDate()));
                } else {
                    lore.add("");
                    lore.add(ChatColor.YELLOW + "Click to cancel listing");
                }
                
                meta.setLore(lore);
                displayItem.setItemMeta(meta);
            }
            
            inventory.setItem(slot, displayItem);
            slotToItemId.put(slot, item.getId());
        }
        
        // Add navigation buttons
        addNavigationButtons(inventory, page, maxPages);
        
        // Back button
        ItemStack backButton = createGuiItem(Material.ARROW, 
                ChatColor.RED + "Back to Main Menu", 
                ChatColor.GRAY + "Return to the main auction house menu");
        inventory.setItem(49, backButton);
        
        player.openInventory(inventory);
    }
    
    /**
     * Starts the process of creating a listing.
     * 
     * @param player The player
     */
    public void startCreateListing(Player player) {
        player.closeInventory();
        player.sendMessage(ChatColor.GREEN + "Hold the item you want to list and type " + 
                ChatColor.GOLD + "/bd auction list <price>" + ChatColor.GREEN + " to create a listing.");
    }
    
    /**
     * Processes a click in the auction house GUI.
     * 
     * @param player The player
     * @param inventory The inventory
     * @param slot The clicked slot
     * @return True if the click was processed
     */
    public boolean handleClick(Player player, Inventory inventory, int slot) {
        String title = inventory.getViewers().isEmpty() ? "" : 
            player.getOpenInventory().getTitle();
        
        if (title.equals("BD Auction House")) {
            // Main menu
            switch (slot) {
                case 10: // Browse listings
                    openBrowseListings(player, 1);
                    return true;
                case 13: // My listings
                    openMyListings(player, 1);
                    return true;
                case 16: // Create listing
                    startCreateListing(player);
                    return true;
                case 22: // Notifications
                    processNotifications(player);
                    return true;
            }
        } else if (title.startsWith("BD Auction House - Page")) {
            // Browse listings
            if (slot >= 0 && slot < ITEMS_PER_PAGE) {
                Map<Integer, UUID> slotToItemId = playerPageItems.get(player.getUniqueId());
                if (slotToItemId != null && slotToItemId.containsKey(slot)) {
                    UUID itemId = slotToItemId.get(slot);
                    confirmPurchase(player, itemId);
                    return true;
                }
            } else if (slot == 45 && hasArrow(inventory, 45)) {
                // Previous page
                int currentPage = playerCurrentPage.getOrDefault(player.getUniqueId(), 1);
                openBrowseListings(player, currentPage - 1);
                return true;
            } else if (slot == 53 && hasArrow(inventory, 53)) {
                // Next page
                int currentPage = playerCurrentPage.getOrDefault(player.getUniqueId(), 1);
                openBrowseListings(player, currentPage + 1);
                return true;
            } else if (slot == 49) {
                // Back to main menu
                openMainMenu(player);
                return true;
            }
        } else if (title.startsWith("BD Auction House - My Listings")) {
            // My listings
            if (slot >= 0 && slot < ITEMS_PER_PAGE) {
                Map<Integer, UUID> slotToItemId = playerPageItems.get(player.getUniqueId());
                if (slotToItemId != null && slotToItemId.containsKey(slot)) {
                    UUID itemId = slotToItemId.get(slot);
                    AuctionItem item = auctionManager.getAuctionItem(itemId);
                    
                    if (item != null && !item.isSold()) {
                        confirmCancelListing(player, itemId);
                        return true;
                    }
                }
            } else if (slot == 45 && hasArrow(inventory, 45)) {
                // Previous page
                int currentPage = playerCurrentPage.getOrDefault(player.getUniqueId(), 1);
                openMyListings(player, currentPage - 1);
                return true;
            } else if (slot == 53 && hasArrow(inventory, 53)) {
                // Next page
                int currentPage = playerCurrentPage.getOrDefault(player.getUniqueId(), 1);
                openMyListings(player, currentPage + 1);
                return true;
            } else if (slot == 49) {
                // Back to main menu
                openMainMenu(player);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Confirms a purchase with the player.
     * 
     * @param player The player
     * @param itemId The ID of the item to purchase
     */
    private void confirmPurchase(Player player, UUID itemId) {
        AuctionItem item = auctionManager.getAuctionItem(itemId);
        
        if (item == null) {
            player.sendMessage(ChatColor.RED + "That item is no longer available.");
            openBrowseListings(player, playerCurrentPage.getOrDefault(player.getUniqueId(), 1));
            return;
        }
        
        player.closeInventory();
        player.sendMessage(ChatColor.GOLD + "Are you sure you want to buy " + 
                (item.getItem().hasItemMeta() && item.getItem().getItemMeta().hasDisplayName() ? 
                item.getItem().getItemMeta().getDisplayName() : 
                item.getItem().getType().toString().toLowerCase().replace('_', ' ')) + 
                " for " + item.getPrice() + " BD coins?");
        
        player.sendMessage(ChatColor.GOLD + "Type " + ChatColor.GREEN + "/bd auction confirm " + 
                itemId + ChatColor.GOLD + " to confirm your purchase.");
    }
    
    /**
     * Confirms cancellation of a listing with the player.
     * 
     * @param player The player
     * @param itemId The ID of the listing to cancel
     */
    private void confirmCancelListing(Player player, UUID itemId) {
        player.closeInventory();
        player.sendMessage(ChatColor.GOLD + "Are you sure you want to cancel this listing? " +
                "The item will be returned to your inventory.");
        
        player.sendMessage(ChatColor.GOLD + "Type " + ChatColor.GREEN + "/bd auction cancel " + 
                itemId + ChatColor.GOLD + " to confirm.");
    }
    
    /**
     * Processes player notifications like pending payments and returns.
     * 
     * @param player The player
     */
    private void processNotifications(Player player) {
        boolean hadNotifications = false;
        
        // Process pending payments
        int pendingPayment = auctionManager.getPendingPayment(player);
        if (pendingPayment > 0) {
            auctionManager.collectPendingPayment(player);
            hadNotifications = true;
        }
        
        // Process pending returns
        if (auctionManager.hasPendingReturns(player)) {
            auctionManager.collectPendingReturns(player);
            hadNotifications = true;
        }
        
        if (!hadNotifications) {
            player.sendMessage(ChatColor.GRAY + "You have no notifications at this time.");
        }
        
        // Reopen main menu
        openMainMenu(player);
    }
    
    /**
     * Adds navigation buttons to an inventory.
     * 
     * @param inventory The inventory
     * @param currentPage The current page
     * @param maxPages The maximum number of pages
     */
    private void addNavigationButtons(Inventory inventory, int currentPage, int maxPages) {
        // Previous page button (if not on first page)
        if (currentPage > 1) {
            ItemStack prevButton = createGuiItem(Material.ARROW, 
                    ChatColor.AQUA + "Previous Page", 
                    ChatColor.GRAY + "Go to page " + (currentPage - 1));
            inventory.setItem(45, prevButton);
        }
        
        // Next page button (if not on last page)
        if (currentPage < maxPages) {
            ItemStack nextButton = createGuiItem(Material.ARROW, 
                    ChatColor.AQUA + "Next Page", 
                    ChatColor.GRAY + "Go to page " + (currentPage + 1));
            inventory.setItem(53, nextButton);
        }
        
        // Current page indicator
        ItemStack pageIndicator = createGuiItem(Material.PAPER, 
                ChatColor.YELLOW + "Page " + currentPage + "/" + maxPages, 
                ChatColor.GRAY + "You are currently viewing page " + currentPage);
        inventory.setItem(4, pageIndicator);
    }
    
    /**
     * Creates a GUI item with custom name and lore.
     * 
     * @param material The material
     * @param name The name
     * @param lore The lore
     * @return The item stack
     */
    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(name);
            
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Checks if an inventory has an arrow at the specified slot.
     * Used for pagination buttons.
     * 
     * @param inventory The inventory
     * @param slot The slot
     * @return True if there's an arrow
     */
    private boolean hasArrow(Inventory inventory, int slot) {
        ItemStack item = inventory.getItem(slot);
        return item != null && item.getType() == Material.ARROW;
    }
    
    /**
     * Removes a player from tracking when they disconnect.
     * 
     * @param player The player
     */
    public void cleanupPlayer(Player player) {
        playerPageItems.remove(player.getUniqueId());
        playerCurrentPage.remove(player.getUniqueId());
    }
}