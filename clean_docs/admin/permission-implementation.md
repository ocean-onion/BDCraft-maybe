# Permission System Implementation Guide

This guide covers the technical implementation details for the BDCraft permission management system, including Java code structure, database schema, and integration patterns.

## System Architecture

### Core Components

#### PermissionManager Class
```java
public class PermissionManager {
    // Core permission checking and management
    public boolean hasPermission(Player player, String permission);
    public void setPermission(Player player, String permission, boolean value);
    public void addPlayerToGroup(Player player, String group);
    public void removePlayerFromGroup(Player player, String group);
}
```

#### Group Management
```java
public class PermissionGroup {
    private String name;
    private String displayName;
    private String prefix;
    private String suffix;
    private int weight;
    private List<String> permissions;
    private List<String> inheritance;
    private boolean isDefault;
}
```

#### GUI Components
```java
public class PermissionGUI {
    public void openMainPermissionGUI(Player admin);
    public void openPlayerPermissionGUI(Player admin, String targetPlayer);
    public void openGroupPermissionGUI(Player admin, String groupName);
    public void openPermissionCategoryGUI(Player admin, PermissionCategory category);
}
```

### Database Schema

#### Groups Table
```sql
CREATE TABLE bdcraft_permission_groups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) UNIQUE NOT NULL,
    display_name VARCHAR(128),
    prefix VARCHAR(64),
    suffix VARCHAR(64),
    weight INT DEFAULT 0,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### Group Permissions Table
```sql
CREATE TABLE bdcraft_group_permissions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    group_name VARCHAR(64) NOT NULL,
    permission VARCHAR(255) NOT NULL,
    value BOOLEAN DEFAULT TRUE,
    world VARCHAR(64),
    expires_at TIMESTAMP NULL,
    FOREIGN KEY (group_name) REFERENCES bdcraft_permission_groups(name) ON DELETE CASCADE,
    UNIQUE KEY unique_group_permission (group_name, permission, world)
);
```

#### Group Inheritance Table
```sql
CREATE TABLE bdcraft_group_inheritance (
    id INT PRIMARY KEY AUTO_INCREMENT,
    child_group VARCHAR(64) NOT NULL,
    parent_group VARCHAR(64) NOT NULL,
    FOREIGN KEY (child_group) REFERENCES bdcraft_permission_groups(name) ON DELETE CASCADE,
    FOREIGN KEY (parent_group) REFERENCES bdcraft_permission_groups(name) ON DELETE CASCADE,
    UNIQUE KEY unique_inheritance (child_group, parent_group)
);
```

#### Player Groups Table
```sql
CREATE TABLE bdcraft_player_groups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    player_uuid VARCHAR(36) NOT NULL,
    group_name VARCHAR(64) NOT NULL,
    world VARCHAR(64),
    expires_at TIMESTAMP NULL,
    assigned_by VARCHAR(36),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_name) REFERENCES bdcraft_permission_groups(name) ON DELETE CASCADE,
    UNIQUE KEY unique_player_group (player_uuid, group_name, world)
);
```

#### Player Permissions Table
```sql
CREATE TABLE bdcraft_player_permissions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    player_uuid VARCHAR(36) NOT NULL,
    permission VARCHAR(255) NOT NULL,
    value BOOLEAN DEFAULT TRUE,
    world VARCHAR(64),
    expires_at TIMESTAMP NULL,
    assigned_by VARCHAR(36),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_player_permission (player_uuid, permission, world)
);
```

#### Permission Audit Table
```sql
CREATE TABLE bdcraft_permission_audit (
    id INT PRIMARY KEY AUTO_INCREMENT,
    action_type ENUM('GRANT', 'REVOKE', 'GROUP_ADD', 'GROUP_REMOVE', 'GROUP_CREATE', 'GROUP_DELETE'),
    target_type ENUM('PLAYER', 'GROUP'),
    target_identifier VARCHAR(255) NOT NULL,
    permission VARCHAR(255),
    value BOOLEAN,
    performed_by VARCHAR(36) NOT NULL,
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    additional_data JSON
);
```

## Configuration Structure

### Main Configuration (permissions.yml)
```yaml
# Permission System Configuration
permissions:
  # System settings
  system:
    storage_type: "yaml"  # yaml, mysql, sqlite
    auto_save: true
    save_interval: 300
    backup_enabled: true
    backup_interval: 3600
    max_backups: 24
    
    # Performance settings
    cache_enabled: true
    cache_size: 1000
    cache_expiry: 300
    
    # Wildcard settings
    wildcard_enabled: true
    negative_permissions: true
    case_sensitive: false
    
    # World settings
    world_specific: false
    sync_across_worlds: true
    
  # Default permissions for new players
  default_permissions:
    - "bdcraft.economy.use"
    - "bdcraft.chat.global"
    - "bdcraft.spawn.use"
    
  # Permission aliases for easier management
  aliases:
    basic_player: "bdcraft.economy.use,bdcraft.chat.global,bdcraft.spawn.use"
    market_user: "bdcraft.market.use,bdcraft.auction.use"
    admin_all: "bdcraft.*"
    
  # Groups configuration
  groups: {}  # Defined in separate section
  
  # Player overrides
  players: {}  # Defined in separate section
