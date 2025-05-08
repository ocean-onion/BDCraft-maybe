package com.bdcraft.plugin.compat;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages plugin conflicts by monitoring for competing functionality
 * and blocking specific commands or features from other plugins that 
 * would interfere with BDCraft's self-contained systems.
 */
public class PluginConflictManager implements Listener {
    private final BDCraft plugin;
    private final Logger logger;
    
    // Plugin conflict configurations
    private final Map<String, PluginConflict> conflictingPlugins = new HashMap<>();
    private final List<String> blockedCommands = new ArrayList<>();
    
    // Feature conflict settings
    private boolean blockEconomyPlugins = true;
    private boolean blockPermissionPlugins = true;
    private boolean blockHomePlugins = true;
    private boolean blockTeleportPlugins = true;
    private boolean notifyAdmins = true;
    
    /**
     * Creates a new plugin conflict manager.
     * @param plugin The BDCraft plugin instance
     */
    public PluginConflictManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        
        // Load configurations
        loadConfig();
        
        // Load known conflicts
        setupKnownConflicts();
        
        // Register event listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Check for existing conflicts on startup
        checkExistingPlugins();
    }
    
    /**
     * Loads configuration settings for conflict management.
     */
    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        
        // Load conflict handling settings
        ConfigurationSection conflictSection = config.getConfigurationSection("conflict-management");
        if (conflictSection != null) {
            this.blockEconomyPlugins = conflictSection.getBoolean("block-economy-plugins", true);
            this.blockPermissionPlugins = conflictSection.getBoolean("block-permission-plugins", true);
            this.blockHomePlugins = conflictSection.getBoolean("block-home-plugins", true);
            this.blockTeleportPlugins = conflictSection.getBoolean("block-teleport-plugins", true);
            this.notifyAdmins = conflictSection.getBoolean("notify-admins", true);
        }
        
        // Load blocked commands
        this.blockedCommands.clear();
        List<String> commandList = config.getStringList("blocked-commands");
        if (commandList != null) {
            this.blockedCommands.addAll(commandList);
        }
    }
    
    /**
     * Sets up known plugin conflicts based on common server plugins.
     */
    private void setupKnownConflicts() {
        // Economy Plugins
        if (blockEconomyPlugins) {
            addConflict("Vault", FeatureType.ECONOMY, ConflictAction.FEATURE_DISABLE, 
                    "BDCraft provides its own economy system", 
                    new String[]{"vault.economy"});
            
            addConflict("EssentialsX", FeatureType.ECONOMY, ConflictAction.FEATURE_DISABLE, 
                    "BDCraft provides its own economy system", 
                    new String[]{"essentials.eco", "essentials.economy"});
            
            addConflict("CMI", FeatureType.ECONOMY, ConflictAction.FEATURE_DISABLE, 
                    "BDCraft provides its own economy system", 
                    new String[]{"cmi.eco"});
        }
        
        // Permission Plugins
        if (blockPermissionPlugins) {
            addConflict("LuckPerms", FeatureType.PERMISSIONS, ConflictAction.FEATURE_DISABLE, 
                    "BDCraft provides its own permission system (BDPerms)", 
                    new String[]{"luckperms", "lp"});
            
            addConflict("PermissionsEx", FeatureType.PERMISSIONS, ConflictAction.FEATURE_DISABLE, 
                    "BDCraft provides its own permission system (BDPerms)", 
                    new String[]{"pex", "permissions"});
        }
        
        // Home Plugins
        if (blockHomePlugins) {
            addConflict("EssentialsX", FeatureType.HOMES, ConflictAction.COMMAND_BLOCK, 
                    "BDCraft provides its own home system", 
                    new String[]{"home", "sethome", "delhome", "homes"});
            
            addConflict("CMI", FeatureType.HOMES, ConflictAction.COMMAND_BLOCK, 
                    "BDCraft provides its own home system", 
                    new String[]{"home", "sethome", "delhome", "homes"});
        }
        
        // Teleport Plugins
        if (blockTeleportPlugins) {
            addConflict("EssentialsX", FeatureType.TELEPORT, ConflictAction.COMMAND_BLOCK, 
                    "BDCraft provides its own teleportation system", 
                    new String[]{"tpa", "tpaccept", "tpdeny", "back", "spawn"});
            
            addConflict("CMI", FeatureType.TELEPORT, ConflictAction.COMMAND_BLOCK, 
                    "BDCraft provides its own teleportation system", 
                    new String[]{"tpa", "tpaccept", "tpdeny", "back", "spawn"});
        }
    }
    
    /**
     * Adds a conflict definition.
     * 
     * @param pluginName The name of the conflicting plugin
     * @param featureType The type of feature that conflicts
     * @param action The action to take when conflict is detected
     * @param reason The reason for the conflict
     * @param commands The commands to block (if applicable)
     */
    private void addConflict(String pluginName, FeatureType featureType, ConflictAction action, String reason, String[] commands) {
        PluginConflict conflict = conflictingPlugins.computeIfAbsent(pluginName, k -> new PluginConflict(pluginName));
        conflict.addFeatureConflict(featureType, action, reason, commands);
    }
    
    /**
     * Checks for existing plugins that might conflict.
     */
    private void checkExistingPlugins() {
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            checkPluginConflict(p);
        }
    }
    
    /**
     * Checks if a plugin conflicts with BDCraft.
     * 
     * @param plugin The plugin to check
     */
    private void checkPluginConflict(Plugin plugin) {
        if (plugin == null || plugin.equals(this.plugin)) {
            return;
        }
        
        String pluginName = plugin.getName();
        PluginConflict conflict = conflictingPlugins.get(pluginName);
        
        if (conflict != null) {
            // Handle the conflict based on configuration
            handleConflict(conflict, plugin);
        }
    }
    
    /**
     * Handles a plugin conflict.
     * 
     * @param conflict The conflict definition
     * @param conflictingPlugin The conflicting plugin
     */
    private void handleConflict(PluginConflict conflict, Plugin conflictingPlugin) {
        logger.warning("Detected potential conflict with plugin: " + conflictingPlugin.getName());
        
        for (FeatureConflict featureConflict : conflict.getFeatureConflicts()) {
            logger.warning(" - Feature conflict: " + featureConflict.getFeatureType() + " - " + featureConflict.getReason());
            
            switch (featureConflict.getAction()) {
                case NOTIFY_ONLY:
                    // Just log the conflict, already done above
                    break;
                    
                case COMMAND_BLOCK:
                    // Add commands to blocked list
                    for (String command : featureConflict.getCommands()) {
                        if (!blockedCommands.contains(command)) {
                            blockedCommands.add(command);
                            logger.warning("   - Blocking command: /" + command);
                        }
                    }
                    break;
                    
                case FEATURE_DISABLE:
                    // Log that we're handling this conflict by disabling our competing feature
                    logger.warning("   - BDCraft will override " + conflictingPlugin.getName() + 
                            " functionality for " + featureConflict.getFeatureType());
                    break;
                    
                case PLUGIN_DISABLE:
                    // This is a severe conflict, disable the other plugin
                    // This option should be used very sparingly
                    if (plugin.getConfig().getBoolean("conflict-management.allow-plugin-disable", false)) {
                        logger.warning("   - Disabling conflicting plugin: " + conflictingPlugin.getName());
                        Bukkit.getPluginManager().disablePlugin(conflictingPlugin);
                    } else {
                        logger.warning("   - Not disabling plugin due to configuration. Manual resolution required.");
                    }
                    break;
            }
            
            // Notify server admins about the conflict
            if (notifyAdmins) {
                for (String line : generateConflictMessage(conflict, featureConflict)) {
                    Bukkit.getConsoleSender().sendMessage(line);
                }
            }
        }
    }
    
    /**
     * Generates a formatted conflict message for admins.
     * 
     * @param conflict The plugin conflict
     * @param featureConflict The specific feature conflict
     * @return The formatted message lines
     */
    private String[] generateConflictMessage(PluginConflict conflict, FeatureConflict featureConflict) {
        List<String> message = new ArrayList<>();
        
        message.add("§c[BDCraft] Plugin Conflict Detected");
        message.add("§e - Plugin: §f" + conflict.getPluginName());
        message.add("§e - Feature: §f" + featureConflict.getFeatureType());
        message.add("§e - Issue: §f" + featureConflict.getReason());
        
        if (featureConflict.getAction() == ConflictAction.COMMAND_BLOCK) {
            message.add("§e - Blocked Commands: §f" + String.join(", ", featureConflict.getCommands()));
        }
        
        message.add("§e - Resolution: §f" + getActionDescription(featureConflict.getAction()));
        
        return message.toArray(new String[0]);
    }
    
    /**
     * Gets a user-friendly description of a conflict action.
     * 
     * @param action The conflict action
     * @return A description of the action
     */
    private String getActionDescription(ConflictAction action) {
        switch (action) {
            case NOTIFY_ONLY:
                return "Notification only, no action taken";
            case COMMAND_BLOCK:
                return "Blocking conflicting commands";
            case FEATURE_DISABLE:
                return "BDCraft will override the conflicting functionality";
            case PLUGIN_DISABLE:
                return "Disabling the conflicting plugin";
            default:
                return "Unknown action";
        }
    }
    
    /**
     * Event handler for plugin enable events.
     * 
     * @param event The plugin enable event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent event) {
        checkPluginConflict(event.getPlugin());
    }
    
    /**
     * Event handler for player command preprocessing.
     * 
     * @param event The player command preprocess event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        String command = event.getMessage().substring(1).split(" ")[0].toLowerCase();
        
        // Check if this command is blocked
        if (blockedCommands.contains(command)) {
            event.setCancelled(true);
            
            // Send message to player
            event.getPlayer().sendMessage("§c[BDCraft] This command is disabled because BDCraft provides this functionality.");
            event.getPlayer().sendMessage("§e Please use BDCraft commands instead. Type §f/bdhelp §efor more information.");
            
            // Log the blocked command
            logger.info("Blocked command /" + command + " from player " + event.getPlayer().getName());
        }
    }
    
    /**
     * An enum for feature types that might conflict.
     */
    public enum FeatureType {
        ECONOMY,
        PERMISSIONS,
        HOMES,
        TELEPORT,
        MESSAGING,
        PLACEHOLDER
    }
    
    /**
     * An enum for conflict actions.
     */
    public enum ConflictAction {
        NOTIFY_ONLY,      // Just notify the admin
        COMMAND_BLOCK,    // Block specific commands from the other plugin
        FEATURE_DISABLE,  // Disable a specific feature in our plugin
        PLUGIN_DISABLE    // Disable the conflicting plugin (use sparingly)
    }
    
    /**
     * A class representing a feature conflict.
     */
    private static class FeatureConflict {
        private final FeatureType featureType;
        private final ConflictAction action;
        private final String reason;
        private final String[] commands;
        
        /**
         * Creates a new feature conflict.
         * 
         * @param featureType The feature type
         * @param action The conflict action
         * @param reason The reason for the conflict
         * @param commands The commands to block (if applicable)
         */
        public FeatureConflict(FeatureType featureType, ConflictAction action, String reason, String[] commands) {
            this.featureType = featureType;
            this.action = action;
            this.reason = reason;
            this.commands = commands;
        }
        
        /**
         * Gets the feature type.
         * 
         * @return The feature type
         */
        public FeatureType getFeatureType() {
            return featureType;
        }
        
        /**
         * Gets the conflict action.
         * 
         * @return The conflict action
         */
        public ConflictAction getAction() {
            return action;
        }
        
        /**
         * Gets the reason for the conflict.
         * 
         * @return The reason
         */
        public String getReason() {
            return reason;
        }
        
        /**
         * Gets the commands to block.
         * 
         * @return The commands
         */
        public String[] getCommands() {
            return commands;
        }
    }
    
    /**
     * A class representing a plugin conflict.
     */
    private static class PluginConflict {
        private final String pluginName;
        private final List<FeatureConflict> featureConflicts = new ArrayList<>();
        
        /**
         * Creates a new plugin conflict.
         * 
         * @param pluginName The plugin name
         */
        public PluginConflict(String pluginName) {
            this.pluginName = pluginName;
        }
        
        /**
         * Gets the plugin name.
         * 
         * @return The plugin name
         */
        public String getPluginName() {
            return pluginName;
        }
        
        /**
         * Adds a feature conflict.
         * 
         * @param featureType The feature type
         * @param action The conflict action
         * @param reason The reason for the conflict
         * @param commands The commands to block (if applicable)
         */
        public void addFeatureConflict(FeatureType featureType, ConflictAction action, String reason, String[] commands) {
            featureConflicts.add(new FeatureConflict(featureType, action, reason, commands));
        }
        
        /**
         * Gets the feature conflicts.
         * 
         * @return The feature conflicts
         */
        public List<FeatureConflict> getFeatureConflicts() {
            return featureConflicts;
        }
    }
}