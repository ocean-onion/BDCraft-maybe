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
import com.bdcraft.plugin.modules.economy.market.BDMarket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Market Owner villager that manages market upgrades and features.
 */
public class MarketOwnerVillager extends BDVillager {
    private final BDMarket market;
    
    /**
     * Creates a new Market Owner villager.
     * 
     * @param plugin The plugin instance
     * @param uuid The UUID of the villager
     * @param market The market this villager belongs to
     * @param entity The villager entity (or null if not spawned yet)
     */
    public MarketOwnerVillager(BDCraft plugin, UUID uuid, BDMarket market, Villager entity) {
        super(plugin, uuid, entity, "Market Owner", VillagerType.MARKET_OWNER);
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
        return Profession.NITWIT; // Distinctive profession that doesn't typically have trades
    }
    
    @Override
    public void setupTrades() {
        if (entity == null) {
            return;
        }
        
        // Clear existing trades
        entity.setRecipes(new ArrayList<>());
        
        // Add market upgrade trades based on current market level
        List<MerchantRecipe> trades = new ArrayList<>();
        
        // Add market level upgrade trade if not max level
        if (market.getLevel() < BDMarket.MAX_MARKET_LEVEL) {
            trades.add(createMarketUpgradeTrade());
        }
        
        // Add radius visualization trade
        trades.add(createRadiusVisualizationTrade());
        
        // Add market info trade
        trades.add(createMarketInfoTrade());
        
        // Apply trades to villager
        entity.setRecipes(trades);
    }
    
    /**
     * Creates a trade for upgrading the market level.
     * 
     * @return The market upgrade trade
     */
    private MerchantRecipe createMarketUpgradeTrade() {
        // Determine the cost based on current level
        ItemStack cost;
        ItemStack result = createNamedItem(Material.PAPER, 
                ChatColor.GREEN + "Market Level " + (market.getLevel() + 1) + " Upgrade",
                ChatColor.GRAY + "Right-click to upgrade your market");
        
        int currentLevel = market.getLevel();
        switch (currentLevel) {
            case 1:
                // Level 1 -> 2: 5 regular crops + 2 emeralds
                cost = plugin.getEconomyModule().getItemManager().createBDCrop(CropType.REGULAR, 5);
                MerchantRecipe level2Recipe = new MerchantRecipe(result, 0, 1, true);
                level2Recipe.addIngredient(cost);
                level2Recipe.addIngredient(new ItemStack(Material.EMERALD, 2));
                return level2Recipe;
            case 2:
                // Level 2 -> 3: 5 green crops + 5 emeralds
                cost = plugin.getEconomyModule().getItemManager().createBDCrop(CropType.GREEN, 5);
                MerchantRecipe level3Recipe = new MerchantRecipe(result, 0, 1, true);
                level3Recipe.addIngredient(cost);
                level3Recipe.addIngredient(new ItemStack(Material.EMERALD, 5));
                return level3Recipe;
            case 3:
                // Level 3 -> 4: 3 purple crops + 10 emeralds
                cost = plugin.getEconomyModule().getItemManager().createBDCrop(CropType.PURPLE, 3);
                MerchantRecipe level4Recipe = new MerchantRecipe(result, 0, 1, true);
                level4Recipe.addIngredient(cost);
                level4Recipe.addIngredient(new ItemStack(Material.EMERALD, 10));
                return level4Recipe;
            case 4:
                // Level 4 -> 5: 10 purple crops + 20 emeralds
                cost = plugin.getEconomyModule().getItemManager().createBDCrop(CropType.PURPLE, 10);
                MerchantRecipe level5Recipe = new MerchantRecipe(result, 0, 1, true);
                level5Recipe.addIngredient(cost);
                level5Recipe.addIngredient(new ItemStack(Material.EMERALD, 20));
                return level5Recipe;
            default:
                // Default case (should not happen)
                MerchantRecipe defaultRecipe = new MerchantRecipe(result, 0, 1, true);
                defaultRecipe.addIngredient(new ItemStack(Material.EMERALD, 10));
                return defaultRecipe;
        }
    }
    
    /**
     * Creates a trade for visualizing the market radius.
     * 
     * @return The radius visualization trade
     */
    private MerchantRecipe createRadiusVisualizationTrade() {
        ItemStack result = createNamedItem(Material.ENDER_EYE, 
                ChatColor.BLUE + "Market Boundary Visualizer",
                ChatColor.GRAY + "Right-click to show market boundaries for 30 seconds");
        
        MerchantRecipe recipe = new MerchantRecipe(result, 0, 3, true);
        recipe.addIngredient(new ItemStack(Material.REDSTONE, 5));
        
        return recipe;
    }
    
    /**
     * Creates a trade for getting market information.
     * 
     * @return The market info trade
     */
    private MerchantRecipe createMarketInfoTrade() {
        ItemStack result = createNamedItem(Material.MAP, 
                ChatColor.GOLD + "Market Information",
                ChatColor.GRAY + "Right-click to view market stats and info");
        
        MerchantRecipe recipe = new MerchantRecipe(result, 0, 5, true);
        recipe.addIngredient(new ItemStack(Material.PAPER, 1));
        
        return recipe;
    }
    
    /**
     * Creates a named item with lore.
     * 
     * @param material The material of the item
     * @param name The name of the item
     * @param lore The lore of the item
     * @return The created item stack
     */
    private ItemStack createNamedItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        
        if (lore.length > 0) {
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(line);
            }
            meta.setLore(loreList);
        }
        
        item.setItemMeta(meta);
        return item;
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
     * Updates the villager's trades based on the current market state.
     */
    public void updateTrades() {
        setupTrades();
    }
}