package com.bdcraft.plugin.modules.vital.commands;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.vital.BDVitalModule;
import com.bdcraft.plugin.modules.vital.teleport.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Commands for teleportation.
 */
public class TeleportCommands {
    private final BDCraft plugin;
    private final TeleportManager teleportManager;
    
    /**
     * Creates new teleport commands.
     * @param plugin The plugin instance
     * @param vitalModule The vital module
     */
    public TeleportCommands(BDCraft plugin, BDVitalModule vitalModule) {
        this.plugin = plugin;
        this.teleportManager = vitalModule.getTeleportManager();
        
        // Register commands
        registerTPCommand();
        registerTPACommand();
        registerTPDCommand();
        registerTPRCommand();
        registerBackCommand();
        registerSpawnCommand();
    }
    
    /**
     * Registers the /bdtp command.
     */
    private void registerTPCommand() {
        plugin.getCommand("bdtp").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                
                if (!player.hasPermission("bdvital.tp")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                if (args.length < 1) {
                    player.sendMessage(ChatColor.RED + "Please specify a player to teleport to.");
                    player.sendMessage(ChatColor.YELLOW + "Usage: /bdtp <player>");
                    return true;
                }
                
                String targetName = args[0];
                // Use the more specific method for exact player name matching
                Player target = Bukkit.getPlayerExact(targetName);
                
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player '" + targetName + "' is not online.");
                    return true;
                }
                
                // Send teleport request
                teleportManager.sendTeleportRequest(player, target, TeleportManager.TeleportType.TO_TARGET);
                
                player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + target.getName() + ".");
                target.sendMessage(ChatColor.YELLOW + player.getName() + " has requested to teleport to you.");
                target.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GREEN + "/bdtpa" + 
                        ChatColor.YELLOW + " to accept or " + ChatColor.RED + "/bdtpd" + 
                        ChatColor.YELLOW + " to deny.");
                
                return true;
            }
        });
        
        plugin.getCommand("bdtp").setTabCompleter(new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                if (args.length == 1) {
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                            .collect(Collectors.toList());
                }
                
                return new ArrayList<>();
            }
        });
    }
    
    /**
     * Registers the /bdtpa command.
     */
    private void registerTPACommand() {
        plugin.getCommand("bdtpa").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                
                if (!player.hasPermission("bdvital.tpa")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                // Accept teleport request
                TeleportManager.TeleportRequest request = teleportManager.getPendingRequest(player.getUniqueId());
                
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "You don't have any pending teleport requests.");
                    return true;
                }
                
                // Use the server method for UUID-based player lookup
                Player requester = Bukkit.getServer().getPlayer(request.getRequesterUuid());
                if (requester != null && !requester.isOnline()) {
                    requester = null; // Ensure consistency with old behavior
                }
                
                if (requester == null) {
                    player.sendMessage(ChatColor.RED + "The player who sent the request is no longer online.");
                    teleportManager.removePendingRequest(player.getUniqueId());
                    return true;
                }
                
                // Accept request
                teleportManager.acceptRequest(player, requester, request.getType());
                
                return true;
            }
        });
    }
    
    /**
     * Registers the /bdtpd command.
     */
    private void registerTPDCommand() {
        plugin.getCommand("bdtpd").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                
                if (!player.hasPermission("bdvital.tpa")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                // Deny teleport request
                TeleportManager.TeleportRequest request = teleportManager.getPendingRequest(player.getUniqueId());
                
                if (request == null) {
                    player.sendMessage(ChatColor.RED + "You don't have any pending teleport requests.");
                    return true;
                }
                // Use the server method for UUID-based player lookup
                Player requester = Bukkit.getServer().getPlayer(request.getRequesterUuid());
                if (requester != null && !requester.isOnline()) {
                    requester = null; // Ensure consistency with old behavior
                }
                
                // Remove request
                teleportManager.removePendingRequest(player.getUniqueId());
                
                player.sendMessage(ChatColor.YELLOW + "You denied the teleport request.");
                
                if (requester != null) {
                    requester.sendMessage(ChatColor.RED + player.getName() + " denied your teleport request.");
                }
                
                return true;
            }
        });
    }
    
    /**
     * Registers the /bdtpr command.
     */
    private void registerTPRCommand() {
        plugin.getCommand("bdtpr").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                
                if (!player.hasPermission("bdvital.tpr")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                if (args.length < 1) {
                    player.sendMessage(ChatColor.RED + "Please specify a player to request to teleport to you.");
                    player.sendMessage(ChatColor.YELLOW + "Usage: /bdtpr <player>");
                    return true;
                }
                
                String targetName = args[0];
                // Use the more specific method for exact player name matching
                Player target = Bukkit.getPlayerExact(targetName);
                
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player '" + targetName + "' is not online.");
                    return true;
                }
                
                // Send teleport request
                teleportManager.sendTeleportRequest(player, target, TeleportManager.TeleportType.TO_REQUESTER);
                
                player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + target.getName() + ".");
                target.sendMessage(ChatColor.YELLOW + player.getName() + " has requested you to teleport to them.");
                target.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GREEN + "/bdtpa" + 
                        ChatColor.YELLOW + " to accept or " + ChatColor.RED + "/bdtpd" + 
                        ChatColor.YELLOW + " to deny.");
                
                return true;
            }
        });
        
        plugin.getCommand("bdtpr").setTabCompleter(new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                if (args.length == 1) {
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                            .collect(Collectors.toList());
                }
                
                return new ArrayList<>();
            }
        });
    }
    
    /**
     * Registers the /bdback command.
     */
    private void registerBackCommand() {
        plugin.getCommand("bdback").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                
                if (!player.hasPermission("bdvital.back")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                // Get last location
                Location lastLocation = teleportManager.getLastLocation(player.getUniqueId());
                
                if (lastLocation == null) {
                    player.sendMessage(ChatColor.RED + "You don't have a previous location to return to.");
                    return true;
                }
                
                // Save current location
                teleportManager.saveLastLocation(player);
                
                // Teleport to last location
                player.teleport(lastLocation);
                
                player.sendMessage(ChatColor.GREEN + "Teleported to your previous location.");
                
                return true;
            }
        });
    }
    
    /**
     * Registers the /bdspawn command.
     */
    private void registerSpawnCommand() {
        plugin.getCommand("bdspawn").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                Player player = (Player) sender;
                
                if (!player.hasPermission("bdvital.spawn")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                // Save current location
                teleportManager.saveLastLocation(player);
                
                // Teleport to spawn
                World world = player.getWorld();
                Location spawnLocation = world.getSpawnLocation();
                
                player.teleport(spawnLocation);
                
                player.sendMessage(ChatColor.GREEN + "Teleported to spawn.");
                
                return true;
            }
        });
    }
}