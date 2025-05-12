package com.bdcraft.plugin.modules.vital;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.vital.modules.chat.ChatModule;
import com.bdcraft.plugin.modules.vital.modules.home.HomeModule;
import com.bdcraft.plugin.modules.vital.modules.tab.TabModule;
import com.bdcraft.plugin.modules.vital.modules.teleport.TeleportModule;
import com.bdcraft.plugin.modules.vital.modules.message.MessageModule;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Main vital module that replaces EssentialsX functionality.
 */
public class BDVitalModule implements ModuleManager {
    private final BDCraft plugin;
    private boolean enabled = false;
    private final Map<String, SubmoduleBase> submodules = new HashMap<>();
    
    /**
     * Creates a new vital module.
     * 
     * @param plugin The plugin instance
     */
    public BDVitalModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Vital";
    }
    
    @Override
    public void enable(BDCraft plugin) {
        if (enabled) {
            return;
        }
        
        plugin.getLogger().info("Enabling BDVital module");
        
        // Register and enable submodules
        registerSubmodules();
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling BDVital module");
        
        // Disable submodules
        for (SubmoduleBase submodule : submodules.values()) {
            try {
                submodule.disable();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error disabling submodule " + submodule.getName(), e);
            }
        }
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        // Reload submodules
        for (SubmoduleBase submodule : submodules.values()) {
            try {
                submodule.reload();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error reloading submodule " + submodule.getName(), e);
            }
        }
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public Object getSubmodule(String name) {
        return submodules.get(name);
    }
    
    @Override
    public void registerSubmodule(Object submodule) {
        if (!(submodule instanceof SubmoduleBase)) {
            throw new IllegalArgumentException("Submodule must implement SubmoduleBase");
        }
        
        SubmoduleBase base = (SubmoduleBase) submodule;
        submodules.put(base.getName(), base);
        
        if (enabled) {
            // Enable the submodule if the parent module is already enabled
            base.enable(this);
        }
    }
    
    /**
     * Registers all submodules.
     */
    private void registerSubmodules() {
        // Register chat module
        registerSubmodule(new ChatModule(plugin));
        
        // Register tab module
        registerSubmodule(new TabModule(plugin));
        
        // Register home module
        registerSubmodule(new HomeModule(plugin));
        
        // Register teleport module
        registerSubmodule(new TeleportModule(plugin));
        
        // Register message module
        registerSubmodule(new MessageModule(plugin));
        
        // Enable all submodules
        for (SubmoduleBase submodule : submodules.values()) {
            boolean enableSubmodule = plugin.getConfig(ConfigType.CONFIG)
                    .getBoolean("modules.vital." + submodule.getName().toLowerCase() + ".enabled", true);
            
            if (enableSubmodule) {
                try {
                    submodule.enable(this);
                    plugin.getLogger().info("Enabled " + submodule.getName() + " submodule");
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error enabling submodule " + submodule.getName(), e);
                }
            }
        }
    }
    
    /**
     * Gets the chat module.
     * 
     * @return The chat module
     */
    public ChatModule getChatModule() {
        return (ChatModule) getSubmodule("Chat");
    }
    
    /**
     * Gets the tab module.
     * 
     * @return The tab module
     */
    public TabModule getTabModule() {
        return (TabModule) getSubmodule("Tab");
    }
    
    /**
     * Gets the home module.
     * 
     * @return The home module
     */
    public HomeModule getHomeModule() {
        return (HomeModule) getSubmodule("Home");
    }
    
    /**
     * Gets the teleport module.
     * 
     * @return The teleport module
     */
    public TeleportModule getTeleportModule() {
        return (TeleportModule) getSubmodule("Teleport");
    }
    
    /**
     * Gets the message module.
     * 
     * @return The message module
     */
    public MessageModule getMessageModule() {
        return (MessageModule) getSubmodule("Message");
    }
}