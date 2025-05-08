package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * BD Collector villager type.
 */
public class BDCollector extends BDVillager {
    public static final String TYPE = "COLLECTOR";
    
    /**
     * Creates a new BD collector.
     * @param plugin The plugin instance
     * @param location The spawn location
     */
    public BDCollector(BDCraft plugin, Location location) {
        super(plugin, location, "BD Collector", Villager.Profession.LIBRARIAN, Villager.Type.PLAINS);
    }
    
    /**
     * Creates a new BD collector from an existing villager.
     * @param plugin The plugin instance
     * @param villager The existing villager
     */
    public BDCollector(BDCraft plugin, Villager villager) {
        super(plugin, villager);
    }
    
    @Override
    protected void initializeTrades() {
        updateTrades();
    }
    
    /**
     * Updates the villager's trades.
     */
    @Override
    public void updateTrades() {
        List<MerchantRecipe> recipes = new ArrayList<>();
        
        // Regular BD Crops trade (player gives 10 regular crops and receives 2 emeralds)
        MerchantRecipe regularCropRecipe = new MerchantRecipe(
                new ItemStack(Material.EMERALD, 2), 
                Integer.MAX_VALUE); // Unlimited uses
        
        // Set ingredients (10 regular BD crops - represented as ferns)
        regularCropRecipe.addIngredient(createBDCropItem("Regular BD Crop", 10));
        
        // Green BD Crops trade (player gives 5 green crops and receives 10 emeralds)
        MerchantRecipe greenCropRecipe = new MerchantRecipe(
                new ItemStack(Material.EMERALD, 10), 
                Integer.MAX_VALUE);
        
        // Set ingredients (5 green BD crops - represented as large ferns)
        greenCropRecipe.addIngredient(createBDCropItem("Green BD Crop", 5));
        
        // Purple BD Crops trade (player gives 3 purple crops and receives 20 emeralds)
        MerchantRecipe purpleCropRecipe = new MerchantRecipe(
                new ItemStack(Material.EMERALD, 20), 
                Integer.MAX_VALUE);
        
        // Set ingredients (3 purple BD crops)
        purpleCropRecipe.addIngredient(createBDCropItem("Purple BD Crop", 3));
        
        // Bulk Regular BD Crops trade (player gives 50 regular crops and receives 1 diamond)
        MerchantRecipe bulkRegularCropRecipe = new MerchantRecipe(
                new ItemStack(Material.DIAMOND, 1), 
                Integer.MAX_VALUE);
        
        // Set ingredients (50 regular BD crops)
        bulkRegularCropRecipe.addIngredient(createBDCropItem("Regular BD Crop", 50));
        
        // Add recipes to list
        recipes.add(regularCropRecipe);
        recipes.add(greenCropRecipe);
        recipes.add(purpleCropRecipe);
        recipes.add(bulkRegularCropRecipe);
        
        // Set recipes
        villager.setRecipes(recipes);
    }
    
    /**
     * Creates a BD crop item for trading.
     * @param name The item name
     * @param amount The amount
     * @return The item
     */
    private ItemStack createBDCropItem(String name, int amount) {
        // This is a placeholder - in real implementation, we would use BDItemFactory
        Material material;
        
        if (name.contains("Regular")) {
            material = Material.FERN;
        } else if (name.contains("Green")) {
            material = Material.LARGE_FERN;
        } else {
            material = Material.TALL_GRASS; // Purple crop placeholder
        }
        
        ItemStack item = new ItemStack(material, amount);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        item.setItemMeta(meta);
        return item;
    }
    
    @Override
    public String getVillagerType() {
        return TYPE;
    }
}