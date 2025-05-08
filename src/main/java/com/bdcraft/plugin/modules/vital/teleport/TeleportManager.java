package com.bdcraft.plugin.modules.vital.teleport;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages teleportation-related functionality.
 */
public class TeleportManager {
    private final BDCraft plugin;
    
    // Maps player UUIDs to their last locations
    private final Map<UUID, Location> lastLocations;
    
    // Maps player UUIDs to their death locations
    private final Map<UUID, Location> deathLocations;
    
    // Maps target UUIDs to pending teleport requests
    private final Map<UUID, TeleportRequest> pendingRequests;
    
    // Teleport request timeout (in seconds)
    private static final int REQUEST_TIMEOUT = 60;
    
    /**
     * Creates a new teleport manager.
     * @param plugin The plugin instance
     */
    public TeleportManager(BDCraft plugin) {
        this.plugin = plugin;
        this.lastLocations = new HashMap<>();
        this.deathLocations = new HashMap<>();
        this.pendingRequests = new HashMap<>();
    }
    
    /**
     * Saves a player's last location.
     * @param player The player
     */
    public void saveLastLocation(Player player) {
        lastLocations.put(player.getUniqueId(), player.getLocation());
    }
    
    /**
     * Gets a player's last location.
     * @param playerUuid The player UUID
     * @return The last location, or null if not found
     */
    public Location getLastLocation(UUID playerUuid) {
        return lastLocations.get(playerUuid);
    }
    
    /**
     * Saves a player's death location.
     * @param player The player
     */
    public void saveDeathLocation(Player player) {
        deathLocations.put(player.getUniqueId(), player.getLocation());
    }
    
    /**
     * Gets a player's death location.
     * @param playerUuid The player UUID
     * @return The death location, or null if not found
     */
    public Location getDeathLocation(UUID playerUuid) {
        return deathLocations.get(playerUuid);
    }
    
    /**
     * Sends a teleport request.
     * @param requester The requester
     * @param target The target
     * @param type The teleport type
     */
    public void sendTeleportRequest(Player requester, Player target, TeleportType type) {
        // Remove any existing request
        pendingRequests.remove(target.getUniqueId());
        
        // Create new request
        TeleportRequest request = new TeleportRequest(requester.getUniqueId(), type);
        pendingRequests.put(target.getUniqueId(), request);
        
        // Schedule timeout
        new BukkitRunnable() {
            @Override
            public void run() {
                TeleportRequest currentRequest = pendingRequests.get(target.getUniqueId());
                
                if (currentRequest != null && currentRequest.equals(request) && !currentRequest.isExpired()) {
                    pendingRequests.remove(target.getUniqueId());
                    
                    Player requesterPlayer = plugin.getServer().getPlayer(request.getRequesterUuid());
                    
                    if (requesterPlayer != null) {
                        requesterPlayer.sendMessage(ChatColor.RED + "Your teleport request to " + 
                                target.getName() + " has expired.");
                    }
                    
                    if (target.isOnline()) {
                        target.sendMessage(ChatColor.RED + "The teleport request from " + 
                                (requesterPlayer != null ? requesterPlayer.getName() : "someone") + 
                                " has expired.");
                    }
                }
            }
        }.runTaskLater(plugin, REQUEST_TIMEOUT * 20L);
    }
    
    /**
     * Gets a pending teleport request.
     * @param targetUuid The target UUID
     * @return The teleport request, or null if not found
     */
    public TeleportRequest getPendingRequest(UUID targetUuid) {
        TeleportRequest request = pendingRequests.get(targetUuid);
        
        if (request != null && request.isExpired()) {
            pendingRequests.remove(targetUuid);
            return null;
        }
        
        return request;
    }
    
    /**
     * Removes a pending teleport request.
     * @param targetUuid The target UUID
     */
    public void removePendingRequest(UUID targetUuid) {
        pendingRequests.remove(targetUuid);
    }
    
    /**
     * Accepts a teleport request.
     * @param target The target
     * @param requester The requester
     * @param type The teleport type
     */
    public void acceptRequest(Player target, Player requester, TeleportType type) {
        // Remove request
        pendingRequests.remove(target.getUniqueId());
        
        // Save last locations
        saveLastLocation(target);
        saveLastLocation(requester);
        
        // Perform teleport
        if (type == TeleportType.TO_TARGET) {
            requester.teleport(target.getLocation());
            requester.sendMessage(ChatColor.GREEN + "Teleported to " + target.getName() + ".");
            target.sendMessage(ChatColor.GREEN + requester.getName() + " teleported to you.");
        } else {
            target.teleport(requester.getLocation());
            target.sendMessage(ChatColor.GREEN + "Teleported to " + requester.getName() + ".");
            requester.sendMessage(ChatColor.GREEN + target.getName() + " teleported to you.");
        }
    }
    
    /**
     * Clears all teleport data for a player.
     * @param playerUuid The player UUID
     */
    public void clearPlayerData(UUID playerUuid) {
        lastLocations.remove(playerUuid);
        deathLocations.remove(playerUuid);
        pendingRequests.remove(playerUuid);
    }
    
    /**
     * Teleport request class.
     */
    public static class TeleportRequest {
        private final UUID requesterUuid;
        private final TeleportType type;
        private final long timestamp;
        
        /**
         * Creates a new teleport request.
         * @param requesterUuid The requester UUID
         * @param type The teleport type
         */
        public TeleportRequest(UUID requesterUuid, TeleportType type) {
            this.requesterUuid = requesterUuid;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
        
        /**
         * Gets the requester UUID.
         * @return The requester UUID
         */
        public UUID getRequesterUuid() {
            return requesterUuid;
        }
        
        /**
         * Gets the teleport type.
         * @return The teleport type
         */
        public TeleportType getType() {
            return type;
        }
        
        /**
         * Checks if the request is expired.
         * @return Whether the request is expired
         */
        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > REQUEST_TIMEOUT * 1000L;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            
            TeleportRequest that = (TeleportRequest) obj;
            
            return requesterUuid.equals(that.requesterUuid) && type == that.type;
        }
        
        @Override
        public int hashCode() {
            int result = requesterUuid.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }
    }
    
    /**
     * Teleport type enum.
     */
    public enum TeleportType {
        TO_TARGET,
        TO_REQUESTER
    }
}