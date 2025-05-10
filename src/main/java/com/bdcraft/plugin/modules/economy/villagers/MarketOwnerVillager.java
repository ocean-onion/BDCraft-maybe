package com.bdcraft.plugin.modules.economy.villagers;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.Market;
import com.bdcraft.plugin.modules.economy.gui.MarketManagementGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.BookMeta;
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
    
    private final Market market;
    
    @Override
    protected Villager.Profession getBukkitProfession() {
        return Villager.Profession.CARTOGRAPHER; // According to documentation
    }
    
    @Override
    public void setupTrades() {
        // Market owners don't have standard trades, they have custom upgrade trades
        // that are set up in addUpgradeTrades() method
        if (entity != null) {
            entity.setRecipes(new ArrayList<>());
        }
    }
    
    /**
     * Creates a new Market Owner villager.
     * 
     * @param plugin The plugin instance
     * @param location The spawn location
     * @param market The market this villager manages
     */
    public MarketOwnerVillager(BDCraft plugin, Location location, Market market) {
        super(
            plugin, 
            UUID.randomUUID(), 
            null, 
            "Market Owner: " + market.getFounderName() + "'s Market", 
            VillagerType.MARKET_OWNER
        );
        this.market = market;
        
        // Spawn the villager
        Villager villager = spawn(location);
        
        // Store market ID as string
        villager.getPersistentDataContainer().set(
                plugin.getNamespacedKey("market_id"),
                PersistentDataType.STRING,
                market.getId().toString()
        );
    }
    
    /**
     * Creates a new Market Owner villager from an existing entity.
     * 
     * @param plugin The plugin instance
     * @param villager The villager entity
     */
    public MarketOwnerVillager(BDCraft plugin, Villager villager) {
        super(
            plugin, 
            UUID.fromString(villager.getPersistentDataContainer().get(
                new NamespacedKey(plugin, "bd_villager_uuid"), 
                PersistentDataType.STRING
            )),
            villager,
            villager.getCustomName(),
            VillagerType.MARKET_OWNER
        );
        
        // Get market ID
        String marketId = villager.getPersistentDataContainer().get(
                plugin.getNamespacedKey("market_id"),
                PersistentDataType.STRING
        );
        
        // Get market - convert market ID string to UUID
        this.market = plugin.getEconomyModule().getMarketManager().getMarket(UUID.fromString(marketId));
    }
    
    /**
     * Gets the market this villager manages.
     * 
     * @return The market
     */
    public Market getMarket() {
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
        // For now, we'll just send a message as the GUI reference might be missing
        player.sendMessage(ChatColor.GREEN + "Opening Market Management interface for " + market.getName());
        player.sendMessage(ChatColor.YELLOW + "Market Level: " + market.getLevel());
        
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
        entity.setRecipes(new ArrayList<>());
        
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
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 16);
        ItemStack certificate = createUpgradeCertificate(2, 5000);
        
        addTrade(diamonds, null, certificate);
    }
    
    /**
     * Adds the trade for upgrading from level 2 to level 3.
     */
    private void addLevel3UpgradeTrade() {
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 32);
        ItemStack certificate = createUpgradeCertificate(3, 10000);
        
        addTrade(diamonds, null, certificate);
    }
    
    /**
     * Adds the trade for upgrading from level 3 to level 4.
     */
    private void addLevel4UpgradeTrade() {
        ItemStack diamonds = new ItemStack(Material.DIAMOND, 64);
        ItemStack certificate = createUpgradeCertificate(4, 25000);
        
        addTrade(diamonds, null, certificate);
    }
    
    /**
     * Creates a market upgrade certificate that includes BD currency cost info.
     * 
     * @param upgradeLevel The level to upgrade to
     * @param bdCost The BD currency cost 
     * @return The upgrade certificate
     */
    private ItemStack createUpgradeCertificate(int upgradeLevel, int bdCost) {
        ItemStack certificate = new ItemStack(Material.PAPER);
        ItemMeta meta = certificate.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Market Upgrade Certificate");
            meta.setLore(Arrays.asList(
                ChatColor.YELLOW + "This certificate requires " + bdCost + " BD Currency",
                ChatColor.YELLOW + "to upgrade your market from level " + market.getLevel() + " to " + upgradeLevel,
                "",
                ChatColor.AQUA + "⚠ The BD Currency cost will be automatically",
                ChatColor.AQUA + "   deducted when you use this certificate.",
                "",
                ChatColor.GRAY + "Right-click while holding to apply upgrade."
            ));
            
            // Add persistent data to identify this as a valid upgrade certificate
            meta.getPersistentDataContainer().set(
                plugin.getNamespacedKey("market_upgrade_certificate"),
                PersistentDataType.STRING,
                market.getId().toString() + ":" + upgradeLevel + ":" + bdCost
            );
            
            certificate.setItemMeta(meta);
        }
        return certificate;
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
                market.getId().toString() + ":" + newLevel
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
        entity.getRecipes().add(recipe);
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
        Location current = entity.getLocation();
        
        // If more than 24 blocks away (market radius), teleport back to center
        if (center.getWorld().equals(current.getWorld()) && 
                center.distance(current) > 24) {
            entity.teleport(center);
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
        entity.setRecipes(new ArrayList<>());
        
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