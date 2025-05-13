package com.bdcraft.plugin.modules.vital.modules.home;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages player homes as a submodule of BDVital.
 */
public class HomeModule implements SubmoduleBase, Listener, CommandExecutor {
    private final BDCraft plugin;
    private final Logger logger;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Home settings
    private int maxHomes;
    private int homeCooldown;
    private boolean homePermissionBased;
    
    // Home data
    private final File homesFile;
    private FileConfiguration homesConfig;
    private final Map<UUID, Map<String, Location>> playerHomes = new HashMap<>();
    private final Map<UUID, Long> homeCooldowns = new HashMap<>();
    
    /**
     * Creates a new home module.
     * 
     * @param plugin The plugin instance
     */
    public HomeModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.homesFile = new File(plugin.getDataFolder(), "homes.yml");
        
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
    }
    
    @Override
    public String getName() {
        return "Home";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        
        plugin.getLogger().info("Enabling Home submodule");
        
        // Load config
        loadConfig();
        
        // Load homes
        loadHomes();
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Register commands
        registerCommands();
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling Home submodule");
        
        // Save homes
        saveHomes();
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        loadConfig();
        loadHomes();
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Loads the configuration.
     */
    private void loadConfig() {
        FileConfiguration config = plugin.getConfig(ConfigType.VITAL);
        maxHomes = config.getInt("homes.max", 3);
        homeCooldown = config.getInt("homes.cooldown", 60);
        homePermissionBased = config.getBoolean("homes.permission-based", false);
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
     * Registers commands.
     */
    private void registerCommands() {
        // Register home command
        PluginCommand homeCommand = plugin.getCommand("bdhome");
        if (homeCommand != null) {
            homeCommand.setExecutor(this);
        }
        
        // Register sethome command
        PluginCommand sethomeCommand = plugin.getCommand("bdsethome");
        if (sethomeCommand != null) {
            sethomeCommand.setExecutor(this);
        }
        
        // Register delhome command
        PluginCommand delhomeCommand = plugin.getCommand("bddelhome");
        if (delhomeCommand != null) {
            delhomeCommand.setExecutor(this);
        }
    }
    
    /**
     * Gets the maximum number of homes a player can have.
     * 
     * @param player The player
     * @return The maximum number of homes
     */
    public int getMaxHomes(Player player) {
        if (!homePermissionBased) {
            return maxHomes;
        }
        
        // Check for permission-based home limits
        for (int i = 50; i > 0; i--) {
            if (player.hasPermission("bdvital.homes." + i)) {
                return i;
            }
        }
        
        return maxHomes;
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
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("bdhome")) {
            handleHomeCommand(player, args);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdsethome")) {
            handleSetHomeCommand(player, args);
            return true;
        } else if (command.getName().equalsIgnoreCase("bddelhome")) {
            handleDelHomeCommand(player, args);
            return true;
        }
        
        return false;
    }
    
    /**
     * Handles the home command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleHomeCommand(Player player, String[] args) {
        String homeName = args.length > 0 ? args[0].toLowerCase() : "home";
        
        if (!hasHome(player.getUniqueId(), homeName)) {
            player.sendMessage(ChatColor.RED + "Home '" + homeName + "' not found.");
            return;
        }
        
        // Check cooldown
        if (homeCooldowns.containsKey(player.getUniqueId())) {
            long lastTeleport = homeCooldowns.get(player.getUniqueId());
            long now = System.currentTimeMillis();
            long cooldownTime = homeCooldown * 1000L;
            
            if (now - lastTeleport < cooldownTime && !player.hasPermission("bdvital.home.bypass-cooldown")) {
                long remainingSeconds = (cooldownTime - (now - lastTeleport)) / 1000;
                player.sendMessage(ChatColor.RED + "You must wait " + remainingSeconds + " seconds before teleporting home.");
                return;
            }
        }
        
        // Teleport player
        Location homeLocation = getHome(player.getUniqueId(), homeName);
        player.teleport(homeLocation);
        player.sendMessage(ChatColor.GREEN + "Teleported to home '" + homeName + "'.");
        
        // Set cooldown
        homeCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    /**
     * Handles the sethome command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleSetHomeCommand(Player player, String[] args) {
        String homeName = args.length > 0 ? args[0].toLowerCase() : "home";
        
        // Check if player has reached the maximum number of homes
        int maxHomes = getMaxHomes(player);
        if (getHomeCount(player.getUniqueId()) >= maxHomes && !hasHome(player.getUniqueId(), homeName)) {
            player.sendMessage(ChatColor.RED + "You have reached the maximum number of homes (" + maxHomes + ").");
            return;
        }
        
        // Set home
        setHome(player.getUniqueId(), homeName, player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' set.");
    }
    
    /**
     * Handles the delhome command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleDelHomeCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /bddelhome <name>");
            return;
        }
        
        String homeName = args[0].toLowerCase();
        
        if (hasHome(player.getUniqueId(), homeName)) {
            removeHome(player.getUniqueId(), homeName);
            player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' deleted.");
        } else {
            player.sendMessage(ChatColor.RED + "Home '" + homeName + "' not found.");
        }
    }
}