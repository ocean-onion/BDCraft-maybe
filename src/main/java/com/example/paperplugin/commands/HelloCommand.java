package com.example.paperplugin.commands;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple "Hello World" command that greets the player.
 * Implements both CommandExecutor for command execution and TabCompleter for command completion.
 */
public class HelloCommand implements CommandExecutor, TabCompleter {

    private final PaperPlugin plugin;

    /**
     * Constructor for the HelloCommand.
     *
     * @param plugin The main plugin instance
     */
    public HelloCommand(PaperPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the command when it is triggered.
     *
     * @param sender  The sender of the command
     * @param command The command that was executed
     * @param label   The alias of the command that was used
     * @param args    The arguments passed to the command
     * @return true if the command was processed successfully, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Get greeting message format from config or use default
        String messageFormat = plugin.getPluginConfig().getGreetingFormat();
        
        // If the sender is a player, send them a colorful message
        if (sender instanceof Player player) {
            // Create a hello message with the player's name using the format from config
            Component message = MessageUtils.formatMessage(messageFormat, player.getName());
            
            // Send the message to the player
            player.sendMessage(message);
            
            // Log that the command was executed
            plugin.getLogger().info("Player " + player.getName() + " executed the hello command");
        } else {
            // If the sender is not a player (e.g., console), send a plain message
            sender.sendMessage("Hello, Console! This command works better in-game.");
            plugin.getLogger().info("Console executed the hello command");
        }
        
        // The command was processed successfully
        return true;
    }
    
    /**
     * Provides tab completion for the command.
     *
     * @param sender  The sender of the command
     * @param command The command that is being tab-completed
     * @param label   The alias of the command that was used
     * @param args    The arguments passed to the command so far
     * @return A list of possible completions for the current argument
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        
        // For now, we don't have any specific tab completions for this simple command
        // But this method could be extended to provide completions for subcommands or arguments
        
        return completions;
    }
}
