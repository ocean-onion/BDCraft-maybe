package com.bdcraft.plugin.modules.economy.market.gui;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.Market;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * GUI for managing markets.
 */
public class MarketManagementGUI {
    private final BDCraft plugin;
    private final Map<UUID, Market> playerMarketContext;
    
    /**
     * Creates a new market management GUI.
     * 
     * @param plugin The plugin instance
     */
    public MarketManagementGUI(BDCraft plugin) {
        this.plugin = plugin;
        this.playerMarketContext = new HashMap<>();
    }
    
    /**
     * Opens the market management GUI for a player.
     * 
     * @param player The player
     * @param market The market
     */
    public void openMarketManagementGUI(Player player, Market market) {
        Inventory gui = Bukkit.createInventory(player, 27, 
                ChatColor.DARK_GREEN + "Market Management: " + market.getName());
        
        playerMarketContext.put(player.getUniqueId(), market);
        
        // Market Info
        ItemStack infoItem = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = infoItem.getItemMeta();
        infoMeta.setDisplayName(ChatColor.YELLOW + "Market Information");
        List<String> infoLore = new ArrayList<>();
        infoLore.add(ChatColor.WHITE + "Name: " + ChatColor.GREEN + market.getName());
        infoLore.add(ChatColor.WHITE + "Owner: " + ChatColor.GREEN + market.getOwnerName());
        infoLore.add(ChatColor.WHITE + "Level: " + ChatColor.GOLD + market.getLevel());
        infoLore.add("");
        infoLore.add(ChatColor.GRAY + "This market allows:");
        infoLore.add(ChatColor.GRAY + "- " + (market.getLevel() >= 1 ? "3" : "0") + " collector houses");
        if (market.getLevel() >= 2) {
            infoLore.add(ChatColor.GRAY + "- 5% better trading prices");
            infoLore.add(ChatColor.GRAY + "- House token sales");
        }
        if (market.getLevel() >= 3) {
            infoLore.add(ChatColor.GRAY + "- Seasonal trader visits");
            infoLore.add(ChatColor.GRAY + "- 10% better trading prices");
        }
        if (market.getLevel() >= 4) {
            infoLore.add(ChatColor.GRAY + "- Minor buffs in market radius");
            infoLore.add(ChatColor.GRAY + "- 15% better trading prices");
        }
        infoMeta.setLore(infoLore);
        infoItem.setItemMeta(infoMeta);
        gui.setItem(4, infoItem);
        
        // Manage Associates
        ItemStack associatesItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta associatesMeta = associatesItem.getItemMeta();
        associatesMeta.setDisplayName(ChatColor.AQUA + "Manage Associates");
        List<String> associatesLore = new ArrayList<>();
        associatesLore.add(ChatColor.GRAY + "Add or remove market associates");
        associatesLore.add("");
        associatesLore.add(ChatColor.WHITE + "Associate count: " + ChatColor.YELLOW + 
                market.getAssociates().size());
        associatesLore.add("");
        associatesLore.add(ChatColor.YELLOW + "Click to manage");
        associatesMeta.setLore(associatesLore);
        associatesItem.setItemMeta(associatesMeta);
        gui.setItem(11, associatesItem);
        
        // Upgrade Market
        ItemStack upgradeItem;
        boolean canUpgrade = market.getLevel() < 4;
        if (canUpgrade) {
            upgradeItem = new ItemStack(Material.EMERALD);
            ItemMeta upgradeMeta = upgradeItem.getItemMeta();
            upgradeMeta.setDisplayName(ChatColor.GREEN + "Upgrade Market");
            List<String> upgradeLore = new ArrayList<>();
            upgradeLore.add(ChatColor.GRAY + "Upgrade to level " + (market.getLevel() + 1));
            upgradeLore.add("");
            
            switch (market.getLevel()) {
                case 1:
                    upgradeLore.add(ChatColor.WHITE + "Cost: " + ChatColor.AQUA + "16 diamonds + 5,000 BD");
                    upgradeLore.add("");
                    upgradeLore.add(ChatColor.GRAY + "Benefits of Level 2:");
                    upgradeLore.add(ChatColor.GRAY + "- Up to 5 collector houses");
                    upgradeLore.add(ChatColor.GRAY + "- 5% better trading prices");
                    upgradeLore.add(ChatColor.GRAY + "- Market Owner sells House Tokens");
                    break;
                case 2:
                    upgradeLore.add(ChatColor.WHITE + "Cost: " + ChatColor.AQUA + "32 diamonds + 10,000 BD");
                    upgradeLore.add("");
                    upgradeLore.add(ChatColor.GRAY + "Benefits of Level 3:");
                    upgradeLore.add(ChatColor.GRAY + "- Up to 7 collector houses");
                    upgradeLore.add(ChatColor.GRAY + "- 10% better trading prices");
                    upgradeLore.add(ChatColor.GRAY + "- Seasonal Trader visits periodically");
                    break;
                case 3:
                    upgradeLore.add(ChatColor.WHITE + "Cost: " + ChatColor.AQUA + "64 diamonds + 25,000 BD");
                    upgradeLore.add("");
                    upgradeLore.add(ChatColor.GRAY + "Benefits of Level 4:");
                    upgradeLore.add(ChatColor.GRAY + "- Up to 10 collector houses");
                    upgradeLore.add(ChatColor.GRAY + "- 15% better trading prices");
                    upgradeLore.add(ChatColor.GRAY + "- Players receive minor buffs in market");
                    break;
            }
            
            upgradeLore.add("");
            upgradeLore.add(ChatColor.YELLOW + "Click to upgrade");
            upgradeMeta.setLore(upgradeLore);
            upgradeItem.setItemMeta(upgradeMeta);
        } else {
            upgradeItem = new ItemStack(Material.BARRIER);
            ItemMeta upgradeMeta = upgradeItem.getItemMeta();
            upgradeMeta.setDisplayName(ChatColor.RED + "Maximum Level Reached");
            List<String> upgradeLore = new ArrayList<>();
            upgradeLore.add(ChatColor.GRAY + "This market is already at maximum level");
            upgradeMeta.setLore(upgradeLore);
            upgradeItem.setItemMeta(upgradeMeta);
        }
        gui.setItem(13, upgradeItem);
        
        // Market Settings
        ItemStack settingsItem = new ItemStack(Material.REDSTONE);
        ItemMeta settingsMeta = settingsItem.getItemMeta();
        settingsMeta.setDisplayName(ChatColor.GOLD + "Market Settings");
        List<String> settingsLore = new ArrayList<>();
        settingsLore.add(ChatColor.GRAY + "Configure market settings");
        settingsLore.add("");
        settingsLore.add(ChatColor.WHITE + "Market sounds: " + 
                (market.hasSounds() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
        settingsLore.add(ChatColor.WHITE + "Market particles: " + 
                (market.hasParticles() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
        settingsLore.add("");
        settingsLore.add(ChatColor.YELLOW + "Click to configure");
        settingsMeta.setLore(settingsLore);
        settingsItem.setItemMeta(settingsMeta);
        gui.setItem(15, settingsItem);
        
        // Exit/Close
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Close");
        closeItem.setItemMeta(closeMeta);
        gui.setItem(22, closeItem);
        
        player.openInventory(gui);
    }
    
    /**
     * Gets the market a player is viewing.
     * 
     * @param playerId The player ID
     * @return The market, or null if not viewing
     */
    public Market getMarketContext(UUID playerId) {
        return playerMarketContext.get(playerId);
    }
    
    /**
     * Clears the market context for a player.
     * 
     * @param playerId The player ID
     */
    public void clearMarketContext(UUID playerId) {
        playerMarketContext.remove(playerId);
    }
}