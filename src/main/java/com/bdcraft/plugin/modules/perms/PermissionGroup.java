package com.bdcraft.plugin.modules.perms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a permission group in the BDCraft permission system.
 */
public class PermissionGroup {
    private final String name;
    private final String prefix;
    private final String suffix;
    private final List<String> permissions;
    private PermissionGroup parent;
    private final Map<String, String> metadata;
    
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
     * Gets the group permissions.
     * 
     * @return The group permissions
     */
    public List<String> getPermissions() {
        return new ArrayList<>(permissions);
    }
    
    /**
     * Gets the parent group.
     * 
     * @return The parent group
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
     * Checks if the group has a permission.
     * 
     * @param permission The permission
     * @return Whether the group has the permission
     */
    public boolean hasPermission(String permission) {
        // Check if this group has the permission
        if (permissions.contains(permission)) {
            return true;
        }
        
        // Check for wildcard
        if (permissions.contains("*")) {
            return true;
        }
        
        // Check for node wildcard
        String[] parts = permission.split("\\.");
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < parts.length - 1; i++) {
            builder.append(parts[i]).append(".");
            if (permissions.contains(builder.toString() + "*")) {
                return true;
            }
        }
        
        // Check parent
        return parent != null && parent.hasPermission(permission);
    }
    
    /**
     * Gets the group metadata.
     * 
     * @return The group metadata
     */
    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
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
    
    /**
     * Sets a metadata value.
     * 
     * @param key The metadata key
     * @param value The metadata value
     */
    public void setMetadata(String key, String value) {
        if (value == null) {
            metadata.remove(key);
        } else {
            metadata.put(key, value);
        }
    }
}