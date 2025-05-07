package com.bdcraft.plugin.modules.perms;

import java.util.*;

/**
 * Represents a permission group with permissions, inheritance, and metadata.
 */
public class PermissionGroup {
    private final String name;
    private final String prefix;
    private final String suffix;
    private final List<String> permissions;
    private final Map<String, String> metadata;
    private PermissionGroup parent;
    
    /**
     * Creates a new permission group.
     * 
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
     * 
     * @return The group name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the group prefix.
     * 
     * @return The group prefix
     */
    public String getPrefix() {
        return prefix;
    }
    
    /**
     * Gets the group suffix.
     * 
     * @return The group suffix
     */
    public String getSuffix() {
        return suffix;
    }
    
    /**
     * Gets the parent group.
     * 
     * @return The parent group, or null if none
     */
    public PermissionGroup getParent() {
        return parent;
    }
    
    /**
     * Sets the parent group.
     * 
     * @param parent The parent group
     */
    public void setParent(PermissionGroup parent) {
        this.parent = parent;
    }
    
    /**
     * Gets the group permissions.
     * 
     * @return The permissions
     */
    public List<String> getPermissions() {
        return new ArrayList<>(permissions);
    }
    
    /**
     * Checks if the group has a permission, including inherited permissions.
     * 
     * @param permission The permission to check
     * @return Whether the group has the permission
     */
    public boolean hasPermission(String permission) {
        if (permissions.contains(permission)) {
            return true;
        }
        
        // Check for wildcards
        for (String perm : permissions) {
            if (perm.endsWith(".*")) {
                String base = perm.substring(0, perm.length() - 2);
                if (permission.startsWith(base)) {
                    return true;
                }
            }
        }
        
        // Check parent
        return parent != null && parent.hasPermission(permission);
    }
    
    /**
     * Gets the group metadata.
     * 
     * @return The metadata
     */
    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }
    
    /**
     * Sets a metadata value.
     * 
     * @param key The metadata key
     * @param value The metadata value
     */
    public void setMetadata(String key, String value) {
        metadata.put(key, value);
    }
    
    /**
     * Gets a metadata value.
     * 
     * @param key The metadata key
     * @return The metadata value, or null if not found
     */
    public String getMetadata(String key) {
        if (metadata.containsKey(key)) {
            return metadata.get(key);
        }
        
        // Check parent
        return parent != null ? parent.getMetadata(key) : null;
    }
}