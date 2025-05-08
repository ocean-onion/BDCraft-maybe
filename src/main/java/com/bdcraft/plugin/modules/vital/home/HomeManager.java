package com.bdcraft.plugin.modules.vital.home;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages player homes.
 */
public class HomeManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final File homesFile;
    private FileConfiguration homesConfig;
    
    // Cache of player homes (player UUID -> map of home name -> location)
    private final Map<UUID, Map<String, Location>> playerHomes;
    
    /**
     * Creates a new home manager.
     * @param plugin The plugin instance
     */
    public HomeManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.homesFile = new File(plugin.getDataFolder(), "homes.yml");
        this.playerHomes = new HashMap<>();
        
        // Ensure file exists
        if (!homesFile.exists()) {
            try {
                if (homesFile.createNewFile()) {
                    logger.info("Created homes.yml file");
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not create homes.yml file", e);
            }
        }
        
        // Load homes
        loadHomes();
    }
    
    /**
     * Loads homes from file.
     */
    public void loadHomes() {
        // Clear cache
        playerHomes.clear();
        
        // Load config
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);
        
        // Check for homes section
        if (!homesConfig.contains("homes")) {
            return;
        }
        
        // Load player homes
        ConfigurationSection homesSection = homesConfig.getConfigurationSection("homes");
        
        for (String uuidStr : homesSection.getKeys(false)) {
            try {
                UUID playerUuid = UUID.fromString(uuidStr);
                Map<String, Location> homes = new HashMap<>();
                
                ConfigurationSection playerSection = homesSection.getConfigurationSection(uuidStr);
                
                for (String homeName : playerSection.getKeys(false)) {
                    Location location = playerSection.getLocation(homeName);
                    homes.put(homeName, location);
                }
                
                playerHomes.put(playerUuid, homes);
            } catch (IllegalArgumentException e) {
                logger.warning("Invalid UUID in homes.yml: " + uuidStr);
            }
        }
        
        logger.info("Loaded " + playerHomes.size() + " player home records");
    }
    
    /**
     * Saves homes to file.
     */
    public void saveHomes() {
        // Clear existing homes
        homesConfig.set("homes", null);
        
        // Save player homes
        for (UUID playerUuid : playerHomes.keySet()) {
            Map<String, Location> homes = playerHomes.get(playerUuid);
            
            for (String homeName : homes.keySet()) {
                Location location = homes.get(homeName);
                homesConfig.set("homes." + playerUuid.toString() + "." + homeName, location);
            }
        }
        
        // Save config
        try {
            homesConfig.save(homesFile);
            logger.info("Saved " + playerHomes.size() + " player home records");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save homes.yml file", e);
        }
    }
    
    /**
     * Sets a home for a player.
     * @param playerUuid The player UUID
     * @param homeName The home name
     * @param location The location
     */
    public void setHome(UUID playerUuid, String homeName, Location location) {
        // Get player homes
        Map<String, Location> homes = playerHomes.computeIfAbsent(playerUuid, k -> new HashMap<>());
        
        // Set home
        homes.put(homeName, location);
        
        // Save homes
        saveHomes();
    }
    
    /**
     * Removes a home for a player.
     * @param playerUuid The player UUID
     * @param homeName The home name
     */
    public void removeHome(UUID playerUuid, String homeName) {
        // Get player homes
        Map<String, Location> homes = playerHomes.get(playerUuid);
        
        if (homes == null) {
            return;
        }
        
        // Remove home
        homes.remove(homeName);
        
        // If no homes left, remove player entry
        if (homes.isEmpty()) {
            playerHomes.remove(playerUuid);
        }
        
        // Save homes
        saveHomes();
    }
    
    /**
     * Gets a player's home.
     * @param playerUuid The player UUID
     * @param homeName The home name
     * @return The home location, or null if not found
     */
    public Location getHome(UUID playerUuid, String homeName) {
        // Get player homes
        Map<String, Location> homes = playerHomes.get(playerUuid);
        
        if (homes == null) {
            return null;
        }
        
        // Get home
        return homes.get(homeName);
    }
    
    /**
     * Checks if a player has a home.
     * @param playerUuid The player UUID
     * @param homeName The home name
     * @return Whether the player has the home
     */
    public boolean hasHome(UUID playerUuid, String homeName) {
        // Get player homes
        Map<String, Location> homes = playerHomes.get(playerUuid);
        
        if (homes == null) {
            return false;
        }
        
        // Check if home exists
        return homes.containsKey(homeName);
    }
    
    /**
     * Gets all homes for a player.
     * @param playerUuid The player UUID
     * @return The homes
     */
    public Map<String, Location> getHomes(UUID playerUuid) {
        // Get player homes
        Map<String, Location> homes = playerHomes.get(playerUuid);
        
        if (homes == null) {
            return new HashMap<>();
        }
        
        // Return a copy of the homes
        return new HashMap<>(homes);
    }
    
    /**
     * Gets the number of homes a player has.
     * @param playerUuid The player UUID
     * @return The number of homes
     */
    public int getHomeCount(UUID playerUuid) {
        // Get player homes
        Map<String, Location> homes = playerHomes.get(playerUuid);
        
        if (homes == null) {
            return 0;
        }
        
        // Return number of homes
        return homes.size();
    }
    
    /**
     * Clears a player's homes.
     * @param playerUuid The player UUID
     */
    public void clearHomes(UUID playerUuid) {
        // Remove player homes
        playerHomes.remove(playerUuid);
        
        // Save homes
        saveHomes();
    }
}