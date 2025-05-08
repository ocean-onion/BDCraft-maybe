package com.bdcraft.plugin.modules.economy.villagers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop.CropType;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed.SeedType;
import com.bdcraft.plugin.modules.economy.market.BDMarket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a BD Dealer villager that buys crops and sells seeds.
 */
public class BDDealerVillager extends BDVillager {
    private final BDMarket market;
    
    /**
     * Creates a new BD Dealer villager.
     * 
     * @param plugin The plugin instance
     * @param uuid The UUID of the villager
     * @param market The market this villager belongs to
     * @param entity The villager entity (or null if not spawned yet)
     */
    public BDDealerVillager(BDCraft plugin, UUID uuid, BDMarket market, Villager entity) {
        super(plugin, uuid, entity, "BD Dealer", VillagerType.BD_DEALER);
        this.market = market;
    }
    
    @Override
    public Villager spawn(Location location) {
        Villager villager = super.spawn(location);
        setupTrades();
        return villager;
    }
    
    @Override
    protected Profession getBukkitProfession() {
        return Profession.FARMER; // Fitting profession for crop trading
    }
    
    @Override
    public void setupTrades() {
        if (entity == null) {
            return;
        }
        
        // Clear existing trades
        entity.setRecipes(new ArrayList<>());
        
        // Add trades based on market level
        List<MerchantRecipe> trades = new ArrayList<>();
        
        // Basic trades available at all levels
        trades.add(createRegularSeedBuyTrade());
        trades.add(createRegularCropSellTrade());
        
        // Level 2+ trades
        if (market.getLevel() >= 2) {
            trades.add(createGreenSeedBuyTrade());
            trades.add(createGreenCropSellTrade());
        }
        
        // Level 4+ trades
        if (market.getLevel() >= 4) {
            trades.add(createPurpleSeedBuyTrade());
            trades.add(createPurpleCropSellTrade());
        }
        
        // Apply trades to villager
        entity.setRecipes(trades);
    }
    
    /**
     * Creates a trade for buying regular seeds.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createRegularSeedBuyTrade() {
        ItemStack seeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.REGULAR, 1);
        
        MerchantRecipe recipe = new MerchantRecipe(seeds, 0, 12, true);
        recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        
        return recipe;
    }
    
    /**
     * Creates a trade for selling regular crops.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createRegularCropSellTrade() {
        ItemStack emeralds = new ItemStack(Material.EMERALD, 1);
        
        MerchantRecipe recipe = new MerchantRecipe(emeralds, 0, 16, true);
        recipe.addIngredient(plugin.getEconomyModule().getItemManager().createBDCrop(CropType.REGULAR, 2));
        
        return recipe;
    }
    
    /**
     * Creates a trade for buying green seeds.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createGreenSeedBuyTrade() {
        ItemStack seeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.GREEN, 1);
        
        MerchantRecipe recipe = new MerchantRecipe(seeds, 0, 8, true);
        recipe.addIngredient(new ItemStack(Material.EMERALD, 3));
        
        return recipe;
    }
    
    /**
     * Creates a trade for selling green crops.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createGreenCropSellTrade() {
        ItemStack emeralds = new ItemStack(Material.EMERALD, 3);
        
        MerchantRecipe recipe = new MerchantRecipe(emeralds, 0, 12, true);
        recipe.addIngredient(plugin.getEconomyModule().getItemManager().createBDCrop(CropType.GREEN, 1));
        
        return recipe;
    }
    
    /**
     * Creates a trade for buying purple seeds.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createPurpleSeedBuyTrade() {
        ItemStack seeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.PURPLE, 1);
        
        MerchantRecipe recipe = new MerchantRecipe(seeds, 0, 5, true);
        recipe.addIngredient(new ItemStack(Material.EMERALD, 8));
        
        return recipe;
    }
    
    /**
     * Creates a trade for selling purple crops.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createPurpleCropSellTrade() {
        ItemStack emeralds = new ItemStack(Material.EMERALD, 10);
        
        MerchantRecipe recipe = new MerchantRecipe(emeralds, 0, 8, true);
        recipe.addIngredient(plugin.getEconomyModule().getItemManager().createBDCrop(CropType.PURPLE, 1));
        
        return recipe;
    }
    
    /**
     * Gets the market this villager belongs to.
     * 
     * @return The market
     */
    public BDMarket getMarket() {
        return market;
    }
    
    /**
     * Updates the villager's trades based on the current market level.
     */
    public void updateTrades() {
        setupTrades();
    }
}