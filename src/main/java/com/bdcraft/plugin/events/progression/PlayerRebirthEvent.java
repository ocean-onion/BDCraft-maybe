package com.bdcraft.plugin.events.progression;

import com.bdcraft.plugin.events.BDEvent;
import org.bukkit.entity.Player;

/**
 * Fired when a player performs a rebirth.
 */
public class PlayerRebirthEvent extends BDEvent {
    private final Player player;
    private final int oldRebirthCount;
    private final int newRebirthCount;
    
    /**
     * Creates a new player rebirth event.
     * 
     * @param player The player
     * @param oldRebirthCount The old rebirth count
     * @param newRebirthCount The new rebirth count
     */
    public PlayerRebirthEvent(Player player, int oldRebirthCount, int newRebirthCount) {
        super(true); // Rebirths can be cancelled
        this.player = player;
        this.oldRebirthCount = oldRebirthCount;
        this.newRebirthCount = newRebirthCount;
    }
    
    /**
     * Gets the player.
     * 
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the old rebirth count.
     * 
     * @return The old rebirth count
     */
    public int getOldRebirthCount() {
        return oldRebirthCount;
    }
    
    /**
     * Gets the new rebirth count.
     * 
     * @return The new rebirth count
     */
    public int getNewRebirthCount() {
        return newRebirthCount;
    }
    
    /**
     * Gets the rebirth level (difference between counts).
     * 
     * @return The rebirth level
     */
    public int getRebirthLevel() {
        return newRebirthCount - oldRebirthCount;
    }
}