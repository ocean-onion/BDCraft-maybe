package com.bdcraft.plugin.modules.economy.villagers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
    public static final String TYPE = "BD_SEASONAL_TRADER";
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
            // Get current day number - 20000 days is a full cycle
            long worldDays = entity.getWorld().getFullTime() / 24000L;
            long cycleDays = worldDays % 20000;
            
            // Use season range from documentation
            for (Season season : Season.values()) {
                if (cycleDays >= season.getStartDay() && cycleDays <= season.getEndDay()) {
                    return season;
                }
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
    
    @Override
    public boolean onInteract(Player player) {
        // Standard trading interface
        return false; // Return false to allow default trading behavior
    }
    
    @Override
    public boolean onDamage(double damage) {
        // Seasonal BD Trader villagers cannot be damaged
        return true; // Cancel damage
    }
    
    @Override
    public boolean onProfessionChange(Villager.Profession newProfession) {
        // Seasonal BD Trader villagers cannot change profession
        return true; // Cancel change
    }
    
    @Override
    public boolean shouldRemove() {
        // These are special traders that should not be removed automatically
        return false;
    }
    
    @Override
    public void onTick() {
        // Nothing special to do per tick
    }
    
    @Override
    public void onRemove() {
        // Nothing special to do
    }
    
    @Override
    public String getTypeName() {
        return TYPE;
    }
    
    /**
     * Enum for Minecraft seasons.
     */
    /**
     * Enum representing Minecraft seasons according to documentation.
     * These align with the Trading Calendar days from the documentation.
     */
    public enum Season {
        /**
         * Spring season (Days 0-5000).
         * Special items: Faster-growing seed variants, more favorable green seed trades.
         */
        SPRING(0, 5000),
        
        /**
         * Summer season (Days 5001-10000).
         * Special items: Heat-resistant seeds that don't require water, BD Harvester with bonus durability.
         */
        SUMMER(5001, 10000),
        
        /**
         * Fall season (Days 10001-15000).
         * Special items: Purple seed discount, special autumn-themed items.
         */
        FALL(10001, 15000),
        
        /**
         * Winter season (Days 15001-20000).
         * Special items: Cold-resistant seeds, special winter-themed items, end-of-year special offers.
         */
        WINTER(15001, 20000);
        
        private final int startDay;
        private final int endDay;
        
        Season(int startDay, int endDay) {
            this.startDay = startDay;
            this.endDay = endDay;
        }
        
        public int getStartDay() {
            return startDay;
        }
        
        public int getEndDay() {
            return endDay;
        }
    }
}