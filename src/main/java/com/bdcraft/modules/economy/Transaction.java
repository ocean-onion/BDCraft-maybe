package com.bdcraft.modules.economy;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a transaction in the BDCraft economy system.
 * Used to track credit movements between players and the system.
 */
public class Transaction {
    
    private final UUID id;
    private final UUID playerId;
    private final UUID initiatorId;
    private final double amount;
    private final TransactionType type;
    private final String description;
    private final Instant timestamp;
    private final double balanceAfter;
    
    /**
     * Creates a new transaction.
     *
     * @param id The unique identifier for this transaction
     * @param playerId The UUID of the player whose account is affected
     * @param initiatorId The UUID of the player or entity that initiated the transaction
     * @param amount The amount of the transaction
     * @param type The type of transaction
     * @param description A description of the transaction
     * @param balanceAfter The player's balance after the transaction
     */
    public Transaction(UUID id, UUID playerId, UUID initiatorId, double amount, 
                     TransactionType type, String description, double balanceAfter) {
        this.id = id;
        this.playerId = playerId;
        this.initiatorId = initiatorId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.timestamp = Instant.now();
        this.balanceAfter = balanceAfter;
    }
    
    /**
     * Gets the transaction ID.
     *
     * @return The transaction ID
     */
    public UUID getId() {
        return id;
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
     * Gets the initiator ID.
     *
     * @return The initiator ID
     */
    public UUID getInitiatorId() {
        return initiatorId;
    }
    
    /**
     * Gets the transaction amount.
     *
     * @return The transaction amount
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
     * Gets the transaction description.
     *
     * @return The transaction description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the transaction timestamp.
     *
     * @return The transaction timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }
    
    /**
     * Gets the player's balance after the transaction.
     *
     * @return The balance after transaction
     */
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    /**
     * Checks if the transaction is a deposit.
     *
     * @return True if the transaction is a deposit
     */
    public boolean isDeposit() {
        return amount > 0;
    }
    
    /**
     * Checks if the transaction is a withdrawal.
     *
     * @return True if the transaction is a withdrawal
     */
    public boolean isWithdrawal() {
        return amount < 0;
    }
    
    /**
     * Returns a string representation of the transaction.
     *
     * @return A string representing the transaction
     */
    @Override
    public String toString() {
        String amountStr = String.format("%.0f", Math.abs(amount));
        String typeStr = isDeposit() ? "received" : "spent";
        
        return String.format("[%s] %s %s credits (%s)", 
                             timestamp, 
                             typeStr, 
                             amountStr,
                             type.getDisplayName());
    }
}