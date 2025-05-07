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
 * Command to delete a home location.
 */
public class DelHomeCommand implements CommandExecutor, TabCompleter {

    private final PaperPlugin plugin;
    private final VitalModule vitalModule;
    private final VitalService vitalService;
    private PermissionsService permissionsService;

    /**
     * Creates a new DelHomeCommand.
     *
     * @param plugin The main plugin instance
     * @param vitalModule The vital module
     */
    public DelHomeCommand(PaperPlugin plugin, VitalModule vitalModule) {
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
        if (permissionsService != null && !permissionsService.hasPermission(player, "bd.vital.delhome")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        // Check for home name
        if (args.length == 0) {
            player.sendMessage("§cUsage: /delhome <name>");
            return true;
        }

        String homeName = args[0].toLowerCase();
        
        // Delete the home
        if (vitalService.deleteHome(player, homeName)) {
            player.sendMessage("§aHome '" + homeName + "' deleted successfully!");
        } else {
            player.sendMessage("§cHome '" + homeName + "' not found.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) || args.length != 1) {
            return null;
        }

        Player player = (Player) sender;
        Map<String, Location> homes = vitalService.getHomes(player);
        List<String> completions = new ArrayList<>(homes.keySet());
        
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