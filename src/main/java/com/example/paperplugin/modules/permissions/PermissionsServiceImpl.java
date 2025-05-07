package com.example.paperplugin.modules.permissions;

import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

/**
 * Implementation of the PermissionsService interface.
 * Provides the permissions service functionality.
 */
public class PermissionsServiceImpl implements PermissionsService {

    private final PermissionsModule permissionsModule;
    
    /**
     * Creates a new PermissionsServiceImpl.
     *
     * @param permissionsModule The permissions module
     */
    public PermissionsServiceImpl(PermissionsModule permissionsModule) {
        this.permissionsModule = permissionsModule;
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        return hasPermission(player.getUniqueId(), permission);
    }

    @Override
    public boolean hasPermission(UUID playerId, String permission) {
        return permissionsModule.hasPermission(playerId, permission);
    }

    @Override
    public String getGroup(Player player) {
        return getGroup(player.getUniqueId());
    }

    @Override
    public String getGroup(UUID playerId) {
        return permissionsModule.getPlayerGroup(playerId);
    }

    @Override
    public boolean setGroup(Player player, String group) {
        return setGroup(player.getUniqueId(), group);
    }

    @Override
    public boolean setGroup(UUID playerId, String group) {
        // Check if the group exists
        if (!permissionsModule.getGroups().contains(group)) {
            return false;
        }
        
        permissionsModule.setPlayerGroup(playerId, group);
        return true;
    }

    @Override
    public Set<String> getGroups() {
        return permissionsModule.getGroups();
    }

    @Override
    public Set<String> getGroupPermissions(String group) {
        return permissionsModule.getGroupPermissions(group);
    }
}