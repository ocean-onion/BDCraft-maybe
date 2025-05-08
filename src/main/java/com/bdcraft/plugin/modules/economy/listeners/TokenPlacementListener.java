package com.bdcraft.plugin.modules.economy.listeners;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.market.MarketManager;

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
 * Listener that handles token placement events.
 */
public class TokenPlacementListener implements Listener {
    private final BDCraft plugin;
    private final BDItemManager itemManager;
    private final MarketManager marketManager;
    
    /**
     * Creates a new token placement listener.
     * @param plugin The plugin instance
     * @param economyModule The economy module
     */
    public TokenPlacementListener(BDCraft plugin, BDEconomyModule economyModule) {
        this.plugin = plugin;
        this.itemManager = economyModule.getItemManager();
        this.marketManager = economyModule.getMarketManager();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onTokenPlace(PlayerInteractEvent event) {
        // Check if the player is right-clicking a block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        // Check if the player is holding a BD item
        ItemStack itemInHand = event.getItem();
        if (itemInHand == null || !itemManager.isBDItem(itemInHand)) {
            return;
        }
        
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        
        if (clickedBlock == null) {
            return;
        }
        
        // Get the BD item type
        String bdItemType = itemManager.getBDItemType(itemInHand);
        if (bdItemType == null) {
            return;
        }
        
        // Handle different token types
        switch (bdItemType) {
            case "market_token":
                handleMarketTokenPlacement(player, clickedBlock, itemInHand);
                event.setCancelled(true);
                break;
                
            case "house_token":
                handleHouseTokenPlacement(player, clickedBlock, itemInHand);
                event.setCancelled(true);
                break;
                
            case "bd_stick":
                // BD Stick gives a temporary farming buff
                handleBDStickUse(player, clickedBlock, itemInHand);
                event.setCancelled(true);
                break;
        }
    }
    
    /**
     * Handles placement of a Market Token.
     * @param player The player placing the token
     * @param clickedBlock The block that was clicked
     * @param token The token item
     */
    private void handleMarketTokenPlacement(Player player, Block clickedBlock, ItemStack token) {
        // Check permissions
        if (!player.hasPermission("bdcraft.market.create")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to create markets.");
            return;
        }
        
        // Check if the top of the clicked block is empty
        Block placementBlock = clickedBlock.getRelative(0, 1, 0);
        if (placementBlock.getType() != Material.AIR) {
            player.sendMessage(ChatColor.RED + "Cannot place market token here. Make sure there's space above the block.");
            return;
        }
        
        // Check platform - token must be placed on a 3x3 stone platform
        if (!isValidMarketPlatform(clickedBlock)) {
            player.sendMessage(ChatColor.RED + "Market tokens must be placed on a 3x3 stone platform.");
            return;
        }
        
        // Check if the player is already within an existing market
        if (marketManager.isInMarket(clickedBlock.getLocation())) {
            player.sendMessage(ChatColor.RED + "Cannot create a market inside an existing market area.");
            return;
        }
        
        // Create the market
        boolean success = marketManager.createMarket(clickedBlock.getLocation(), player) != null;
        
        if (success) {
            // Consume the token
            if (token.getAmount() > 1) {
                token.setAmount(token.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
            
            player.sendMessage(ChatColor.GREEN + "Market created successfully!");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to create market. Check the console for details.");
        }
    }
    
    /**
     * Handles placement of a House Token.
     * @param player The player placing the token
     * @param clickedBlock The block that was clicked
     * @param token The token item
     */
    private void handleHouseTokenPlacement(Player player, Block clickedBlock, ItemStack token) {
        // Check permissions
        if (!player.hasPermission("bdcraft.market.house")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to create collector houses.");
            return;
        }
        
        // Check if the top of the clicked block is empty
        Block placementBlock = clickedBlock.getRelative(0, 1, 0);
        if (placementBlock.getType() != Material.AIR) {
            player.sendMessage(ChatColor.RED + "Cannot place house token here. Make sure there's space above the block.");
            return;
        }
        
        // Check if the player is inside their own market
        if (!marketManager.isPlayerInOwnMarket(player, clickedBlock.getLocation())) {
            player.sendMessage(ChatColor.RED + "House tokens can only be placed inside your own market.");
            return;
        }
        
        // Add the collector house to the market
        boolean success = marketManager.addCollectorHouse(player, clickedBlock.getLocation());
        
        if (success) {
            // Consume the token
            if (token.getAmount() > 1) {
                token.setAmount(token.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
            
            player.sendMessage(ChatColor.GREEN + "Collector house added to your market!");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to add collector house. You may have reached the maximum for your market level.");
        }
    }
    
    /**
     * Handles use of a BD Stick.
     * @param player The player using the stick
     * @param clickedBlock The block that was clicked
     * @param stick The stick item
     */
    private void handleBDStickUse(Player player, Block clickedBlock, ItemStack stick) {
        // Check permissions
        if (!player.hasPermission("bdcraft.items.use.stick")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use BD Sticks.");
            return;
        }
        
        // Apply farming buff to player
        // This buff will be handled by a BuffManager in a future implementation
        player.sendMessage(ChatColor.GREEN + "You feel a surge of farming energy! (Farming speed +20% for 5 minutes)");
        
        // Reduce stick durability (simple version - just consume it)
        if (stick.getAmount() > 1) {
            stick.setAmount(stick.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
    
    /**
     * Checks if a block is the center of a valid 3x3 stone platform.
     * @param centerBlock The center block
     * @return Whether the platform is valid
     */
    private boolean isValidMarketPlatform(Block centerBlock) {
        // Check if center block is stone
        if (centerBlock.getType() != Material.STONE) {
            return false;
        }
        
        // Check 3x3 platform of stone blocks
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = centerBlock.getRelative(x, 0, z);
                
                // Skip the center block (already checked)
                if (x == 0 && z == 0) {
                    continue;
                }
                
                if (block.getType() != Material.STONE) {
                    return false;
                }
            }
        }
        
        return true;
    }
}