package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

import java.util.logging.Logger;

/**
 * Listener that awards experience for various farming and trading activities
 * within the BD progression system.
 */
public class ProgressionExperienceListener implements Listener {
    private final BDCraft plugin;
    private final Logger logger;
    private final BDProgressionModule progressionModule;
    
    /**
     * Creates a new progression experience listener.
     * @param plugin The plugin instance
     * @param progressionModule The progression module
     */
    public ProgressionExperienceListener(BDCraft plugin, BDProgressionModule progressionModule) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.progressionModule = progressionModule;
    }
    
    /**
     * Handles player join events to set up display names.
     * @param event The event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Update player display name based on rank
        BDRankManager rankManager = progressionModule.getRankManager();
        if (rankManager != null) {
            String displayName = rankManager.getPlayerDisplayName(player);
            player.setDisplayName(displayName);
            player.setPlayerListName(displayName);
        }
    }
    
    /**
     * Handles player harvesting crops to award experience.
     * @param event The event
     */
    @EventHandler
    public void onPlayerHarvest(PlayerHarvestBlockEvent event) {
        Player player = event.getPlayer();
        
        // Check if the harvested crop is a BD crop
        BDItemManager itemManager = plugin.getEconomyModule().getItemManager();
        for (ItemStack item : event.getItemsHarvested()) {
            if (itemManager.isBDItem(item)) {
                String itemType = itemManager.getBDItemType(item);
                
                // Award experience based on crop type
                if (itemType != null) {
                    int expAmount = 0;
                    
                    switch (itemType) {
                        case "bd_crop":
                            expAmount = 10 * item.getAmount(); // 10 XP per regular crop
                            break;
                        case "green_bd_crop":
                            expAmount = 25 * item.getAmount(); // 25 XP per green crop
                            break;
                        case "purple_bd_crop":
                            expAmount = 50 * item.getAmount(); // 50 XP per purple crop
                            break;
                    }
                    
                    if (expAmount > 0) {
                        progressionModule.addPlayerExperience(player.getUniqueId(), expAmount);
                        player.sendMessage(ChatColor.GREEN + "+" + expAmount + " farming experience!");
                    }
                }
            }
        }
    }
    
    /**
     * Handles trading with BD villagers to award experience.
     * @param event The event
     */
    @EventHandler
    public void onTradeWithVillager(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player) || 
            event.getInventory().getType() != InventoryType.MERCHANT) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        MerchantInventory merchantInv = (MerchantInventory) event.getInventory();
        
        // Only process result slot clicks (completing a trade)
        if (event.getRawSlot() != 2) {
            return;
        }
        
        // Check if merchant is a BD villager
        if (merchantInv.getMerchant() instanceof Villager) {
            Villager villager = (Villager) merchantInv.getMerchant();
            
            if (plugin.getVillagerAPI().isBDVillager(villager)) {
                String villagerType = plugin.getVillagerAPI().getBDVillagerType(villager);
                
                if (villagerType != null) {
                    // Get result item
                    ItemStack resultItem = event.getCurrentItem();
                    if (resultItem != null && !resultItem.getType().isAir()) {
                        // Award experience based on villager type and trade value
                        int expAmount = 0;
                        
                        switch (villagerType) {
                            case "DEALER":
                                // Buying seeds or tools from dealer
                                expAmount = 5; // Base XP for any dealer trade
                                break;
                            case "COLLECTOR":
                                // Selling crops to collector (most important for progression)
                                expAmount = 15; // More XP for selling to collectors
                                break;
                            case "MARKET_OWNER":
                                // Upgrading market or buying tokens
                                expAmount = 25; // Significant XP for market interactions
                                break;
                            case "SEASONAL":
                                // Special seasonal trades
                                expAmount = 40; // Most XP for seasonal trader interactions
                                break;
                        }
                        
                        if (expAmount > 0) {
                            // Add experience
                            progressionModule.addPlayerExperience(player.getUniqueId(), expAmount);
                            player.sendMessage(ChatColor.GREEN + "+" + expAmount + " trading experience!");
                            
                            // Possibly improve reputation with this villager
                            int reputationChange = 1; // Small reputation increase per trade
                            int newReputation = plugin.getVillagerAPI().changeReputation(player, villager, reputationChange);
                            
                            // If reputation is a multiple of 10, notify the player
                            if (newReputation % 10 == 0) {
                                player.sendMessage(ChatColor.GOLD + "Your reputation with this BD villager has improved!");
                                player.sendMessage(ChatColor.GOLD + "Current reputation: " + newReputation);
                            }
                        }
                    }
                }
            }
        }
    }
}