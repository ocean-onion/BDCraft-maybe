package com.bdcraft.plugin.modules.economy.crafting;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.bdcraft.plugin.BDCraft;

/**
 * Listener for BD recipe preparation.
 */
public class BDRecipeListener implements Listener {
    private final BDCraft plugin;
    private final BDRecipeManager recipeManager;
    
    /**
     * Creates a new BD recipe listener.
     * 
     * @param plugin The plugin instance
     * @param recipeManager The recipe manager
     */
    public BDRecipeListener(BDCraft plugin, BDRecipeManager recipeManager) {
        this.plugin = plugin;
        this.recipeManager = recipeManager;
    }
    
    /**
     * Handles recipe preparation.
     * 
     * @param event The recipe preparation event
     */
    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();
        
        // Check if the crafting matrix contains any invalid BD items
        if (recipeManager.containsInvalidBDItems(matrix)) {
            // Cancel crafting by setting the result to null
            inventory.setResult(null);
            
            // Notify player
            if (event.getView().getPlayer() != null) {
                event.getView().getPlayer().sendMessage(
                        ChatColor.RED + "BD items cannot be used in regular crafting recipes.");
            }
        }
    }
}