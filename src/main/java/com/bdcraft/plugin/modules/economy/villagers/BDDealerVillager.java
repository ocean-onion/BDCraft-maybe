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
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop.CropType;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed.SeedType;
import com.bdcraft.plugin.modules.economy.items.tools.ToolType;
import com.bdcraft.plugin.modules.economy.market.BDMarket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a BD Dealer villager that buys crops and sells seeds.
 */
public class BDDealerVillager extends BDVillager {
    public static final String TYPE = "BD_DEALER";
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
        
        // Add trades based on market level and according to documentation
        List<MerchantRecipe> trades = new ArrayList<>();
        
        // Regular BD Seeds: Always available
        trades.add(createRegularSeedBuyTrade());
        
        // Green BD Seeds: Available to Farmer rank (or market level 2+)
        if (market.getLevel() >= 2) {
            trades.add(createGreenSeedBuyTrade());
        }
        
        // Purple BD Seeds: Available to Master Farmer rank (or market level 4+)
        if (market.getLevel() >= 4) {
            trades.add(createPurpleSeedBuyTrade());
        }
        
        // BD Harvester: Available to Expert Farmer rank (or market level 3+)
        if (market.getLevel() >= 3) {
            trades.add(createBDHarvesterTrade());
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
        // Per documentation: Regular BD Seeds: 5 seeds for 1 emerald
        ItemStack seeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.REGULAR, 5);
        
        MerchantRecipe recipe = new MerchantRecipe(seeds, 0, 12, true);
        recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        
        return recipe;
    }
    
    /**
     * Creates a trade for buying green seeds.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createGreenSeedBuyTrade() {
        // Per documentation: Green BD Seeds: 9 emeralds each
        ItemStack seeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.GREEN, 1);
        
        MerchantRecipe recipe = new MerchantRecipe(seeds, 0, 8, true);
        recipe.addIngredient(new ItemStack(Material.EMERALD, 9));
        
        return recipe;
    }
    
    /**
     * Creates a trade for buying purple seeds.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createPurpleSeedBuyTrade() {
        // Per documentation: Purple BD Seeds: 25 emeralds each
        ItemStack seeds = plugin.getEconomyModule().getItemManager().createBDSeed(SeedType.PURPLE, 1);
        
        MerchantRecipe recipe = new MerchantRecipe(seeds, 0, 5, true);
        recipe.addIngredient(new ItemStack(Material.EMERALD, 25));
        
        return recipe;
    }
    
    /**
     * Creates a trade for the BD Harvester tool.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createBDHarvesterTrade() {
        // Per documentation: BD Harvester: 16 diamonds
        ItemStack harvester = plugin.getEconomyModule().getItemManager().createBDTool(ToolType.HARVESTER);
        
        MerchantRecipe recipe = new MerchantRecipe(harvester, 0, 3, true);
        recipe.addIngredient(new ItemStack(Material.DIAMOND, 16));
        
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
    
    @Override
    public boolean onInteract(Player player) {
        // Standard trading interface
        return false; // Return false to allow default trading behavior
    }
    
    @Override
    public boolean onDamage(double damage) {
        // BD Dealer villagers cannot be damaged
        return true; // Cancel damage
    }
    
    @Override
    public boolean onProfessionChange(Villager.Profession newProfession) {
        // BD Dealer villagers cannot change profession
        return true; // Cancel change
    }
    
    @Override
    public boolean shouldRemove() {
        // Remove if market is null or market is removed
        return market == null || market.isRemoved();
    }
    
    @Override
    public void onTick() {
        if (market == null) {
            return;
        }
        
        // Check if villager is too far from market center
        Location center = market.getCenter();
        Location current = entity.getLocation();
        
        // If more than 24 blocks away (market radius), teleport back to center
        if (center.getWorld().equals(current.getWorld()) && 
                center.distance(current) > 24) {
            entity.teleport(center);
        }
    }
    
    @Override
    public void onRemove() {
        // Nothing special to do
    }
    
    @Override
    public String getTypeName() {
        return TYPE;
    }
}