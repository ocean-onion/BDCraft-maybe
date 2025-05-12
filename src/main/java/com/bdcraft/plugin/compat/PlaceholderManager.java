package com.bdcraft.plugin.compat;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.EconomyAPI;
import com.bdcraft.plugin.api.ProgressionAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages placeholders for the BDCraft plugin.
 * This class provides a comprehensive placeholder system that:
 * 
 * 1. Handles internal placeholders with format {placeholder_name}
 * 2. Seamlessly integrates with PlaceholderAPI if installed
 * 3. Supports player-specific context for dynamic content
 * 4. Provides consistent number formatting and null safety
 * 
 * All placeholders are registered through this central manager to ensure
 * consistent behavior across different plugin modules and components.
 * 
 * @see com.bdcraft.plugin.api.ProgressionAPI
 * @see com.bdcraft.plugin.api.EconomyAPI
 */
public class PlaceholderManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<String, Function<Player, String>> placeholders = new ConcurrentHashMap<>();
    private final Pattern placeholderPattern = Pattern.compile("\\{([^{}]+)\\}");
    private boolean placeholderAPIHooked = false;
    
    /**
     * Creates a new placeholder manager.
     * 
     * @param plugin The plugin instance
     */
    public PlaceholderManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        
        // Register default placeholders
        registerDefaultPlaceholders();
        
        // Attempt to hook into PlaceholderAPI
        hookPlaceholderAPI();
    }
    
    /**
     * Registers the default placeholders for the BDCraft plugin.
     * 
     * Available placeholders:
     * - {rank}: Display name of player's current rank
     * - {rank_id}: Numerical ID of player's current rank
     * - {balance}: Player's current economy balance (formatted)
     * - {rebirth}: Player's rebirth level
     * - {rank_progress}: Progress percentage to next rank
     * - {next_rank}: Display name of next rank (or "Maximum Rank" if at max)
     */
    private void registerDefaultPlaceholders() {
        // Rank placeholders
        registerPlaceholder("rank", player -> {
            ProgressionAPI progressAPI = plugin.getProgressionAPI();
            if (progressAPI == null) return "Unknown";
            ProgressionAPI.Rank rank = progressAPI.getPlayerRank(player.getUniqueId());
            return progressAPI.getRankDisplayName(rank);
        });
        
        registerPlaceholder("rank_id", player -> {
            ProgressionAPI progressAPI = plugin.getProgressionAPI();
            if (progressAPI == null) return "0";
            ProgressionAPI.Rank rank = progressAPI.getPlayerRank(player.getUniqueId());
            return String.valueOf(rank.ordinal());
        });
        
        // Economy placeholders
        registerPlaceholder("balance", player -> {
            EconomyAPI economyAPI = plugin.getEconomyAPI();
            double balance = economyAPI != null ? economyAPI.getPlayerBalance(player.getUniqueId()) : 0;
            return formatNumber(balance);
        });
        
        // Rebirth placeholders
        registerPlaceholder("rebirth", player -> {
            ProgressionAPI progressAPI = plugin.getProgressionAPI();
            int rebirthCount = progressAPI != null ? progressAPI.getPlayerRebirthLevel(player.getUniqueId()) : 0;
            return String.valueOf(rebirthCount);
        });
        
        // Progression placeholders
        registerPlaceholder("rank_progress", player -> {
            ProgressionAPI progressAPI = plugin.getProgressionAPI();
            double progress = progressAPI != null ? progressAPI.getProgressPercentage(player.getUniqueId()) : 0;
            return String.format("%.1f%%", progress);
        });
        
        registerPlaceholder("next_rank", player -> {
            ProgressionAPI progressAPI = plugin.getProgressionAPI();
            if (progressAPI == null) return "Unknown";
            ProgressionAPI.Rank nextRank = progressAPI.getNextRank(player.getUniqueId());
            return nextRank != null ? progressAPI.getRankDisplayName(nextRank) : "Maximum Rank";
        });
    }
    
    /**
     * Registers a new placeholder with the placeholder system.
     * Placeholders are registered as name-function pairs, where the function
     * provides the real-time value for a specific player when requested.
     * 
     * Example usage:
     * ```
     * registerPlaceholder("custom_stat", player -> {
     *     return String.valueOf(getPlayerStat(player));
     * });
     * ```
     * 
     * @param placeholder The placeholder name (will be converted to lowercase)
     * @param function The function that generates the placeholder value for a player
     */
    public void registerPlaceholder(String placeholder, Function<Player, String> function) {
        placeholders.put(placeholder.toLowerCase(), function);
    }
    
    /**
     * Retrieves the value for a registered placeholder for a specific player.
     * This method safely handles player null checks and exceptions during value generation.
     * 
     * The method will return null in these cases:
     * - If the player parameter is null
     * - If no placeholder with the given name exists
     * - If an exception occurs during placeholder value generation
     * 
     * @param placeholder The placeholder name (case-insensitive)
     * @param player The player for whom to retrieve the placeholder value
     * @return The placeholder value as a string, or null if unavailable
     */
    public String getPlaceholderValue(String placeholder, Player player) {
        if (player == null) {
            return null;
        }
        
        Function<Player, String> function = placeholders.get(placeholder.toLowerCase());
        
        if (function == null) {
            return null;
        }
        
        try {
            return function.apply(player);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error getting placeholder value for " + placeholder, e);
            return null;
        }
    }
    
    /**
     * Formats a message by replacing placeholders with their actual values for a specific player.
     * This method processes a message string in two stages:
     * 1. Replaces color codes (e.g., &amp;a becomes §a)
     * 2. Replaces all placeholder patterns {placeholder_name} with their corresponding values
     * 
     * If either message or player is null, the original message is returned unchanged.
     * If a placeholder doesn't have a registered value, it remains unchanged in the text.
     * 
     * Example: "Welcome &amp;e{player_name}! Your rank is {rank}."
     * 
     * @param message The message containing placeholders and color codes
     * @param player The player for whom to format the message
     * @return The formatted message with all placeholders replaced
     */
    public String formatMessage(String message, Player player) {
        if (message == null || player == null) {
            return message;
        }
        
        // Replace color codes
        message = message.replace("&", "§");
        
        // Replace placeholders
        Matcher matcher = placeholderPattern.matcher(message);
        StringBuffer buffer = new StringBuffer();
        
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String value = getPlaceholderValue(placeholder, player);
            
            if (value != null) {
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(value));
            } else {
                matcher.appendReplacement(buffer, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        
        matcher.appendTail(buffer);
        
        return buffer.toString();
    }
    
    /**
     * Attempts to hook into PlaceholderAPI if it's present on the server.
     * 
     * This method uses reflection to:
     * 1. Detect if PlaceholderAPI is installed on the server
     * 2. Register our custom expansion without creating a hard dependency
     * 3. Track successful registration for later API checks
     * 
     * Using reflection allows our plugin to function properly even when
     * PlaceholderAPI is not installed, while still providing integration
     * capabilities when it is available.
     */
    private void hookPlaceholderAPI() {
        Plugin placeholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        
        if (placeholderAPI == null) {
            logger.info("PlaceholderAPI not found, using built-in placeholder system only.");
            return;
        }
        
        try {
            // Using reflection to avoid hard dependency
            Class<?> expansionClass = Class.forName("me.clip.placeholderapi.expansion.PlaceholderExpansion");
            Object expansion = new BDPlaceholderExpansion();
            
            // Call the register method
            Method registerMethod = expansion.getClass().getMethod("register");
            boolean registered = (boolean) registerMethod.invoke(expansion);
            
            if (registered) {
                placeholderAPIHooked = true;
                logger.info("Successfully registered with PlaceholderAPI.");
            } else {
                logger.warning("Failed to register with PlaceholderAPI.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to hook into PlaceholderAPI:", e);
        }
    }
    
    /**
     * Checks if BDCraft has successfully registered with PlaceholderAPI.
     * 
     * This method allows other components of the plugin to determine if PlaceholderAPI
     * integration is available, which can be useful for:
     * - Determining which placeholder format to use in messages
     * - Detecting if external placeholders can be used
     * - Logging or debugging purposes
     * 
     * @return true if PlaceholderAPI is installed and our expansion is registered, false otherwise
     */
    public boolean isPlaceholderAPIHooked() {
        return placeholderAPIHooked;
    }
    
    /**
     * Formats a number.
     * 
     * @param number The number
     * @return The formatted number
     */
    private String formatNumber(double number) {
        DecimalFormat formatter = new DecimalFormat("#,##0.##");
        return formatter.format(number);
    }
    
    /**
     * A class that integrates with PlaceholderAPI.
     * Uses reflection to avoid hard dependency.
     */
    public class BDPlaceholderExpansion {
        /**
         * Gets the identifier for this expansion.
         * 
         * @return The identifier
         */
        public String getIdentifier() {
            return "bdcraft";
        }
        
        /**
         * Gets the author of this expansion.
         * 
         * @return The author
         */
        public String getAuthor() {
            return plugin.getDescription().getAuthors().get(0);
        }
        
        /**
         * Gets the version of this expansion.
         * 
         * @return The version
         */
        public String getVersion() {
            return plugin.getDescription().getVersion();
        }
        
        /**
         * Checks if this expansion should be enabled by default.
         * 
         * @return Whether it should be enabled by default
         */
        public boolean canRegister() {
            return true;
        }
        
        /**
         * Checks if this expansion is persistent.
         * 
         * @return Whether it's persistent
         */
        public boolean persist() {
            return true;
        }
        
        /**
         * Gets the value for a placeholder.
         * 
         * @param player The player
         * @param identifier The identifier
         * @return The value
         */
        public String onPlaceholderRequest(Player player, String identifier) {
            if (player == null) {
                return "";
            }
            
            return getPlaceholderValue(identifier, player);
        }
    }
}