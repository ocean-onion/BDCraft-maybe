package com.bdcraft.plugin.modules.vital.modules.message;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player-to-player messaging.
 */
public class MessageManager {
    private final BDCraft plugin;
    private final Map<UUID, UUID> lastMessaged = new HashMap<>();
    
    /**
     * Creates a new MessageManager.
     *
     * @param plugin The plugin instance
     */
    public MessageManager(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Sends a private message from one player to another.
     *
     * @param sender The sender
     * @param recipient The recipient
     * @param message The message
     * @return true if the message was sent, false otherwise
     */
    public boolean sendMessage(Player sender, Player recipient, String message) {
        // Logic for sending messages would be here
        lastMessaged.put(sender.getUniqueId(), recipient.getUniqueId());
        lastMessaged.put(recipient.getUniqueId(), sender.getUniqueId());
        return true;
    }
    
    /**
     * Gets the last player that another player messaged.
     *
     * @param player The player
     * @return The UUID of the last player messaged, or null if none
     */
    public UUID getLastMessaged(Player player) {
        return lastMessaged.get(player.getUniqueId());
    }
    
    /**
     * Replies to the last player that messaged this player.
     *
     * @param sender The sender
     * @param message The message
     * @return true if the reply was sent, false otherwise
     */
    public boolean replyToLastMessage(Player sender, String message) {
        UUID lastUUID = getLastMessaged(sender);
        if (lastUUID == null) {
            return false;
        }
        
        Player recipient = plugin.getServer().getPlayer(lastUUID);
        if (recipient == null || !recipient.isOnline()) {
            return false;
        }
        
        return sendMessage(sender, recipient, message);
    }
}