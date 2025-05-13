package com.bdcraft.plugin.modules.economy.modules.auction;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.economy.auction.AuctionHouseGUI;
import com.bdcraft.plugin.modules.economy.auction.AuctionItem;
import com.bdcraft.plugin.modules.economy.auction.AuctionListener;
import com.bdcraft.plugin.modules.economy.auction.AuctionManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Module for handling auction house functionality.
 * This module provides an interface for players to buy and sell items.
 */
public class BDAuctionModule implements SubmoduleBase, CommandExecutor {
    private final BDCraft plugin;
    private final Logger logger;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Components
    private AuctionManager auctionManager;
    private AuctionListener auctionListener;
    
    /**
     * Creates a new auction module.
     * 
     * @param plugin The plugin instance
     */
    public BDAuctionModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    @Override
    public String getName() {
        return "Auction";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        logger.info("Enabling Auction submodule");
        
        // Initialize components
        this.auctionManager = new AuctionManager(plugin);
        this.auctionListener = new AuctionListener(plugin, auctionManager);
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(auctionListener, plugin);
        
        // Register commands
        registerCommands();
        
        enabled = true;
        logger.info("Auction submodule enabled");
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        logger.info("Disabling Auction submodule");
        
        // Unregister events
        HandlerList.unregisterAll(auctionListener);
        
        // Save any pending data
        auctionManager.saveAuctions();
        
        enabled = false;
        logger.info("Auction submodule disabled");
    }
    
    @Override
    public void reload() {
        if (enabled) {
            // Reload auction data
            auctionManager.loadAuctions();
            logger.info("Auction submodule reloaded");
        }
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Registers commands for the auction module.
     */
    private void registerCommands() {
        // Register auction house command
        plugin.getCommand("bdauction").setExecutor(this);
        plugin.getCommand("ah").setExecutor(this);
    }
    
    /**
     * Opens the auction house GUI for a player.
     * 
     * @param player The player
     */
    public void openAuctionHouse(Player player) {
        if (auctionManager != null) {
            auctionManager.openAuctionHouse(player);
        }
    }
    
    /**
     * Gets the auction manager instance.
     * 
     * @return The auction manager
     */
    public AuctionManager getAuctionManager() {
        return auctionManager;
    }
    
    /**
     * Handles auction-related commands.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("bdauction") || command.getName().equalsIgnoreCase("ah")) {
            openAuctionHouse(player);
            return true;
        }
        
        return false;
    }
    
    /**
     * Lists an item in the auction house.
     * 
     * @param player The player listing the item
     * @param item The item to list
     * @param price The price
     * @return Whether the listing was successful
     */
    public boolean listItem(Player player, AuctionItem item, double price) {
        if (auctionManager != null) {
            return auctionManager.listItem(player, item, price);
        }
        return false;
    }
    
    /**
     * Buys an item from the auction house.
     * 
     * @param player The player buying the item
     * @param auctionId The auction ID
     * @return Whether the purchase was successful
     */
    public boolean buyItem(Player player, UUID auctionId) {
        if (auctionManager != null) {
            return auctionManager.buyItem(player, auctionId);
        }
        return false;
    }
    
    /**
     * Cancels an auction.
     * 
     * @param player The player cancelling the auction
     * @param auctionId The auction ID
     * @return Whether the cancellation was successful
     */
    public boolean cancelAuction(Player player, UUID auctionId) {
        if (auctionManager != null) {
            return auctionManager.cancelAuction(player, auctionId);
        }
        return false;
    }
}