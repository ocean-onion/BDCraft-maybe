package com.bdcraft.plugin.modules.economy.villagers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop.CropType;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed.SeedType;
import com.bdcraft.plugin.modules.economy.items.tools.ToolType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Represents a Seasonal BD Trader villager that offers rare and seasonal items.
 */
public class SeasonalBDTraderVillager extends BDVillager {
    private final Season currentSeason;
    
    /**
     * Creates a new Seasonal BD Trader villager.
     * 
     * @param plugin The plugin instance
     * @param uuid The UUID of the villager
     * @param entity The villager entity (or null if not spawned yet)
     */
    public SeasonalBDTraderVillager(BDCraft plugin, UUID uuid, Villager entity) {
        super(plugin, uuid, entity, "Seasonal BD Trader", VillagerType.SEASONAL_TRADER);
        this.currentSeason = determineSeason();
    }
    
    @Override
    public Villager spawn(Location location) {
        Villager villager = super.spawn(location);
        setupTrades();
        return villager;
    }
    
    @Override
    protected Profession getBukkitProfession() {
        return Profession.NITWIT; // According to documentation
    }
    
    @Override
    public void setupTrades() {
        if (entity == null) {
            return;
        }
        
        // Clear existing trades
        entity.setRecipes(new ArrayList<>());
        
        // Add trades based on season
        List<MerchantRecipe> trades = new ArrayList<>();
        
        // Always have purple BD seeds available
        trades.add(createPurpleSeedTrade());
        
        // Add Ultimate BD Harvester (rare)
        trades.add(createUltimateHarvesterTrade());
        
        // Add season-specific trades
        switch (currentSeason) {
            case SPRING:
                trades.addAll(createSpringTrades());
                break;
            case SUMMER:
                trades.addAll(createSummerTrades());
                break;
            case FALL:
                trades.addAll(createFallTrades());
                break;
            case WINTER:
                trades.addAll(createWinterTrades());
                break;
        }
        
        // Apply trades to villager
        entity.setRecipes(trades);
    }
    
