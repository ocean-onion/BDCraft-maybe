package com.bdcraft.plugin.modules.economy.auction;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener for auction-related events.
 */
public class AuctionListener implements Listener {
    private final BDCraft plugin;
    private final AuctionHouseGUI auctionHouseGUI;
    
    /**
     * Creates a new auction listener.
     * 
     * @param plugin The plugin instance
     * @param auctionHouseGUI The auction house GUI
     */
    public AuctionListener(BDCraft plugin, AuctionHouseGUI auctionHouseGUI) {
        this.plugin = plugin;
        this.auctionHouseGUI = auctionHouseGUI;
    }
    
    /**
     * Handles inventory clicks in the auction house GUI.
     * 
     * @param event The click event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.startsWith("BD Auction House")) {
            event.setCancelled(true);
            
            if (event.getClickedInventory() == event.getView().getTopInventory()) {
                auctionHouseGUI.handleClick(player, event.getClickedInventory(), event.getSlot());
            }
        }
    }
    
    /**
     * Handles players quitting the server.
     * 
     * @param event The quit event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        auctionHouseGUI.cleanupPlayer(event.getPlayer());
    }
}