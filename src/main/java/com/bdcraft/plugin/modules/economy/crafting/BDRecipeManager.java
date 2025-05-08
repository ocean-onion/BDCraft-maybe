package com.bdcraft.plugin.modules.economy.crafting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop.CropType;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed.SeedType;
import com.bdcraft.plugin.modules.economy.items.tokens.BDToken;
import com.bdcraft.plugin.modules.economy.items.tokens.TokenType;
import com.bdcraft.plugin.modules.economy.items.tools.BDTool;
import com.bdcraft.plugin.modules.economy.items.tools.ToolType;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages BD recipes.
 */
public class BDRecipeManager {
    private final BDCraft plugin;
    private final BDItemManager itemManager;
    private final List<NamespacedKey> registeredRecipes;
    
    /**
     * Creates a new BD recipe manager.
     * 
     * @param plugin The plugin instance
     */
    public BDRecipeManager(BDCraft plugin) {
        this.plugin = plugin;
        this.itemManager = plugin.getEconomyModule().getItemManager();
        this.registeredRecipes = new ArrayList<>();
    }
    
    /**
     * Registers all BD recipes.
     */
    public void registerRecipes() {
        // Register token recipes
        registerMarketTokenRecipe();
        registerHouseTokenRecipe();
        registerTradeTokenRecipe();
        
        // Register tool recipes
        registerHarvesterRecipe();
        registerUltimateHarvesterRecipe();
        
        plugin.getLogger().info("Registered " + registeredRecipes.size() + " BD recipes");
    }
    
    /**
     * Unregisters all BD recipes.
     */
    public void unregisterRecipes() {
        for (NamespacedKey key : registeredRecipes) {
            Bukkit.removeRecipe(key);
        }
        
        registeredRecipes.clear();
        plugin.getLogger().info("Unregistered BD recipes");
    }
    
    /**
     * Registers the market token recipe.
     * 
     * Pattern:
     * EEE
     * EBE
     * EEE
     * 
     * Where E = Emerald, B = BD Crop
     */
    private void registerMarketTokenRecipe() {
        ItemStack marketToken = itemManager.createBDToken(TokenType.MARKET, 1);
        NamespacedKey key = new NamespacedKey(plugin, "market_token");
        
        ShapedRecipe recipe = new ShapedRecipe(key, marketToken);
        recipe.shape("EEE", "EBE", "EEE");
        recipe.setIngredient('E', Material.EMERALD);
        
        // Custom ingredient for BD Crop
        RecipeChoice.ExactChoice bdCropChoice = new RecipeChoice.ExactChoice(
                itemManager.createBDCrop(CropType.REGULAR, 1));
        recipe.setIngredient('B', bdCropChoice);
        
        Bukkit.addRecipe(recipe);
        registeredRecipes.add(key);
    }
    
    /**
     * Registers the house token recipe.
     * 
     * Pattern:
     * PPP
     * PBP
     * PPP
     * 
     * Where P = Planks (any type), B = Green BD Crop
     */
    private void registerHouseTokenRecipe() {
        ItemStack houseToken = itemManager.createBDToken(TokenType.HOUSE, 1);
        NamespacedKey key = new NamespacedKey(plugin, "house_token");
        
        ShapedRecipe recipe = new ShapedRecipe(key, houseToken);
        recipe.shape("PPP", "PBP", "PPP");
        
        // Accept any type of planks
        RecipeChoice.MaterialChoice planksChoice = new RecipeChoice.MaterialChoice(
                Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.BIRCH_PLANKS,
                Material.JUNGLE_PLANKS, Material.ACACIA_PLANKS, Material.DARK_OAK_PLANKS,
                Material.CRIMSON_PLANKS, Material.WARPED_PLANKS);
        recipe.setIngredient('P', planksChoice);
        
        // Custom ingredient for Green BD Crop
        RecipeChoice.ExactChoice bdCropChoice = new RecipeChoice.ExactChoice(
                itemManager.createBDCrop(CropType.GREEN, 1));
        recipe.setIngredient('B', bdCropChoice);
        
        Bukkit.addRecipe(recipe);
        registeredRecipes.add(key);
    }
    
