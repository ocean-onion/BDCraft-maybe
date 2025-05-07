package com.bdcraft.plugin.commands;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * Interface for subcommands of BDCraft commands.
 */
public interface SubCommand {
    /**
     * Gets the name of the subcommand.
     * @return The subcommand name
     */
    String getName();
    
    /**
     * Gets the description of the subcommand.
     * @return The subcommand description
     */
    String getDescription();
    
    /**
     * Gets the usage string for the subcommand.
     * @return The usage string
     */
    String getUsage();
    
    /**
     * Gets the permission required to use this subcommand.
     * @return The permission node
     */
    String getPermission();
    
    /**
     * Gets the aliases for this subcommand.
     * @return List of aliases
     */
    default List<String> getAliases() {
        return Collections.emptyList();
    }
    
    /**
     * Gets the minimum number of arguments required for this subcommand.
     * @return The minimum number of arguments
     */
    default int getMinArgs() {
        return 0;
    }
    
    /**
     * Checks if this subcommand can only be used by players.
     * @return True if the subcommand is player-only
     */
    default boolean isPlayerOnly() {
        return false;
    }
    
    /**
     * Executes the subcommand.
     * @param sender The command sender
     * @param args The command arguments (excluding the subcommand name)
     * @return True if the command was executed successfully
     */
    boolean execute(CommandSender sender, String[] args);
    
    /**
     * Provides tab completions for this subcommand.
     * @param sender The command sender
     * @param args The command arguments (excluding the subcommand name)
     * @return List of tab completions, or null for default behavior
     */
    default List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}