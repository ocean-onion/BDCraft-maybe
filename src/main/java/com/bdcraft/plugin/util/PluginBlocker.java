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
        try {
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            Bukkit.getLogger().info("BDCraft disabled conflicting plugin: " + plugin.getName());
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to disable plugin: " + plugin.getName(), e);
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