package com.example.paperplugin.listeners;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener for player join events.
 * Sends a custom welcome message to players when they join the server.
 */
public class PlayerJoinListener implements Listener {

    private final PaperPlugin plugin;

    /**
     * Constructor for the PlayerJoinListener.
     *
     * @param plugin The main plugin instance
     */
    public PlayerJoinListener(PaperPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the player join event.
     * Sends a welcome message to the player.
     *
     * @param event The player join event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Get the welcome message format from config
        String welcomeFormat = plugin.getPluginConfig().getWelcomeMessageFormat();
        
        // Create a welcome message for the player using the format from config
        Component welcomeMessage = MessageUtils.formatMessage(welcomeFormat, player.getName());
        
        // Send the welcome message to the player
        player.sendMessage(welcomeMessage);
        
        // Inform the player about the hello command
        Component commandInfo = Component.text("Try typing ")
                .color(NamedTextColor.GREEN)
                .append(Component.text("/hello")
                        .color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.ITALIC))
                .append(Component.text(" to see a greeting!")
                        .color(NamedTextColor.GREEN));
        
        // Send the command info message to the player after a short delay (20 ticks = 1 second)
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage(commandInfo);
        }, 20L);
        
        // If it's the player's first time joining, show additional info
        if (!player.hasPlayedBefore()) {
            Component firstJoinMessage = Component.text("Welcome to the server for the first time!")
                    .color(NamedTextColor.LIGHT_PURPLE)
                    .decorate(TextDecoration.BOLD);
            
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.sendMessage(firstJoinMessage);
            }, 40L);
        }
        
        // Log that a player has joined
        plugin.getLogger().info("Player " + player.getName() + " has joined the server");
    }
}
