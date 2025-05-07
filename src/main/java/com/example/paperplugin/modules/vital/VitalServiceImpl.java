package com.example.paperplugin.modules.vital;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Implementation of the VitalService interface.
 * Provides the vital service functionality.
 */
public class VitalServiceImpl implements VitalService {

    private final VitalModule vitalModule;
    private Map<UUID, Map<String, Location>> playerHomes;
    private Map<String, Location> serverWarps;
    private Location serverSpawn;
    
    /**
     * Creates a new VitalServiceImpl.
     *
     * @param vitalModule The vital module
     */
    public VitalServiceImpl(VitalModule vitalModule) {
        this.vitalModule = vitalModule;
        this.playerHomes = new HashMap<>();
        this.serverWarps = new HashMap<>();
        
        // Load data from config
        loadSpawn();
        loadHomes();
        loadWarps();
    }
    
    /**
     * Loads the server spawn location from config.
     */
    private void loadSpawn() {
        if (vitalModule.getVitalConfig().contains("spawn")) {
            ConfigurationSection spawnSection = vitalModule.getVitalConfig().getConfigurationSection("spawn");
            
            String worldName = spawnSection.getString("world");
            World world = Bukkit.getWorld(worldName);
            
            if (world != null) {
                double x = spawnSection.getDouble("x");
                double y = spawnSection.getDouble("y");
                double z = spawnSection.getDouble("z");
                float yaw = (float) spawnSection.getDouble("yaw");
                float pitch = (float) spawnSection.getDouble("pitch");
                
                serverSpawn = new Location(world, x, y, z, yaw, pitch);
            }
        }
    }
    
    /**
     * Loads player homes from config.
     */
    private void loadHomes() {
        playerHomes.clear();
        
        if (vitalModule.getVitalConfig().contains("homes")) {
            ConfigurationSection homesSection = vitalModule.getVitalConfig().getConfigurationSection("homes");
            
            for (String uuidString : homesSection.getKeys(false)) {
                UUID playerId = UUID.fromString(uuidString);
                Map<String, Location> homes = new HashMap<>();
                
                ConfigurationSection playerHomesSection = homesSection.getConfigurationSection(uuidString);
                
                for (String homeName : playerHomesSection.getKeys(false)) {
                    ConfigurationSection homeSection = playerHomesSection.getConfigurationSection(homeName);
                    
                    String worldName = homeSection.getString("world");
                    World world = Bukkit.getWorld(worldName);
                    
                    if (world != null) {
                        double x = homeSection.getDouble("x");
                        double y = homeSection.getDouble("y");
                        double z = homeSection.getDouble("z");
                        float yaw = (float) homeSection.getDouble("yaw");
                        float pitch = (float) homeSection.getDouble("pitch");
                        
                        homes.put(homeName, new Location(world, x, y, z, yaw, pitch));
                    }
                }
                
                playerHomes.put(playerId, homes);
            }
        }
    }
    
    /**
     * Loads server warps from config.
     */
    private void loadWarps() {
        serverWarps.clear();
        
        if (vitalModule.getVitalConfig().contains("warps")) {
            ConfigurationSection warpsSection = vitalModule.getVitalConfig().getConfigurationSection("warps");
            
            for (String warpName : warpsSection.getKeys(false)) {
                ConfigurationSection warpSection = warpsSection.getConfigurationSection(warpName);
                
                String worldName = warpSection.getString("world");
                World world = Bukkit.getWorld(worldName);
                
                if (world != null) {
                    double x = warpSection.getDouble("x");
                    double y = warpSection.getDouble("y");
                    double z = warpSection.getDouble("z");
                    float yaw = (float) warpSection.getDouble("yaw");
                    float pitch = (float) warpSection.getDouble("pitch");
                    
                    serverWarps.put(warpName, new Location(world, x, y, z, yaw, pitch));
                }
            }
        }
    }
    
    /**
     * Saves the server spawn location to config.
     */
    private void saveSpawn() {
        if (serverSpawn != null) {
            vitalModule.getVitalConfig().set("spawn.world", serverSpawn.getWorld().getName());
            vitalModule.getVitalConfig().set("spawn.x", serverSpawn.getX());
            vitalModule.getVitalConfig().set("spawn.y", serverSpawn.getY());
            vitalModule.getVitalConfig().set("spawn.z", serverSpawn.getZ());
            vitalModule.getVitalConfig().set("spawn.yaw", serverSpawn.getYaw());
            vitalModule.getVitalConfig().set("spawn.pitch", serverSpawn.getPitch());
        }
    }
    
