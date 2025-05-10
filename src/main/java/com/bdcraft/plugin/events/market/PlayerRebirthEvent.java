package com.bdcraft.plugin.events.market;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player is reborn.
 */
public class PlayerRebirthEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final int newRebirthLevel;
    
    /**
     * Creates a new player rebirth event.
     * 
     * @param player The player
     * @param newRebirthLevel The new rebirth level
     */
    public PlayerRebirthEvent(Player player, int newRebirthLevel) {
        this.player = player;
        this.newRebirthLevel = newRebirthLevel;
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
     * Gets the new rebirth level.
     * 
     * @return The new rebirth level
     */
    public int getNewRebirthLevel() {
        return newRebirthLevel;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}