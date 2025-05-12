package com.bdcraft.plugin.modules.economy.listeners;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.items.tokens.BDToken;
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
        if (!itemManager.isBDItem(item) || !itemManager.getBDItemType(item).equals(BDToken.MARKET_TOKEN_KEY)) {
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
            player.sendMessage(ChatColor.YELLOW + "Requirements:");
            player.sendMessage(ChatColor.YELLOW + "• 3x3 solid blocks for foundation");
            player.sendMessage(ChatColor.YELLOW + "• 3x3 solid blocks for roof");
            player.sendMessage(ChatColor.YELLOW + "• Walls with at least one door");
            player.sendMessage(ChatColor.YELLOW + "• A bed placed inside");
            return;
        }
        
        // Check if in another market
        MarketManager marketManager = plugin.getEconomyModule().getMarketManager();
        if (marketManager.isInMarket(clickedBlock.getLocation())) {
            player.sendMessage(ChatColor.RED + "This location is already within another market's radius.");
            return;
        }
        
        // Try to create the market
        String marketName = "Market of " + player.getName();
        if (marketManager.createMarket(player, marketName, clickedBlock.getLocation()) != null) {
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
        // Check that we have a valid foundation (3x3 solid blocks)
        boolean hasValidFoundation = true;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block foundationBlock = centerBlock.getRelative(x, 0, z);
                if (!foundationBlock.getType().isSolid()) {
                    hasValidFoundation = false;
                    break;
                }
            }
            if (!hasValidFoundation) break;
        }
        
        if (!hasValidFoundation) {
            return false;
        }
        
        // Check for a roof (3x3 solid blocks at some height)
        boolean foundRoof = false;
        roofSearch:
        for (int y = 2; y <= 5; y++) {  // Search up to 5 blocks high for roof
            boolean completeRoof = true;
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Block roofBlock = centerBlock.getRelative(x, y, z);
                    if (!roofBlock.getType().isSolid()) {
                        completeRoof = false;
                        break;
                    }
                }
                if (!completeRoof) break;
            }
            
            if (completeRoof) {
                foundRoof = true;
                break roofSearch;
            }
        }
        
        if (!foundRoof) {
            return false;
        }
        
        // Check for walls with a door
        boolean hasWalls = false;
        boolean hasDoor = false;
        
        // Check perimeter for walls and door
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                // Skip interior blocks and corners
                if (Math.abs(x) != 1 && Math.abs(z) != 1) continue;
                
                // Check perimeter blocks
                for (int y = 1; y <= 2; y++) {
                    Block wallBlock = centerBlock.getRelative(x, y, z);
                    
                    // If it's a door
                    if (wallBlock.getType().toString().contains("DOOR")) {
                        hasDoor = true;
                    } 
                    // If it's a solid block (part of wall)
                    else if (wallBlock.getType().isSolid()) {
                        hasWalls = true;
                    }
                }
            }
        }
        
        if (!hasWalls || !hasDoor) {
            return false;
        }
        
        // Check for a bed inside
        boolean hasBed = false;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                // Check interior blocks
                Block interiorBlock = centerBlock.getRelative(x, 1, z);
                if (interiorBlock.getType().toString().contains("BED")) {
                    hasBed = true;
                    break;
                }
            }
            if (hasBed) break;
        }
        
        return hasBed;
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
        
        player.getInventory().setContents(player.getInventory().getContents());
    }
}