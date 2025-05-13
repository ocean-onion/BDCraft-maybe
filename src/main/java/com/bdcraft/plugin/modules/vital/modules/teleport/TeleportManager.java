package com.bdcraft.plugin.modules.vital.modules.teleport;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player teleportation.
 */
public class TeleportManager {
    private final BDCraft plugin;
    private final Map<UUID, TeleportRequest> teleportRequests = new HashMap<>();
    private final Map<UUID, Location> lastLocation = new HashMap<>();
    
    /**
     * Creates a new TeleportManager.
     *
     * @param plugin The plugin instance
     */
    public TeleportManager(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Teleports a player to another player.
     *
     * @param player The player to teleport
     * @param target The target player
     * @return true if the teleport was successful, false otherwise
     */
    public boolean teleportToPlayer(Player player, Player target) {
        if (player == null || target == null || !target.isOnline()) {
            return false;
        }
        
        lastLocation.put(player.getUniqueId(), player.getLocation());
        player.teleport(target.getLocation());
        return true;
    }
    
    /**
     * Sends a teleport request from one player to another.
     *
     * @param sender The sender
     * @param recipient The recipient
     * @return true if the request was sent, false otherwise
     */
    public boolean sendTeleportRequest(Player sender, Player recipient) {
        if (sender == null || recipient == null || !recipient.isOnline()) {
            return false;
        }
        
        teleportRequests.put(recipient.getUniqueId(), new TeleportRequest(sender.getUniqueId(), System.currentTimeMillis()));
        return true;
    }
    
    /**
     * Accepts a teleport request.
     *
     * @param player The player accepting the request
     * @return true if the request was accepted and the teleport was successful, false otherwise
     */
    public boolean acceptTeleportRequest(Player player) {
        TeleportRequest request = teleportRequests.get(player.getUniqueId());
        if (request == null || System.currentTimeMillis() - request.getTimestamp() > 120000) {
            // Request expired or doesn't exist
            teleportRequests.remove(player.getUniqueId());
            return false;
        }
        
        Player requester = plugin.getServer().getPlayer(request.getRequesterUUID());
        if (requester == null || !requester.isOnline()) {
            teleportRequests.remove(player.getUniqueId());
            return false;
        }
        
        teleportRequests.remove(player.getUniqueId());
        return teleportToPlayer(requester, player);
    }
    
    /**
     * Denies a teleport request.
     *
     * @param player The player denying the request
     * @return true if the request was denied, false if there was no request
     */
    public boolean denyTeleportRequest(Player player) {
        if (teleportRequests.containsKey(player.getUniqueId())) {
            teleportRequests.remove(player.getUniqueId());
            return true;
        }
        return false;
    }
    
    /**
     * Teleports a player back to their previous location.
     *
     * @param player The player
     * @return true if the teleport was successful, false otherwise
     */
    public boolean teleportBack(Player player) {
        Location loc = lastLocation.get(player.getUniqueId());
        if (loc == null) {
            return false;
        }
        
        player.teleport(loc);
        lastLocation.remove(player.getUniqueId());
        return true;
    }
    
    /**
     * Internal class representing a teleport request.
     */
    private static class TeleportRequest {
        private final UUID requesterUUID;
        private final long timestamp;
        
        /**
         * Creates a new TeleportRequest.
         *
         * @param requesterUUID The UUID of the player making the request
         * @param timestamp The time the request was made
         */
        public TeleportRequest(UUID requesterUUID, long timestamp) {
            this.requesterUUID = requesterUUID;
            this.timestamp = timestamp;
        }
        
        /**
         * Gets the UUID of the player who made the request.
         *
         * @return The requester's UUID
         */
        public UUID getRequesterUUID() {
            return requesterUUID;
        }
        
        /**
         * Gets the time the request was made.
         *
         * @return The timestamp
         */
        public long getTimestamp() {
            return timestamp;
        }
    }
}