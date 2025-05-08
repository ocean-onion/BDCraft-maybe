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
import java.util.Random;

/**
 * BD Seasonal Trader villager type.
 */
public class BDSeasonalTrader extends BDVillager {
    public static final String TYPE = "SEASONAL_TRADER";
    private String currentSeason;
    
    /**
     * Creates a new BD seasonal trader.
     * @param plugin The plugin instance
     * @param location The spawn location
     */
    public BDSeasonalTrader(BDCraft plugin, Location location) {
        super(plugin, location, "Seasonal BD Trader", Villager.Profession.NITWIT, Villager.Type.PLAINS);
        this.currentSeason = determineCurrentSeason();
    }
    
    /**
     * Creates a new BD seasonal trader from an existing villager.
     * @param plugin The plugin instance
     * @param villager The villager
     */
    public BDSeasonalTrader(BDCraft plugin, Villager villager) {
        super(plugin, villager);
        this.currentSeason = determineCurrentSeason();
    }
    
    /**
     * Determines the current season based on real-world time.
     * @return The current season
     */
    private String determineCurrentSeason() {
        // In a real implementation, this would use real-world date
        // For simplicity, we'll just randomly pick a season
        String[] seasons = {"SPRING", "SUMMER", "FALL", "WINTER"};
        Random random = new Random();
        return seasons[random.nextInt(seasons.length)];
    }
    
    @Override
    protected void initializeTrades() {
        List<MerchantRecipe> recipes = new ArrayList<>();
        
        // Add seasonal specialties based on current season
        recipes.addAll(createSeasonalSpecialties());
        
        // Add special items (available in all seasons)
        recipes.add(createSpecialItemRecipe());
        
        // Add rare purple BD seeds
        recipes.add(createPurpleSeedRecipe());
        
        // Set recipes
        villager.setRecipes(recipes);
    }
    
    /**
     * Creates seasonal specialty recipes.
     * @return The recipes
     */
    private List<MerchantRecipe> createSeasonalSpecialties() {
        List<MerchantRecipe> recipes = new ArrayList<>();
        
        switch (currentSeason) {
            case "SPRING":
                // Spring-specific trades
                recipes.add(createSpringSpecialtyRecipe());
                break;
            case "SUMMER":
                // Summer-specific trades
                recipes.add(createSummerSpecialtyRecipe());
                break;
            case "FALL":
                // Fall-specific trades
                recipes.add(createFallSpecialtyRecipe());
                break;
            case "WINTER":
                // Winter-specific trades
                recipes.add(createWinterSpecialtyRecipe());
                break;
        }
        
        return recipes;
    }
    
    /**
     * Creates a spring specialty recipe.
     * @return The recipe
     */
    private MerchantRecipe createSpringSpecialtyRecipe() {
        // Create spring specialty item
        ItemStack item = new ItemStack(Material.LILY_PAD, 3);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Spring Growth Charm");
        item.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(item, 5); // Limited uses
        
        // Set ingredients
        recipe.addIngredient(new ItemStack(Material.EMERALD, 8));
        
        return recipe;
    }
    
    /**
     * Creates a summer specialty recipe.
     * @return The recipe
     */
    private MerchantRecipe createSummerSpecialtyRecipe() {
        // Create summer specialty item
        ItemStack item = new ItemStack(Material.SUNFLOWER, 2);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Summer Sun Charm");
        item.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(item, 5); // Limited uses
        
        // Set ingredients
        recipe.addIngredient(new ItemStack(Material.EMERALD, 8));
        
        return recipe;
    }
    
    /**
     * Creates a fall specialty recipe.
     * @return The recipe
     */
    private MerchantRecipe createFallSpecialtyRecipe() {
        // Create fall specialty item
        ItemStack item = new ItemStack(Material.PUMPKIN, 3);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Autumn Harvest Charm");
        item.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(item, 5); // Limited uses
        
        // Set ingredients
        recipe.addIngredient(new ItemStack(Material.EMERALD, 8));
        
        return recipe;
    }
    
    /**
     * Creates a winter specialty recipe.
     * @return The recipe
     */
    private MerchantRecipe createWinterSpecialtyRecipe() {
        // Create winter specialty item
        ItemStack item = new ItemStack(Material.SNOWBALL, 16);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Winter Frost Charm");
        item.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(item, 5); // Limited uses
        
        // Set ingredients
        recipe.addIngredient(new ItemStack(Material.EMERALD, 8));
        
        return recipe;
    }
    
    /**
     * Creates a special item recipe.
     * @return The recipe
     */
    private MerchantRecipe createSpecialItemRecipe() {
        // Create special item
        ItemStack item = new ItemStack(Material.GOLDEN_HOE, 1);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "BD Fortune Hoe");
        item.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(item, 1); // Very limited uses
        
        // Set ingredients
        recipe.addIngredient(new ItemStack(Material.DIAMOND, 2));
        recipe.addIngredient(new ItemStack(Material.EMERALD, 20));
        
        return recipe;
    }
    
    /**
     * Creates a purple BD seed recipe.
     * @return The recipe
     */
    private MerchantRecipe createPurpleSeedRecipe() {
        // Create purple BD seed
        ItemStack item = new ItemStack(Material.PUMPKIN_SEEDS, 1);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Purple BD Seed");
        item.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(item, 1); // Very limited uses
        
        // Set ingredients
        recipe.addIngredient(new ItemStack(Material.DIAMOND, 1));
        recipe.addIngredient(new ItemStack(Material.EMERALD, 25));
        
        return recipe;
    }
    
    /**
     * Gets the current season.
     * @return The current season
     */
    public String getCurrentSeason() {
        return currentSeason;
    }
    
    /**
     * Sets the current season and updates trades.
     * @param season The season
     */
    public void setCurrentSeason(String season) {
        this.currentSeason = season;
        // Update trades to reflect the new season
        initializeTrades();
    }
    
    @Override
    public String getVillagerType() {
        return TYPE;
    }
}