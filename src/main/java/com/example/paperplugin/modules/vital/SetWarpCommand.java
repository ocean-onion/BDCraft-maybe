package com.example.paperplugin.modules.vital;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.modules.permissions.PermissionsService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to set a server warp point.
 */
public class SetWarpCommand implements CommandExecutor {

    private final PaperPlugin plugin;
    private final VitalModule vitalModule;
    private final VitalService vitalService;
    private PermissionsService permissionsService;

    /**
     * Creates a new SetWarpCommand.
     *
     * @param plugin The main plugin instance
     * @param vitalModule The vital module
     */
    public SetWarpCommand(PaperPlugin plugin, VitalModule vitalModule) {
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
        if (permissionsService != null && !permissionsService.hasPermission(player, "bd.vital.setwarp")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        // Check for warp name
        if (args.length == 0) {
            player.sendMessage("§cUsage: /setwarp <name>");
            return true;
        }

        String warpName = args[0].toLowerCase();
        
        // Set the warp
        if (vitalService.setWarp(warpName, player.getLocation())) {
            player.sendMessage("§aWarp '" + warpName + "' set successfully!");
        } else {
            player.sendMessage("§cFailed to set warp location.");
        }

        return true;
    }
}