    /**
     * Creates a trade for purple BD seeds.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createPurpleSeedTrade() {
        ItemStack seeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.PURPLE, 1);
        
        MerchantRecipe recipe = new MerchantRecipe(seeds, 0, 3, true);
        recipe.addIngredient(new ItemStack(Material.EMERALD, 25));
        
        return recipe;
    }
    
    /**
     * Creates a trade for the Ultimate BD Harvester.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createUltimateHarvesterTrade() {
        ItemStack harvester = plugin.getEconomyModule().getItemManager().createBDTool(ToolType.ULTIMATE_HARVESTER);
        
        MerchantRecipe recipe = new MerchantRecipe(harvester, 0, 1, true);
        recipe.addIngredient(new ItemStack(Material.DIAMOND, 32));
        
        return recipe;
    }
    
    /**
     * Creates spring-specific trades.
     * 
     * @return A list of spring trades
     */
    private List<MerchantRecipe> createSpringTrades() {
        List<MerchantRecipe> trades = new ArrayList<>();
        
        // Faster-growing seed variants
        ItemStack fasterSeeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.GREEN, 3);
        MerchantRecipe fasterSeedRecipe = new MerchantRecipe(fasterSeeds, 0, 5, true);
        fasterSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 20));
        trades.add(fasterSeedRecipe);
        
        // More favorable green seed trade
        ItemStack greenSeeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.GREEN, 1);
        MerchantRecipe greenSeedRecipe = new MerchantRecipe(greenSeeds, 0, 10, true);
        greenSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 6)); // Reduced price
        trades.add(greenSeedRecipe);
        
        return trades;
    }
    
    /**
     * Creates summer-specific trades.
     * 
     * @return A list of summer trades
     */
    private List<MerchantRecipe> createSummerTrades() {
        List<MerchantRecipe> trades = new ArrayList<>();
        
        // Heat-resistant seeds - custom lore on regular seeds
        ItemStack heatSeeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.REGULAR, 5);
        org.bukkit.inventory.meta.ItemMeta meta = heatSeeds.getItemMeta();
        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
        lore.add(ChatColor.RED + "Heat-resistant: Doesn't require water");
        meta.setLore(lore);
        heatSeeds.setItemMeta(meta);
        
        MerchantRecipe heatSeedRecipe = new MerchantRecipe(heatSeeds, 0, 5, true);
        heatSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 15));
        trades.add(heatSeedRecipe);
        
        // BD Harvester with bonus durability
        ItemStack harvester = plugin.getEconomyModule().getItemManager().createBDTool(ToolType.HARVESTER);
        org.bukkit.inventory.meta.ItemMeta harvesterMeta = harvester.getItemMeta();
        List<String> harvesterLore = harvesterMeta.getLore() != null ? harvesterMeta.getLore() : new ArrayList<>();
        harvesterLore.add(ChatColor.GOLD + "Summer Special: +10 uses");
        harvesterMeta.setLore(harvesterLore);
        harvester.setItemMeta(harvesterMeta);
        
        MerchantRecipe harvesterRecipe = new MerchantRecipe(harvester, 0, 3, true);
        harvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 12));
        trades.add(harvesterRecipe);
        
        return trades;
    }
    
    /**
     * Creates fall-specific trades.
     * 
     * @return A list of fall trades
     */
    private List<MerchantRecipe> createFallTrades() {
        List<MerchantRecipe> trades = new ArrayList<>();
        
        // Purple seed discount
        ItemStack purpleSeeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.PURPLE, 1);
        MerchantRecipe purpleSeedRecipe = new MerchantRecipe(purpleSeeds, 0, 5, true);
        purpleSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 20)); // Discounted from 25
        trades.add(purpleSeedRecipe);
        
        // Special autumn-themed item - custom named pumpkin
        ItemStack autumnItem = new ItemStack(Material.PUMPKIN);
        org.bukkit.inventory.meta.ItemMeta meta = autumnItem.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Harvest Pumpkin");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "A special autumn-themed decoration");
        meta.setLore(lore);
        autumnItem.setItemMeta(meta);
        
        MerchantRecipe autumnRecipe = new MerchantRecipe(autumnItem, 0, 10, true);
        autumnRecipe.addIngredient(new ItemStack(Material.EMERALD, 5));
        trades.add(autumnRecipe);
        
        return trades;
    }
    
    /**
     * Creates winter-specific trades.
     * 
     * @return A list of winter trades
     */
    private List<MerchantRecipe> createWinterTrades() {
        List<MerchantRecipe> trades = new ArrayList<>();
        
        // Cold-resistant seeds - custom lore on regular seeds
        ItemStack coldSeeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.REGULAR, 5);
        org.bukkit.inventory.meta.ItemMeta meta = coldSeeds.getItemMeta();
        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
        lore.add(ChatColor.AQUA + "Cold-resistant: Grows in cold biomes");
        meta.setLore(lore);
        coldSeeds.setItemMeta(meta);
        
        MerchantRecipe coldSeedRecipe = new MerchantRecipe(coldSeeds, 0, 5, true);
        coldSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 15));
        trades.add(coldSeedRecipe);
        
        // Special winter item - snowball that gives frost walker effect
        ItemStack winterItem = new ItemStack(Material.SNOWBALL);
        org.bukkit.inventory.meta.ItemMeta winterMeta = winterItem.getItemMeta();
        winterMeta.setDisplayName(ChatColor.AQUA + "Frost Walker Snowball");
        List<String> winterLore = new ArrayList<>();
        winterLore.add(ChatColor.WHITE + "Gives Frost Walker effect when thrown");
        winterMeta.setLore(winterLore);
        winterItem.setItemMeta(winterMeta);
        
        MerchantRecipe winterRecipe = new MerchantRecipe(winterItem, 0, 10, true);
        winterRecipe.addIngredient(new ItemStack(Material.EMERALD, 8));
        trades.add(winterRecipe);
        
        // End-of-year special offer - discounted bundle of seeds
        ItemStack seedBundle = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.GREEN, 5);
        MerchantRecipe bundleRecipe = new MerchantRecipe(seedBundle, 0, 2, true);
        bundleRecipe.addIngredient(new ItemStack(Material.EMERALD, 10)); // Great value
        trades.add(bundleRecipe);
        
        return trades;
    }
    
    /**
     * Determines the current Minecraft season based on the world's time.
     * 
     * @return The current season
     */
    private Season determineSeason() {
        if (entity != null && entity.getWorld() != null) {
            long worldDays = entity.getWorld().getFullTime() / 24000L;
            
            if (worldDays % 20000 < 5000) {
                return Season.SPRING;
            } else if (worldDays % 20000 < 10000) {
                return Season.SUMMER;
            } else if (worldDays % 20000 < 15000) {
                return Season.FALL;
            } else {
                return Season.WINTER;
            }
        }
        
        // Default to a random season if world is not available
        return Season.values()[new Random().nextInt(Season.values().length)];
    }
    
    /**
     * Gets the current season for this trader.
     * 
     * @return The current season
     */
    public Season getCurrentSeason() {
        return currentSeason;
    }
    
    /**
     * Enum for Minecraft seasons.
     */
    public enum Season {
        SPRING,
        SUMMER,
        FALL,
        WINTER
    }
}