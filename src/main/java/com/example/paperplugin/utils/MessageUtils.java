package com.example.paperplugin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Utility class for message-related operations.
 * Provides methods for formatting and styling text messages.
 */
public class MessageUtils {

    /**
     * Formats a message by replacing placeholders with values and adding colors.
     *
     * @param format The message format with {player} placeholder
     * @param playerName The player name to insert in the placeholder
     * @return A formatted Component ready to be sent to players
     */
    public static Component formatMessage(String format, String playerName) {
        // Replace the player placeholder with the actual player name
        String replacedFormat = format.replace("{player}", playerName);
        
        // Split the string around the player name to apply different styles
        String[] parts = replacedFormat.split(playerName);
        
        // Build the component with proper styling
        TextComponent.Builder builder = Component.text();
        
        if (parts.length > 0) {
            // Add the first part before the player name
            builder.append(Component.text(parts[0])
                    .color(NamedTextColor.GOLD));
            
            // Add the player name with a different color
            builder.append(Component.text(playerName)
                    .color(NamedTextColor.GREEN)
                    .decorate(TextDecoration.BOLD));
            
            // If there's content after the player name, add it too
            if (parts.length > 1) {
                builder.append(Component.text(parts[1])
                        .color(NamedTextColor.GOLD));
            }
        } else {
            // If there are no parts (unlikely), just return the whole text
            builder.append(Component.text(replacedFormat)
                    .color(NamedTextColor.GOLD));
        }
        
        return builder.build();
    }
    
    /**
     * Creates a styled error message component.
     *
     * @param message The error message text
     * @return A styled error message component
     */
    public static Component errorMessage(String message) {
        return Component.text("Error: ")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD)
                .append(Component.text(message)
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.BOLD, false));
    }
    
    /**
     * Creates a styled success message component.
     *
     * @param message The success message text
     * @return A styled success message component
     */
    public static Component successMessage(String message) {
        return Component.text("Success: ")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)
                .append(Component.text(message)
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.BOLD, false));
    }
}
