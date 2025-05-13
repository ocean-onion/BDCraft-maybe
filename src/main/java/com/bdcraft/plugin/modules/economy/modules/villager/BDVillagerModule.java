package com.bdcraft.plugin.modules.economy.modules.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.economy.modules.villager.impl.BDVillager;
import com.bdcraft.plugin.modules.economy.modules.villager.impl.BDVillagerManager;
import com.bdcraft.plugin.modules.economy.modules.villager.impl.VillagerAPIImpl;
import com.bdcraft.plugin.modules.economy.modules.villager.impl.VillagerManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Module for handling custom villager functionality.
 * This module provides specialized villager types with custom trades.
 */
public class BDVillagerModule implements SubmoduleBase, CommandExecutor, Listener {
    private final BDCraft plugin;
    private final Logger logger;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Components
    private BDVillagerManager bdVillagerManager;
    private VillagerManager villagerManager;
    private VillagerAPIImpl villagerAPI;
    
    /**
     * Creates a new villager module.
     * 
     * @param plugin The plugin instance
     */
    public BDVillagerModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    @Override
    public String getName() {
        return "Villager";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        logger.info("Enabling Villager submodule");
        
        // Initialize components
        this.bdVillagerManager = new BDVillagerManager(plugin);
        this.villagerManager = new VillagerManager(plugin);
        this.villagerAPI = new VillagerAPIImpl(villagerManager);
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Register commands
        registerCommands();
        
        // Load villager data
        bdVillagerManager.loadVillagers();
        
        enabled = true;
        logger.info("Villager submodule enabled");
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        logger.info("Disabling Villager submodule");
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        // Save villager data
        bdVillagerManager.saveVillagers();
        
        enabled = false;
        logger.info("Villager submodule disabled");
    }
    
    @Override
    public void reload() {
        if (enabled) {
            // Reload villager data
            bdVillagerManager.loadVillagers();
            logger.info("Villager submodule reloaded");
        }
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Registers commands for the villager module.
     */
    private void registerCommands() {
        // Register villager command
        plugin.getCommand("bdvillager").setExecutor(this);
    }
    
    /**
     * Gets the BD villager manager instance.
     * 
     * @return The BD villager manager
     */
    public BDVillagerManager getBDVillagerManager() {
        return bdVillagerManager;
    }
    
    /**
     * Gets the villager manager instance.
     * 
     * @return The villager manager
     */
    public VillagerManager getVillagerManager() {
        return villagerManager;
    }
    
    /**
     * Gets the villager API implementation.
     * 
     * @return The villager API
     */
    public VillagerAPIImpl getVillagerAPI() {
        return villagerAPI;
    }
    
    /**
     * Creates a custom villager at a player's location.
     * 
     * @param player The player
     * @param villagerType The villager type
     * @return The created villager, or null if failed
     */
    public BDVillager createVillager(Player player, String villagerType) {
        if (bdVillagerManager != null) {
            return bdVillagerManager.createVillager(player.getLocation(), villagerType);
        }
        return null;
    }
    
    /**
     * Gets a custom villager by entity UUID.
     * 
     * @param villagerUUID The villager UUID
     * @return The custom villager, or null if not found
     */
    public BDVillager getVillager(UUID villagerUUID) {
        if (bdVillagerManager != null) {
            return bdVillagerManager.getVillager(villagerUUID);
        }
        return null;
    }
    
    /**
     * Event handler for villager interaction.
     * 
     * @param event The event
     */
    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            Villager villager = (Villager) event.getRightClicked();
            UUID villagerUUID = villager.getUniqueId();
            
            // Check if this is a custom villager
            BDVillager bdVillager = getVillager(villagerUUID);
            if (bdVillager != null) {
                // Let the custom villager handle the interaction
                bdVillager.onInteract(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }
    
    /**
     * Event handler for villager trade acquisition.
     * 
     * @param event The event
     */
    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        if (event.getEntity() instanceof Villager) {
            Villager villager = (Villager) event.getEntity();
            UUID villagerUUID = villager.getUniqueId();
            
            // Check if this is a custom villager
            BDVillager bdVillager = getVillager(villagerUUID);
            if (bdVillager != null) {
                // Custom villagers have fixed trades, don't let them acquire random trades
                event.setCancelled(true);
            }
        }
    }
    
    /**
     * Handles villager-related commands.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("bdvillager")) {
            if (args.length >= 2) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    // Spawn a custom villager
                    String villagerType = args[1];
                    BDVillager villager = createVillager(player, villagerType);
                    if (villager != null) {
                        player.sendMessage("§aSpawned a " + villagerType + " villager.");
                    } else {
                        player.sendMessage("§cInvalid villager type or couldn't spawn villager.");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("remove") && player.hasPermission("bdcraft.admin")) {
                    // Admin command to remove nearby villagers
                    int radius = 5;
                    try {
                        if (args.length >= 3) {
                            radius = Integer.parseInt(args[2]);
                        }
                    } catch (NumberFormatException e) {
                        // Use default radius
                    }
                    
                    int count = bdVillagerManager.removeNearbyVillagers(player.getLocation(), radius);
                    player.sendMessage("§aRemoved " + count + " custom villagers within " + radius + " blocks.");
                    return true;
                }
            }
            
            // Show help message
            player.sendMessage("§e===== BDVillager Commands =====");
            player.sendMessage("§6/bdvillager spawn <type> §7- Spawns a custom villager");
            if (player.hasPermission("bdcraft.admin")) {
                player.sendMessage("§6/bdvillager remove <type> [radius] §7- Removes nearby villagers");
            }
            return true;
        }
        
        return false;
    }
}