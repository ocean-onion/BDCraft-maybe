package com.bdcraft.plugin.events.market;

import com.bdcraft.plugin.events.BDEvent;
import com.bdcraft.plugin.modules.economy.market.Market;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

/**
 * Fired when a player trades with a market villager.
 */
public class MarketTradeEvent extends BDEvent {
    private final Player player;
    private final Market market;
    private final Villager villager;
    private final ItemStack offered;
    private final ItemStack received;
    private final double price;
    
    /**
     * Creates a new market trade event.
     * 
     * @param player The player trading
     * @param market The market
     * @param villager The villager
     * @param offered The item offered
     * @param received The item received
     * @param price The price
     */
    public MarketTradeEvent(Player player, Market market, Villager villager, 
            ItemStack offered, ItemStack received, double price) {
        super(true); // Trades can be cancelled
        this.player = player;
        this.market = market;
        this.villager = villager;
        this.offered = offered;
        this.received = received;
        this.price = price;
    }
    
    /**
     * Gets the player who is trading.
     * 
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the market.
     * 
     * @return The market
     */
    public Market getMarket() {
        return market;
    }
    
    /**
     * Gets the villager.
     * 
     * @return The villager
     */
    public Villager getVillager() {
        return villager;
    }
    
    /**
     * Gets the item offered by the player.
     * 
     * @return The item offered
     */
    public ItemStack getOffered() {
        return offered;
    }
    
    /**
     * Gets the item received by the player.
     * 
     * @return The item received
     */
    public ItemStack getReceived() {
        return received;
    }
    
    /**
     * Gets the price of the trade.
     * 
     * @return The price
     */
    public double getPrice() {
        return price;
    }
}