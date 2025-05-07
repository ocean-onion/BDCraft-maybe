package com.bdcraft.modules.economy;

/**
 * Represents a currency in the BDCraft economy system.
 * Used for formatting and displaying currency values.
 */
public class Currency {
    
    private final String name;
    private final String pluralName;
    private final String symbol;
    
    /**
     * Creates a new Currency.
     *
     * @param name The singular name of the currency
     * @param pluralName The plural name of the currency
     * @param symbol The symbol of the currency
     */
    public Currency(String name, String pluralName, String symbol) {
        this.name = name;
        this.pluralName = pluralName;
        this.symbol = symbol;
    }
    
    /**
     * Gets the name of the currency.
     *
     * @return The currency name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the plural name of the currency.
     *
     * @return The plural currency name
     */
    public String getPluralName() {
        return pluralName;
    }
    
    /**
     * Gets the symbol of the currency.
     *
     * @return The currency symbol
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Formats an amount with the currency name.
     *
     * @param amount The amount to format
     * @return The formatted amount
     */
    public String format(double amount) {
        if (amount == 1.0) {
            return String.format("%.0f %s", amount, name);
        } else {
            return String.format("%.0f %s", amount, pluralName);
        }
    }
    
    /**
     * Formats an amount with the currency symbol.
     *
     * @param amount The amount to format
     * @return The formatted amount
     */
    public String formatWithSymbol(double amount) {
        return String.format("%s%.0f", symbol, amount);
    }
    
    /**
     * Returns a string representation of the currency.
     *
     * @return A string representing the currency
     */
    @Override
    public String toString() {
        return String.format("%s (%s)", name, symbol);
    }
}