    /**
     * Registers the trade token recipe.
     * 
     * Pattern:
     * GGG
     * GBG
     * GGG
     * 
     * Where G = Gold Ingot, B = Purple BD Crop
     */
    private void registerTradeTokenRecipe() {
        ItemStack tradeToken = itemManager.createBDToken(TokenType.TRADE, 1);
        NamespacedKey key = new NamespacedKey(plugin, "trade_token");
        
        ShapedRecipe recipe = new ShapedRecipe(key, tradeToken);
        recipe.shape("GGG", "GBG", "GGG");
        recipe.setIngredient('G', Material.GOLD_INGOT);
        
        // Custom ingredient for Purple BD Crop
        RecipeChoice.ExactChoice bdCropChoice = new RecipeChoice.ExactChoice(
                itemManager.createBDCrop(CropType.PURPLE, 1));
        recipe.setIngredient('B', bdCropChoice);
        
        Bukkit.addRecipe(recipe);
        registeredRecipes.add(key);
    }
    
    /**
     * Registers the harvester tool recipe.
     * 
     * Pattern:
     * BB
     * IS
     *  S
     * 
     * Where B = BD Crop, I = Iron Ingot, S = Stick
     */
    private void registerHarvesterRecipe() {
        ItemStack harvester = itemManager.createBDTool(ToolType.HARVESTER);
        NamespacedKey key = new NamespacedKey(plugin, "bd_harvester");
        
        ShapedRecipe recipe = new ShapedRecipe(key, harvester);
        recipe.shape("BB ", "IS ", " S ");
        
        // Custom ingredient for BD Crop
        RecipeChoice.ExactChoice bdCropChoice = new RecipeChoice.ExactChoice(
                itemManager.createBDCrop(CropType.REGULAR, 1));
        recipe.setIngredient('B', bdCropChoice);
        
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.STICK);
        
        Bukkit.addRecipe(recipe);
        registeredRecipes.add(key);
    }
    
    /**
     * Registers the ultimate harvester tool recipe.
     * 
     * Pattern:
     * BB
     * DS
     *  S
     * 
     * Where B = Green BD Crop, D = Diamond, S = Stick
     */
    private void registerUltimateHarvesterRecipe() {
        ItemStack ultimateHarvester = itemManager.createBDTool(ToolType.ULTIMATE_HARVESTER);
        NamespacedKey key = new NamespacedKey(plugin, "bd_ultimate_harvester");
        
        ShapedRecipe recipe = new ShapedRecipe(key, ultimateHarvester);
        recipe.shape("BB ", "DS ", " S ");
        
        // Custom ingredient for Green BD Crop
        RecipeChoice.ExactChoice bdCropChoice = new RecipeChoice.ExactChoice(
                itemManager.createBDCrop(CropType.GREEN, 1));
        recipe.setIngredient('B', bdCropChoice);
        
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('S', Material.STICK);
        
        Bukkit.addRecipe(recipe);
        registeredRecipes.add(key);
    }
    
    /**
     * Prevents using BD items in vanilla recipes.
     * This method should be called in recipe prepare events.
     * 
     * @param matrix The crafting matrix
     * @return True if any invalid BD items were found in the matrix
     */
    public boolean containsInvalidBDItems(ItemStack[] matrix) {
        for (ItemStack item : matrix) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            
            // Check if the item is a BD item
            if (itemManager.isBDItem(item)) {
                // Check if the recipe is a BD recipe (has a BD key in metadata)
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.getPersistentDataContainer().has(
                        new NamespacedKey(plugin, "bd_recipe"), PersistentDataType.STRING)) {
                    continue; // This is a valid BD recipe
                }
                
                return true; // Invalid usage of BD item in non-BD recipe
            }
        }
        
        return false;
    }
}