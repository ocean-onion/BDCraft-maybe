package com.bdcraft.plugin.modules.vital.config;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Configuration manager for the BDVital module.
 */
public class VitalConfig {
    private final BDCraft plugin;
    private FileConfiguration config;
    
    private int teleportCooldown;
    private int teleportWarmup;
    private int teleportationRandomMaxAttempts;
    private int teleportationRandomMaxRange;
    private int teleportationRandomMinRange;
    private boolean teleportationRandomSafe;
    private int maxHomes;
    private int maxMailsPerPlayer;
    private int maxMailRetentionDays;
    private boolean enableBackCommand;
    private int maxWarps;
    
    /**
     * Creates a new vital configuration.
     * @param plugin The plugin instance
     */
    public VitalConfig(BDCraft plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig(ConfigManager.ConfigType.VITAL);
        loadConfig();
    }
    
    /**
     * Loads the configuration from file.
     */
    public void loadConfig() {
        teleportCooldown = config.getInt("teleport.cooldown", 30);
        teleportWarmup = config.getInt("teleport.warmup", 3);
        teleportationRandomMaxAttempts = config.getInt("teleport.random.max-attempts", 10);
        teleportationRandomMaxRange = config.getInt("teleport.random.max-range", 5000);
        teleportationRandomMinRange = config.getInt("teleport.random.min-range", 500);
        teleportationRandomSafe = config.getBoolean("teleport.random.safe", true);
        maxHomes = config.getInt("home.max-homes", 3);
        maxMailsPerPlayer = config.getInt("mail.max-mails", 10);
        maxMailRetentionDays = config.getInt("mail.retention-days", 30);
        enableBackCommand = config.getBoolean("teleport.enable-back", true);
        maxWarps = config.getInt("warps.max-warps", 20);
    }
    
    /**
     * Gets the teleport cooldown.
     * @return The teleport cooldown
     */
    public int getTeleportCooldown() {
        return teleportCooldown;
    }
    
    /**
     * Gets the teleport warmup.
     * @return The teleport warmup
     */
    public int getTeleportWarmup() {
        return teleportWarmup;
    }
    
    /**
     * Gets the maximum random teleport attempts.
     * @return The maximum random teleport attempts
     */
    public int getTeleportationRandomMaxAttempts() {
        return teleportationRandomMaxAttempts;
    }
    
    /**
     * Gets the maximum random teleport range.
     * @return The maximum random teleport range
     */
    public int getTeleportationRandomMaxRange() {
        return teleportationRandomMaxRange;
    }
    
    /**
     * Gets the minimum random teleport range.
     * @return The minimum random teleport range
     */
    public int getTeleportationRandomMinRange() {
        return teleportationRandomMinRange;
    }
    
    /**
     * Checks if random teleportation should be safe.
     * @return Whether random teleportation should be safe
     */
    public boolean isTeleportationRandomSafe() {
        return teleportationRandomSafe;
    }
    
    /**
     * Gets the default maximum homes per player.
     * @return The maximum homes per player
     */
    public int getMaxHomes() {
        return maxHomes;
    }
    
    /**
     * Gets the maximum mails per player.
     * @return The maximum mails per player
     */
    public int getMaxMailsPerPlayer() {
        return maxMailsPerPlayer;
    }
    
    /**
     * Gets the maximum mail retention days.
     * @return The maximum mail retention days
     */
    public int getMaxMailRetentionDays() {
        return maxMailRetentionDays;
    }
    
    /**
     * Checks if the back command is enabled.
     * @return Whether the back command is enabled
     */
    public boolean isEnableBackCommand() {
        return enableBackCommand;
    }
    
    /**
     * Gets the maximum warps.
     * @return The maximum warps
     */
    public int getMaxWarps() {
        return maxWarps;
    }
}