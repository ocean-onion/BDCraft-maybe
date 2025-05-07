package com.example.paperplugin.modules.vital;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.modules.permissions.PermissionsService;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Command to teleport to a server warp point.
 */
public class WarpCommand implements CommandExecutor, TabCompleter {

    private final PaperPlugin plugin;
    private final VitalModule vitalModule;
    private final VitalService vitalService;
    private PermissionsService permissionsService;

    /**
     * Creates a new WarpCommand.
     *
     * @param plugin The main plugin instance
     * @param vitalModule The vital module
     */
    public WarpCommand(PaperPlugin plugin, VitalModule vitalModule) {
        this.plugin = plugin;
        this.vitalModule = vitalModule;
        this.vitalService = vitalModule.getModuleManager().getService(VitalService.class);
        this.permissionsService = vitalModule.getModuleManager().getService(PermissionsService.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (permissionsService != null && !permissionsService.hasPermission(player, "bd.vital.warp")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        // No arguments, show list of warps
        if (args.length == 0) {
            List<String> warps = vitalService.getWarpNames();
            
            if (warps.isEmpty()) {
                player.sendMessage("§cThere are no warps available.");
                return true;
            }

            player.sendMessage("§6Available warps:");
            for (String warpName : warps) {
                // Check if player has permission for this specific warp
                if (permissionsService == null || 
                    permissionsService.hasPermission(player, "bd.vital.warp.*") || 
                    permissionsService.hasPermission(player, "bd.vital.warp." + warpName)) {
                    
                    player.sendMessage("§e- " + warpName);
                }
            }
            return true;
        }

        // Teleport to specific warp
        String warpName = args[0].toLowerCase();
        
        // Check permission for this specific warp
        if (permissionsService != null && 
            !permissionsService.hasPermission(player, "bd.vital.warp.*") && 
            !permissionsService.hasPermission(player, "bd.vital.warp." + warpName)) {
            
            player.sendMessage("§cYou don't have permission to warp to '" + warpName + "'.");
            return true;
        }
        
        // Teleport player
        if (vitalService.teleportToWarp(player, warpName)) {
            player.sendMessage("§aTeleported to warp '" + warpName + "'!");
        } else {
            player.sendMessage("§cWarp '" + warpName + "' not found.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) || args.length != 1) {
            return null;
        }

        Player player = (Player) sender;
        List<String> warps = vitalService.getWarpNames();
        List<String> completions = new ArrayList<>();
        
        // Only show warps the player has permission to use
        for (String warp : warps) {
            if (permissionsService == null || 
                permissionsService.hasPermission(player, "bd.vital.warp.*") || 
                permissionsService.hasPermission(player, "bd.vital.warp." + warp)) {
                
                completions.add(warp);
            }
        }
        
        if (args[0].isEmpty()) {
            return completions;
        }
        
        List<String> filtered = new ArrayList<>();
        String input = args[0].toLowerCase();
        for (String name : completions) {
            if (name.toLowerCase().startsWith(input)) {
                filtered.add(name);
            }
        }
        
        return filtered;
    }
}