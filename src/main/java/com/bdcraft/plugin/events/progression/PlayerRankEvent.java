package com.bdcraft.plugin.events.progression;

import com.bdcraft.plugin.api.ProgressionAPI;
import com.bdcraft.plugin.events.BDEvent;
import org.bukkit.entity.Player;

/**
 * Fired when a player's rank changes.
 */
public class PlayerRankEvent extends BDEvent {
    private final Player player;
    private final ProgressionAPI.Rank oldRank;
    private final ProgressionAPI.Rank newRank;
    private final boolean isPromotion;
    
    /**
     * Creates a new player rank event.
     * 
     * @param player The player
     * @param oldRank The old rank
     * @param newRank The new rank
     */
    public PlayerRankEvent(Player player, ProgressionAPI.Rank oldRank, ProgressionAPI.Rank newRank) {
        super(false); // Rank changes cannot be cancelled
        this.player = player;
        this.oldRank = oldRank;
        this.newRank = newRank;
        this.isPromotion = newRank.ordinal() > oldRank.ordinal();
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
     * Gets the old rank.
     * 
     * @return The old rank
     */
    public ProgressionAPI.Rank getOldRank() {
        return oldRank;
    }
    
    /**
     * Gets the new rank.
     * 
     * @return The new rank
     */
    public ProgressionAPI.Rank getNewRank() {
        return newRank;
    }
    
    /**
     * Checks if this is a promotion.
     * 
     * @return Whether this is a promotion
     */
    public boolean isPromotion() {
        return isPromotion;
    }
    
    /**
     * Checks if this is a demotion.
     * 
     * @return Whether this is a demotion
     */
    public boolean isDemotion() {
        return !isPromotion && oldRank != newRank;
    }
}