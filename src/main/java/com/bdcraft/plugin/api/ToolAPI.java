package com.bdcraft.plugin.api;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * API for BD tools operations.
 */
public interface ToolAPI {
    /**
     * Gets the tool type of an item.
     * @param item The item to check
     * @return The tool type, or null if not a BD tool
     */
    String getToolType(ItemStack item);
    
    /**
     * Checks if an item is a valid BD tool.
     * @param item The item to check
     * @return Whether the item is a BD tool
     */
    boolean isBDTool(ItemStack item);
    
    /**
     * Creates a BD Stick item.
     * @return The BD Stick item
     */
    ItemStack createBDStick();
    
    /**
     * Creates a BD Harvester item.
     * @return The BD Harvester item
     */
    ItemStack createBDHarvester();
    
    /**
     * Creates an Ultimate BD Harvester item.
     * @return The Ultimate BD Harvester item
     */
    ItemStack createUltimateBDHarvester();
    
    /**
     * Gets the durability of a BD tool.
     * @param item The tool item
     * @return The current durability, or -1 if not a valid tool
     */
    int getToolDurability(ItemStack item);
    
    /**
     * Gets the maximum durability of a BD tool.
     * @param item The tool item
     * @return The maximum durability, or -1 if not a valid tool
     */
    int getToolMaxDurability(ItemStack item);
    
    /**
     * Damages a BD tool.
     * @param item The tool item
     * @param amount The amount of damage to apply
     * @return Whether the tool survived the damage
     */
    boolean damageTool(ItemStack item, int amount);
    
    /**
     * Repairs a BD tool.
     * @param item The tool item
     * @param amount The amount of durability to restore
     * @return Whether the repair was successful
     */
    boolean repairTool(ItemStack item, int amount);
    
    /**
     * Uses a BD Stick.
     * @param player The player using the stick
     * @param item The BD Stick item
     * @return Whether the stick was used successfully
     */
    boolean useBDStick(Player player, ItemStack item);
    
    /**
     * Uses a harvester on a crop block.
     * @param player The player using the harvester
     * @param item The harvester item
     * @param block The crop block
     * @return The number of extra drops generated, or -1 if unsuccessful
     */
    int useHarvester(Player player, ItemStack item, Block block);
}