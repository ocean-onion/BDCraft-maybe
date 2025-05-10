package com.bdcraft.plugin.modules.economy.listeners;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.villager.BDCollector;
import com.bdcraft.plugin.modules.economy.villager.BDDealer;
import com.bdcraft.plugin.modules.economy.villager.BDMarketOwner;
import com.bdcraft.plugin.modules.economy.villager.BDVillager;
import com.bdcraft.plugin.modules.economy.villager.VillagerManager;
import com.bdcraft.plugin.modules.progression.BDRankManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;

/**
 * Listener for trades with BD villagers.
 */
public class VillagerTradeListener implements Listener {
    private final BDCraft plugin;
    
    /**
     * Creates a new villager trade listener.
     * @param plugin The plugin instance
     */
    public VillagerTradeListener(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof MerchantInventory)) {
            return;
        }
        
        // Only process when clicking the result slot
        if (event.getRawSlot() != 2 || event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }
        
        // Ensure the click was valid for a trade
        if (!event.isLeftClick() || event.getClick().isShiftClick() || event.getClick().isKeyboardClick()) {
            return;
        }
        
        // Get relevant objects
        Player player = (Player) event.getWhoClicked();
        MerchantInventory merchantInventory = (MerchantInventory) event.getInventory();
        Villager villager = (Villager) merchantInventory.getMerchant();
        VillagerManager villagerManager = plugin.getEconomyModule().getVillagerManager();
        
        // Check if it's a BD villager
        if (!villagerManager.isBDVillager(villager)) {
            return;
        }
        
        // Get traded item and recipe
        ItemStack result = event.getCurrentItem();
        if (result == null || result.getType().isAir()) {
            return;
        }
        
        // Get the specific trade being performed
        MerchantRecipe recipe = merchantInventory.getSelectedRecipe();
        if (recipe == null) {
            return;
        }
        
        // Process the trade
        BDVillager bdVillager = villagerManager.getVillager(villager);
        
        if (bdVillager != null) {
            processVillagerTrade(player, bdVillager, recipe, result);
        }
    }
    
    /**
     * Processes a trade with a BD villager.
     * @param player The player
     * @param bdVillager The BD villager
     * @param recipe The recipe
     * @param result The result item
     */
    private void processVillagerTrade(Player player, BDVillager bdVillager, MerchantRecipe recipe, ItemStack result) {
        // Update reputation
        int repChange = 0;
        
        if (bdVillager instanceof BDCollector) {
            repChange = 3; // Collectors give more reputation
        } else if (bdVillager instanceof BDDealer) {
            repChange = 1;
        } else if (bdVillager instanceof BDMarketOwner) {
            repChange = 2;
        }
        
        if (repChange > 0) {
            int newRep = bdVillager.changeReputation(player.getUniqueId(), repChange);
            player.sendMessage(ChatColor.GREEN + "Your reputation with this villager increased to " + newRep + ".");
        }
        
        // Handle currency from lore if present
        BDItemManager itemManager = plugin.getEconomyModule().getItemManager();
        BDEconomyModule economyModule = plugin.getEconomyModule();
        
        if (result.hasItemMeta() && result.getItemMeta().hasLore()) {
            List<String> lore = result.getItemMeta().getLore();
            
            for (String line : lore) {
                if (line.contains("Currency")) {
                    // Extract currency amount from lore
                    String amountStr = line.replaceAll("[^0-9]", "");
                    
                    try {
                        int amount = Integer.parseInt(amountStr);
                        
                        // Add currency to player
                        economyModule.addPlayerBalance(player, amount);
                        player.sendMessage(ChatColor.GREEN + "+" + amount + " BD Currency from trade.");
                        
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Failed to parse currency amount from lore: " + line);
                    }
                }
            }
        }
        
        // Award progression experience
        BDRankManager rankManager = plugin.getProgressionModule().getRankManager();
        int expAmount = 0;
        
        // Award experience based on trade type
        if (bdVillager instanceof BDCollector) {
            // Selling crops
            if (itemManager.isBDItem(recipe.getIngredients().get(0))) {
                String itemType = itemManager.getBDItemType(recipe.getIngredients().get(0));
                
                if (itemType.equals("bd_crop")) {
                    expAmount = 5;
                } else if (itemType.equals("green_bd_crop")) {
                    expAmount = 10;
                } else if (itemType.equals("purple_bd_crop")) {
                    expAmount = 15;
                }
            }
        } else if (bdVillager instanceof BDDealer) {
            // Buying seeds or tools
            expAmount = 2;
        } else if (bdVillager instanceof BDMarketOwner) {
            // Market transactions
            expAmount = 3;
            
            // Extra for upgrades
            if (result.getType().name().contains("MAP") && 
                    result.hasItemMeta() && 
                    result.getItemMeta().getDisplayName().contains("Upgrade")) {
                expAmount = 10;
            }
        }
        
        if (expAmount > 0) {
            rankManager.addExperience(player, expAmount);
            player.sendMessage(ChatColor.AQUA + "+" + expAmount + " progression experience from trade.");
        }
    }
}