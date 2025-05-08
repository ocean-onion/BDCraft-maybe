package com.bdcraft.plugin.modules.economy.villagers;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.market.gui.MarketManagementGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Represents a Market Owner villager.
 * This villager manages market upgrades and settings through a custom GUI.
 */
public class MarketOwnerVillager extends BDVillager {
    public static final String TYPE = "MARKET_OWNER";
    
    private final BDMarket market;
    
    /**
     * Creates a new Market Owner villager.
     * 
     * @param plugin The plugin instance
     * @param location The spawn location
     * @param market The market this villager manages
     */
    public MarketOwnerVillager(BDCraft plugin, Location location, BDMarket market) {
        super(plugin, location);
        this.market = market;
        
        // Set villager type
        setVillagerType(VillagerType.MARKET_OWNER);
        
        // Set villager appearance
        Villager villager = getVillager();
        villager.setProfession(Villager.Profession.LIBRARIAN);
        villager.setVillagerLevel(5);
        villager.setCanPickupItems(false);
        villager.setCustomName(ChatColor.GOLD + "Market Owner: " + ChatColor.YELLOW + market.getFounderName() + "'s Market");
        villager.setCustomNameVisible(true);
        
        // Store market ID
        getVillager().getPersistentDataContainer().set(
                plugin.getNamespacedKey("market_id"),
                PersistentDataType.STRING,
                market.getId()
        );
    }
    
    /**
     * Creates a new Market Owner villager from an existing entity.
     * 
     * @param plugin The plugin instance
     * @param villager The villager entity
     */
    public MarketOwnerVillager(BDCraft plugin, Villager villager) {
        super(plugin, villager);
        
        // Get market ID
        String marketId = villager.getPersistentDataContainer().get(
                plugin.getNamespacedKey("market_id"),
                PersistentDataType.STRING
        );
        
        // Get market
        this.market = plugin.getEconomyModule().getMarketManager().getMarket(marketId);
        
        // Set villager type
        setVillagerType(VillagerType.MARKET_OWNER);
    }
    
    /**
     * Gets the market this villager manages.
     * 
     * @return The market
     */
    public BDMarket getMarket() {
        return market;
    }
    
    /**
     * Handles a player interacting with this villager.
     * 
     * @param player The player
     * @return True if interaction was handled
     */
    @Override
    public boolean onInteract(Player player) {
        // Verify market exists
        if (market == null) {
            player.sendMessage(ChatColor.RED + "This market owner is not linked to a valid market.");
            return true;
        }
        
        // Check permissions
        boolean isFounder = player.getUniqueId().equals(market.getFounderId());
        boolean isAssociate = market.isAssociate(player.getUniqueId());
        boolean isAdmin = player.hasPermission("bdcraft.admin.market");
        
        if (!isFounder && !isAssociate && !isAdmin) {
            player.sendMessage(ChatColor.RED + "Only the market founder and associates can manage this market.");
            return true;
        }
        
        // Open the market management GUI
        MarketManagementGUI gui = plugin.getEconomyModule().getMarketManagementGUI();
        gui.openMainMenu(player, market);
        
        return true;
    }
    
    /**
     * Opens the upgrade trading interface with this villager.
     * This shows the standard villager trading GUI with upgrade options.
     * 
     * @param player The player
     */
    public void openUpgradeTrading(Player player) {
        // Verify market exists
        if (market == null) {
            player.sendMessage(ChatColor.RED + "This market owner is not linked to a valid market.");
            return;
        }
        
        // Check if already max level
        if (market.getLevel() >= 4) {
            player.sendMessage(ChatColor.RED + "This market is already at maximum level.");
            return;
        }
        
        // Remove any existing trades
        getVillager().setRecipes(new ArrayList<>());
        
        // Add appropriate upgrade trades based on current market level
        addUpgradeTrades();
        
        // Allow standard villager trading interface to open
        player.sendMessage(ChatColor.GREEN + "Click the Market Owner to view upgrade options.");
    }
    
    /**
     * Adds upgrade trades based on the market's current level.
     */
    private void addUpgradeTrades() {
        int currentLevel = market.getLevel();
        
        switch(currentLevel) {
            case 1:
                // Level 1 to Level 2 upgrade
                addLevel2UpgradeTrade();
                break;
            case 2:
                // Level 2 to Level 3 upgrade
                addLevel3UpgradeTrade();
                break;
            case 3:
                // Level 3 to Level 4 upgrade
                addLevel4UpgradeTrade();
                break;
        }
    }
    
    /**
     * Adds the trade for upgrading from level 1 to level 2.
     */
    private void addLevel2UpgradeTrade() {
        ItemStack result = createUpgradeResult(2);
        
        // Required items - BD Currency note (representing BD currency)
        ItemStack bdCurrency = createBDCurrencyItem(5000);
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 16);
        
