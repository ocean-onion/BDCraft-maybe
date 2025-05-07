package com.bdcraft.modules.economy;

/**
 * Represents different transaction types in the BDCraft economy system.
 * These types help categorize and track different kinds of transactions.
 */
public enum TransactionType {

    /**
     * Player-to-player transaction where one player sends credits to another.
     */
    PLAYER_TO_PLAYER("Player Payment"),

    /**
     * Payment from a player to a shop or market.
     */
    PLAYER_TO_SHOP("Shop Purchase"),

    /**
     * Payment to a player from a shop or market.
     */
    SHOP_TO_PLAYER("Shop Sale"),

    /**
     * Payment made by a player for a rank upgrade.
     */
    RANK_PURCHASE("Rank Purchase"),

    /**
     * Payment made by a player for auction fees.
     */
    AUCTION_FEE("Auction Fee"),

    /**
     * Payment from auction sales.
     */
    AUCTION_SALE("Auction Sale"),

    /**
     * Payment for auction purchases.
     */
    AUCTION_PURCHASE("Auction Purchase"),

    /**
     * Credits given by an admin.
     */
    ADMIN_GIVE("Admin Credit Grant"),

    /**
     * Credits taken by an admin.
     */
    ADMIN_TAKE("Admin Credit Removal"),

    /**
     * Credits earned from town or village activities.
     */
    VILLAGE_EARNINGS("Village Earnings"),

    /**
     * Credits taxed by a town or village.
     */
    VILLAGE_TAX("Village Tax"),

    /**
     * Credits earned from trading with villagers.
     */
    VILLAGER_TRADE("Villager Trade"),

    /**
     * Credits earned from collectors.
     */
    COLLECTOR_EARNINGS("Collector Earnings"),

    /**
     * Credits spent on special items.
     */
    SPECIAL_ITEM_PURCHASE("Special Item Purchase"),

    /**
     * Other transaction types not covered by the above.
     */
    OTHER("Other Transaction");

    private final String displayName;

    /**
     * Creates a new TransactionType.
     *
     * @param displayName The human-readable name of the transaction type
     */
    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the transaction type.
     *
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}