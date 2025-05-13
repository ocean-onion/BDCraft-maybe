package com.bdcraft.plugin.modules.vital.listeners;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.vital.BDVitalModule;
import com.bdcraft.plugin.modules.vital.modules.message.MessageModule;
import com.bdcraft.plugin.modules.vital.modules.teleport.TeleportModule;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

/**
 * Listener for player events.
 */
public class PlayerListener implements Listener {
    private final BDCraft plugin;
    private final BDVitalModule vitalModule;
    private final TeleportModule teleportModule;
    private final MessageModule messageModule;
    
    /**
     * Creates a new player listener.
     * @param plugin The plugin instance
     * @param vitalModule The vital module
     */
    public PlayerListener(BDCraft plugin, BDVitalModule vitalModule) {
        this.plugin = plugin;
        this.vitalModule = vitalModule;
        this.teleportModule = vitalModule.getTeleportModule();
        this.messageModule = vitalModule.getMessageModule();
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Check for mail
        List<MessageModule.Mail> mails = messageModule.getMail(player.getName());
        
        if (!mails.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "You have " + mails.size() + " unread mail messages.");
            player.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GREEN + "/bdmail read" + 
                    ChatColor.YELLOW + " to read them.");
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Clear teleport requests
        teleportModule.removePendingRequest(player.getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // Don't save location for same world teleports within a small distance
        if (event.getFrom().getWorld().equals(event.getTo().getWorld()) && 
                event.getFrom().distance(event.getTo()) < 10) {
            return;
        }
        
        // Save last location
        teleportModule.saveLastLocation(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        // Save death location
        teleportModule.saveDeathLocation(player);
        
        // Inform player
        if (player.hasPermission("bdvital.deathinfo")) {
            player.sendMessage(ChatColor.RED + "You died at: " + 
                    ChatColor.YELLOW + formatLocation(player.getLocation()));
            player.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GREEN + "/bdback" + 
                    ChatColor.YELLOW + " to return to your death point.");
        }
    }
    
    /**
     * Formats a location for display.
     * @param location The location
     * @return The formatted location
     */
    private String formatLocation(org.bukkit.Location location) {
        return location.getWorld().getName() + ", " + 
                location.getBlockX() + ", " + 
                location.getBlockY() + ", " + 
                location.getBlockZ();
    }
}