package com.bdcraft.plugin.events.market;

import com.bdcraft.plugin.events.BDEvent;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Fired when a new market is created.
 */
public class MarketCreateEvent extends BDEvent {
    private final Player player;
    private final BDMarket market;
    private final Location location;
    
    /**
     * Creates a new market create event.
     * 
     * @param player The player creating the market
     * @param market The market being created
     * @param location The location of the market
     */
    public MarketCreateEvent(Player player, BDMarket market, Location location) {
        super(true); // Market creation can be cancelled
        this.player = player;
        this.market = market;
        this.location = location;
    }
    
    /**
     * Gets the player who is creating the market.
     * 
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the market being created.
     * 
     * @return The market
     */
    public BDMarket getMarket() {
        return market;
    }
    
    /**
     * Gets the location of the market.
     * 
     * @return The location
     */
    public Location getLocation() {
        return location;
    }
}