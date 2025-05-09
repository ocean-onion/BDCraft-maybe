package com.bdcraft.plugin.modules.economy.listeners;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

/**
 * Validates crafting recipes that require custom BD items.
 */
public class RecipeValidationListener implements Listener {
    private final BDCraft plugin;
    
    /**
     * Creates a new recipe validation listener.
     * 
     * @param plugin The plugin instance
     */
    public RecipeValidationListener(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Validates custom BD recipes during crafting preparation.
     * 
     * @param event The prepare item craft event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        CraftingInventory inventory = event.getInventory();
        
        if (recipe == null) {
            return;
        }
        
        // Check if this is a BD Stick recipe
        if (isBDStickRecipe(recipe)) {
            validateBDStickRecipe(event, inventory);
        }
        
        // Additional recipe validations can be added here in the future
    }
    
    /**
     * Determines if a recipe is for crafting a BD Stick.
     * 
     * @param recipe The recipe
     * @return true if the recipe is for a BD Stick, false otherwise
     */
    private boolean isBDStickRecipe(Recipe recipe) {
        if (recipe instanceof ShapelessRecipe) {
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
            
            // Instead of using getRecipeKey, we'll check the result item name directly
            ItemStack result = shapelessRecipe.getResult();
            if (result != null && result.hasItemMeta() && result.getItemMeta().hasDisplayName()) {
                String displayName = result.getItemMeta().getDisplayName();
                // Check if the result is a BD Stick
                if (displayName.contains("BD Stick")) {
                    return true;
                }
            }
            
            // Check by key name pattern as a fallback
            String keyName = shapelessRecipe.getKey().getKey();
            return keyName.equalsIgnoreCase("bd_stick");
        }
        
        return false;
    }
    
    /**
     * Validates that a BD Stick recipe has a proper BD Crop.
     * 
     * @param event The prepare item craft event
     * @param inventory The crafting inventory
     */
    private void validateBDStickRecipe(PrepareItemCraftEvent event, CraftingInventory inventory) {
        // Check if one of the ingredients is a BD Crop
        boolean hasBDCrop = false;
        
        for (ItemStack item : inventory.getMatrix()) {
            // Since we're not using BDCrop.isBDCrop, we need to detect BD crops by their attributes
            // BD crops have custom display names and lore
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String displayName = item.getItemMeta().getDisplayName();
                // Check if this is a BD crop by its display name
                if (displayName.contains("Crop") && 
                    (displayName.contains("Standard") || 
                     displayName.contains("Quality") || 
                     displayName.contains("Premium") || 
                     displayName.contains("Exceptional") || 
                     displayName.contains("Legendary"))) {
                    hasBDCrop = true;
                    break;
                }
            }
        }
        
        if (!hasBDCrop) {
            // No BD Crop found, cancel crafting by setting result to null
            inventory.setResult(null);
            
            // Notify the player if possible
            if (event.getView().getPlayer() instanceof Player) {
                Player player = (Player) event.getView().getPlayer();
                player.sendMessage(ChatColor.RED + "You need to use a BD Crop for this recipe!");
            }
        }
    }
}