package com.example.paperplugin.modules.vital;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.modules.permissions.PermissionsService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Command to delete a server warp point.
 */
public class DelWarpCommand implements CommandExecutor, TabCompleter {

    private final PaperPlugin plugin;
    private final VitalModule vitalModule;
    private final VitalService vitalService;
    private PermissionsService permissionsService;

    /**
     * Creates a new DelWarpCommand.
     *
     * @param plugin The main plugin instance
     * @param vitalModule The vital module
     */
    public DelWarpCommand(PaperPlugin plugin, VitalModule vitalModule) {
        this.plugin = plugin;
        this.vitalModule = vitalModule;
        this.vitalService = vitalModule.getModuleManager().getService(VitalService.class);
        this.permissionsService = vitalModule.getModuleManager().getService(PermissionsService.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check permission - allow console to use this command
        if (sender instanceof Player && permissionsService != null && 
            !permissionsService.hasPermission((Player) sender, "bd.vital.delwarp")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        // Check for warp name
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /delwarp <name>");
            return true;
        }

        String warpName = args[0].toLowerCase();
        
        // Delete the warp
        if (vitalService.deleteWarp(warpName)) {
            sender.sendMessage("§aWarp '" + warpName + "' deleted successfully!");
        } else {
            sender.sendMessage("§cWarp '" + warpName + "' not found.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            return null;
        }

        List<String> warps = vitalService.getWarpNames();
        
        if (args[0].isEmpty()) {
            return warps;
        }
        
        List<String> filtered = new ArrayList<>();
        String input = args[0].toLowerCase();
        for (String name : warps) {
            if (name.toLowerCase().startsWith(input)) {
                filtered.add(name);
            }
        }
        
        return filtered;
    }
}