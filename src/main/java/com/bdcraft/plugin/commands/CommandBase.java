package com.bdcraft.plugin.commands;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base class for all BDCraft commands.
 * Provides common functionality and a standardized command structure.
 */
public abstract class CommandBase implements CommandExecutor, TabCompleter {
    protected final BDCraft plugin;
    protected final String name;
    protected final String permission;
    protected final List<String> aliases = new ArrayList<>();
    private final List<SubCommand> subCommands = new ArrayList<>();
    
    /**
     * Creates a new command.
     * @param plugin The plugin instance
     * @param name The command name
     * @param permission The permission required to use this command
     */
    public CommandBase(BDCraft plugin, String name, String permission) {
        this.plugin = plugin;
        this.name = name;
        this.permission = permission;
        
        // Register this command with the plugin
        plugin.getCommand(name).setExecutor(this);
        plugin.getCommand(name).setTabCompleter(this);
    }
    
    /**
     * Adds a subcommand to this command.
     * @param subCommand The subcommand to add
     */
    protected void addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }
    
    /**
     * Gets all subcommands for this command.
     * @return The list of subcommands
     */
    protected List<SubCommand> getSubCommands() {
        return subCommands;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        // If no arguments, show help
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        // Try to find a matching subcommand
        String subCommandName = args[0].toLowerCase();
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equals(subCommandName) || 
                    subCommand.getAliases().contains(subCommandName)) {
                
                // Check subcommand permission
                if (!sender.hasPermission(subCommand.getPermission())) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this subcommand.");
                    return true;
                }
                
                // Check player-only command
                if (subCommand.isPlayerOnly() && !(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                // Check argument count
                if (args.length - 1 < subCommand.getMinArgs()) {
                    sender.sendMessage(ChatColor.RED + "Not enough arguments. Usage: " + 
                            ChatColor.GRAY + "/" + name + " " + subCommand.getName() + " " + subCommand.getUsage());
                    return true;
                }
                
                // Execute the subcommand
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                return subCommand.execute(sender, subArgs);
            }
        }
        
        // No matching subcommand found
        sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use " + 
                ChatColor.GRAY + "/" + name + " help" + ChatColor.RED + " for a list of commands.");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        // First argument - subcommand names
        if (args.length == 1) {
            String partialSubCommand = args[0].toLowerCase();
            for (SubCommand subCommand : subCommands) {
                if (sender.hasPermission(subCommand.getPermission())) {
                    if (subCommand.getName().startsWith(partialSubCommand)) {
                        completions.add(subCommand.getName());
                    }
                    
                    // Also check aliases
                    for (String subAlias : subCommand.getAliases()) {
                        if (subAlias.startsWith(partialSubCommand)) {
                            completions.add(subAlias);
                        }
                    }
                }
            }
            return completions;
        }
        
        // Second+ arguments - delegate to subcommand
        String subCommandName = args[0].toLowerCase();
        for (SubCommand subCommand : subCommands) {
            if ((subCommand.getName().equals(subCommandName) || 
                    subCommand.getAliases().contains(subCommandName)) && 
                    sender.hasPermission(subCommand.getPermission())) {
                
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                List<String> subCompletions = subCommand.tabComplete(sender, subArgs);
                if (subCompletions != null) {
                    completions.addAll(subCompletions);
                }
                break;
            }
        }
        
        return completions;
    }
    
    /**
     * Shows help information for this command.
     * @param sender The command sender
     */
    protected void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== " + ChatColor.YELLOW + name.toUpperCase() + " Commands" + 
                ChatColor.GOLD + " ===");
        
        for (SubCommand subCommand : subCommands) {
            if (sender.hasPermission(subCommand.getPermission())) {
                sender.sendMessage(ChatColor.YELLOW + "/" + name + " " + subCommand.getName() + " " + 
                        subCommand.getUsage() + ChatColor.GRAY + " - " + subCommand.getDescription());
            }
        }
    }
}