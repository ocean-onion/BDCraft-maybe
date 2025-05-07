package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

/**
 * Listener for player progression-related events.
 */
public class PlayerProgressionListener implements Listener {
    private final BDCraft plugin;
    private final BDRankManager rankManager;
    
    /**
     * Creates a new player progression listener.
     * @param plugin The plugin instance
     * @param rankManager The rank manager
     */
    public PlayerProgressionListener(BDCraft plugin, BDRankManager rankManager) {
        this.plugin = plugin;
        this.rankManager = rankManager;
    }
    
    /**
     * Called when a player joins the server.
     * @param event The event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Welcome message based on player rank
        if (!player.hasPlayedBefore()) {
            // First-time welcome message
            player.sendMessage(ChatColor.GREEN + "Welcome to BDCraft! You are starting as a " + 
                    rankManager.getPlayerColoredRankName(player) + ChatColor.GREEN + ".");
            player.sendMessage(ChatColor.GREEN + "Farm crops and sell them to collectors to earn experience and rank up!");
        } else {
            // Regular welcome message
            player.sendMessage(ChatColor.GREEN + "Welcome back! Your current rank is " + 
                    rankManager.getPlayerColoredRankName(player) + ChatColor.GREEN + ".");
            
            // Show progress to next rank if applicable
            int currentRank = rankManager.getPlayerRank(player);
            if (currentRank < BDRankManager.RANK_MASTER_FARMER) {
                int expForNextRank = rankManager.getExperienceForNextRank(player);
                int currentExp = rankManager.getPlayerExperience(player);
                
                player.sendMessage(ChatColor.GREEN + "Progress to next rank: " + 
                        currentExp + "/" + expForNextRank + " XP (" + rankManager.getProgressPercentage(player) + "%)");
                player.sendMessage(rankManager.getProgressBar(player));
            } else if (currentRank == BDRankManager.RANK_MASTER_FARMER) {
                player.sendMessage(ChatColor.GREEN + "You are a " + rankManager.getPlayerColoredRankName(player) + 
                        ChatColor.GREEN + "! Type " + ChatColor.GOLD + "/bdrank rebirth" + 
                        ChatColor.GREEN + " to reset your rank and gain a permanent experience bonus.");
            }
        }
    }
    
    /**
     * Called when a player interacts with an entity.
     * @param event The event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager)) {
            return;
        }
        
        Villager villager = (Villager) event.getRightClicked();
        Player player = event.getPlayer();
        
        // Check if this is a collector villager
        if (villager.getCustomName() != null && villager.getCustomName().contains("BD Collector")) {
            int playerRank = rankManager.getPlayerRank(player);
            
            // Apply rank-based permissions for interacting with collectors
            // For now, all players can interact with collectors
        }
    }
    
    /**
     * Called when a player clicks in an inventory.
     * @param event The event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        
        // Check if this is a merchant inventory (villager trade)
        if (event.getInventory() instanceof MerchantInventory) {
            MerchantInventory merchantInventory = (MerchantInventory) event.getInventory();
            
            // Process completed trades
            if (event.getSlot() == 2 && event.getCurrentItem() != null) {
                ItemStack tradeResult = event.getCurrentItem();
                
                // Calculate experience gained from trade
                int xpGained = calculateTradeExperience(tradeResult);
                
                if (xpGained > 0) {
                    // Add experience to the player
                    int newExperience = rankManager.addPlayerExperience(player, xpGained);
                    
                    // Check for rank up
                    boolean didRankUp = rankManager.checkRankUp(player);
                    
                    // Notify player of experience gain
                    player.sendMessage(ChatColor.GREEN + "+" + xpGained + " farming experience! (Total: " + newExperience + ")");
                    
                    if (didRankUp) {
                        // The rank up notification is handled in checkRankUp method
                    }
                }
            }
        }
    }
    
    /**
     * Calculates the experience gained from a trade.
     * @param tradeResult The trade result item
     * @return The experience gained
     */
    private int calculateTradeExperience(ItemStack tradeResult) {
        // For now, a simple calculation based on the item type and count
        int baseXP = 0;
        
        if (tradeResult.getType() == Material.EMERALD) {
            baseXP = 10 * tradeResult.getAmount();
        } else if (tradeResult.getType() == Material.DIAMOND) {
            baseXP = 25 * tradeResult.getAmount();
        } else if (tradeResult.getType() == Material.GOLD_INGOT) {
            baseXP = 5 * tradeResult.getAmount();
        } else if (tradeResult.getType() == Material.IRON_INGOT) {
            baseXP = 3 * tradeResult.getAmount();
        } else {
            baseXP = 1 * tradeResult.getAmount();
        }
        
        return baseXP;
    }
    
    /**
     * Called when a player closes an inventory.
     * @param event The event
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getPlayer();
        
        // Check if this is a merchant inventory (villager trade)
        if (event.getInventory() instanceof MerchantInventory) {
            MerchantInventory merchantInventory = (MerchantInventory) event.getInventory();
            
            // Additional logic can be added here if needed
        }
    }
}