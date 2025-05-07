package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemFactory;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a BD Market Owner villager.
 */
public class BDMarketOwner extends BDVillager {
    private static final String TYPE = "MARKET_OWNER";
    private final BDCraft plugin;
    private BDMarket market;
    
    /**
     * Creates a new BD Market Owner.
     * @param plugin The plugin instance
     * @param entity The villager entity
     * @param market The associated market
     */
    public BDMarketOwner(BDCraft plugin, Villager entity, BDMarket market) {
        super(entity, TYPE);
        this.plugin = plugin;
        this.market = market;
        
        // Set market owner appearance
        entity.setProfession(Villager.Profession.CARTOGRAPHER);
        entity.setVillagerType(Villager.Type.PLAINS);
        entity.setVillagerLevel(4);
        entity.setCustomName(ChatColor.GOLD + "Market Owner");
        entity.setCustomNameVisible(true);
        
        // Set up trades
        setupTrades();
    }
    
    /**
     * Sets up the trades for this market owner.
     */
    private void setupTrades() {
        BDItemFactory itemFactory = plugin.getEconomyModule().getItemManager().getItemFactory();
        List<MerchantRecipe> recipes = new ArrayList<>();
        
        // Market level determines available trades
        int marketLevel = market.getLevel();
        
        // Market Information (1 emerald for current market status report)
        ItemStack marketInfoPaper = new ItemStack(Material.PAPER);
        ItemMeta marketInfoMeta = marketInfoPaper.getItemMeta();
        if (marketInfoMeta != null) {
            marketInfoMeta.setDisplayName(ChatColor.GOLD + "Market Status Report");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Contains information about this market.");
            lore.add(ChatColor.GRAY + "Level: " + marketLevel);
            lore.add(ChatColor.GRAY + "Founder: " + market.getFounderName());
            lore.add(ChatColor.GRAY + "Collectors: " + market.getTraderCount("COLLECTOR") + "/" + getMaxCollectors());
            marketInfoMeta.setLore(lore);
            marketInfoPaper.setItemMeta(marketInfoMeta);
        }
        
        MerchantRecipe marketInfoRecipe = new MerchantRecipe(marketInfoPaper, 0, 1000, true);
        marketInfoRecipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        recipes.add(marketInfoRecipe);
        
        // Market Upgrade (diamonds + currency for next level)
        if (marketLevel < 4) { // Max level is 4
            int diamondsRequired = 5 * marketLevel; // 5, 10, 15 diamonds for levels 1, 2, 3
            
            ItemStack upgradeVoucher = new ItemStack(Material.MAP);
            ItemMeta upgradeVoucherMeta = upgradeVoucher.getItemMeta();
            if (upgradeVoucherMeta != null) {
                upgradeVoucherMeta.setDisplayName(ChatColor.GOLD + "Market Upgrade Voucher");
                List<String> upgradeLore = new ArrayList<>();
                upgradeLore.add(ChatColor.GRAY + "Upgrades this market to level " + (marketLevel + 1));
                upgradeLore.add(ChatColor.GRAY + "Current Level: " + marketLevel);
                upgradeLore.add(ChatColor.GRAY + "New Level: " + (marketLevel + 1));
                upgradeLore.add(ChatColor.GRAY + "Benefits:");
                
                if (marketLevel == 1) {
                    upgradeLore.add(ChatColor.GRAY + "- Increased collector limit (5)");
                    upgradeLore.add(ChatColor.GRAY + "- 5% better prices");
                    upgradeLore.add(ChatColor.GRAY + "- House tokens available");
                } else if (marketLevel == 2) {
                    upgradeLore.add(ChatColor.GRAY + "- Increased collector limit (7)");
                    upgradeLore.add(ChatColor.GRAY + "- 10% better prices");
                    upgradeLore.add(ChatColor.GRAY + "- Seasonal trader visits");
                } else if (marketLevel == 3) {
                    upgradeLore.add(ChatColor.GRAY + "- Increased collector limit (10)");
                    upgradeLore.add(ChatColor.GRAY + "- 15% better prices");
                    upgradeLore.add(ChatColor.GRAY + "- Player buffs in market radius");
                }
                
                upgradeVoucherMeta.setLore(upgradeLore);
                upgradeVoucher.setItemMeta(upgradeVoucherMeta);
            }
            
            MerchantRecipe upgradeRecipe = new MerchantRecipe(upgradeVoucher, 0, 1, true);
            upgradeRecipe.addIngredient(new ItemStack(Material.DIAMOND, diamondsRequired));
            recipes.add(upgradeRecipe);
        }
        
        // House Tokens (available at level 2+)
        if (marketLevel >= 2) {
            int emeraldsRequired = 10;
            MerchantRecipe houseTokenRecipe = new MerchantRecipe(itemFactory.createHouseToken(), 0, 5, true);
            houseTokenRecipe.addIngredient(new ItemStack(Material.EMERALD, emeraldsRequired));
            recipes.add(houseTokenRecipe);
        }
        
        // Special Decorations (unique items for market decoration)
        ItemStack marketBanner = new ItemStack(Material.BLUE_BANNER);
        ItemMeta marketBannerMeta = marketBanner.getItemMeta();
        if (marketBannerMeta != null) {
            marketBannerMeta.setDisplayName(ChatColor.GOLD + "Market Banner");
            marketBanner.setItemMeta(marketBannerMeta);
        }
        MerchantRecipe bannerRecipe = new MerchantRecipe(marketBanner, 0, 10, true);
        bannerRecipe.addIngredient(new ItemStack(Material.EMERALD, 3));
        recipes.add(bannerRecipe);
        
        entity.setRecipes(recipes);
    }
    
    /**
     * Gets the maximum number of collectors allowed based on market level.
     * @return The maximum number of collectors
     */
    private int getMaxCollectors() {
        int marketLevel = market.getLevel();
        
        switch (marketLevel) {
            case 1:
                return 3; // Initial limit
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
     * Updates trades based on the market level.
     */
    public void updateTrades() {
        setupTrades();
    }
    
    /**
     * Gets the associated market.
     * @return The associated market
     */
    public BDMarket getMarket() {
        return market;
    }
    
    /**
     * Sets the associated market.
     * @param market The market to associate with this owner
     */
    public void setMarket(BDMarket market) {
        this.market = market;
        updateTrades();
    }
    
    @Override
    public int changeReputation(UUID playerUuid, int amount) {
        // Market Owners give +2 reputation per trade
        int newRep = super.changeReputation(playerUuid, amount);
        
        // Update trades if reputation changes significantly
        if (amount != 0 && Math.abs(amount) >= 5) {
            updateTrades();
        }
        
        return newRep;
    }
}