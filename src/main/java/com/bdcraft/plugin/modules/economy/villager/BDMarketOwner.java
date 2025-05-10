package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.Market;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * BD Market Owner villager type.
 */
public class BDMarketOwner extends BDVillager {
    public static final String TYPE = "MARKET_OWNER";
    private int marketLevel;
    private Market market;
    
    /**
     * Creates a new BD market owner.
     * @param plugin The plugin instance
     * @param location The spawn location
     */
    public BDMarketOwner(BDCraft plugin, Location location) {
        super(plugin, location, "Market Owner", Villager.Profession.CARTOGRAPHER, Villager.Type.PLAINS);
        this.marketLevel = 1;
    }
    
    /**
     * Creates a new BD market owner associated with a market.
     * @param plugin The plugin instance
     * @param location The spawn location
     * @param market The associated market
     */
    public BDMarketOwner(BDCraft plugin, Location location, Market market) {
        super(plugin, location, "Market Owner", Villager.Profession.CARTOGRAPHER, Villager.Type.PLAINS);
        this.marketLevel = 1;
        this.market = market;
    }
    
    /**
     * Creates a new BD market owner from an existing villager.
     * @param plugin The plugin instance
     * @param villager The villager
     */
    public BDMarketOwner(BDCraft plugin, Villager villager) {
        super(plugin, villager);
        this.marketLevel = 1;
    }
    
    /**
     * Creates a new BD market owner from an existing villager with a market.
     * @param plugin The plugin instance
     * @param villager The villager
     * @param market The associated market
     */
    public BDMarketOwner(BDCraft plugin, Villager villager, Market market) {
        super(plugin, villager);
        this.marketLevel = 1;
        this.market = market;
    }
    
    @Override
    protected void initializeTrades() {
        updateTrades();
    }
    
    /**
     * Updates trades based on the current market level.
     */
    @Override
    public void updateTrades() {
        List<MerchantRecipe> recipes = new ArrayList<>();
        
        // Market upgrade trade varies by current level
        if (marketLevel < 4) { // Max level is 4
            recipes.add(createMarketUpgradeRecipe());
        }
        
        // Add market status report trade
        recipes.add(createMarketStatusRecipe());
        
        // Add decoration items
        recipes.add(createDecorationRecipe());
        
        // Add house token trade if level >= 2
        if (marketLevel >= 2) {
            recipes.add(createHouseTokenRecipe());
        }
        
        // Set recipes
        villager.setRecipes(recipes);
    }
    
    /**
     * Creates the market upgrade recipe based on current level.
     * @return The recipe
     */
    private MerchantRecipe createMarketUpgradeRecipe() {
        int nextLevel = marketLevel + 1;
        String upgradeName = "Market Upgrade to Level " + nextLevel;
        
        // Create upgrade item
        ItemStack upgradeItem = new ItemStack(Material.PAPER, 1);
        var meta = upgradeItem.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + upgradeName);
        upgradeItem.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(upgradeItem, 1);
        
        // Set ingredients based on current level
        switch (marketLevel) {
            case 1:
                recipe.addIngredient(new ItemStack(Material.DIAMOND, 3));
                recipe.addIngredient(new ItemStack(Material.EMERALD, 16));
                break;
            case 2:
                recipe.addIngredient(new ItemStack(Material.DIAMOND, 5));
                recipe.addIngredient(new ItemStack(Material.EMERALD, 32));
                break;
            case 3:
                recipe.addIngredient(new ItemStack(Material.DIAMOND, 10));
                recipe.addIngredient(new ItemStack(Material.EMERALD, 64));
                break;
        }
        
        return recipe;
    }
    
    /**
     * Creates the market status report recipe.
     * @return The recipe
     */
    private MerchantRecipe createMarketStatusRecipe() {
        // Create status report item
        ItemStack reportItem = new ItemStack(Material.MAP, 1);
        var meta = reportItem.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Market Status Report");
        reportItem.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(reportItem, Integer.MAX_VALUE);
        
        // Set ingredient
        recipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        
        return recipe;
    }
    
    /**
     * Creates a decoration item recipe.
     * @return The recipe
     */
    private MerchantRecipe createDecorationRecipe() {
        // Create decoration item
        ItemStack decorationItem = new ItemStack(Material.LANTERN, 4);
        var meta = decorationItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Market Decoration");
        decorationItem.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(decorationItem, Integer.MAX_VALUE);
        
        // Set ingredient
        recipe.addIngredient(new ItemStack(Material.EMERALD, 2));
        
        return recipe;
    }
    
    /**
     * Creates a house token recipe.
     * @return The recipe
     */
    private MerchantRecipe createHouseTokenRecipe() {
        // Create house token item
        ItemStack tokenItem = new ItemStack(Material.ITEM_FRAME, 1);
        var meta = tokenItem.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "House Token");
        tokenItem.setItemMeta(meta);
        
        // Create recipe
        MerchantRecipe recipe = new MerchantRecipe(tokenItem, 3); // Limited to 3 uses
        
        // Set ingredients
        recipe.addIngredient(new ItemStack(Material.DIAMOND, 1));
        recipe.addIngredient(new ItemStack(Material.EMERALD, 10));
        
        return recipe;
    }
    
    /**
     * Gets the market level.
     * @return The market level
     */
    public int getMarketLevel() {
        return marketLevel;
    }
    
    /**
     * Sets the market level and updates trades.
     * @param marketLevel The market level
     */
    public void setMarketLevel(int marketLevel) {
        if (marketLevel < 1) {
            marketLevel = 1;
        } else if (marketLevel > 4) {
            marketLevel = 4;
        }
        
        this.marketLevel = marketLevel;
        updateTrades();
    }
    
    /**
     * Gets the associated market.
     * @return The market
     */
    public Market getMarket() {
        return market;
    }
    
    /**
     * Sets the associated market.
     * @param market The market
     */
    public void setMarket(Market market) {
        this.market = market;
    }
    
    /**
     * Gets the collector limit based on market level.
     * @return The collector limit
     */
    public int getCollectorLimit() {
        switch (marketLevel) {
            case 1:
                return 3;
            case 2:
                return 5;
            case 3:
                return 7;
            case 4:
                return 10;
            default:
                return 3;
        }
    }
    
    /**
     * Gets the price modifier based on market level.
     * @return The price modifier (e.g., 0.05 for 5% better prices)
     */
    public double getPriceModifier() {
        switch (marketLevel) {
            case 1:
                return 0.0;
            case 2:
                return 0.05;
            case 3:
                return 0.1;
            case 4:
                return 0.15;
            default:
                return 0.0;
        }
    }
    
    /**
     * Checks if the market can have seasonal traders.
     * @return True if the market can have seasonal traders, false otherwise
     */
    public boolean canHaveSeasonalTraders() {
        return marketLevel >= 3;
    }
    
    @Override
    public String getVillagerType() {
        return TYPE;
    }
}