package com.bdcraft.plugin.modules.display.command;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.display.BDScoreboardManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command for managing scoreboards.
 */
public class ScoreboardCommand implements CommandExecutor, TabCompleter {
    private final BDCraft plugin;
    private final BDScoreboardManager scoreboardManager;
    
    /**
     * Creates a new scoreboard command.
     *
     * @param plugin The plugin instance
     * @param scoreboardManager The scoreboard manager
     */
    public ScoreboardCommand(BDCraft plugin, BDScoreboardManager scoreboardManager) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdcraft.scoreboard")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "toggle":
                toggleScoreboard(player);
                break;
                
            case "on":
                enableScoreboard(player);
                break;
                
            case "off":
                disableScoreboard(player);
                break;
                
            case "reload":
                if (player.hasPermission("bdcraft.admin")) {
                    reloadScoreboards(player);
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have permission to reload scoreboards.");
                }
                break;
                
            default:
                sendHelpMessage(player);
                break;
        }
        
        return true;
    }
    
    /**
     * Sends the help message to a player.
     *
     * @param player The player
     */
    private void sendHelpMessage(Player player) {
        player.sendMessage(ChatColor.GOLD + "======= " + ChatColor.GREEN + "BDCraft Scoreboard" + ChatColor.GOLD + " =======");
        player.sendMessage(ChatColor.YELLOW + "/bdscoreboard toggle " + ChatColor.WHITE + "- Toggles the scoreboard on/off");
        player.sendMessage(ChatColor.YELLOW + "/bdscoreboard on " + ChatColor.WHITE + "- Enables the scoreboard");
        player.sendMessage(ChatColor.YELLOW + "/bdscoreboard off " + ChatColor.WHITE + "- Disables the scoreboard");
        
        if (player.hasPermission("bdcraft.admin")) {
            player.sendMessage(ChatColor.YELLOW + "/bdscoreboard reload " + ChatColor.WHITE + "- Reloads all scoreboards");
        }
    }
    
    /**
     * Toggles the scoreboard for a player.
     *
     * @param player The player
     */
    private void toggleScoreboard(Player player) {
        if (scoreboardManager.hasScoreboard(player)) {
            disableScoreboard(player);
        } else {
            enableScoreboard(player);
        }
    }
    
    /**
     * Enables the scoreboard for a player.
     *
     * @param player The player
     */
    private void enableScoreboard(Player player) {
        scoreboardManager.setupScoreboard(player);
        player.sendMessage(ChatColor.GREEN + "Scoreboard enabled.");
    }
    
    /**
     * Disables the scoreboard for a player.
     *
     * @param player The player
     */
    private void disableScoreboard(Player player) {
        scoreboardManager.removeScoreboard(player);
        player.sendMessage(ChatColor.GREEN + "Scoreboard disabled.");
    }
    
    /**
     * Reloads all scoreboards.
     *
     * @param player The player who executed the command
     */
    private void reloadScoreboards(Player player) {
        scoreboardManager.disable();
        
        // Recreate the scoreboard manager via the display module
        plugin.getDisplayModule().reload();
        
        player.sendMessage(ChatColor.GREEN + "Scoreboards reloaded.");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdcraft.scoreboard")) {
            return new ArrayList<>();
        }
        
        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>(Arrays.asList("toggle", "on", "off", "help"));
            
            if (player.hasPermission("bdcraft.admin")) {
                subCommands.add("reload");
            }
            
            String input = args[0].toLowerCase();
            return subCommands.stream()
                    .filter(s -> s.startsWith(input))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}