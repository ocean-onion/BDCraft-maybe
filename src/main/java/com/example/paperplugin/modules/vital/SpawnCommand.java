package com.example.paperplugin.modules.vital;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.modules.permissions.PermissionsService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to teleport to the server spawn point.
 */
public class SpawnCommand implements CommandExecutor {

    private final PaperPlugin plugin;
    private final VitalModule vitalModule;
    private final VitalService vitalService;
    private PermissionsService permissionsService;

    /**
     * Creates a new SpawnCommand.
     *
     * @param plugin The main plugin instance
     * @param vitalModule The vital module
     */
    public SpawnCommand(PaperPlugin plugin, VitalModule vitalModule) {
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
        if (permissionsService != null && !permissionsService.hasPermission(player, "bd.vital.spawn")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        // Handle /setspawn
        if (label.equalsIgnoreCase("setspawn")) {
            // Check permission for setting spawn
            if (permissionsService != null && !permissionsService.hasPermission(player, "bd.vital.setspawn")) {
                player.sendMessage("§cYou don't have permission to set the spawn point.");
                return true;
            }

            vitalService.setSpawn(player.getLocation());
            player.sendMessage("§aSpawn point set successfully!");
            return true;
        }

        // Handle /spawn
        if (vitalService.teleportToSpawn(player)) {
            player.sendMessage("§aTeleported to spawn!");
        } else {
            player.sendMessage("§cFailed to teleport to spawn. The spawn point might not be set.");
        }

        return true;
    }
}