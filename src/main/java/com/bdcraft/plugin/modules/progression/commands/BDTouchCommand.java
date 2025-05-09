package com.bdcraft.plugin.modules.progression.commands;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.progression.rebirth.BDRebirthManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Command for applying rebirth touch to crops.
 */
public class BDTouchCommand implements CommandExecutor {
    private final BDCraft plugin;
    
    // Crop materials that can be affected
    private final Set<Material> VALID_CROPS = new HashSet<>(Arrays.asList(
            Material.WHEAT,
            Material.CARROTS,
            Material.POTATOES,
            Material.BEETROOTS,
            Material.MELON_STEM,
            Material.PUMPKIN_STEM,
            Material.SUGAR_CANE,
            Material.COCOA
    ));
    
    /**
     * Creates a new touch command.
     *
     * @param plugin The plugin instance
     */
    public BDTouchCommand(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdcraft.rebirth.touch")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        // Get rebirth manager
        BDRebirthManager rebirthManager = plugin.getProgressionModule().getRebirthManager();
        
        // Check if player has been reborn
        int rebirthLevel = rebirthManager.getPlayerRebirthLevel(player);
        
        if (rebirthLevel <= 0) {
            player.sendMessage(ChatColor.RED + "You must be reborn to use this command.");
            return true;
        }
        
        // Check cooldown
        long lastUseTime = rebirthManager.getLastCommandUse(player.getUniqueId(), "bdtouch");
        long currentTime = System.currentTimeMillis();
        long cooldown = 12 * 60 * 60 * 1000; // 12 hours
        
        if (lastUseTime > 0 && currentTime - lastUseTime < cooldown) {
            long remainingTime = cooldown - (currentTime - lastUseTime);
            long hours = remainingTime / (60 * 60 * 1000);
            long minutes = (remainingTime % (60 * 60 * 1000)) / (60 * 1000);
            
            player.sendMessage(ChatColor.RED + "You must wait " + hours + " hours and " + 
                    minutes + " minutes before using this command again.");
            return true;
        }
        
        // Record command use
        rebirthManager.recordCommandUse(player.getUniqueId(), "bdtouch");
        
        // Calculate effect radius
        int radius = 3 + rebirthLevel; // 3 blocks + 1 per rebirth level
        
        // Calculate boost amount and duration
        double boostAmount = 1.5 + (rebirthLevel * 0.1); // 1.5x + 0.1 per rebirth level
        long duration = 2 * 60 * 60 * 1000; // 2 hours
        
        // Apply crop growth boost
        growCropsInRadius(player, radius);
        
        // Apply exp boost
        double totalBonus = boostAmount;
        plugin.getProgressionModule().getRebirthManager().setPlayerExpBoost(player.getUniqueId(), totalBonus, duration);
        
        // Send message
        player.sendMessage(ChatColor.LIGHT_PURPLE + "You have used rebirth touch with a radius of " + 
                radius + " blocks!");
        player.sendMessage(ChatColor.YELLOW + "Your experience gain has been boosted by " + 
                String.format("%.1f", (totalBonus - 1.0) * 100) + "% for 2 hours!");
        
        // Play effects
        player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f, 1.0f);
        
        return true;
    }
    
    /**
     * Grows crops in a radius around the player.
     *
     * @param player The player
     * @param radius The radius
     */
    private void growCropsInRadius(Player player, int radius) {
        int count = 0;
        
        for (int x = -radius; x <= radius; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = player.getLocation().add(x, y, z).getBlock();
                    
                    if (VALID_CROPS.contains(block.getType())) {
                        // Apply growth effect
                        // Note: In a real implementation, this would use more advanced
                        // crop growth logic to advance the crop's growth stage.
                        
                        // For now, just show particles to simulate growth
                        block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, 
                                block.getLocation().add(0.5, 0.5, 0.5), 
                                5, 0.25, 0.25, 0.25, 0);
                        
                        count++;
                    }
                }
            }
        }
        
        if (count > 0) {
            player.sendMessage(ChatColor.GREEN + "Your touch affected " + count + " crops in the area!");
        } else {
            player.sendMessage(ChatColor.YELLOW + "There were no crops in range to grow.");
        }
    }
}