```

### GUI Configuration (gui-permissions.yml)
```yaml
# Permission GUI Configuration
gui:
  # Main GUI settings
  main:
    title: "&6&lPermission Manager"
    size: 54
    update_interval: 20  # ticks
    
  # Player list GUI
  player_list:
    title: "&6Players - Page {page}"
    size: 54
    players_per_page: 45
    sort_by: "name"  # name, group, last_seen
    
  # Group list GUI
  group_list:
    title: "&6Permission Groups"
    size: 54
    
  # Permission category GUI
  categories:
    title: "&6Permissions - {category}"
    size: 54
    
  # Items configuration
  items:
    player_head:
      material: "PLAYER_HEAD"
      name: "&f{player_name}"
      lore:
        - "&7Groups: &e{groups}"
        - "&7Permissions: &e{permission_count}"
        - "&7Last Seen: &e{last_seen}"
        - ""
        - "&aClick to edit permissions"
        
    group_item:
      material: "PAPER"
      name: "&e{group_name}"
      lore:
        - "&7Display: &f{display_name}"
        - "&7Weight: &e{weight}"
        - "&7Members: &e{member_count}"
        - "&7Permissions: &e{permission_count}"
        - ""
        - "&aLeft click to edit"
        - "&cRight click to delete"
        
    permission_granted:
      material: "LIME_CONCRETE"
      name: "&a✓ {permission}"
      lore:
        - "&7Status: &aGranted"
        - "&7Source: &e{source}"
        - ""
        - "&cClick to revoke"
        
    permission_denied:
      material: "RED_CONCRETE"
      name: "&c✗ {permission}"
      lore:
        - "&7Status: &cDenied"
        - "&7Source: &e{source}"
        - ""
        - "&aClick to grant"
        
    permission_inherited:
      material: "YELLOW_CONCRETE"
      name: "&e○ {permission}"
      lore:
        - "&7Status: &eInherited"
        - "&7Source: &e{source}"
        - ""
        - "&aClick to override"
        
    # Navigation items
    next_page:
      material: "ARROW"
      name: "&aNext Page"
      slot: 53
      
    previous_page:
      material: "ARROW"
      name: "&aPrevious Page"
      slot: 45
      
    back_button:
      material: "BARRIER"
      name: "&cBack"
      slot: 49
