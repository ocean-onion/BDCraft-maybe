package com.bdcraft.plugin.modules.economy.listeners;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import com.bdcraft.plugin.modules.progression.BDRankManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
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
 * Listener for market token placement.
 */
public class MarketTokenListener implements Listener {
    private final BDCraft plugin;
    
    /**
     * Creates a new market token listener.
     * @param plugin The plugin instance
     */
    public MarketTokenListener(BDCraft plugin) {
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
        
        // Check if the item is a market token
        BDItemManager itemManager = plugin.getEconomyModule().getItemManager();
        if (!itemManager.isBDItem(item) || !itemManager.getBDItemType(item).equals("market_token")) {
            return;
        }
        
        // Cancel the event to prevent normal item usage
        event.setCancelled(true);
        
        // Check if player has permission
        if (!player.hasPermission("bdcraft.market.create")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to create markets.");
            return;
        }
        
        // Check if player meets rank requirements
        BDRankManager rankManager = plugin.getProgressionModule().getRankManager();
        int playerRank = rankManager.getPlayerRank(player);
        
        if (playerRank < BDRankManager.RANK_EXPERT_FARMER) {
            player.sendMessage(ChatColor.RED + "You need to be at least an Expert Farmer to create a market.");
            return;
        }
        
        // Check if the block is a valid surface
        if (!isValidSurface(clickedBlock)) {
            player.sendMessage(ChatColor.RED + "Markets can only be created on solid ground.");
            return;
        }
        
        // Check if the structure meets requirements
        if (!checkMarketStructure(clickedBlock)) {
            player.sendMessage(ChatColor.RED + "The area does not meet the requirements for a market.");
            player.sendMessage(ChatColor.YELLOW + "You need a 3x3 flat area of solid blocks with at least 3 blocks of open space above.");
            return;
        }
        
        // Check if in another market
        MarketManager marketManager = plugin.getEconomyModule().getMarketManager();
        if (marketManager.isInMarket(clickedBlock.getLocation())) {
            player.sendMessage(ChatColor.RED + "This location is already within another market's radius.");
            return;
        }
        
        // Try to create the market
        if (marketManager.createMarket(clickedBlock.getLocation(), player) != null) {
            // Success, consume token
            consumeItem(player, item);
            
            player.sendMessage(ChatColor.GREEN + "Market created successfully!");
            player.sendMessage(ChatColor.YELLOW + "You now have a Market Owner and Dealer villager to trade with.");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to create market. Please try a different location.");
        }
    }
    
    /**
     * Checks if a block is a valid surface for a market.
     * @param block The block
     * @return Whether the block is a valid surface
     */
    private boolean isValidSurface(Block block) {
        Material type = block.getType();
        
        // Check if block is a valid surface block
        return type.isSolid() && !type.isTransparent() && type.isOccluding();
    }
    
    /**
     * Checks if the area around a block meets the structure requirements for a market.
     * @param centerBlock The center block
     * @return Whether the area meets the requirements
     */
    private boolean checkMarketStructure(Block centerBlock) {
        // Check 3x3 area
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = centerBlock.getRelative(x, 0, z);
                
                // Check if block is solid
                if (!block.getType().isSolid()) {
                    return false;
                }
                
                // Check for open space above
                for (int y = 1; y <= 3; y++) {
                    Block above = block.getRelative(0, y, 0);
                    
                    if (!above.getType().isAir()) {
                        return false;
                    }
                }
            }
        }
        
        return true;
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