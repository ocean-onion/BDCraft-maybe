package com.bdcraft.plugin.modules.progression;

import org.bukkit.ChatColor;

/**
 * Represents a player rank.
 */
public enum Rank {
    DEFAULT("Player", ChatColor.GRAY),
    NOVICE("Novice", ChatColor.GREEN),
    APPRENTICE("Apprentice", ChatColor.AQUA),
    EXPERT("Expert", ChatColor.BLUE),
    MASTER("Master", ChatColor.GOLD),
    GRANDMASTER("Grandmaster", ChatColor.LIGHT_PURPLE),
    LEGEND("Legend", ChatColor.RED);
    
    private final String name;
    private final ChatColor color;
    
    /**
     * Creates a new rank.
     * 
     * @param name The name
     * @param color The color
     */
    Rank(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }
    
    /**
     * Gets the name of this rank.
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the color of this rank.
     * 
     * @return The color
     */
    public ChatColor getColor() {
        return color;
    }
    
    /**
     * Gets the formatted name of this rank.
     * 
     * @return The formatted name
     */
    public String getFormattedName() {
        return color + name;
    }
    
    /**
     * Gets the next rank.
     * 
     * @return The next rank, or this rank if this is the highest rank
     */
    public Rank getNext() {
        int ordinal = ordinal();
        if (ordinal < values().length - 1) {
            return values()[ordinal + 1];
        }
        return this;
    }
    
    /**
     * Gets the previous rank.
     * 
     * @return The previous rank, or this rank if this is the lowest rank
     */
    public Rank getPrevious() {
        int ordinal = ordinal();
        if (ordinal > 0) {
            return values()[ordinal - 1];
        }
        return this;
    }
    
    /**
     * Gets a rank by name.
     * 
     * @param name The name
     * @return The rank, or DEFAULT if not found
     */
    public static Rank getByName(String name) {
        for (Rank rank : values()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return DEFAULT;
    }
}