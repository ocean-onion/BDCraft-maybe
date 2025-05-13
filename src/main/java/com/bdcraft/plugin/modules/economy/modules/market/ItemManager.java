package com.bdcraft.plugin.modules.economy.modules.market;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manages item operations for the economy module, including
 * tracking player emerald balances and handling item exchanges.
 */
public class ItemManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<UUID, Integer> cachedEmeraldBalances = new HashMap<>();
    
    /**
     * Creates a new item manager.
     * 
     * @param plugin The plugin instance
     */
    public ItemManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    /**
     * Gets the amount of emeralds a player has in their inventory.
     * 
     * @param player The player to check
     * @return The number of emeralds
     */
    public int getPlayerEmeralds(Player player) {
        int emeralds = 0;
        PlayerInventory inventory = player.getInventory();
        
        // Count emerald items
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.EMERALD) {
                emeralds += item.getAmount();
            }
        }
        
        // Count emerald blocks (worth 9 emeralds each)
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.EMERALD_BLOCK) {
                emeralds += (item.getAmount() * 9);
            }
        }
        
        // Update cache
        cachedEmeraldBalances.put(player.getUniqueId(), emeralds);
        
        return emeralds;
    }
    
    /**
     * Attempts to remove emeralds from a player's inventory.
     * Will optimize by using blocks where possible.
     * 
     * @param player The player to remove emeralds from
     * @param amount The amount of emeralds to remove
     * @return True if successful, false if the player doesn't have enough emeralds
     */
    public boolean removePlayerEmeralds(Player player, int amount) {
        // First, check if player has enough emeralds
        int currentEmeralds = getPlayerEmeralds(player);
        if (currentEmeralds < amount) {
            return false;
        }
        
        PlayerInventory inventory = player.getInventory();
        int remainingToRemove = amount;
        
        // First try to use emerald blocks if removing 9+ emeralds
        if (remainingToRemove >= 9) {
            int blocksToRemove = remainingToRemove / 9;
            
            for (int i = 0; i < inventory.getSize() && blocksToRemove > 0; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() == Material.EMERALD_BLOCK) {
                    int blockCount = item.getAmount();
                    
                    if (blockCount <= blocksToRemove) {
                        // Remove all blocks in this stack
                        remainingToRemove -= (blockCount * 9);
                        blocksToRemove -= blockCount;
                        inventory.setItem(i, null);
                    } else {
                        // Remove partial stack
                        item.setAmount(blockCount - blocksToRemove);
                        remainingToRemove -= (blocksToRemove * 9);
                        blocksToRemove = 0;
                    }
                }
            }
        }
        
        // Remove remaining emeralds
        if (remainingToRemove > 0) {
            for (int i = 0; i < inventory.getSize() && remainingToRemove > 0; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() == Material.EMERALD) {
                    int emeraldCount = item.getAmount();
                    
                    if (emeraldCount <= remainingToRemove) {
                        // Remove all emeralds in this stack
                        remainingToRemove -= emeraldCount;
                        inventory.setItem(i, null);
                    } else {
                        // Remove partial stack
                        item.setAmount(emeraldCount - remainingToRemove);
                        remainingToRemove = 0;
                    }
                }
            }
        }
        
        // If we still have emeralds to remove, something went wrong
        if (remainingToRemove > 0) {
            logger.warning("Failed to remove all emeralds from player " + player.getName() + 
                    " (requested: " + amount + ", remaining: " + remainingToRemove + ")");
            return false;
        }
        
        // Update cache
        cachedEmeraldBalances.put(player.getUniqueId(), currentEmeralds - amount);
        return true;
    }
    
    /**
     * Adds emeralds to a player's inventory.
     * Will optimize by using blocks where possible.
     * 
     * @param player The player to give emeralds to
     * @param amount The amount of emeralds to give
     * @return True if successful, false if there was an issue
     */
    public boolean addPlayerEmeralds(Player player, int amount) {
        PlayerInventory inventory = player.getInventory();
        
        // First, give as many emerald blocks as possible
        int blocksToGive = amount / 9;
        int remainingEmeralds = amount % 9;
        
        if (blocksToGive > 0) {
            ItemStack blocks = new ItemStack(Material.EMERALD_BLOCK, blocksToGive);
            HashMap<Integer, ItemStack> leftover = inventory.addItem(blocks);
            
            // If there's leftover, convert back to emeralds
            if (!leftover.isEmpty()) {
                ItemStack leftoverBlocks = leftover.get(0);
                if (leftoverBlocks != null) {
                    remainingEmeralds += (leftoverBlocks.getAmount() * 9);
                }
            }
        }
        
        // Give remaining emeralds
        if (remainingEmeralds > 0) {
            ItemStack emeralds = new ItemStack(Material.EMERALD, remainingEmeralds);
            HashMap<Integer, ItemStack> leftover = inventory.addItem(emeralds);
            
            if (!leftover.isEmpty()) {
                // Couldn't add all emeralds
                logger.warning("Could not add all emeralds to player " + player.getName() + 
                        " (leftover: " + leftover.get(0).getAmount() + ")");
                
                // Drop leftover emeralds at player's feet
                player.getWorld().dropItemNaturally(player.getLocation(), leftover.get(0));
            }
        }
        
        // Update cache
        cachedEmeraldBalances.put(player.getUniqueId(), getPlayerEmeralds(player));
        return true;
    }
    
    /**
     * Gets the cached emerald balance for a player.
     * Uses getPlayerEmeralds() if cache is empty.
     * 
     * @param player The player to check
     * @return The number of emeralds
     */
    public int getCachedEmeralds(Player player) {
        UUID playerId = player.getUniqueId();
        if (cachedEmeraldBalances.containsKey(playerId)) {
            return cachedEmeraldBalances.get(playerId);
        }
        
        return getPlayerEmeralds(player);
    }
    
    /**
     * Clears the emerald balance cache.
     */
    public void clearCache() {
        cachedEmeraldBalances.clear();
    }
    
    /**
     * Creates a custom item with specific metadata.
     * 
     * @param material The material type
     * @param name The custom display name
     * @param amount The amount to create
     * @return The created item stack
     */
    public ItemStack createCustomItem(Material material, String name, int amount) {
        ItemStack item = new ItemStack(material, amount);
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        
        return item;
    }
}