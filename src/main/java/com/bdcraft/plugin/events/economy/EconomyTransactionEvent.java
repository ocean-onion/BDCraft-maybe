package com.bdcraft.plugin.events.economy;

import com.bdcraft.plugin.events.BDEvent;

import java.util.UUID;

/**
 * Fired when an economy transaction occurs.
 */
public class EconomyTransactionEvent extends BDEvent {
    private final UUID playerId;
    private final double amount;
    private final TransactionType type;
    private final String reason;
    
    /**
     * Creates a new economy transaction event.
     * 
     * @param playerId The player ID
     * @param amount The amount
     * @param type The transaction type
     * @param reason The reason for the transaction
     */
    public EconomyTransactionEvent(UUID playerId, double amount, TransactionType type, String reason) {
        super(true); // Economy transactions can be cancelled
        this.playerId = playerId;
        this.amount = amount;
        this.type = type;
        this.reason = reason;
    }
    
    /**
     * Gets the player ID.
     * 
     * @return The player ID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Gets the transaction amount.
     * 
     * @return The amount
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Gets the transaction type.
     * 
     * @return The transaction type
     */
    public TransactionType getType() {
        return type;
    }
    
    /**
     * Gets the reason for the transaction.
     * 
     * @return The reason
     */
    public String getReason() {
        return reason;
    }
    
    /**
     * Types of economy transactions.
     */
    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        SET,
        TRANSFER
    }
}