package com.bdcraft.plugin.modules.economy.crafting;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
     * DDD
     * ESE
     * DDD
     * 
     * Where D = Diamond, E = Emerald, S = BD Stick
     */
    private void registerMarketTokenRecipe() {
        ItemStack marketToken = itemManager.createBDToken(TokenType.MARKET, 1);
        NamespacedKey key = new NamespacedKey(plugin, "market_token");
        
        ShapedRecipe recipe = new ShapedRecipe(key, marketToken);
        recipe.shape("DDD", "ESE", "DDD");
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('E', Material.EMERALD);
        
        // Custom ingredient for BD Stick
        ItemStack bdStick = new ItemStack(Material.STICK);
        ItemMeta stickMeta = bdStick.getItemMeta();
        stickMeta.setDisplayName(ChatColor.GOLD + "BD Stick");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A special stick imbued with BD power");
        stickMeta.setLore(lore);
        stickMeta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "bd_item"), 
                PersistentDataType.STRING, "bd_stick");
        bdStick.setItemMeta(stickMeta);
        
        RecipeChoice.ExactChoice bdStickChoice = new RecipeChoice.ExactChoice(bdStick);
        recipe.setIngredient('S', bdStickChoice);
        
        Bukkit.addRecipe(recipe);
        registeredRecipes.add(key);
    }
    
    /**
     * Registers the house token recipe.
     * 
     * Pattern:
     * WWW
     * WBW
     * WWW
     * 
     * Where W = Wood Log (any type), B = BD Crop (any type)
     */
    private void registerHouseTokenRecipe() {
        ItemStack houseToken = itemManager.createBDToken(TokenType.HOUSE, 1);
        NamespacedKey key = new NamespacedKey(plugin, "house_token");
        
        ShapedRecipe recipe = new ShapedRecipe(key, houseToken);
        recipe.shape("WWW", "WBW", "WWW");
        
        // Accept any type of logs
        RecipeChoice.MaterialChoice logChoice = new RecipeChoice.MaterialChoice(
                Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG,
                Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG,
                Material.CRIMSON_STEM, Material.WARPED_STEM);
        recipe.setIngredient('W', logChoice);
        
        // Custom ingredient for any BD Crop
        List<ItemStack> cropOptions = new ArrayList<>();
        cropOptions.add(itemManager.createBDCrop(CropType.REGULAR, 1));
        cropOptions.add(itemManager.createBDCrop(CropType.GREEN, 1));
        cropOptions.add(itemManager.createBDCrop(CropType.PURPLE, 1));
        
        RecipeChoice.ExactChoice bdCropChoice = new RecipeChoice.ExactChoice(cropOptions);
        recipe.setIngredient('B', bdCropChoice);
        
        Bukkit.addRecipe(recipe);
        registeredRecipes.add(key);
    }
    
    /**
     * Registers the trade token recipe.
     * 
     * Pattern:
     * GDG
     * DBD
     * GDG
     * 
     * Where G = Gold Block, D = Diamond, B = Purple BD Crop
     */
    private void registerTradeTokenRecipe() {
        ItemStack tradeToken = itemManager.createBDToken(TokenType.TRADE, 1);
        NamespacedKey key = new NamespacedKey(plugin, "trade_token");
        
        ShapedRecipe recipe = new ShapedRecipe(key, tradeToken);
        recipe.shape("GDG", "DBD", "GDG");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('D', Material.DIAMOND);
        
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
     * CCC
     * IHI
     * BSB
     * 
     * Where C = BD Crop, I = Iron Ingot, H = Iron Hoe, B = Iron Block, S = Stick
     */
    private void registerHarvesterRecipe() {
        ItemStack harvester = itemManager.createBDTool(ToolType.HARVESTER);
        NamespacedKey key = new NamespacedKey(plugin, "bd_harvester");
        
        ShapedRecipe recipe = new ShapedRecipe(key, harvester);
        recipe.shape("CCC", "IHI", "BSB");
        
        // Custom ingredient for BD Crop
        RecipeChoice.ExactChoice bdCropChoice = new RecipeChoice.ExactChoice(
                itemManager.createBDCrop(CropType.REGULAR, 1));
        recipe.setIngredient('C', bdCropChoice);
        
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('H', Material.IRON_HOE);
        recipe.setIngredient('B', Material.IRON_BLOCK);
        recipe.setIngredient('S', Material.STICK);
        
        Bukkit.addRecipe(recipe);
        registeredRecipes.add(key);
    }
    
    /**
     * Registers the ultimate harvester tool recipe.
     * 
     * Pattern:
     * PPP
     * DHD
     * DBD
     * 
     * Where P = Purple BD Crop, D = Diamond, H = Diamond Hoe, B = Diamond Block
     */
    private void registerUltimateHarvesterRecipe() {
        ItemStack ultimateHarvester = itemManager.createBDTool(ToolType.ULTIMATE_HARVESTER);
        NamespacedKey key = new NamespacedKey(plugin, "bd_ultimate_harvester");
        
        ShapedRecipe recipe = new ShapedRecipe(key, ultimateHarvester);
        recipe.shape("PPP", "DHD", "DBD");
        
        // Custom ingredient for Purple BD Crop
        RecipeChoice.ExactChoice bdCropChoice = new RecipeChoice.ExactChoice(
                itemManager.createBDCrop(CropType.PURPLE, 1));
        recipe.setIngredient('P', bdCropChoice);
        
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('H', Material.DIAMOND_HOE);
        recipe.setIngredient('B', Material.DIAMOND_BLOCK);
        
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