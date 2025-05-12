package com.bdcraft.plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * Utility class for disabling plugins at runtime.
 */
public class PluginBlocker {
    
    /**
     * Disables a conflicting plugin.
     *
     * @param plugin The plugin to disable
     */
    public static void disablePlugin(Plugin plugin) {
        String pluginName = plugin.getName();
        
        try {
            Bukkit.getLogger().warning("========================= PLUGIN CONFLICT =========================");
            Bukkit.getLogger().warning("BDCraft detected a conflicting plugin: " + pluginName);
            Bukkit.getLogger().warning("Attempting to disable " + pluginName + " to prevent conflicts...");
            
            // Get all commands from the plugin
            Bukkit.getLogger().info("Attempting to block commands from " + pluginName);
            
            // Try to force the plugin to disable
            Bukkit.getLogger().info("Attempting to disable plugin directly...");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            
            // Check if it actually got disabled
            if (plugin.isEnabled()) {
                Bukkit.getLogger().warning(pluginName + " could not be disabled automatically!");
                Bukkit.getLogger().warning("This may be due to server protection preventing plugins from disabling each other.");
                Bukkit.getLogger().warning("Please disable " + pluginName + " manually to ensure BDCraft works correctly.");
            } else {
                Bukkit.getLogger().info("Successfully disabled conflicting plugin: " + pluginName);
            }
            
            Bukkit.getLogger().warning("=====================================================================");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to disable plugin: " + pluginName, e);
            Bukkit.getLogger().warning("Please disable " + pluginName + " manually to ensure BDCraft works correctly.");
        }
    }
    
    /**
     * Disables a conflicting plugin by name.
     *
     * @param pluginName The name of the plugin to disable
     * @return True if the plugin was found and disabled
     */
    public static boolean disablePluginByName(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null && plugin.isEnabled()) {
            disablePlugin(plugin);
            return true;
        }
        return false;
    }
}