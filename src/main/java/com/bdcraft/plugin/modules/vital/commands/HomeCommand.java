package com.bdcraft.plugin.modules.vital.commands;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.vital.BDVitalModule;
import com.bdcraft.plugin.modules.vital.home.HomeManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command for managing homes.
 */
public class HomeCommand implements CommandExecutor, TabCompleter {
    private final BDCraft plugin;
    private final HomeManager homeManager;
    
    /**
     * Creates a new home command.
     * @param plugin The plugin instance
     * @param vitalModule The vital module
     */
    public HomeCommand(BDCraft plugin, BDVitalModule vitalModule) {
        this.plugin = plugin;
        this.homeManager = vitalModule.getHomeManager();
        
        plugin.getCommand("bdhome").setExecutor(this);
        plugin.getCommand("bdhome").setTabCompleter(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdvital.home")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use homes.");
            return true;
        }
        
        if (args.length == 0) {
            // Teleport to default home
            teleportToHome(player, "default");
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "set":
                setHome(player, args);
                break;
            case "del":
            case "delete":
                deleteHome(player, args);
                break;
            case "list":
                listHomes(player);
                break;
            default:
                // Assume it's a home name
                teleportToHome(player, args[0]);
                break;
        }
        
        return true;
    }
    
    /**
     * Sets a home.
     * @param player The player
     * @param args The command arguments
     */
    private void setHome(Player player, String[] args) {
        if (!player.hasPermission("bdvital.sethome")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to set homes.");
            return;
        }
        
        String homeName = args.length > 1 ? args[1].toLowerCase() : "default";
        
        // Get max homes
        int maxHomes = getMaxHomes(player);
        
        // Check if player has reached max homes
        Map<String, Location> homes = homeManager.getHomes(player.getUniqueId());
        
        if (homes.size() >= maxHomes && !homes.containsKey(homeName)) {
            player.sendMessage(ChatColor.RED + "You've reached your maximum number of homes (" + maxHomes + ").");
            player.sendMessage(ChatColor.YELLOW + "Delete a home before setting a new one.");
            return;
        }
        
        // Set home
        homeManager.setHome(player.getUniqueId(), homeName, player.getLocation());
        
        player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' set successfully.");
    }
    
    /**
     * Deletes a home.
     * @param player The player
     * @param args The command arguments
     */
    private void deleteHome(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Please specify a home name to delete.");
            player.sendMessage(ChatColor.YELLOW + "Usage: /bdhome del <name>");
            return;
        }
        
        String homeName = args[1].toLowerCase();
        
        // Check if home exists
        if (!homeManager.hasHome(player.getUniqueId(), homeName)) {
            player.sendMessage(ChatColor.RED + "You don't have a home named '" + homeName + "'.");
            return;
        }
        
        // Delete home
        homeManager.removeHome(player.getUniqueId(), homeName);
        
        player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' deleted successfully.");
    }
    
    /**
     * Lists a player's homes.
     * @param player The player
     */
    private void listHomes(Player player) {
        Map<String, Location> homes = homeManager.getHomes(player.getUniqueId());
        
        if (homes.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "You haven't set any homes yet.");
            player.sendMessage(ChatColor.YELLOW + "Use /bdhome set <name> to set a home.");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "Your Homes:");
        
        for (String homeName : homes.keySet()) {
            Location location = homes.get(homeName);
            player.sendMessage(ChatColor.YELLOW + "- " + homeName + ": " + 
                    ChatColor.WHITE + location.getWorld().getName() + ", " + 
                    formatCoordinates(location));
        }
        
        player.sendMessage(ChatColor.YELLOW + "You have " + homes.size() + "/" + getMaxHomes(player) + " homes set.");
    }
    
    /**
     * Teleports a player to a home.
     * @param player The player
     * @param homeName The home name
     */
    private void teleportToHome(Player player, String homeName) {
        // Check if home exists
        if (!homeManager.hasHome(player.getUniqueId(), homeName)) {
            if (homeName.equals("default")) {
                player.sendMessage(ChatColor.RED + "You haven't set a default home yet.");
                player.sendMessage(ChatColor.YELLOW + "Use /bdhome set to set your default home.");
            } else {
                player.sendMessage(ChatColor.RED + "You don't have a home named '" + homeName + "'.");
                player.sendMessage(ChatColor.YELLOW + "Use /bdhome list to see your homes.");
            }
            return;
        }
        
        // Get home location
        Location location = homeManager.getHome(player.getUniqueId(), homeName);
        
        // Teleport player
        player.teleport(location);
        
        player.sendMessage(ChatColor.GREEN + "Teleported to home '" + homeName + "'.");
    }
    
    /**
     * Gets the maximum number of homes a player can have.
     * @param player The player
     * @return The maximum number of homes
     */
    private int getMaxHomes(Player player) {
        if (player.hasPermission("bdvital.home.unlimited")) {
            return Integer.MAX_VALUE;
        } else if (player.hasPermission("bdvital.home.10")) {
            return 10;
        } else if (player.hasPermission("bdvital.home.5")) {
            return 5;
        } else if (player.hasPermission("bdvital.home.3")) {
            return 3;
        } else {
            return 1;
        }
    }
    
    /**
     * Formats coordinates for display.
     * @param location The location
     * @return The formatted coordinates
     */
    private String formatCoordinates(Location location) {
        return (int) location.getX() + ", " + 
                (int) location.getY() + ", " + 
                (int) location.getZ();
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }
        
        Player player = (Player) sender;
        
        if (args.length == 1) {
            List<String> completions = new ArrayList<>(Arrays.asList("set", "del", "list"));
            
            // Add home names
            completions.addAll(homeManager.getHomes(player.getUniqueId()).keySet());
            
            return completions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete"))) {
            // Return home names for deletion
            return homeManager.getHomes(player.getUniqueId()).keySet().stream()
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}