package com.bdcraft.plugin.modules.economy.items.tools;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.bdcraft.plugin.BDCraft;

import java.util.Random;

/**
 * Handles BD Stick functionality.
 */
public class BDStickHandler {
    private static final Random RANDOM = new Random();
    private static final PotionEffectType[] POTION_EFFECTS = {
        PotionEffectType.SPEED,
        PotionEffectType.JUMP_BOOST,
        PotionEffectType.NIGHT_VISION,
        PotionEffectType.STRENGTH, // Strength (formerly INCREASE_DAMAGE)
        PotionEffectType.FIRE_RESISTANCE,
        PotionEffectType.ABSORPTION
    };
    
    private final BDCraft plugin;
    
    /**
     * Creates a new BD stick handler.
     * 
     * @param plugin The plugin instance
     */
    public BDStickHandler(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Handles the use of a BD stick.
     * 
     * @param player The player
     * @param item The BD stick item
     * @return True if the stick was used successfully
     */
    public boolean handleUse(Player player, ItemStack item) {
        if (!BDTool.isBDTool(item)) {
            return false;
        }
        
        ToolType toolType = BDTool.getToolType(item);
        
        if (toolType != ToolType.BDSTICK) {
            return false;
        }
        
        // Apply random potion effect
        applyRandomPotionEffect(player);
        
        // Create particle effect
        player.getWorld().spawnParticle(Particle.COMPOSTER, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
        
        // Play sound
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        
        // Damage the tool
        boolean toolSurvived = BDTool.damageTool(item, 1);
        
        if (!toolSurvived) {
            // Tool broke
            player.getInventory().setItemInMainHand(null);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            player.sendMessage(ChatColor.RED + "Your BD Stick has broken!");
        }
        
        return true;
    }
    
    /**
     * Applies a random potion effect to a player.
     * 
     * @param player The player
     */
    private void applyRandomPotionEffect(Player player) {
        PotionEffectType effectType = POTION_EFFECTS[RANDOM.nextInt(POTION_EFFECTS.length)];
        
        // Duration is 20 ticks * 30 seconds = 600 ticks
        int duration = 20 * 30;
        
        // Amplifier is 0-based (level 1 = amplifier 0)
        int amplifier = 0;
        
        player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
        
        String effectName = formatPotionEffectName(effectType);
        player.sendMessage(ChatColor.GOLD + "Your BD Stick grants you " + effectName + " for 30 seconds!");
    }
    
    /**
     * Formats a potion effect name to be more readable.
     * 
     * @param effectType The potion effect type
     * @return The formatted name
     */
    private String formatPotionEffectName(PotionEffectType effectType) {
        String name = effectType.getName();
        
        // Replace underscores with spaces
        name = name.replace('_', ' ');
        
        // Capitalize each word
        String[] words = name.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
}