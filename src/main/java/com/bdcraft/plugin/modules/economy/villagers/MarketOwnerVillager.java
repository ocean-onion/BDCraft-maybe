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
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
     * Gets the type name of this villager.
     * 
     * @return The type name
     */
    @Override
    public String getTypeName() {
        return TYPE;
    }
}