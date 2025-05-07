package com.example.paperplugin.modules.vital;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.modules.permissions.PermissionsService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to set a home location.
 */
public class SetHomeCommand implements CommandExecutor {

    private final PaperPlugin plugin;
    private final VitalModule vitalModule;
    private final VitalService vitalService;
    private PermissionsService permissionsService;
    
    private static final int DEFAULT_MAX_HOMES = 3;

    /**
     * Creates a new SetHomeCommand.
     *
     * @param plugin The main plugin instance
     * @param vitalModule The vital module
     */
    public SetHomeCommand(PaperPlugin plugin, VitalModule vitalModule) {
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
        if (permissionsService != null && !permissionsService.hasPermission(player, "bd.vital.sethome")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        // Get home name (default: "home")
        String homeName = args.length > 0 ? args[0].toLowerCase() : "home";
        
        // Check if player has reached the home limit
        int maxHomes = DEFAULT_MAX_HOMES;
        
        // If permissions service exists, check for tier permissions
        if (permissionsService != null) {
            if (permissionsService.hasPermission(player, "bd.vital.homes.tier3")) {
                maxHomes = 10;
            } else if (permissionsService.hasPermission(player, "bd.vital.homes.tier2")) {
                maxHomes = 5;
            }
        }
        
        // Check if adding a new home (not overwriting)
        int currentHomes = vitalService.getHomes(player).size();
        boolean isNewHome = !vitalService.getHomes(player).containsKey(homeName);
        
        if (isNewHome && currentHomes >= maxHomes) {
            player.sendMessage("§cYou have reached your limit of " + maxHomes + " homes.");
            player.sendMessage("§cUse §6/delhome <name> §cto remove a home first.");
            return true;
        }

        // Set the home
        if (vitalService.setHome(player, homeName, player.getLocation())) {
            player.sendMessage("§aHome '" + homeName + "' set successfully!");
        } else {
            player.sendMessage("§cFailed to set home location.");
        }

        return true;
    }
}