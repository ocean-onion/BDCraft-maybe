package com.bdcraft.plugin.util;

import com.bdcraft.plugin.BDCraft;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Blocks and disables plugins that conflict with BDCraft functionality.
 */
public class PluginBlocker implements Listener {
    private final BDCraft plugin;
    
    // Plugins to block, categorized by type
    private static final List<String> ECONOMY_PLUGINS = Arrays.asList(
        "Vault", 
        "EssentialsX", 
        "CMI", 
        "GemsEconomy",
        "TokenManager",
        "CoinsEngine",
        "PlayerPoints",
        "CraftConomy",
        "iConomy",
        "RealEconomy"
    );
    
    private static final List<String> PERMISSION_PLUGINS = Arrays.asList(
        "LuckPerms",
        "PermissionsEx",
        "GroupManager",
        "UltraPermissions",
        "PowerRanks",
        "PEX",
        "PermissionManager",
        "RankSystem"
    );
    
    private static final List<String> PROGRESSION_PLUGINS = Arrays.asList(
        "McMMO",
        "AureliumSkills",
        "Jobs",
        "JobsReborn",
        "SkillAPI",
        "RPGCore",
        "LevelledMobs",
        "ProgressionSystem",
        "PlayerLevels",
        "RankUP"
    );
    
    private static final List<String> MARKET_PLUGINS = Arrays.asList(
        "QuickShop",
        "Shopkeepers",
        "BossShop",
        "ChestShop",
        "CraftShop",
        "MarketPlace",
        "DynamicShop",
        "Shop",
        "AuctionHouse"
    );
    
    /**
     * Creates a new plugin blocker.
     * 
     * @param plugin The plugin instance
     */
    public PluginBlocker(BDCraft plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Runs on plugin load to check and disable any conflicting plugins.
     */
    public void checkAndDisableConflictingPlugins() {
        plugin.getLogger().info("Checking for conflicting plugins to block...");
        
        for (Plugin otherPlugin : Bukkit.getPluginManager().getPlugins()) {
            if (shouldBlockPlugin(otherPlugin)) {
                disablePlugin(otherPlugin);
            }
        }
    }
    
    /**
     * Listens for plugin enable events to prevent conflicting plugins from loading.
     * 
     * @param event The plugin enable event
     */
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        Plugin otherPlugin = event.getPlugin();
        
        if (shouldBlockPlugin(otherPlugin)) {
            disablePlugin(otherPlugin);
        }
    }
    
    /**
     * Checks if a plugin should be blocked.
     * 
     * @param otherPlugin The plugin to check
     * @return Whether the plugin should be blocked
     */
    private boolean shouldBlockPlugin(Plugin otherPlugin) {
        if (otherPlugin == plugin) {
            return false; // Don't block ourselves
        }
        
        String pluginName = otherPlugin.getName();
        
        return ECONOMY_PLUGINS.contains(pluginName) ||
               PERMISSION_PLUGINS.contains(pluginName) ||
               PROGRESSION_PLUGINS.contains(pluginName) ||
               MARKET_PLUGINS.contains(pluginName);
    }
    
    /**
     * Disables a plugin.
     * 
     * @param pluginToDisable The plugin to disable
     */
    private void disablePlugin(Plugin pluginToDisable) {
        try {
            String name = pluginToDisable.getName();
            plugin.getLogger().warning("Disabling conflicting plugin: " + name);
            Bukkit.getPluginManager().disablePlugin(pluginToDisable);
            
            plugin.getLogger().info(name + " has been disabled as it conflicts with BDCraft functionality.");
            
            // Categorize the plugin
            if (ECONOMY_PLUGINS.contains(name)) {
                plugin.getLogger().info(name + " conflicts with BDCraft economy system.");
            } else if (PERMISSION_PLUGINS.contains(name)) {
                plugin.getLogger().info(name + " conflicts with BDCraft permissions system.");
            } else if (PROGRESSION_PLUGINS.contains(name)) {
                plugin.getLogger().info(name + " conflicts with BDCraft progression system.");
            } else if (MARKET_PLUGINS.contains(name)) {
                plugin.getLogger().info(name + " conflicts with BDCraft market and trading system.");
            }
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error disabling conflicting plugin", e);
        }
    }
}