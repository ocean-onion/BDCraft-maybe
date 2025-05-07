package com.bdcraft.modules.economy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a player's economy account in the BDCraft economy system.
 * Tracks credits balance and transaction history.
 */
public class PlayerAccount {
    
    private final UUID playerId;
    private final String playerName;
    private double credits;
    private final List<Transaction> transactions;
    
    /**
     * Creates a new player account.
     *
     * @param playerId The UUID of the player
     * @param playerName The name of the player
     */
    public PlayerAccount(UUID playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.credits = 0; // Credits start at 0 and are earned through play
        this.transactions = new ArrayList<>();
    }
    
    /**
     * Gets the player's UUID.
     *
     * @return The player's UUID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Gets the player's name.
     *
     * @return The player's name
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Gets the player's credit balance.
     *
     * @return The credit balance
     */
    public double getCredits() {
        return credits;
    }
    
    /**
     * Adds credits to the player's account.
     *
     * @param amount The amount to add
     * @param initiatorId The UUID of who initiated the transaction
     * @param type The transaction type
     * @param description The transaction description
     * @return The new balance
     */
    public double addCredits(double amount, UUID initiatorId, TransactionType type, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        credits += amount;
        
        // Create and add transaction record
        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            playerId,
            initiatorId,
            amount,
            type,
            description,
            credits
        );
        
        transactions.add(transaction);
        
        return credits;
    }
    
    /**
     * Removes credits from the player's account.
     *
     * @param amount The amount to remove
     * @param initiatorId The UUID of who initiated the transaction
     * @param type The transaction type
     * @param description The transaction description
     * @return True if successful, false if not enough credits
     */
    public boolean removeCredits(double amount, UUID initiatorId, TransactionType type, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        // Check if the player has enough credits
        if (credits < amount) {
            return false;
        }
        
        credits -= amount;
        
        // Create and add transaction record
        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            playerId,
            initiatorId,
            -amount, // Negative for removal
            type,
            description,
            credits
        );
        
        transactions.add(transaction);
        
        return true;
    }
    
    /**
     * Sets the player's credit balance to a specific amount.
     *
     * @param amount The new amount
     * @param description The reason for the change
     * @param initiatorId The UUID of who initiated the change
     */
    public void setCredits(double amount, String description, UUID initiatorId) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        
        double difference = amount - credits;
        TransactionType type = difference > 0 ? TransactionType.ADMIN_GIVE : TransactionType.ADMIN_TAKE;
        
        credits = amount;
        
        // Create and add transaction record
        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            playerId,
            initiatorId,
            difference,
            type,
            description,
            credits
        );
        
        transactions.add(transaction);
    }
    
    /**
     * Gets the player's transaction history.
     *
     * @return The transaction history
     */
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
    
    /**
     * Gets the player's recent transactions, limited to a certain number.
     *
     * @param limit The maximum number of transactions to return
     * @return The recent transactions
     */
    public List<Transaction> getRecentTransactions(int limit) {
        int size = transactions.size();
        if (size <= limit) {
            return new ArrayList<>(transactions);
        }
        return new ArrayList<>(transactions.subList(size - limit, size));
    }
    
    /**
     * Checks if the player has at least a certain amount of credits.
     *
     * @param amount The amount to check
     * @return True if the player has at least the amount
     */
    public boolean hasEnoughCredits(double amount) {
        return credits >= amount;
    }
}