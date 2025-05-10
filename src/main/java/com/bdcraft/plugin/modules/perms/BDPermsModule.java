package com.bdcraft.plugin.modules.perms;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.PermissionAPI;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.perms.BDPermissionAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Logger;

/**
 * Module that manages permissions, groups, and chat formatting.
 */
public class BDPermsModule extends BDModule {
    private final ModuleManager moduleManager;
    private final Logger logger;
    private ConfigurationSection config;
    private Map<UUID, String> playerGroups;
    private Map<String, PermissionGroup> groups;
    
    /**
     * Creates a new permissions module.
     * @param plugin The plugin instance
     * @param moduleManager The module manager
     */
    public BDPermsModule(BDCraft plugin, ModuleManager moduleManager) {
        super(plugin, "perms");
        this.moduleManager = moduleManager;
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
        
        // Initialize permission API - using our implementation class
        plugin.setPermissionAPI(new BDPermissionAPI(plugin, this));
        
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
    public void reload() {
        logger.info("Reloading Permissions Module...");
        
        // Reload configuration
        config = plugin.getConfigManager().getModuleConfig("permissions");
        
        // Reload groups
        loadGroups();
        
        logger.info("Permissions Module reloaded!");
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
     * Checks if a player has a permission.
     * 
     * @param playerUuid The player UUID
     * @param permission The permission
     * @return Whether the player has the permission
     */
    public boolean hasPermission(UUID playerUuid, String permission) {
        String groupName = getGroupForPlayer(playerUuid);
        PermissionGroup group = groups.get(groupName.toLowerCase());
        
        if (group == null) {
            return false;
        }
        
        return group.hasPermission(permission);
    }
    
    /**
     * Gets a player's group.
     * 
     * @param playerUuid The player UUID
     * @return The group name, or "default" if none
     */
    private String getGroupForPlayer(UUID playerUuid) {
        return playerGroups.getOrDefault(playerUuid, "default");
    }
    
    /**
     * Sets a player's group.
     * 
     * @param playerUuid The player UUID
     * @param group The group name
     */
    public void setPlayerGroup(UUID playerUuid, String group) {
        playerGroups.put(playerUuid, group);
    }
    
    /**
     * Checks if a group exists.
     * 
     * @param group The group name
     * @return Whether the group exists
     */
    public boolean groupExists(String group) {
        return groups.containsKey(group.toLowerCase());
    }
    
    /**
     * Creates a new group.
     * 
     * @param group The group name
     * @param parentGroup The parent group (can be null)
     * @return Whether the operation was successful
     */
    public boolean createGroup(String group, String parentGroup) {
        if (groups.containsKey(group.toLowerCase())) {
            return false;
        }
        
        PermissionGroup newGroup = new PermissionGroup(group, "", "", new ArrayList<>());
        
        if (parentGroup != null && !parentGroup.isEmpty()) {
            PermissionGroup parent = groups.get(parentGroup.toLowerCase());
            if (parent != null) {
                newGroup.setParent(parent);
            }
        }
        
        groups.put(group.toLowerCase(), newGroup);
        return true;
    }
    
    /**
     * Adds a permission to a group.
     * 
     * @param group The group name
     * @param permission The permission
     * @return Whether the operation was successful
     */
    public boolean addGroupPermission(String group, String permission) {
        PermissionGroup permGroup = groups.get(group.toLowerCase());
        if (permGroup == null) {
            return false;
        }
        
        List<String> permissions = new ArrayList<>(permGroup.getPermissions());
        if (!permissions.contains(permission)) {
            permissions.add(permission);
            groups.put(group.toLowerCase(), new PermissionGroup(
                    permGroup.getName(),
                    permGroup.getPrefix(),
                    permGroup.getSuffix(),
                    permissions
            ));
            
            PermissionGroup newGroup = groups.get(group.toLowerCase());
            newGroup.setParent(permGroup.getParent());
            
            // Copy metadata
            for (Map.Entry<String, String> entry : permGroup.getMetadata().entrySet()) {
                newGroup.setMetadata(entry.getKey(), entry.getValue());
            }
        }
        return true;
    }
    
    /**
     * Removes a permission from a group.
     * 
     * @param group The group name
     * @param permission The permission
     * @return Whether the operation was successful
     */
    public boolean removeGroupPermission(String group, String permission) {
        PermissionGroup permGroup = groups.get(group.toLowerCase());
        if (permGroup == null) {
            return false;
        }
        
        List<String> permissions = new ArrayList<>(permGroup.getPermissions());
        if (permissions.contains(permission)) {
            permissions.remove(permission);
            groups.put(group.toLowerCase(), new PermissionGroup(
                    permGroup.getName(),
                    permGroup.getPrefix(),
                    permGroup.getSuffix(),
                    permissions
            ));
            
            PermissionGroup newGroup = groups.get(group.toLowerCase());
            newGroup.setParent(permGroup.getParent());
            
            // Copy metadata
            for (Map.Entry<String, String> entry : permGroup.getMetadata().entrySet()) {
                newGroup.setMetadata(entry.getKey(), entry.getValue());
            }
        }
        return true;
    }
    
    /**
     * Gets a group's permissions.
     * 
     * @param group The group name
     * @return The permissions
     */
    public List<String> getGroupPermissions(String group) {
        PermissionGroup permGroup = groups.get(group.toLowerCase());
        if (permGroup == null) {
            return new ArrayList<>();
        }
        return permGroup.getPermissions();
    }
    
    /**
     * Gets a group's prefix.
     * 
     * @param group The group name
     * @return The prefix
     */
    public String getGroupPrefix(String group) {
        PermissionGroup permGroup = groups.get(group.toLowerCase());
        if (permGroup == null) {
            return "";
        }
        return permGroup.getPrefix();
    }
    
    /**
     * Sets a group's prefix.
     * 
     * @param group The group name
     * @param prefix The prefix
     * @return Whether the operation was successful
     */
    public boolean setGroupPrefix(String group, String prefix) {
        PermissionGroup permGroup = groups.get(group.toLowerCase());
        if (permGroup == null) {
            return false;
        }
        
        groups.put(group.toLowerCase(), new PermissionGroup(
                permGroup.getName(),
                prefix,
                permGroup.getSuffix(),
                permGroup.getPermissions()
        ));
        
        PermissionGroup newGroup = groups.get(group.toLowerCase());
        newGroup.setParent(permGroup.getParent());
        
        // Copy metadata
        for (Map.Entry<String, String> entry : permGroup.getMetadata().entrySet()) {
            newGroup.setMetadata(entry.getKey(), entry.getValue());
        }
        
        return true;
    }
    
    /**
     * Gets a group's suffix.
     * 
     * @param group The group name
     * @return The suffix
     */
    public String getGroupSuffix(String group) {
        PermissionGroup permGroup = groups.get(group.toLowerCase());
        if (permGroup == null) {
            return "";
        }
        return permGroup.getSuffix();
    }
    
    /**
     * Sets a group's suffix.
     * 
     * @param group The group name
     * @param suffix The suffix
     * @return Whether the operation was successful
     */
    public boolean setGroupSuffix(String group, String suffix) {
        PermissionGroup permGroup = groups.get(group.toLowerCase());
        if (permGroup == null) {
            return false;
        }
        
        groups.put(group.toLowerCase(), new PermissionGroup(
                permGroup.getName(),
                permGroup.getPrefix(),
                suffix,
                permGroup.getPermissions()
        ));
        
        PermissionGroup newGroup = groups.get(group.toLowerCase());
        newGroup.setParent(permGroup.getParent());
        
        // Copy metadata
        for (Map.Entry<String, String> entry : permGroup.getMetadata().entrySet()) {
            newGroup.setMetadata(entry.getKey(), entry.getValue());
        }
        
        return true;
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
     * Gets a player's metadata value.
     * @param uuid The player's UUID
     * @param key The metadata key
     * @return The metadata value, or null if not found
     */
    public String getPlayerMetadata(UUID uuid, String key) {
        String groupName = getGroupForPlayer(uuid);
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
}