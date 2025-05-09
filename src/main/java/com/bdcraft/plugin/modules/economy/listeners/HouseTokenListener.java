package com.bdcraft.plugin.modules.economy.listeners;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.items.tokens.BDToken;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.market.BDMarketManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener for house token placement.
 */
public class HouseTokenListener implements Listener {
    private final BDCraft plugin;
    
    /**
     * Creates a new house token listener.
     * @param plugin The plugin instance
     */
    public HouseTokenListener(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onTokenPlace(PlayerInteractEvent event) {
        // Only handle right clicks on blocks with items
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getItem() == null) {
            return;
        }
        
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        ItemStack item = event.getItem();
        
        // Check if the item is a house token
        BDItemManager itemManager = plugin.getEconomyModule().getItemManager();
        if (!itemManager.isBDItem(item) || !itemManager.getBDItemType(item).equals(BDToken.HOUSE_TOKEN_KEY)) {
            return;
        }
        
        // Cancel the event to prevent normal item usage
        event.setCancelled(true);
        
        // Check if the block is a valid surface
        if (!isValidSurface(clickedBlock)) {
            player.sendMessage(ChatColor.RED + "Houses can only be placed on solid ground.");
            return;
        }
        
        // Check if in a market
        BDMarketManager marketManager = plugin.getEconomyModule().getBDMarketManager();
        BDMarket market = marketManager.getMarketAt(clickedBlock.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "Houses can only be placed within a market.");
            return;
        }
        
        // Check if player owns the market
        if (!market.getFounderId().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You can only place houses in your own market.");
            return;
        }
        
        // Check collector count
        int currentCollectors = market.getTraderCount("COLLECTOR");
        int maxCollectors = getMaxCollectors(market);
        
        if (currentCollectors >= maxCollectors) {
            player.sendMessage(ChatColor.RED + "This market already has the maximum number of collectors (" + 
                    maxCollectors + ").");
            player.sendMessage(ChatColor.YELLOW + "Upgrade your market to increase this limit.");
            return;
        }
        
        // Try to add the collector house
        if (marketManager.addCollectorHouse(player, clickedBlock.getLocation())) {
            // Success, consume token
            consumeItem(player, item);
            
            player.sendMessage(ChatColor.GREEN + "House placed successfully!");
            player.sendMessage(ChatColor.YELLOW + "A new BD Collector has moved in and is ready to trade.");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to place house. Please try a different location.");
        }
    }
    
    /**
     * Checks if a block is a valid surface for a house.
     * @param block The block
     * @return Whether the block is a valid surface
     */
    private boolean isValidSurface(Block block) {
        Material type = block.getType();
        
        // Check if block is a valid surface block
        return type.isSolid() && !type.isTransparent() && type.isOccluding();
    }
    
    /**
     * Gets the maximum number of collectors for a market.
     * @param market The market
     * @return The maximum number of collectors
     */
    private int getMaxCollectors(BDMarket market) {
        int level = market.getLevel();
        
        switch (level) {
            case 1:
                return 3;
            case 2:
                return 5;
            case 3:
                return 7;
            case 4:
                return 10;
            default:
                return 3;
        }
    }
    
    /**
     * Consumes an item from a player's inventory.
     * @param player The player
     * @param item The item
     */
    private void consumeItem(Player player, ItemStack item) {
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
        
        player.updateInventory();
    }
}