    /**
     * Saves player homes to config.
     */
    private void saveHomes() {
        // Clear existing homes in config
        vitalModule.getVitalConfig().set("homes", null);
        
        // Save current homes
        for (Map.Entry<UUID, Map<String, Location>> playerEntry : playerHomes.entrySet()) {
            String uuidString = playerEntry.getKey().toString();
            
            for (Map.Entry<String, Location> homeEntry : playerEntry.getValue().entrySet()) {
                String homeName = homeEntry.getKey();
                Location location = homeEntry.getValue();
                
                String path = "homes." + uuidString + "." + homeName;
                vitalModule.getVitalConfig().set(path + ".world", location.getWorld().getName());
                vitalModule.getVitalConfig().set(path + ".x", location.getX());
                vitalModule.getVitalConfig().set(path + ".y", location.getY());
                vitalModule.getVitalConfig().set(path + ".z", location.getZ());
                vitalModule.getVitalConfig().set(path + ".yaw", location.getYaw());
                vitalModule.getVitalConfig().set(path + ".pitch", location.getPitch());
            }
        }
    }
    
    /**
     * Saves server warps to config.
     */
    private void saveWarps() {
        // Clear existing warps in config
        vitalModule.getVitalConfig().set("warps", null);
        
        // Save current warps
        for (Map.Entry<String, Location> warpEntry : serverWarps.entrySet()) {
            String warpName = warpEntry.getKey();
            Location location = warpEntry.getValue();
            
            String path = "warps." + warpName;
            vitalModule.getVitalConfig().set(path + ".world", location.getWorld().getName());
            vitalModule.getVitalConfig().set(path + ".x", location.getX());
            vitalModule.getVitalConfig().set(path + ".y", location.getY());
            vitalModule.getVitalConfig().set(path + ".z", location.getZ());
            vitalModule.getVitalConfig().set(path + ".yaw", location.getYaw());
            vitalModule.getVitalConfig().set(path + ".pitch", location.getPitch());
        }
    }

    @Override
    public void setSpawn(Location location) {
        serverSpawn = location.clone();
        saveSpawn();
    }

    @Override
    public Location getSpawn() {
        return serverSpawn != null ? serverSpawn.clone() : null;
    }

    @Override
    public boolean teleportToSpawn(Player player) {
        if (serverSpawn != null) {
            return player.teleport(serverSpawn);
        }
        return false;
    }

    @Override
    public boolean setHome(Player player, String homeName, Location location) {
        UUID playerId = player.getUniqueId();
        
        // Get or create the player's homes map
        Map<String, Location> homes = playerHomes.getOrDefault(playerId, new HashMap<>());
        
        // Add or update the home location
        homes.put(homeName, location.clone());
        
        // Update the player's homes map
        playerHomes.put(playerId, homes);
        
        // Save to config
        saveHomes();
        
        return true;
    }

    @Override
    public Location getHome(Player player, String homeName) {
        return getHomes(player).get(homeName);
    }

    @Override
    public Map<String, Location> getHomes(Player player) {
        return getHomes(player.getUniqueId());
    }

    @Override
    public Map<String, Location> getHomes(UUID playerId) {
        Map<String, Location> homes = playerHomes.getOrDefault(playerId, new HashMap<>());
        
        // Create a copy of the map with cloned locations
        Map<String, Location> result = new HashMap<>();
        for (Map.Entry<String, Location> entry : homes.entrySet()) {
            result.put(entry.getKey(), entry.getValue().clone());
        }
        
        return result;
    }

    @Override
    public boolean deleteHome(Player player, String homeName) {
        UUID playerId = player.getUniqueId();
        
        // Get the player's homes map
        Map<String, Location> homes = playerHomes.getOrDefault(playerId, new HashMap<>());
        
        // Remove the home if it exists
        boolean removed = homes.remove(homeName) != null;
        
        // Update the player's homes map if the home was removed
        if (removed) {
            // If there are no more homes, remove the player from the map
            if (homes.isEmpty()) {
                playerHomes.remove(playerId);
            } else {
                playerHomes.put(playerId, homes);
            }
            
            // Save to config
            saveHomes();
        }
        
        return removed;
    }

    @Override
    public boolean setWarp(String warpName, Location location) {
        // Add or update the warp location
        serverWarps.put(warpName, location.clone());
        
        // Save to config
        saveWarps();
        
        return true;
    }

    @Override
    public Location getWarp(String warpName) {
        Location warp = serverWarps.get(warpName);
        return warp != null ? warp.clone() : null;
    }

    @Override
    public Map<String, Location> getWarps() {
        // Create a copy of the map with cloned locations
        Map<String, Location> result = new HashMap<>();
        for (Map.Entry<String, Location> entry : serverWarps.entrySet()) {
            result.put(entry.getKey(), entry.getValue().clone());
        }
        
        return result;
    }

    @Override
    public List<String> getWarpNames() {
        return new ArrayList<>(serverWarps.keySet());
    }

    @Override
    public boolean deleteWarp(String warpName) {
        // Remove the warp if it exists
        boolean removed = serverWarps.remove(warpName) != null;
        
        // Save to config if the warp was removed
        if (removed) {
            saveWarps();
        }
        
        return removed;
    }

    @Override
    public boolean teleportToWarp(Player player, String warpName) {
        Location warp = getWarp(warpName);
        
        if (warp != null) {
            return player.teleport(warp);
        }
        
        return false;
    }

    @Override
    public boolean teleportToHome(Player player, String homeName) {
        Location home = getHome(player, homeName);
        
        if (home != null) {
            return player.teleport(home);
        }
        
        return false;
    }
}