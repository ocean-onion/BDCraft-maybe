package com.bdcraft.plugin.modules.permissions;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.BDModule;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Logger;

/**
 * Module that provides permissions management and group handling.
 */
public class BDPermissionsModule implements BDModule {

    private final BDCraft plugin;
    private final Logger logger;
    private FileConfiguration config;
    private Map<UUID, String> playerGroups;
    private Map<String, PermissionGroup> groups;
    
    /**
     * Creates a new permissions module.
     * @param plugin The plugin instance
     */
    public BDPermissionsModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.playerGroups = new HashMap<>();
        this.groups = new HashMap<>();
    }
    
    @Override
    public void onEnable() {
        logger.info("Enabling Permissions Module...");
        
        // Load configuration
        config = plugin.getConfigManager().getModuleConfig("permissions");
        
        // Load groups
        loadGroups();
        
        // Register commands
        registerCommands();
        
        // Register event listeners
        // registerListeners();
        
        logger.info("Permissions Module enabled!");
    }
    
    @Override
    public void onDisable() {
        logger.info("Disabling Permissions Module...");
        
        // Save data
        // saveData();
        
        logger.info("Permissions Module disabled!");
    }
    
    @Override
    public void onReload() {
        logger.info("Reloading Permissions Module...");
        
        // Reload configuration
        config = plugin.getConfigManager().getModuleConfig("permissions");
        
        // Reload groups
        loadGroups();
        
        logger.info("Permissions Module reloaded!");
    }
    
    @Override
    public String getName() {
        return "perms";
    }
    
    @Override
    public List<String> getDependencies() {
        return Collections.emptyList();
    }
    
    /**
     * Loads permission groups from configuration.
     */
    private void loadGroups() {
        groups.clear();
        
        ConfigurationSection groupsSection = config.getConfigurationSection("groups");
        if (groupsSection == null) {
            logger.warning("No groups defined in permissions.yml");
            return;
        }
        
        // First load all group data
        for (String groupName : groupsSection.getKeys(false)) {
            ConfigurationSection groupSection = groupsSection.getConfigurationSection(groupName);
            if (groupSection == null) continue;
            
            String prefix = groupSection.getString("prefix", "");
            String suffix = groupSection.getString("suffix", "");
            List<String> permissions = groupSection.getStringList("permissions");
            
            PermissionGroup group = new PermissionGroup(groupName, prefix, suffix, permissions);
            
            // Load metadata
            ConfigurationSection metaSection = groupSection.getConfigurationSection("meta");
            if (metaSection != null) {
                for (String key : metaSection.getKeys(false)) {
                    group.setMetadata(key, metaSection.getString(key));
                }
            }
            
            groups.put(groupName.toLowerCase(), group);
        }
        
        // Then set up inheritance
        for (String groupName : groupsSection.getKeys(false)) {
            ConfigurationSection groupSection = groupsSection.getConfigurationSection(groupName);
            if (groupSection == null) continue;
            
            String parentName = groupSection.getString("parent");
            if (parentName != null && !parentName.isEmpty()) {
                PermissionGroup group = groups.get(groupName.toLowerCase());
                PermissionGroup parent = groups.get(parentName.toLowerCase());
                
                if (parent != null && group != null) {
                    group.setParent(parent);
                } else {
                    logger.warning("Could not find parent group " + parentName + " for group " + groupName);
                }
            }
        }
    }
    
    /**
     * Registers commands for this module.
     */
    private void registerCommands() {
        new CommandBase(plugin, "bdrank", "bdcraft.rank") {
            {
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "info";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Shows rank information";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "[player]";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.info";
                    }
                    
                    @Override
                    public List<String> getAliases() {
                        return Collections.singletonList("i");
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        if (args.length == 0) {
                            // Show own rank
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                                return true;
                            }
                            
                            Player player = (Player) sender;
                            String groupName = getPlayerGroup(player.getUniqueId());
                            PermissionGroup group = groups.get(groupName.toLowerCase());
                            
                            if (group == null) {
                                sender.sendMessage(ChatColor.RED + "You don't have a rank. Contact an administrator.");
                                return true;
                            }
                            
                            sendGroupInfo(sender, group);
                            return true;
                        } else {
                            // Show other player's rank
                            if (!sender.hasPermission("bdcraft.rank.info.others")) {
                                sender.sendMessage(ChatColor.RED + "You don't have permission to view other players' ranks.");
                                return true;
                            }
                            
                            String playerName = args[0];
                            Player target = plugin.getServer().getPlayer(playerName);
                            
                            if (target == null) {
                                sender.sendMessage(ChatColor.RED + "Player not found.");
                                return true;
                            }
                            
                            String groupName = getPlayerGroup(target.getUniqueId());
                            PermissionGroup group = groups.get(groupName.toLowerCase());
                            
                            if (group == null) {
                                sender.sendMessage(ChatColor.RED + target.getName() + " doesn't have a rank.");
                                return true;
                            }
                            
                            sender.sendMessage(ChatColor.YELLOW + target.getName() + "'s rank information:");
                            sendGroupInfo(sender, group);
                            return true;
                        }
                    }
                });
                
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "set";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Sets a player's rank";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "<player> <rank>";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.set";
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "Usage: /bdrank set <player> <rank>");
                            return true;
                        }
                        
                        String playerName = args[0];
                        String groupName = args[1];
                        
                        Player target = plugin.getServer().getPlayer(playerName);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Player not found.");
                            return true;
                        }
                        
                        PermissionGroup group = groups.get(groupName.toLowerCase());
                        if (group == null) {
                            sender.sendMessage(ChatColor.RED + "Rank not found. Available ranks: " + 
                                    String.join(", ", groups.keySet()));
                            return true;
                        }
                        
                        setPlayerGroup(target.getUniqueId(), groupName);
                        
                        sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s rank to " + group.getName() + ".");
                        target.sendMessage(ChatColor.GREEN + "Your rank has been changed to " + group.getName() + ".");
                        
                        return true;
                    }
                });
                
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "list";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Lists available ranks";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.rank.list";
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        sender.sendMessage(ChatColor.YELLOW + "Available ranks:");
                        
                        for (PermissionGroup group : groups.values()) {
                            sender.sendMessage(ChatColor.YELLOW + "- " + group.getName());
                        }
                        
                        return true;
                    }
                });
            }
            
            @Override
            protected void showHelp(CommandSender sender) {
                sender.sendMessage(ChatColor.GOLD + "=== " + ChatColor.YELLOW + "BDRank Help" + 
                        ChatColor.GOLD + " ===");
                
                sender.sendMessage(ChatColor.YELLOW + "/bdrank info [player]" + ChatColor.GRAY + " - Shows rank information");
                
                if (sender.hasPermission("bdcraft.rank.set")) {
                    sender.sendMessage(ChatColor.YELLOW + "/bdrank set <player> <rank>" + ChatColor.GRAY + " - Sets a player's rank");
                }
                
                sender.sendMessage(ChatColor.YELLOW + "/bdrank list" + ChatColor.GRAY + " - Lists available ranks");
            }
        };
    }
    
    /**
     * Sends information about a permission group to a sender.
     * @param sender The command sender
     * @param group The permission group
     */
    private void sendGroupInfo(CommandSender sender, PermissionGroup group) {
        sender.sendMessage(ChatColor.YELLOW + "Rank: " + ChatColor.WHITE + group.getName());
        
        if (group.getParent() != null) {
            sender.sendMessage(ChatColor.YELLOW + "Parent rank: " + ChatColor.WHITE + group.getParent().getName());
        }
        
        sender.sendMessage(ChatColor.YELLOW + "Prefix: " + ChatColor.translateAlternateColorCodes('&', group.getPrefix()));
        
        if (sender.hasPermission("bdcraft.rank.info.advanced")) {
            sender.sendMessage(ChatColor.YELLOW + "Permissions: " + ChatColor.WHITE + 
                    String.join(", ", group.getPermissions()));
            
            sender.sendMessage(ChatColor.YELLOW + "Metadata:");
            for (Map.Entry<String, String> entry : group.getMetadata().entrySet()) {
                sender.sendMessage(ChatColor.YELLOW + "  " + entry.getKey() + ": " + ChatColor.WHITE + entry.getValue());
            }
        }
    }
    
    /**
     * Gets a player's permission group.
     * @param uuid The player's UUID
     * @return The player's group name
     */
    public String getPlayerGroup(UUID uuid) {
        return playerGroups.getOrDefault(uuid, "default");
    }
    
    /**
     * Sets a player's permission group.
     * @param uuid The player's UUID
     * @param groupName The group name
     */
    public void setPlayerGroup(UUID uuid, String groupName) {
        if (groups.containsKey(groupName.toLowerCase())) {
            playerGroups.put(uuid, groupName.toLowerCase());
        }
    }
    
    /**
     * Checks if a player has a permission.
     * @param uuid The player's UUID
     * @param permission The permission node
     * @return Whether the player has the permission
     */
    public boolean hasPermission(UUID uuid, String permission) {
        String groupName = getPlayerGroup(uuid);
        PermissionGroup group = groups.get(groupName.toLowerCase());
        
        if (group == null) {
            return false;
        }
        
        return group.hasPermission(permission);
    }
    
    /**
     * Gets a player's metadata value.
     * @param uuid The player's UUID
     * @param key The metadata key
     * @return The metadata value, or null if not found
     */
    public String getPlayerMetadata(UUID uuid, String key) {
        String groupName = getPlayerGroup(uuid);
        PermissionGroup group = groups.get(groupName.toLowerCase());
        
        if (group == null) {
            return null;
        }
        
        return group.getMetadata(key);
    }
    
    /**
     * Gets a player's metadata value as an integer.
     * @param uuid The player's UUID
     * @param key The metadata key
     * @param defaultValue The default value if the metadata is not found or not a number
     * @return The metadata value as an integer
     */
    public int getPlayerMetadataInt(UUID uuid, String key, int defaultValue) {
        String value = getPlayerMetadata(uuid, key);
        
        if (value == null) {
            return defaultValue;
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Gets a player's chat prefix.
     * @param uuid The player's UUID
     * @return The player's chat prefix
     */
    public String getPlayerPrefix(UUID uuid) {
        String groupName = getPlayerGroup(uuid);
        PermissionGroup group = groups.get(groupName.toLowerCase());
        
        if (group == null) {
            return "";
        }
        
        return group.getPrefix();
    }
    
    /**
     * Gets a player's chat suffix.
     * @param uuid The player's UUID
     * @return The player's chat suffix
     */
    public String getPlayerSuffix(UUID uuid) {
        String groupName = getPlayerGroup(uuid);
        PermissionGroup group = groups.get(groupName.toLowerCase());
        
        if (group == null) {
            return "";
        }
        
        return group.getSuffix();
    }
    
    /**
     * A class representing a permission group.
     */
    public static class PermissionGroup {
        private final String name;
        private final String prefix;
        private final String suffix;
        private final List<String> permissions;
        private final Map<String, String> metadata;
        private PermissionGroup parent;
        
        /**
         * Creates a new permission group.
         * @param name The group name
         * @param prefix The group prefix
         * @param suffix The group suffix
         * @param permissions The group permissions
         */
        public PermissionGroup(String name, String prefix, String suffix, List<String> permissions) {
            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;
            this.permissions = new ArrayList<>(permissions);
            this.metadata = new HashMap<>();
        }
        
        /**
         * Gets the group name.
         * @return The group name
         */
        public String getName() {
            return name;
        }
        
        /**
         * Gets the group prefix.
         * @return The group prefix
         */
        public String getPrefix() {
            return prefix;
        }
        
        /**
         * Gets the group suffix.
         * @return The group suffix
         */
        public String getSuffix() {
            return suffix;
        }
        
        /**
         * Gets the group permissions.
         * @return The group permissions
         */
        public List<String> getPermissions() {
            return Collections.unmodifiableList(permissions);
        }
        
        /**
         * Gets the group parent.
         * @return The group parent, or null if none
         */
        public PermissionGroup getParent() {
            return parent;
        }
        
        /**
         * Sets the group parent.
         * @param parent The group parent
         */
        public void setParent(PermissionGroup parent) {
            this.parent = parent;
        }
        
        /**
         * Sets a metadata value.
         * @param key The metadata key
         * @param value The metadata value
         */
        public void setMetadata(String key, String value) {
            metadata.put(key, value);
        }
        
        /**
         * Gets a metadata value.
         * @param key The metadata key
         * @return The metadata value, or null if not found
         */
        public String getMetadata(String key) {
            String value = metadata.get(key);
            
            if (value == null && parent != null) {
                value = parent.getMetadata(key);
            }
            
            return value;
        }
        
        /**
         * Gets all metadata.
         * @return An unmodifiable view of the metadata
         */
        public Map<String, String> getMetadata() {
            return Collections.unmodifiableMap(metadata);
        }
        
        /**
         * Checks if this group has a permission.
         * @param permission The permission node
         * @return Whether the group has the permission
         */
        public boolean hasPermission(String permission) {
            // Check this group's permissions
            for (String perm : permissions) {
                if (perm.equals(permission) || perm.equals("*")) {
                    return true;
                }
                
                if (perm.endsWith(".*")) {
                    String basePermission = perm.substring(0, perm.length() - 2);
                    if (permission.startsWith(basePermission)) {
                        return true;
                    }
                }
            }
            
            // Check parent permissions
            return parent != null && parent.hasPermission(permission);
        }
    }
}