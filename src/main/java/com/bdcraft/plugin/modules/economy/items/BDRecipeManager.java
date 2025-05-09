package com.bdcraft.plugin.modules.economy.items;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages BD item crafting recipes.
 */
public class BDRecipeManager {
    private final BDCraft plugin;
    private final BDItemManager itemManager;
    private final Logger logger;
    private final Map<String, NamespacedKey> recipeKeys;

    /**
     * Creates a new recipe manager.
     * @param plugin The plugin instance
     * @param itemManager The item manager
     */
    public BDRecipeManager(BDCraft plugin, BDItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.logger = plugin.getLogger();
        this.recipeKeys = new HashMap<>();
        
        // Register recipes
        registerRecipes();
    }
    
    /**
     * Registers all BD item crafting recipes.
     */
    private void registerRecipes() {
        // Register BD Market Token recipe
        registerMarketTokenRecipe();
        
        // Register BD House Token recipe
        registerHouseTokenRecipe();
        
        // Register BD Stick recipe
        registerBDStickRecipe();
        
        logger.info("Registered BD item recipes");
    }
    
    /**
     * Registers the BD Market Token recipe.
     */
    private void registerMarketTokenRecipe() {
        // Create recipe key
        NamespacedKey key = new NamespacedKey(plugin, "bd_market_token");
        recipeKeys.put("market_token", key);
        
        // Create the item
        ItemStack marketToken = itemManager.createItem("market_token", 1);
        
        // Create the recipe
        ShapedRecipe recipe = new ShapedRecipe(key, marketToken);
        
        // Set the shape (emerald in center, gold blocks in corners, iron blocks on edges)
        recipe.shape("GIG", "IEI", "GIG");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('E', Material.EMERALD_BLOCK);
        
        // Register the recipe
        Bukkit.addRecipe(recipe);
        logger.info("Registered BD Market Token recipe");
    }
    
    /**
     * Registers the BD House Token recipe.
     */
    private void registerHouseTokenRecipe() {
        // Create recipe key
        NamespacedKey key = new NamespacedKey(plugin, "bd_house_token");
        recipeKeys.put("house_token", key);
        
        // Create the item
        ItemStack houseToken = itemManager.createItem("house_token", 1);
        
        // Create the recipe
        ShapedRecipe recipe = new ShapedRecipe(key, houseToken);
        
        // Set the shape (diamond block in center, oak planks around it)
        recipe.shape("OOO", "ODO", "OOO");
        recipe.setIngredient('O', Material.OAK_PLANKS);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        
        // Register the recipe
        Bukkit.addRecipe(recipe);
        logger.info("Registered BD House Token recipe");
    }
    
    /**
     * Registers the BD Stick recipe.
     */
    private void registerBDStickRecipe() {
        // Create recipe key
        NamespacedKey key = new NamespacedKey(plugin, "bd_stick");
        recipeKeys.put("bd_stick", key);
        
        // Create the item
        ItemStack bdStick = itemManager.createItem("bd_stick", 1);
        
        // Create the recipe - shapeless: paper + BD crop + flint
        ShapelessRecipe recipe = new ShapelessRecipe(key, bdStick);
        
        // Add ingredients
        recipe.addIngredient(Material.PAPER);
        recipe.addIngredient(Material.FLINT);
        
        // Special ingredient: BD Crop - for crafting with metadata items, we have to use the material
        // but check for the custom item in the CraftItemEvent
        recipe.addIngredient(Material.WHEAT);
        
        // Register a custom recipe checker in the plugin's listener to validate that the wheat
        // is actually a BD Crop when crafting occurs
        
        // Register the recipe
        Bukkit.addRecipe(recipe);
        logger.info("Registered BD Stick recipe");
    }
    
    /**
     * Removes all BD item recipes.
     */
    public void unregisterRecipes() {
        for (NamespacedKey key : recipeKeys.values()) {
            Bukkit.removeRecipe(key);
        }
        recipeKeys.clear();
        logger.info("Unregistered all BD item recipes");
    }
    
    /**
     * Gets a recipe key by name.
     * @param name The recipe name
     * @return The recipe key, or null if it doesn't exist
     */
    public NamespacedKey getRecipeKey(String name) {
        return recipeKeys.get(name.toLowerCase());
    }
}