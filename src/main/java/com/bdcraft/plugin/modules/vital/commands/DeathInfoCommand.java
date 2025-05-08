package com.bdcraft.plugin.modules.vital.commands;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.vital.BDVitalModule;
import com.bdcraft.plugin.modules.vital.teleport.TeleportManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command for showing death information.
 */
public class DeathInfoCommand implements CommandExecutor {
    private final BDCraft plugin;
    private final TeleportManager teleportManager;
    
    /**
     * Creates a new death info command.
     * @param plugin The plugin instance
     * @param vitalModule The vital module
     */
    public DeathInfoCommand(BDCraft plugin, BDVitalModule vitalModule) {
        this.plugin = plugin;
        this.teleportManager = vitalModule.getTeleportManager();
        
        plugin.getCommand("bddeathinfo").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdvital.deathinfo")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        // Get death location
        Location deathLocation = teleportManager.getDeathLocation(player.getUniqueId());
        
        if (deathLocation == null) {
            player.sendMessage(ChatColor.RED + "You haven't died recently, or your death location was not recorded.");
            return true;
        }
        
        // Show death info
        player.sendMessage(ChatColor.GOLD + "=== Your Last Death ===");
        player.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.WHITE + deathLocation.getWorld().getName());
        player.sendMessage(ChatColor.YELLOW + "Coordinates: " + ChatColor.WHITE + 
                formatCoordinates(deathLocation));
        player.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GREEN + "/bdback" + 
                ChatColor.YELLOW + " to return to your death location.");
        
        return true;
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
}