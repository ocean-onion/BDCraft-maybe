package com.bdcraft.plugin.modules.vital.modules.teleport;

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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages teleportation as a submodule of BDVital.
 */
public class TeleportModule implements SubmoduleBase, Listener, CommandExecutor {
    private final BDCraft plugin;
    private ModuleManager parentModule;
    private boolean enabled = false;
    
    // Teleport settings
    private int tpaCooldown;
    private int tpaTimeout;
    private int backCooldown;
    private boolean enableBack;
    
    // Maps player UUIDs to their last locations
    private final Map<UUID, Location> lastLocations = new HashMap<>();
    
    // Maps player UUIDs to their death locations
    private final Map<UUID, Location> deathLocations = new HashMap<>();
    
    // Maps target UUIDs to pending teleport requests
    private final Map<UUID, TeleportRequest> pendingRequests = new HashMap<>();
    
    private final Map<UUID, Long> backCooldowns = new HashMap<>();
    
    /**
     * Creates a new teleport module.
     * 
     * @param plugin The plugin instance
     */
    public TeleportModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Teleport";
    }
    
    @Override
    public void enable(ModuleManager parentModule) {
        if (enabled) {
            return;
        }
        
        this.parentModule = parentModule;
        
        plugin.getLogger().info("Enabling Teleport submodule");
        
        // Load config
        loadConfig();
        
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
        
        plugin.getLogger().info("Disabling Teleport submodule");
        
        // Clean up
        lastLocations.clear();
        deathLocations.clear();
        pendingRequests.clear();
        
        // Unregister events
        HandlerList.unregisterAll(this);
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        loadConfig();
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
        tpaCooldown = config.getInt("teleport.tpa.cooldown", 60);
        tpaTimeout = config.getInt("teleport.tpa.timeout", 60);
        backCooldown = config.getInt("teleport.back.cooldown", 60);
        enableBack = config.getBoolean("teleport.back.enabled", true);
    }
    
    /**
     * Registers commands.
     */
    private void registerCommands() {
        // Register tpa command
        PluginCommand tpaCommand = plugin.getCommand("bdtpa");
        if (tpaCommand != null) {
            tpaCommand.setExecutor(this);
        }
        
        // Register tpaccept command
        PluginCommand tpacceptCommand = plugin.getCommand("bdtpaccept");
        if (tpacceptCommand != null) {
            tpacceptCommand.setExecutor(this);
        }
        
        // Register tpdeny command
        PluginCommand tpdenyCommand = plugin.getCommand("bdtpdeny");
        if (tpdenyCommand != null) {
            tpdenyCommand.setExecutor(this);
        }
        
        // Register back command
        PluginCommand backCommand = plugin.getCommand("bdback");
        if (backCommand != null) {
            backCommand.setExecutor(this);
        }
    }
    
    /**
     * Saves a player's last location.
     * @param player The player
     */
    public void saveLastLocation(Player player) {
        lastLocations.put(player.getUniqueId(), player.getLocation());
    }
    
    /**
     * Gets a player's last location.
     * @param playerUuid The player UUID
     * @return The last location, or null if not found
     */
    public Location getLastLocation(UUID playerUuid) {
        return lastLocations.get(playerUuid);
    }
    
    /**
     * Saves a player's death location.
     * @param player The player
     */
    public void saveDeathLocation(Player player) {
        deathLocations.put(player.getUniqueId(), player.getLocation());
    }
    
    /**
     * Gets a player's death location.
     * @param playerUuid The player UUID
     * @return The death location, or null if not found
     */
    public Location getDeathLocation(UUID playerUuid) {
        return deathLocations.get(playerUuid);
    }
    
    /**
     * Sends a teleport request.
     * @param requester The requester
     * @param target The target
     * @param type The teleport type
     */
    public void sendTeleportRequest(Player requester, Player target, TeleportType type) {
        // Remove any existing request
        pendingRequests.remove(target.getUniqueId());
        
        // Create new request
        TeleportRequest request = new TeleportRequest(requester.getUniqueId(), type);
        pendingRequests.put(target.getUniqueId(), request);
        
        // Schedule timeout
        new BukkitRunnable() {
            @Override
            public void run() {
                TeleportRequest currentRequest = pendingRequests.get(target.getUniqueId());
                
                if (currentRequest != null && currentRequest.equals(request) && !currentRequest.isExpired()) {
                    pendingRequests.remove(target.getUniqueId());
                    
                    Player requesterPlayer = plugin.getServer().getPlayer(request.getRequesterUuid());
                    
                    if (requesterPlayer != null) {
                        requesterPlayer.sendMessage(ChatColor.RED + "Your teleport request to " + 
                                target.getName() + " has expired.");
                    }
                    
                    if (target.isOnline()) {
                        target.sendMessage(ChatColor.RED + "The teleport request from " + 
                                (requesterPlayer != null ? requesterPlayer.getName() : "someone") + 
                                " has expired.");
                    }
                }
            }
        }.runTaskLater(plugin, tpaTimeout * 20L);
    }
    
    /**
     * Gets a pending teleport request.
     * @param targetUuid The target UUID
     * @return The teleport request, or null if not found
     */
    public TeleportRequest getPendingRequest(UUID targetUuid) {
        TeleportRequest request = pendingRequests.get(targetUuid);
        
        if (request != null && request.isExpired()) {
            pendingRequests.remove(targetUuid);
            return null;
        }
        
        return request;
    }
    
    /**
     * Removes a pending teleport request.
     * @param targetUuid The target UUID
     */
    public void removePendingRequest(UUID targetUuid) {
        pendingRequests.remove(targetUuid);
    }
    
    /**
     * Accepts a teleport request.
     * @param target The target
     * @param requester The requester
     * @param type The teleport type
     */
    public void acceptRequest(Player target, Player requester, TeleportType type) {
        // Remove request
        pendingRequests.remove(target.getUniqueId());
        
        // Save last locations
        saveLastLocation(target);
        saveLastLocation(requester);
        
        // Perform teleport
        if (type == TeleportType.TO_TARGET) {
            requester.teleport(target.getLocation());
            requester.sendMessage(ChatColor.GREEN + "Teleported to " + target.getName() + ".");
            target.sendMessage(ChatColor.GREEN + requester.getName() + " teleported to you.");
        } else {
            target.teleport(requester.getLocation());
            target.sendMessage(ChatColor.GREEN + "Teleported to " + requester.getName() + ".");
            requester.sendMessage(ChatColor.GREEN + target.getName() + " teleported to you.");
        }
    }
    
    /**
     * Clears all teleport data for a player.
     * @param playerUuid The player UUID
     */
    public void clearPlayerData(UUID playerUuid) {
        lastLocations.remove(playerUuid);
        deathLocations.remove(playerUuid);
        pendingRequests.remove(playerUuid);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("bdtpa")) {
            handleTpaCommand(player, args);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdtpaccept")) {
            handleTpAcceptCommand(player);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdtpdeny")) {
            handleTpDenyCommand(player);
            return true;
        } else if (command.getName().equalsIgnoreCase("bdback")) {
            handleBackCommand(player);
            return true;
        }
        
        return false;
    }
    
    /**
     * Handles the tpa command.
     * 
     * @param player The player
     * @param args The arguments
     */
    private void handleTpaCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /bdtpa <player>");
            return;
        }
        
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }
        
        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "You cannot teleport to yourself.");
            return;
        }
        
        // Send teleport request
        sendTeleportRequest(player, target, TeleportType.TO_TARGET);
        
        // Send messages
        player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + target.getName() + ".");
        target.sendMessage(ChatColor.YELLOW + player.getName() + " has requested to teleport to you.");
        target.sendMessage(ChatColor.YELLOW + "Type /bdtpaccept to accept or /bdtpdeny to deny.");
    }
    
    /**
     * Handles the tpaccept command.
     * 
     * @param player The player
     */
    private void handleTpAcceptCommand(Player player) {
        TeleportRequest request = getPendingRequest(player.getUniqueId());
        
        if (request == null) {
            player.sendMessage(ChatColor.RED + "You have no pending teleport requests.");
            return;
        }
        
        UUID requesterUuid = request.getRequesterUuid();
        Player requester = plugin.getServer().getPlayer(requesterUuid);
        
        if (requester == null || !requester.isOnline()) {
            player.sendMessage(ChatColor.RED + "The player who requested to teleport is no longer online.");
            removePendingRequest(player.getUniqueId());
            return;
        }
        
        // Accept request
        acceptRequest(player, requester, request.getType());
    }
    
    /**
     * Handles the tpdeny command.
     * 
     * @param player The player
     */
    private void handleTpDenyCommand(Player player) {
        TeleportRequest request = getPendingRequest(player.getUniqueId());
        
        if (request == null) {
            player.sendMessage(ChatColor.RED + "You have no pending teleport requests.");
            return;
        }
        
        UUID requesterUuid = request.getRequesterUuid();
        Player requester = plugin.getServer().getPlayer(requesterUuid);
        
        // Send messages
        player.sendMessage(ChatColor.GREEN + "Teleport request denied.");
        
        if (requester != null && requester.isOnline()) {
            requester.sendMessage(ChatColor.RED + player.getName() + " denied your teleport request.");
        }
        
        // Remove request
        removePendingRequest(player.getUniqueId());
    }
    
    /**
     * Handles the back command.
     * 
     * @param player The player
     */
    private void handleBackCommand(Player player) {
        if (!enableBack) {
            player.sendMessage(ChatColor.RED + "The back command is disabled.");
            return;
        }
        
        if (!player.hasPermission("bdvital.teleport.back")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use the back command.");
            return;
        }
        
        Location lastLocation = getLastLocation(player.getUniqueId());
        if (lastLocation == null) {
            player.sendMessage(ChatColor.RED + "You have no previous location to teleport to.");
            return;
        }
        
        // Check cooldown
        if (backCooldowns.containsKey(player.getUniqueId())) {
            long lastBack = backCooldowns.get(player.getUniqueId());
            long now = System.currentTimeMillis();
            long cooldownTime = backCooldown * 1000L;
            
            if (now - lastBack < cooldownTime && !player.hasPermission("bdvital.teleport.bypass-cooldown")) {
                long remainingSeconds = (cooldownTime - (now - lastBack)) / 1000;
                player.sendMessage(ChatColor.RED + "You must wait " + remainingSeconds + " seconds before using the back command again.");
                return;
            }
        }
        
        // Save current location
        Location currentLocation = player.getLocation().clone();
        
        // Teleport to last location
        player.teleport(lastLocation);
        player.sendMessage(ChatColor.GREEN + "Teleported to your previous location.");
        
        // Update last location (for /back to bring back to where they were)
        lastLocations.put(player.getUniqueId(), currentLocation);
        
        // Set cooldown
        backCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    /**
     * Handles player join events.
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Clean up any teleport requests to or from this player
        for (UUID targetUuid : pendingRequests.keySet()) {
            TeleportRequest request = pendingRequests.get(targetUuid);
            if (request != null && request.getRequesterUuid().equals(player.getUniqueId())) {
                pendingRequests.remove(targetUuid);
            }
        }
    }
    
    /**
     * Teleport request class.
     */
    public static class TeleportRequest {
        private final UUID requesterUuid;
        private final TeleportType type;
        private final long timestamp;
        
        /**
         * Creates a new teleport request.
         * @param requesterUuid The requester UUID
         * @param type The teleport type
         */
        public TeleportRequest(UUID requesterUuid, TeleportType type) {
            this.requesterUuid = requesterUuid;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
        
        /**
         * Gets the requester UUID.
         * @return The requester UUID
         */
        public UUID getRequesterUuid() {
            return requesterUuid;
        }
        
        /**
         * Gets the teleport type.
         * @return The teleport type
         */
        public TeleportType getType() {
            return type;
        }
        
        /**
         * Checks if the request is expired.
         * @return Whether the request is expired
         */
        public boolean isExpired() {
            // Default timeout is 60 seconds
            return System.currentTimeMillis() - timestamp > 60 * 1000L;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            
            TeleportRequest that = (TeleportRequest) obj;
            
            return requesterUuid.equals(that.requesterUuid) && type == that.type;
        }
        
        @Override
        public int hashCode() {
            int result = requesterUuid.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }
    }
    
    /**
     * Teleport type enum.
     */
    public enum TeleportType {
        TO_TARGET,
        TO_REQUESTER
    }
}