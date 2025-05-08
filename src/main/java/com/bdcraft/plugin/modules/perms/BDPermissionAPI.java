package com.bdcraft.plugin.modules.perms;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.PermissionAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the PermissionAPI for BD permission operations.
 */
public class BDPermissionAPI implements PermissionAPI {
    private final BDCraft plugin;
    private final BDPermsModule permsModule;
    
    /**
     * Creates a new BD permission API.
     * @param plugin The plugin instance
     * @param permsModule The permissions module
     */
    public BDPermissionAPI(BDCraft plugin, BDPermsModule permsModule) {
        this.plugin = plugin;
        this.permsModule = permsModule;
    }
    
    @Override
    public boolean hasPermission(Player player, String permission) {
        return permsModule.hasPermission(player.getUniqueId(), permission) || player.hasPermission(permission);
    }
    
    @Override
    public String getGroup(Player player) {
        return permsModule.getPlayerGroup(player.getUniqueId());
    }
    
    @Override
    public boolean setGroup(Player player, String group) {
        if (!permsModule.groupExists(group)) {
            return false;
        }
        permsModule.setPlayerGroup(player.getUniqueId(), group);
        return true;
    }
    
    @Override
    public boolean groupExists(String group) {
        return permsModule.groupExists(group);
    }
    
    @Override
    public boolean createGroup(String group, String parentGroup) {
        return permsModule.createGroup(group, parentGroup);
    }
    
    @Override
    public boolean addGroupPermission(String group, String permission) {
        return permsModule.addGroupPermission(group, permission);
    }
    
    @Override
    public boolean removeGroupPermission(String group, String permission) {
        return permsModule.removeGroupPermission(group, permission);
    }
    
    @Override
    public List<String> getGroupPermissions(String group) {
        return permsModule.getGroupPermissions(group);
    }
    
    @Override
    public List<String> getPlayerPermissions(Player player) {
        String groupName = permsModule.getPlayerGroup(player.getUniqueId());
        return permsModule.getGroupPermissions(groupName);
    }
    
    @Override
    public String getGroupPrefix(String group) {
        return permsModule.getGroupPrefix(group);
    }
    
    @Override
    public boolean setGroupPrefix(String group, String prefix) {
        return permsModule.setGroupPrefix(group, prefix);
    }
    
    @Override
    public String getGroupSuffix(String group) {
        return permsModule.getGroupSuffix(group);
    }
    
    @Override
    public boolean setGroupSuffix(String group, String suffix) {
        return permsModule.setGroupSuffix(group, suffix);
    }
}