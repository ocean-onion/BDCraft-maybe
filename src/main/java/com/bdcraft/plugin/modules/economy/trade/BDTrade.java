package com.bdcraft.plugin.modules.economy.trade;

import org.bukkit.Material;

import java.util.UUID;

/**
 * Represents a trade in the BDCraft economy system.
 */
public class BDTrade {
    private final UUID tradeId;
    private final UUID playerId;
    private final String cropType;
    private final int quantity;
    private final double emeraldsEarned;
    private final int bdCurrencyEarned;
    private final long timestamp;
    private final boolean isPremium; // Whether this was a premium (green/purple) trade
    
    /**
     * Creates a new trade record.
     *
     * @param tradeId The unique trade ID
     * @param playerId The player's UUID
     * @param cropType The crop type (material name)
     * @param quantity The quantity of crops traded
     * @param emeraldsEarned The number of emeralds earned
     * @param bdCurrencyEarned The amount of BD currency earned
     * @param timestamp The timestamp of the trade
     * @param isPremium Whether this was a premium trade
     */
    public BDTrade(UUID tradeId, UUID playerId, String cropType, int quantity, 
                  double emeraldsEarned, int bdCurrencyEarned, long timestamp, boolean isPremium) {
        this.tradeId = tradeId;
        this.playerId = playerId;
        this.cropType = cropType;
        this.quantity = quantity;
        this.emeraldsEarned = emeraldsEarned;
        this.bdCurrencyEarned = bdCurrencyEarned;
        this.timestamp = timestamp;
        this.isPremium = isPremium;
    }
    
    /**
     * Gets the trade ID.
     *
     * @return The trade ID
     */
    public UUID getTradeId() {
        return tradeId;
    }
    
    /**
     * Gets the player ID.
     *
     * @return The player's UUID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Gets the crop type.
     *
     * @return The crop type
     */
    public String getCropType() {
        return cropType;
    }
    
    /**
     * Gets the quantity of crops traded.
     *
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Gets the number of emeralds earned.
     *
     * @return The emeralds earned
     */
    public double getEmeraldsEarned() {
        return emeraldsEarned;
    }
    
    /**
     * Gets the amount of BD currency earned.
     *
     * @return The BD currency earned
     */
    public int getBdCurrencyEarned() {
        return bdCurrencyEarned;
    }
    
    /**
     * Gets the timestamp of the trade.
     *
     * @return The timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Checks if this was a premium trade.
     *
     * @return True if this was a premium trade
     */
    public boolean isPremium() {
        return isPremium;
    }
    
    /**
     * Serializes the trade to a string for storage.
     *
     * @return The serialized trade data
     */
    public String serialize() {
        return tradeId + ";" +
               playerId + ";" +
               cropType + ";" +
               quantity + ";" +
               emeraldsEarned + ";" +
               bdCurrencyEarned + ";" +
               timestamp + ";" +
               (isPremium ? "1" : "0");
    }
    
    /**
     * Deserializes a trade from a string.
     *
     * @param data The serialized trade data
     * @return The deserialized trade, or null if invalid
     */
    public static BDTrade deserialize(String data) {
        try {
            String[] parts = data.split(";");
            
            if (parts.length != 8) {
                return null;
            }
            
            UUID tradeId = UUID.fromString(parts[0]);
            UUID playerId = UUID.fromString(parts[1]);
            String cropType = parts[2];
            int quantity = Integer.parseInt(parts[3]);
            double emeraldsEarned = Double.parseDouble(parts[4]);
            int bdCurrencyEarned = Integer.parseInt(parts[5]);
            long timestamp = Long.parseLong(parts[6]);
            boolean isPremium = parts[7].equals("1");
            
            return new BDTrade(tradeId, playerId, cropType, quantity, 
                              emeraldsEarned, bdCurrencyEarned, timestamp, isPremium);
        } catch (Exception e) {
            return null;
        }
    }
}