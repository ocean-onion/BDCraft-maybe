package com.bdcraft.plugin.compat;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.ModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Manages plugin conflicts by specifically targeting competing features
 * rather than entire plugins. This allows BDCraft to selectively disable
 * just the conflicting features from other plugins while allowing
 * non-conflicting features to continue working.
 */
public class PluginConflictManager implements Listener {
    private final BDCraft plugin;
    private final Logger logger;
    private final ModuleManager moduleManager;
    
    // Plugin conflict configurations
    private final Map<String, PluginFeatureHandler> pluginHandlers = new HashMap<>();
    private final Map<FeatureType, List<CommandBlocker>> commandBlockers = new HashMap<>();
    private final Map<FeatureType, List<ListenerBlocker>> listenerBlockers = new HashMap<>();
    private final Map<FeatureType, Set<String>> disabledPluginFeatures = new HashMap<>();
    
    // Blocked command patterns with their replacement suggestions
    private final Map<Pattern, String> blockedCommandPatterns = new HashMap<>();
    
    // Reflection helpers
    private static final String[] PLUGIN_CLASS_PATTERNS = {
        "net.essentialsx.api",
        "com.earth2me.essentials",
        "net.ess3",
        "org.maxgamer.quickshop",
        "com.griefcraft.lwc",
        "me.ryanhamshire.GriefPrevention",
        "net.luckperms",
        "ru.tehkode.permissions",
        "com.sk89q.worldguard",
        "com.palmergames.bukkit.towny",
        "me.clip.placeholderapi",
        "com.gmail.filoghost.holographicdisplays",
        "com.jaoafa.mymaid4",
        "net.coreprotect",
        "me.lucko.luckperms"
    };
    
    /**
     * Creates a new plugin conflict manager.
     * @param plugin The BDCraft plugin instance
     */
    public PluginConflictManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.moduleManager = plugin.getModuleManager();
        
        // Initialize feature conflict maps
        for (FeatureType type : FeatureType.values()) {
            commandBlockers.put(type, new ArrayList<>());
            listenerBlockers.put(type, new ArrayList<>());
            disabledPluginFeatures.put(type, new HashSet<>());
        }
        
        // Load configurations
        loadConfig();
        
        // Register feature conflicts with detailed handlers
        registerFeatureConflicts();
        
        // Register event listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Check for existing conflicts on startup
        checkExistingPlugins();
        
