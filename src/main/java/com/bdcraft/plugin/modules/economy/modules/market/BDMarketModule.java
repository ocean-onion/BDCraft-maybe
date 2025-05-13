package com.bdcraft.plugin.modules.economy.modules.market;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.economy.market.Market;
import com.bdcraft.plugin.modules.economy.market.MarketAPIImpl;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import com.bdcraft.plugin.modules.economy.market.PlayerMarket;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Module for handling player markets and market functionality.
 * This module allows players to create and manage their own markets.
 */
public class BDMarketModule implements SubmoduleBase, CommandExecutor, Listener {
    private final BDCraft plugin;
    private final Logger logger;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Components
    private MarketManager marketManager;
    private MarketAPIImpl marketAPI;
    
    /**
     * Creates a new market module.
     * 
     * @param plugin The plugin instance
     */
    public BDMarketModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    @Override
    public String getName() {
        return "Market";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        logger.info("Enabling Market submodule");
        
        // Initialize components
        this.marketManager = new MarketManager(plugin);
        this.marketAPI = new MarketAPIImpl(marketManager);
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Register commands
        registerCommands();
        
        // Load market data
        marketManager.loadMarkets();
        
        enabled = true;
        logger.info("Market submodule enabled");
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        logger.info("Disabling Market submodule");
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        // Save market data
        marketManager.saveMarkets();
        
        enabled = false;
        logger.info("Market submodule disabled");
    }
    
    @Override
    public void reload() {
        if (enabled) {
            // Reload market data
            marketManager.loadMarkets();
            logger.info("Market submodule reloaded");
        }
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Registers commands for the market module.
     */
    private void registerCommands() {
        // Register market command
        plugin.getCommand("bdmarket").setExecutor(this);
        plugin.getCommand("market").setExecutor(this);
    }
    
    /**
     * Gets the market manager instance.
     * 
     * @return The market manager
     */
    public MarketManager getMarketManager() {
        return marketManager;
    }
    
    /**
     * Gets the market API implementation.
     * 
     * @return The market API
     */
    public MarketAPIImpl getMarketAPI() {
        return marketAPI;
    }
    
    /**
     * Creates a market for a player.
     * 
     * @param player The player
     * @param marketName The market name
     * @return The created market, or null if failed
     */
    public Market createMarket(Player player, String marketName) {
        if (marketManager != null) {
            return marketManager.createMarket(player, marketName);
        }
        return null;
    }
    
    /**
     * Gets a player's market.
     * 
     * @param player The player
     * @return The player's market, or null if none
     */
    public PlayerMarket getPlayerMarket(Player player) {
        if (marketManager != null) {
            return marketManager.getPlayerMarket(player);
        }
        return null;
    }
    
    /**
     * Gets a market by ID.
     * 
     * @param marketId The market ID
     * @return The market, or null if not found
     */
    public Market getMarket(UUID marketId) {
        if (marketManager != null) {
            return marketManager.getMarket(marketId);
        }
        return null;
    }
    
    /**
     * Opens a market GUI for a player.
     * 
     * @param player The player
     * @param marketId The market ID
     * @return Whether the market was opened
     */
    public boolean openMarket(Player player, UUID marketId) {
        if (marketManager != null) {
            return marketManager.openMarket(player, marketId);
        }
        return false;
    }
    
    /**
     * Gets a player's currency amount.
     * 
     * @param uuid The player's UUID
     * @return The currency amount
     */
    public int getPlayerCurrency(UUID uuid) {
        if (marketManager != null) {
            return marketManager.getPlayerCurrency(uuid);
        }
        return 0;
    }
    
    /**
     * Sets a player's currency amount.
     * 
     * @param uuid The player's UUID
     * @param amount The amount to set
     */
    public void setPlayerCurrency(UUID uuid, int amount) {
        if (marketManager != null) {
            marketManager.setPlayerCurrency(uuid, amount);
        }
    }
    
    /**
     * Gets the item manager.
     * 
     * @return The item manager
     */
    public ItemManager getItemManager() {
        if (marketManager != null) {
            return marketManager.getItemManager();
        }
        return null;
    }
    
    /**
     * Handles market-related commands.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("bdmarket") || command.getName().equalsIgnoreCase("market")) {
            if (args.length == 0) {
                // Open market GUI
                PlayerMarket playerMarket = getPlayerMarket(player);
                if (playerMarket != null) {
                    openMarket(player, playerMarket.getMarketId());
                } else {
                    player.sendMessage("§cYou don't have a market. Create one with /market create <name>");
                }
                return true;
            } else if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("create") && args.length >= 2) {
                    // Create a new market
                    String marketName = args[1];
                    Market market = createMarket(player, marketName);
                    if (market != null) {
                        player.sendMessage("§aYou have created a market called §e" + marketName);
                    } else {
                        player.sendMessage("§cYou already have a market or couldn't create one.");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("visit") && args.length >= 2) {
                    // Visit another player's market
                    String playerName = args[1];
                    Player target = plugin.getServer().getPlayer(playerName);
                    if (target != null) {
                        PlayerMarket targetMarket = getPlayerMarket(target);
                        if (targetMarket != null) {
                            openMarket(player, targetMarket.getMarketId());
                        } else {
                            player.sendMessage("§cThat player doesn't have a market.");
                        }
                    } else {
                        player.sendMessage("§cPlayer not found.");
                    }
                    return true;
                }
            }
        }
        
        return false;
    }
}