        addTrade(bdCurrency, diamonds, result);
    }
    
    /**
     * Adds the trade for upgrading from level 2 to level 3.
     */
    private void addLevel3UpgradeTrade() {
        ItemStack result = createUpgradeResult(3);
        
        // Required items - according to documentation
        ItemStack bdCurrency = createBDCurrencyItem(10000);
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 32);
        
        addTrade(bdCurrency, diamonds, result);
    }
    
    /**
     * Adds the trade for upgrading from level 3 to level 4.
     */
    private void addLevel4UpgradeTrade() {
        ItemStack result = createUpgradeResult(4);
        
        // Required items - according to documentation
        ItemStack bdCurrency = createBDCurrencyItem(25000);
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 64);
        
        addTrade(bdCurrency, diamonds, result);
    }
    
    /**
     * Creates a BD Currency item representing a certain amount of BD currency.
     * 
     * @param amount The amount of BD currency
     * @return The BD currency item
     */
    private ItemStack createBDCurrencyItem(int amount) {
        ItemStack currency = new ItemStack(Material.PAPER);
        ItemMeta meta = currency.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "BD Currency Note: " + amount);
            meta.setLore(Arrays.asList(
                ChatColor.YELLOW + "Value: " + amount + " BD",
                "",
                ChatColor.GRAY + "This note represents BD currency",
                ChatColor.GRAY + "used for market transactions."
            ));
            
            // Add persistent data to identify this as a BD currency note
            meta.getPersistentDataContainer().set(
                plugin.getNamespacedKey("bd_currency"),
                PersistentDataType.INTEGER,
                amount
            );
            
            currency.setItemMeta(meta);
        }
        return currency;
    }
    
    /**
     * Creates the upgrade result item.
     * 
     * @param newLevel The new level after upgrade
     * @return The upgrade result item
     */
    private ItemStack createUpgradeResult(int newLevel) {
        ItemStack certificate = new ItemStack(Material.PAPER);
        ItemMeta meta = certificate.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Market Upgrade Certificate");
            meta.setLore(Arrays.asList(
                ChatColor.YELLOW + "Use this to upgrade your market",
                ChatColor.YELLOW + "from level " + market.getLevel() + " to level " + newLevel,
                "",
                ChatColor.GRAY + "Right-click while holding to apply upgrade."
            ));
            
            // Add persistent data to identify this as a valid upgrade certificate
            meta.getPersistentDataContainer().set(
                plugin.getNamespacedKey("market_upgrade"),
                PersistentDataType.STRING,
                market.getId() + ":" + newLevel
            );
            
            certificate.setItemMeta(meta);
        }
        return certificate;
    }
    
    /**
     * Adds a trade to this villager.
     * 
     * @param item1 The first input item
     * @param item2 The second input item (can be null)
     * @param result The result item
     */
    private void addTrade(ItemStack item1, ItemStack item2, ItemStack result) {
        // Create the trade
        MerchantRecipe recipe = new MerchantRecipe(result, 0, 1, false, 0, 0);
        recipe.addIngredient(item1);
        if (item2 != null) {
            recipe.addIngredient(item2);
        }
        
        // Add the trade to the villager
        getVillager().getRecipes().add(recipe);
    }
    
    /**
     * Handles the villager getting damaged.
     * 
     * @param damage The damage
     * @return Always returns true to cancel damage
     */
    @Override
    public boolean onDamage(double damage) {
        // Market Owner villagers cannot be damaged
        return true;
    }
    
    /**
     * Handles the villager trying to change career/profession.
     * 
     * @param newProfession The new profession
     * @return Always returns true to cancel change
     */
    @Override
    public boolean onProfessionChange(Villager.Profession newProfession) {
        // Market Owner villagers cannot change profession
        return true;
    }
    
    /**
     * Checks if this villager should be removed.
     * 
     * @return True if the villager should be removed
     */
    @Override
    public boolean shouldRemove() {
        // Remove if market is null or market is removed
        if (market == null) {
            return true;
        }
        
        // Check if market still exists
        return market.isRemoved();
    }
    
    /**
     * Makes sure this villager stays within market boundaries.
     */
    @Override
    public void onTick() {
        if (market == null) {
            return;
        }
        
        // Check if villager is too far from market center
        Location center = market.getCenter();
        Location current = getVillager().getLocation();
        
        // If more than 24 blocks away (market radius), teleport back to center
        if (center.getWorld().equals(current.getWorld()) && 
                center.distance(current) > 24) {
            getVillager().teleport(center);
        }
    }
    
    /**
     * Called when this villager is removed.
     */
    @Override
    public void onRemove() {
        // Nothing special to do
    }
    
    /**
     * Updates the trades for this villager based on the current market level.
     * This method should be called when the market level changes.
     */
    public void updateTrades() {
        // Clear current trades
        getVillager().setRecipes(new ArrayList<>());
        
        // Add appropriate upgrade trades based on current market level
        addUpgradeTrades();
    }
    
    /**
     * Gets the type name of this villager.
     * 
     * @return The type name
     */
    @Override
    public String getTypeName() {
        return TYPE;
    }
}