        logger.info("Initialized BDCraft Plugin Conflict Manager - " + 
                pluginHandlers.size() + " potential plugin conflicts registered");
    }
    
    /**
     * Loads configuration settings for conflict management.
     */
    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        
        // Create default configuration if it doesn't exist
        if (!config.contains("conflict-management")) {
            config.set("conflict-management.enabled", true);
            config.set("conflict-management.economy-features", true);
            config.set("conflict-management.market-features", true);
            config.set("conflict-management.permission-features", true);
            config.set("conflict-management.teleport-features", true);
            config.set("conflict-management.home-features", true);
            config.set("conflict-management.messaging-features", true);
            config.set("conflict-management.progression-features", true);
            config.set("conflict-management.notify-admins", true);
            config.set("conflict-management.allow-plugin-disable", false);
            config.set("conflict-management.redirect-commands", true);
            plugin.saveConfig();
        }
    }
    
    /**
     * Registers all known feature conflicts with specific handlers.
     */
    private void registerFeatureConflicts() {
        // === Economy Features ===
        registerEconomyConflicts();
        
        // === Market Features ===
        registerMarketConflicts();
        
        // === Permissions Features ===
        registerPermissionConflicts();
        
        // === Teleport Features ===
        registerTeleportConflicts();
        
        // === Home Features ===
        registerHomeConflicts();
        
        // === Messaging Features ===
        registerMessagingConflicts();
        
        // === Progression Features ===
        registerProgressionConflicts();
        
        // === Placeholder Features ===
        registerPlaceholderConflicts();
    }
    
    /**
     * Registers economy feature conflicts.
     */
    private void registerEconomyConflicts() {
        // Vault Economy
        addPluginHandler("Vault", plugin -> {
            try {
                // Find the Vault Economy Service Manager
                // Rather than disable Vault, we'll override its economy provider
                Class<?> vaultClass = Class.forName("net.milkbowl.vault.Vault");
                
                if (vaultClass.isInstance(plugin)) {
                    logger.info("Detected Vault - BDCraft will use its own economy system instead");
                    
                    // Apply command blockers
                    addCommandBlocker(FeatureType.ECONOMY, "eco", "/bdeco", "Vault");
                    addCommandBlocker(FeatureType.ECONOMY, "economy", "/bdeco", "Vault");
                    addCommandBlocker(FeatureType.ECONOMY, "bal", "/bdbal", "Vault");
                    addCommandBlocker(FeatureType.ECONOMY, "balance", "/bdbal", "Vault");
                    addCommandBlocker(FeatureType.ECONOMY, "pay", "/bdpay", "Vault");
                    
                    return DisableResult.FEATURE_DISABLED;
                }
            } catch (ClassNotFoundException e) {
                // Vault not found, which is fine
            }
            return DisableResult.NO_ACTION;
        });
        
        // EssentialsX Economy
        addPluginHandler("Essentials", plugin -> {
            // Apply command blockers
            addCommandBlocker(FeatureType.ECONOMY, "eco", "/bdeco", "Essentials");
            addCommandBlocker(FeatureType.ECONOMY, "economy", "/bdeco", "Essentials");
            addCommandBlocker(FeatureType.ECONOMY, "bal", "/bdbal", "Essentials");
            addCommandBlocker(FeatureType.ECONOMY, "balance", "/bdbal", "Essentials");
            addCommandBlocker(FeatureType.ECONOMY, "pay", "/bdpay", "Essentials");
            addCommandBlocker(FeatureType.ECONOMY, "balancetop", "/bdtop", "Essentials");
            addCommandBlocker(FeatureType.ECONOMY, "baltop", "/bdtop", "Essentials");
            
            logger.info("Detected Essentials - BDCraft will block economy commands");
            return DisableResult.FEATURE_DISABLED;
        });
        
        // CMI Economy
        addPluginHandler("CMI", plugin -> {
            // Apply command blockers for CMI economy
            addCommandBlocker(FeatureType.ECONOMY, "cmi money", "/bdeco", "CMI");
            addCommandBlocker(FeatureType.ECONOMY, "cmi balance", "/bdbal", "CMI");
            addCommandBlocker(FeatureType.ECONOMY, "cmi baltop", "/bdtop", "CMI");
            addCommandBlocker(FeatureType.ECONOMY, "cmi pay", "/bdpay", "CMI");
            
            logger.info("Detected CMI - BDCraft will block economy commands");
            return DisableResult.FEATURE_DISABLED;
        });
    }
    
    /**
     * Registers market feature conflicts.
     */
    private void registerMarketConflicts() {
        // QuickShop
        addPluginHandler("QuickShop", plugin -> {
            // Block shop-related commands that conflict with BD markets
            addCommandBlocker(FeatureType.MARKETS, "qs", "/bdmarket", "QuickShop");
            addCommandBlocker(FeatureType.MARKETS, "shop", "/bdmarket", "QuickShop");
            addCommandBlocker(FeatureType.MARKETS, "quickshop", "/bdmarket", "QuickShop");
            
            logger.info("Detected QuickShop - BDCraft will use its own market system instead");
            return DisableResult.FEATURE_DISABLED;
        });
        
        // ShopKeepers
        addPluginHandler("Shopkeepers", plugin -> {
            // Block shop-related commands that conflict with BD markets
            addCommandBlocker(FeatureType.MARKETS, "shopkeeper", "/bdmarket", "Shopkeepers");
            addCommandBlocker(FeatureType.MARKETS, "shopkeepers", "/bdmarket", "Shopkeepers");
            
            logger.info("Detected Shopkeepers - BDCraft will use its own market system instead");
            return DisableResult.FEATURE_DISABLED;
        });
    }
    
    /**
     * Registers permission feature conflicts.
     */
    private void registerPermissionConflicts() {
        // LuckPerms
        addPluginHandler("LuckPerms", plugin -> {
            // We won't block LuckPerms commands entirely, but we'll inform admins
            // that our permissions system works alongside it
            logger.info("Detected LuckPerms - BDCraft will use internal permission system for BD features");
            return DisableResult.NOTIFY_ONLY;
        });
        
        // PermissionsEx
        addPluginHandler("PermissionsEx", plugin -> {
            // We won't block PermissionsEx commands entirely, but we'll inform admins
            // that our permissions system works alongside it
            logger.info("Detected PermissionsEx - BDCraft will use internal permission system for BD features");
            return DisableResult.NOTIFY_ONLY;
        });
    }
    
    /**
     * Registers teleport feature conflicts.
     */
    private void registerTeleportConflicts() {
        // EssentialsX Teleport Commands
        addPluginHandler("Essentials", plugin -> {
            // Block teleport-related commands
            addCommandBlocker(FeatureType.TELEPORT, "tpa", "/bdtp", "Essentials");
            addCommandBlocker(FeatureType.TELEPORT, "tpaccept", "/bdtpa", "Essentials");
            addCommandBlocker(FeatureType.TELEPORT, "tpdeny", "/bdtpd", "Essentials");
            addCommandBlocker(FeatureType.TELEPORT, "tphere", "/bdtpr", "Essentials");
            addCommandBlocker(FeatureType.TELEPORT, "back", "/bdback", "Essentials");
            addCommandBlocker(FeatureType.TELEPORT, "spawn", "/bdspawn", "Essentials");
            
            logger.info("Detected Essentials - BDCraft will block teleport commands");
            return DisableResult.FEATURE_DISABLED;
        });
        
        // CMI Teleport Commands
        addPluginHandler("CMI", plugin -> {
            // Block teleport-related commands
            addCommandBlocker(FeatureType.TELEPORT, "tpa", "/bdtp", "CMI");
            addCommandBlocker(FeatureType.TELEPORT, "tpaccept", "/bdtpa", "CMI");
            addCommandBlocker(FeatureType.TELEPORT, "tpdeny", "/bdtpd", "CMI");
            addCommandBlocker(FeatureType.TELEPORT, "cmi tpa", "/bdtp", "CMI");
            addCommandBlocker(FeatureType.TELEPORT, "cmi tpaccept", "/bdtpa", "CMI");
            addCommandBlocker(FeatureType.TELEPORT, "cmi tpdeny", "/bdtpd", "CMI");
            addCommandBlocker(FeatureType.TELEPORT, "back", "/bdback", "CMI");
            addCommandBlocker(FeatureType.TELEPORT, "spawn", "/bdspawn", "CMI");
            
            logger.info("Detected CMI - BDCraft will block teleport commands");
            return DisableResult.FEATURE_DISABLED;
        });
    }
    
    /**
     * Registers home feature conflicts.
     */
    private void registerHomeConflicts() {
        // EssentialsX Home Commands
        addPluginHandler("Essentials", plugin -> {
            // Block home-related commands
            addCommandBlocker(FeatureType.HOMES, "home", "/bdhome", "Essentials");
            addCommandBlocker(FeatureType.HOMES, "homes", "/bdhome list", "Essentials");
            addCommandBlocker(FeatureType.HOMES, "sethome", "/bdhome set", "Essentials");
            addCommandBlocker(FeatureType.HOMES, "delhome", "/bdhome del", "Essentials");
            
            logger.info("Detected Essentials - BDCraft will block home commands");
            return DisableResult.FEATURE_DISABLED;
        });
        
        // CMI Home Commands
        addPluginHandler("CMI", plugin -> {
            // Block home-related commands
            addCommandBlocker(FeatureType.HOMES, "home", "/bdhome", "CMI");
            addCommandBlocker(FeatureType.HOMES, "homes", "/bdhome list", "CMI");
            addCommandBlocker(FeatureType.HOMES, "sethome", "/bdhome set", "CMI");
            addCommandBlocker(FeatureType.HOMES, "delhome", "/bdhome del", "CMI");
            addCommandBlocker(FeatureType.HOMES, "cmi home", "/bdhome", "CMI");
            addCommandBlocker(FeatureType.HOMES, "cmi sethome", "/bdhome set", "CMI");
            addCommandBlocker(FeatureType.HOMES, "cmi delhome", "/bdhome del", "CMI");
            
            logger.info("Detected CMI - BDCraft will block home commands");
            return DisableResult.FEATURE_DISABLED;
        });
    }
    
    /**
     * Registers messaging feature conflicts.
     */
    private void registerMessagingConflicts() {
        // EssentialsX Messaging
        addPluginHandler("Essentials", plugin -> {
            // Block message-related commands
            addCommandBlocker(FeatureType.MESSAGING, "msg", "/bdmsg", "Essentials");
            addCommandBlocker(FeatureType.MESSAGING, "message", "/bdmsg", "Essentials");
            addCommandBlocker(FeatureType.MESSAGING, "tell", "/bdmsg", "Essentials");
            addCommandBlocker(FeatureType.MESSAGING, "whisper", "/bdmsg", "Essentials");
            addCommandBlocker(FeatureType.MESSAGING, "w", "/bdmsg", "Essentials");
            addCommandBlocker(FeatureType.MESSAGING, "r", "/bdr", "Essentials");
            addCommandBlocker(FeatureType.MESSAGING, "reply", "/bdr", "Essentials");
            addCommandBlocker(FeatureType.MESSAGING, "mail", "/bdmail", "Essentials");
            
            logger.info("Detected Essentials - BDCraft will block messaging commands");
            return DisableResult.FEATURE_DISABLED;
        });
        
        // CMI Messaging
        addPluginHandler("CMI", plugin -> {
            // Block message-related commands
            addCommandBlocker(FeatureType.MESSAGING, "msg", "/bdmsg", "CMI");
            addCommandBlocker(FeatureType.MESSAGING, "message", "/bdmsg", "CMI");
            addCommandBlocker(FeatureType.MESSAGING, "tell", "/bdmsg", "CMI");
            addCommandBlocker(FeatureType.MESSAGING, "whisper", "/bdmsg", "CMI");
            addCommandBlocker(FeatureType.MESSAGING, "r", "/bdr", "CMI");
            addCommandBlocker(FeatureType.MESSAGING, "reply", "/bdr", "CMI");
            addCommandBlocker(FeatureType.MESSAGING, "cmi msg", "/bdmsg", "CMI");
            addCommandBlocker(FeatureType.MESSAGING, "cmi mail", "/bdmail", "CMI");
            
            logger.info("Detected CMI - BDCraft will block messaging commands");
            return DisableResult.FEATURE_DISABLED;
        });
    }
    
    /**
     * Registers progression feature conflicts.
     */
    private void registerProgressionConflicts() {
        // Level/Rank plugins
        addPluginHandler("EssentialsX", plugin -> {
            // Only block specific rank-related commands
            addCommandBlocker(FeatureType.PROGRESSION, "rank", "/bdrank", "EssentialsX");
            return DisableResult.FEATURE_DISABLED;
        });
        
        // PlayerLevels plugin
        addPluginHandler("PlayerLevels", plugin -> {
            // Block all commands to avoid conflicting with our progression system
            addCommandBlocker(FeatureType.PROGRESSION, "level", "/bdrank", "PlayerLevels");
            addCommandBlocker(FeatureType.PROGRESSION, "levels", "/bdrank", "PlayerLevels");
            addCommandBlocker(FeatureType.PROGRESSION, "playerlevel", "/bdrank", "PlayerLevels");
            addCommandBlocker(FeatureType.PROGRESSION, "playerlevels", "/bdrank", "PlayerLevels");
            return DisableResult.FEATURE_DISABLED;
        });
    }
    
    /**
     * Registers placeholder conflicts.
     */
    private void registerPlaceholderConflicts() {
        // No need to block PlaceholderAPI as we'll hook into it
        addPluginHandler("PlaceholderAPI", plugin -> {
            logger.info("Detected PlaceholderAPI - BDCraft will register custom placeholders");
            return DisableResult.NOTIFY_ONLY;
        });
    }
    
    /**
     * Adds a new plugin handler to check for conflicts.
     * 
     * @param pluginName The name of the plugin to check
     * @param handler The handler to process the plugin
     */
    private void addPluginHandler(String pluginName, PluginFeatureHandler handler) {
        pluginHandlers.put(pluginName, handler);
    }
    
    /**
     * Adds a command blocker for a specific feature.
     * 
     * @param featureType The feature type
     * @param command The command to block
     * @param replacement The suggested replacement command
     * @param pluginName The name of the plugin that provides the command
     */
    private void addCommandBlocker(FeatureType featureType, String command, String replacement, String pluginName) {
        commandBlockers.get(featureType).add(new CommandBlocker(command, replacement, pluginName));
        
        // Create a pattern to match this command
        // This handles commands with or without slash and with arguments
        Pattern pattern = Pattern.compile("^/?" + Pattern.quote(command) + "(?:\\s+.*)?$", Pattern.CASE_INSENSITIVE);
        blockedCommandPatterns.put(pattern, replacement);
    }
    
    /**
     * Checks for existing plugins that might conflict.
     */
    private void checkExistingPlugins() {
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            handlePluginConflict(p);
        }
    }
    
    /**
     * Handles plugin conflict resolution for a specific plugin.
     * 
     * @param plugin The plugin to check
     */
    private void handlePluginConflict(Plugin plugin) {
        if (plugin == null || plugin.equals(this.plugin)) {
            return;
        }
        
        String pluginName = plugin.getName();
        PluginFeatureHandler handler = pluginHandlers.get(pluginName);
        
        if (handler != null) {
            DisableResult result = handler.handleFeature(plugin);
            
            if (result != DisableResult.NO_ACTION) {
                logger.info("Handled potential conflict with plugin " + pluginName + 
                        ": " + result.name());
                
                if (result == DisableResult.PLUGIN_DISABLED) {
                    // Only disable the entire plugin as a last resort and if allowed
                    if (plugin.getConfig().getBoolean("conflict-management.allow-plugin-disable", false)) {
                        logger.warning("Disabling conflicting plugin: " + pluginName);
                        Bukkit.getPluginManager().disablePlugin(plugin);
                    } else {
                        logger.warning("Not disabling plugin due to configuration. Manual resolution may be required.");
                    }
                }
            }
        }
    }
    
    /**
     * Event handler for plugin enable events.
     * 
     * @param event The plugin enable event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent event) {
        handlePluginConflict(event.getPlugin());
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
        
        String fullCommand = event.getMessage();
        
        // Check if this command matches any blocked command pattern
        for (Map.Entry<Pattern, String> entry : blockedCommandPatterns.entrySet()) {
            if (entry.getKey().matcher(fullCommand).matches()) {
                event.setCancelled(true);
                
                // Send message to player
                Player player = event.getPlayer();
                player.sendMessage("§c[BDCraft] This command is disabled because BDCraft provides this functionality.");
                player.sendMessage("§e Please use §f" + entry.getValue() + " §einstead. Type §f/bdhelp §efor more information.");
                
                // Log the blocked command
                logger.info("Blocked command " + fullCommand + " from player " + player.getName());
                return;
            }
        }
    }
    
    /**
     * Event handler for server commands.
     * 
     * @param event The server command event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerCommand(ServerCommandEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        String fullCommand = event.getCommand();
        
        // Check if this command matches any blocked command pattern
        for (Map.Entry<Pattern, String> entry : blockedCommandPatterns.entrySet()) {
            if (entry.getKey().matcher(fullCommand).matches()) {
                event.setCancelled(true);
                
                // Send message to console
                event.getSender().sendMessage("§c[BDCraft] This command is disabled because BDCraft provides this functionality.");
                event.getSender().sendMessage("§e Please use §f" + entry.getValue() + " §einstead.");
                
                // Log the blocked command
                logger.info("Blocked console command: " + fullCommand);
                return;
            }
        }
    }
    
    /**
     * An enum for feature types that might conflict.
     */
    public enum FeatureType {
        ECONOMY,
        MARKETS,
        PERMISSIONS,
        TELEPORT,
        HOMES,
        MESSAGING,
        PROGRESSION,
        PLACEHOLDER
    }
    
    /**
     * An enum for disable results.
     */
    public enum DisableResult {
        NO_ACTION,        // No action needed
        NOTIFY_ONLY,      // Just notify the admin about potential conflict
        FEATURE_DISABLED, // The feature was disabled in the other plugin
        PLUGIN_DISABLED   // The entire plugin was disabled
    }
    
    /**
     * Interface for plugin feature handlers.
     */
    @FunctionalInterface
    private interface PluginFeatureHandler {
        /**
         * Handles a potentially conflicting feature.
         * 
         * @param plugin The plugin to handle
         * @return The disable result
         */
        DisableResult handleFeature(Plugin plugin);
    }
    
    /**
     * Class for command blocking.
     */
    private static class CommandBlocker {
        private final String command;
        private final String replacement;
        private final String pluginName;
        
        /**
         * Creates a new command blocker.
         * 
         * @param command The command to block
         * @param replacement The replacement command
         * @param pluginName The name of the plugin that provides the command
         */
        public CommandBlocker(String command, String replacement, String pluginName) {
            this.command = command;
            this.replacement = replacement;
            this.pluginName = pluginName;
        }
        
        /**
         * Gets the command to block.
         * 
         * @return The command
         */
        public String getCommand() {
            return command;
        }
        
        /**
         * Gets the replacement command.
         * 
         * @return The replacement
         */
        public String getReplacement() {
            return replacement;
        }
        
        /**
         * Gets the name of the plugin that provides the command.
         * 
         * @return The plugin name
         */
        public String getPluginName() {
            return pluginName;
        }
    }
    
    /**
     * Class for event listener blocking.
     */
    private static class ListenerBlocker {
        private final Class<?> listenerClass;
        private final String pluginName;
        
        /**
         * Creates a new listener blocker.
         * 
         * @param listenerClass The listener class to block
         * @param pluginName The name of the plugin that provides the listener
         */
        public ListenerBlocker(Class<?> listenerClass, String pluginName) {
            this.listenerClass = listenerClass;
            this.pluginName = pluginName;
        }
        
        /**
         * Gets the listener class to block.
         * 
         * @return The listener class
         */
        public Class<?> getListenerClass() {
            return listenerClass;
        }
        
        /**
         * Gets the name of the plugin that provides the listener.
         * 
         * @return The plugin name
         */
        public String getPluginName() {
            return pluginName;
        }
    }
}