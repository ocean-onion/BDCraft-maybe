package com.bdcraft.plugin.compat;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.util.PluginBlocker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Manages conflicts with other plugins.
 */
public class PluginConflictManager {
    private final BDCraft plugin;
    
    // Lists of competing plugins
    private final List<String> economyPlugins = Arrays.asList(
            "Vault", 
            "EssentialsX", 
            "CMI", 
            "GemsEconomy", 
            "TokenManager",
            "PlayerPoints"
    );
    
    private final List<String> marketPlugins = Arrays.asList(
            "ShopKeepers", 
            "Shops", 
            "QuickShop", 
            "PlayerShops", 
            "BetterShops",
            "ChestShop"
    );
    
    private final List<String> scoreboardPlugins = Arrays.asList(
            "AnimatedScoreboard", 
            "Featherboard", 
            "DeluxeMenus", 
            "TAB"
    );
    
    private final List<String> blockedPlugins = new ArrayList<>();
    
    /**
     * Creates a new plugin conflict manager.
     *
     * @param plugin The plugin instance
     */
    public PluginConflictManager(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Checks for conflicts with other plugins.
     */
    public void checkForConflicts() {
        plugin.getLogger().info("Checking for plugin conflicts...");
        
        // Check for economy plugins
        int conflicts = 0;
        for (String pluginName : economyPlugins) {
            Plugin conflictingPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (conflictingPlugin != null && conflictingPlugin.isEnabled()) {
                plugin.getLogger().warning("Found conflicting economy plugin: " + pluginName);
                PluginBlocker.disablePlugin(conflictingPlugin);
                blockedPlugins.add(pluginName);
                conflicts++;
            }
        }
        
        // Check for market plugins
        for (String pluginName : marketPlugins) {
            Plugin conflictingPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (conflictingPlugin != null && conflictingPlugin.isEnabled()) {
                plugin.getLogger().warning("Found conflicting market plugin: " + pluginName);
                PluginBlocker.disablePlugin(conflictingPlugin);
                blockedPlugins.add(pluginName);
                conflicts++;
            }
        }
        
        // Check for scoreboard plugins
        for (String pluginName : scoreboardPlugins) {
            Plugin conflictingPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (conflictingPlugin != null && conflictingPlugin.isEnabled()) {
                plugin.getLogger().warning("Found conflicting scoreboard plugin: " + pluginName);
                PluginBlocker.disablePlugin(conflictingPlugin);
                blockedPlugins.add(pluginName);
                conflicts++;
            }
        }
        
        if (conflicts > 0) {
            plugin.getLogger().warning("Disabled " + conflicts + " conflicting plugins.");
        } else {
            plugin.getLogger().info("No plugin conflicts found.");
        }
    }
    
    /**
     * Gets the list of blocked plugins.
     *
     * @return The list of blocked plugins
     */
    public List<String> getBlockedPlugins() {
        return new ArrayList<>(blockedPlugins);
    }
    
    /**
     * Unblocks a plugin.
     *
     * @param pluginName The plugin name
     * @return True if the plugin was unblocked
     */
    public boolean unblockPlugin(String pluginName) {
        if (blockedPlugins.contains(pluginName)) {
            blockedPlugins.remove(pluginName);
            
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            if (plugin != null) {
                try {
                    Bukkit.getPluginManager().enablePlugin(plugin);
                    this.plugin.getLogger().info("Unblocked and enabled plugin: " + pluginName);
                    return true;
                } catch (Exception e) {
                    this.plugin.getLogger().log(Level.SEVERE, "Failed to enable plugin: " + pluginName, e);
                }
            }
        }
        
        return false;
    }
}