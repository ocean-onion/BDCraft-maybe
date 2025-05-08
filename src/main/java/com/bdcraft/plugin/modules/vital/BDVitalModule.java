package com.bdcraft.plugin.modules.vital;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.Module;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.vital.commands.DeathInfoCommand;
import com.bdcraft.plugin.modules.vital.commands.HomeCommand;
import com.bdcraft.plugin.modules.vital.commands.MessageCommands;
import com.bdcraft.plugin.modules.vital.commands.TeleportCommands;
import com.bdcraft.plugin.modules.vital.home.HomeManager;
import com.bdcraft.plugin.modules.vital.listeners.PlayerListener;
import com.bdcraft.plugin.modules.vital.message.MessageManager;
import com.bdcraft.plugin.modules.vital.teleport.TeleportManager;

import java.util.logging.Logger;

/**
 * The BD Vital Module providing essential server functionality.
 */
public class BDVitalModule implements Module {
    private final BDCraft plugin;
    private final ModuleManager moduleManager;
    private final Logger logger;
    private boolean enabled = false;
    
    private HomeManager homeManager;
    private TeleportManager teleportManager;
    private MessageManager messageManager;
    
    private HomeCommand homeCommand;
    private TeleportCommands teleportCommands;
    private MessageCommands messageCommands;
    private DeathInfoCommand deathInfoCommand;
    
    private PlayerListener playerListener;
    
    /**
     * Creates a new BD vital module.
     * @param plugin The plugin instance
     * @param moduleManager The module manager
     */
    public BDVitalModule(BDCraft plugin, ModuleManager moduleManager) {
        this.plugin = plugin;
        this.moduleManager = moduleManager;
        this.logger = plugin.getLogger();
    }
    
    @Override
    public String getName() {
        return "Vital";
    }
    
    @Override
    public void enable() {
        if (enabled) {
            return;
        }
        
        logger.info("Enabling BD Vital Module");
        
        // Initialize managers
        this.homeManager = new HomeManager(plugin);
        this.teleportManager = new TeleportManager(plugin);
        this.messageManager = new MessageManager(plugin);
        
        // Register commands
        this.homeCommand = new HomeCommand(plugin, this);
        this.teleportCommands = new TeleportCommands(plugin, this);
        this.messageCommands = new MessageCommands(plugin, this);
        this.deathInfoCommand = new DeathInfoCommand(plugin, this);
        
        // Register listeners
        this.playerListener = new PlayerListener(plugin, this);
        plugin.getServer().getPluginManager().registerEvents(playerListener, plugin);
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        logger.info("Disabling BD Vital Module");
        
        // Save data
        homeManager.saveHomes();
        messageManager.saveMail();
        
        enabled = false;
    }
    
    /**
     * Gets the home manager.
     * @return The home manager
     */
    public HomeManager getHomeManager() {
        return homeManager;
    }
    
    /**
     * Gets the teleport manager.
     * @return The teleport manager
     */
    public TeleportManager getTeleportManager() {
        return teleportManager;
    }
    
    /**
     * Gets the message manager.
     * @return The message manager
     */
    public MessageManager getMessageManager() {
        return messageManager;
    }
}