```

## Command Implementation

### Command Registration
```java
// Register permission commands in main plugin class
@Override
public void onEnable() {
    // Register commands
    getCommand("bdpermissions").setExecutor(new PermissionCommand());
    getCommand("bdgroup").setExecutor(new GroupCommand());
    getCommand("bdperm").setExecutor(new PermissionCommand());
    
    // Register tab completers
    getCommand("bdpermissions").setTabCompleter(new PermissionTabCompleter());
    getCommand("bdgroup").setTabCompleter(new GroupTabCompleter());
}
```

### Command Classes
```java
public class PermissionCommand implements CommandExecutor, TabCompleter {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bdcraft.permissions.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }
        
        if (args.length == 0) {
            if (sender instanceof Player) {
                permissionGUI.openMainPermissionGUI((Player) sender);
            } else {
                sendHelpMessage(sender);
            }
            return true;
        }
        
        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "set":
                return handleSetCommand(sender, args);
            case "unset":
                return handleUnsetCommand(sender, args);
            case "check":
                return handleCheckCommand(sender, args);
            case "list":
                return handleListCommand(sender, args);
            // ... other subcommands
        }
        
        return false;
    }
    
    private boolean handleSetCommand(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage("Usage: /bdperm set <player> <permission> <true/false>");
            return true;
        }
        
        String playerName = args[1];
        String permission = args[2];
        boolean value = Boolean.parseBoolean(args[3]);
        
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("Player not found: " + playerName);
            return true;
        }
        
        permissionManager.setPlayerPermission(target, permission, value);
        sender.sendMessage(String.format("Set permission %s to %s for %s", 
            permission, value, target.getName()));
        
        // Log the action
        auditLogger.logPermissionChange(sender, target, permission, value);
        
        return true;
    }
}
```

## Integration with Existing Systems

### Economy Module Integration
```java
public class EconomyPermissionIntegration {
    
    public void setupEconomyPermissions() {
        // Register economy-specific permission checks
        permissionManager.registerPermissionCheck("bdcraft.economy.use", this::checkEconomyAccess);
        permissionManager.registerPermissionCheck("bdcraft.market.create", this::checkMarketCreation);
        permissionManager.registerPermissionCheck("bdcraft.auction.featured", this::checkFeaturedAuctions);
    }
    
    private boolean checkEconomyAccess(Player player) {
        // Additional logic for economy access
        return player.hasPermission("bdcraft.economy.use") && 
               !economyModule.isPlayerBanned(player);
    }
    
    private boolean checkMarketCreation(Player player) {
        return player.hasPermission("bdcraft.market.create") && 
               marketModule.getPlayerMarketCount(player) < marketModule.getMaxMarkets(player);
    }
}
```

### Rank System Integration
```java
public class RankPermissionIntegration {
    
    public void setupRankBasedPermissions() {
        // Automatically update permissions when rank changes
        rankModule.registerRankChangeListener(this::onRankChange);
    }
    
    private void onRankChange(Player player, Rank oldRank, Rank newRank) {
        // Remove old rank permissions
        if (oldRank != null) {
            permissionManager.removePlayerFromGroup(player, "rank_" + oldRank.getName());
        }
        
        // Add new rank permissions
        permissionManager.addPlayerToGroup(player, "rank_" + newRank.getName());
        
        // Grant rank-specific permissions
        List<String> rankPermissions = getRankPermissions(newRank);
        for (String permission : rankPermissions) {
            permissionManager.setPlayerPermission(player, permission, true);
        }
    }
}
```

## Performance Optimization

### Permission Caching
```java
public class PermissionCache {
    private final Cache<String, Boolean> permissionCache;
    private final Cache<String, Set<String>> groupCache;
    
    public PermissionCache() {
        this.permissionCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
            
        this.groupCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    }
    
    public boolean checkPermission(UUID playerUuid, String permission) {
        String cacheKey = playerUuid + ":" + permission;
        Boolean cached = permissionCache.getIfPresent(cacheKey);
        
        if (cached != null) {
            return cached;
        }
        
        // Calculate permission and cache result
        boolean result = calculatePermission(playerUuid, permission);
        permissionCache.put(cacheKey, result);
        return result;
    }
    
    public void invalidatePlayer(UUID playerUuid) {
        // Remove all cached permissions for this player
        permissionCache.asMap().keySet().removeIf(key -> key.startsWith(playerUuid.toString()));
    }
}
```

### Async Permission Loading
```java
public class AsyncPermissionLoader {
    
