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
 * BD Dealer villager type.
 */
public class BDDealer extends BDVillager {
    public static final String TYPE = "DEALER";
    
    /**
     * Creates a new BD dealer.
     * @param plugin The plugin instance
     * @param location The spawn location
     */
    public BDDealer(BDCraft plugin, Location location) {
        super(plugin, location, "BD Dealer", Villager.Profession.FARMER, Villager.Type.PLAINS);
    }
    
    /**
     * Creates a new BD dealer from an existing villager.
     * @param plugin The plugin instance
     * @param villager The villager
     */
    public BDDealer(BDCraft plugin, Villager villager) {
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
        
        // Add regular BD seeds trade (5 seeds for 1 emerald)
        MerchantRecipe regularSeedRecipe = new MerchantRecipe(
                createBDSeedItem("Regular BD Seeds", 5), 
                Integer.MAX_VALUE); // Unlimited uses
        
        // Set ingredients (1 emerald)
        regularSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        
        // Add green BD seeds trade (2 seeds for 5 emeralds)
        MerchantRecipe greenSeedRecipe = new MerchantRecipe(
                createBDSeedItem("Green BD Seeds", 2), 
                Integer.MAX_VALUE);
        
        // Set ingredients (5 emeralds)
        greenSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 5));
        
        // Add purple BD seeds trade (1 seed for 25 emeralds)
        MerchantRecipe purpleSeedRecipe = new MerchantRecipe(
                createBDSeedItem("Purple BD Seeds", 1), 
                Integer.MAX_VALUE);
        
        // Set ingredients (25 emeralds)
        purpleSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 25));
        
        // Add BD harvester trade (1 harvester for 16 emeralds + 1 diamond)
        MerchantRecipe harvesterRecipe = new MerchantRecipe(
                createBDHarvesterItem("BD Harvester", 1), 
                10); // Limited uses
        
        // Set ingredients (16 emeralds + 1 diamond)
        harvesterRecipe.addIngredient(new ItemStack(Material.EMERALD, 16));
        harvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 1));
        
        // Add Ultimate BD harvester trade (1 harvester for 32 emeralds + 5 diamonds)
        MerchantRecipe ultimateHarvesterRecipe = new MerchantRecipe(
                createBDHarvesterItem("Ultimate BD Harvester", 1), 
                5); // Very limited uses
        
        // Set ingredients (32 emeralds + 5 diamonds)
        ultimateHarvesterRecipe.addIngredient(new ItemStack(Material.EMERALD, 32));
        ultimateHarvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 5));
        
        // Add recipes to list
        recipes.add(regularSeedRecipe);
        recipes.add(greenSeedRecipe);
        recipes.add(purpleSeedRecipe);
        recipes.add(harvesterRecipe);
        recipes.add(ultimateHarvesterRecipe);
        
        // Set recipes
        villager.setRecipes(recipes);
    }
    
    /**
     * Creates a BD seed item for trading.
     * @param name The item name
     * @param amount The amount
     * @return The item
     */
    private ItemStack createBDSeedItem(String name, int amount) {
        // This is a placeholder - in real implementation, we would use BDItemFactory
        ItemStack item = new ItemStack(Material.WHEAT_SEEDS, amount);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a BD harvester item for trading.
     * @param name The item name
     * @param amount The amount
     * @return The item
     */
    private ItemStack createBDHarvesterItem(String name, int amount) {
        // This is a placeholder - in real implementation, we would use BDItemFactory
        ItemStack item = new ItemStack(Material.SHEARS, amount);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + name);
        item.setItemMeta(meta);
        return item;
    }
    
    @Override
    public String getVillagerType() {
        return TYPE;
    }
}