    public CompletableFuture<Set<String>> loadPlayerPermissions(UUID playerUuid) {
        return CompletableFuture.supplyAsync(() -> {
            // Load from database asynchronously
            return databaseManager.getPlayerPermissions(playerUuid);
        }, asyncExecutor);
    }
    
    public CompletableFuture<Void> savePlayerPermissions(UUID playerUuid, Set<String> permissions) {
        return CompletableFuture.runAsync(() -> {
            databaseManager.savePlayerPermissions(playerUuid, permissions);
        }, asyncExecutor);
    }
}
```

## Security Features

### Permission Validation
```java
public class PermissionValidator {
    
    public boolean isValidPermission(String permission) {
        // Check permission format
        if (!permission.matches("^[a-z0-9._-]+$")) {
            return false;
        }
        
        // Check against known permissions
        return knownPermissions.contains(permission) || 
               permission.startsWith("bdcraft.");
    }
    
    public boolean canGrantPermission(CommandSender granter, String permission) {
        // Check if granter has permission to grant this permission
        if (granter.hasPermission("bdcraft.permissions.admin")) {
            return true;
        }
        
        // Check specific grant permissions
        return granter.hasPermission("bdcraft.permissions.grant." + permission);
    }
}
```

### Audit Logging
```java
public class PermissionAuditLogger {
    
    public void logPermissionChange(CommandSender performer, Player target, 
                                  String permission, boolean value) {
        AuditEntry entry = new AuditEntry()
            .setAction(value ? AuditAction.GRANT : AuditAction.REVOKE)
            .setPerformer(performer instanceof Player ? 
                ((Player) performer).getUniqueId() : null)
            .setTarget(target.getUniqueId())
            .setPermission(permission)
            .setValue(value)
            .setTimestamp(System.currentTimeMillis());
            
        auditDatabase.saveEntry(entry);
        
        // Send notification to online admins
        notifyAdmins(String.format("%s %s permission %s for %s",
            performer.getName(),
            value ? "granted" : "revoked",
            permission,
            target.getName()));
    }
}
```

## Migration and Import Tools

### Import from Other Plugins
```java
public class PermissionImporter {
    
    public void importFromLuckPerms() {
        // Import groups and permissions from LuckPerms
        File luckPermsData = new File("plugins/LuckPerms/data");
        if (!luckPermsData.exists()) {
            throw new IllegalStateException("LuckPerms data not found");
        }
        
        // Process LuckPerms JSON files
        importLuckPermsGroups(luckPermsData);
        importLuckPermsUsers(luckPermsData);
    }
    
    public void importFromGroupManager() {
        // Import from GroupManager YAML files
        File gmData = new File("plugins/GroupManager/worlds");
        if (!gmData.exists()) {
            throw new IllegalStateException("GroupManager data not found");
        }
        
        importGroupManagerData(gmData);
    }
}
```

## API for Plugin Developers

### Permission API
```java
public interface BDCraftPermissionAPI {
    
    // Check permissions
    boolean hasPermission(Player player, String permission);
    boolean hasPermission(UUID playerUuid, String permission);
    
    // Group management
    void addPlayerToGroup(Player player, String group);
    void removePlayerFromGroup(Player player, String group);
    Set<String> getPlayerGroups(Player player);
    
    // Permission management
    void setPlayerPermission(Player player, String permission, boolean value);
    void unsetPlayerPermission(Player player, String permission);
    Map<String, Boolean> getPlayerPermissions(Player player);
    
    // Group creation and management
    void createGroup(String name, String displayName, int weight);
    void deleteGroup(String name);
    void setGroupPermission(String group, String permission, boolean value);
    
    // Events
    void registerPermissionChangeListener(PermissionChangeListener listener);
}
```

This implementation guide provides the technical foundation for creating a comprehensive permission management system within BDCraft, ensuring seamless integration with existing modules while maintaining high performance and security standards.