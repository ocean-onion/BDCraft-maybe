package com.bdcraft.plugin.modules.logging;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.Module;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Module for logging BDCraft activities.
 */
public class BDLoggingModule implements Module {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<String, List<LogEntry>> logEntries = new ConcurrentHashMap<>();
    private final String logsFilePath;
    private boolean enabled = false;
    private int logRetentionDays;
    private BukkitTask autoSaveTask;
    private BukkitTask logCleanupTask;
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Creates a new logging module.
     * @param plugin The plugin instance
     */
    public BDLoggingModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.logsFilePath = plugin.getDataFolder() + "/data/bdlogs/logs.json";
    }
    
    @Override
    public String getName() {
        return "Logging";
    }
    
    @Override
    public void enable() {
        if (enabled) {
            return;
        }
        
        logger.info("Enabling BD Logging Module");
        
        // Create data directory
        File dataDir = new File(plugin.getDataFolder() + "/data/bdlogs");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Load config
        loadConfig();
        
        // Load existing logs
        loadLogs();
        
        // Register commands
        registerCommands();
        
        // Schedule auto-save task (every 5 minutes)
        autoSaveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::saveLogs, 
                20 * 60 * 5, 20 * 60 * 5);
        
        // Schedule log cleanup task (once a day)
        logCleanupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::cleanupOldLogs, 
                20 * 60 * 60, 20 * 60 * 60 * 24);
        
        enabled = true;
        
        logger.info("BD Logging Module enabled!");
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        logger.info("Disabling BD Logging Module");
        
        // Save logs
        saveLogs();
        
        // Cancel tasks
        if (autoSaveTask != null) {
            autoSaveTask.cancel();
            autoSaveTask = null;
        }
        
        if (logCleanupTask != null) {
            logCleanupTask.cancel();
            logCleanupTask = null;
        }
        
        enabled = false;
        
        logger.info("BD Logging Module disabled!");
    }
    
    /**
     * Loads configuration settings.
     */
    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        
        // Load log retention days (default: 30)
        logRetentionDays = config.getInt("bdlogging.retention-days", 30);
    }
    
    /**
     * Loads logs from the data file.
     */
    private void loadLogs() {
        File file = new File(logsFilePath);
        
        if (!file.exists()) {
            return;
        }
        
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type type = new TypeToken<Map<String, List<LogEntry>>>(){}.getType();
            Map<String, List<LogEntry>> logs = gson.fromJson(reader, type);
            
            if (logs != null) {
                logEntries.putAll(logs);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to load logs", e);
        }
    }
    
    /**
     * Saves logs to the data file.
     */
    public void saveLogs() {
        File file = new File(logsFilePath);
        
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(logEntries, writer);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to save logs", e);
        }
    }
    
    /**
     * Cleans up old logs based on retention policy.
     */
    private void cleanupOldLogs() {
        // Get current time
        long now = Instant.now().getEpochSecond();
        long cutoff = now - (logRetentionDays * 24 * 60 * 60);
        
        // Clean up logs
        int removed = 0;
        
        for (Map.Entry<String, List<LogEntry>> entry : logEntries.entrySet()) {
            List<LogEntry> logs = entry.getValue();
            int sizeBefore = logs.size();
            
            // Remove old logs
            logs.removeIf(log -> log.timestamp < cutoff);
            
            // Update removed count
            removed += sizeBefore - logs.size();
            
            // Remove entry if no logs left
            if (logs.isEmpty()) {
                logEntries.remove(entry.getKey());
            }
        }
        
        // Log cleanup
        if (removed > 0) {
            logger.info("Cleaned up " + removed + " old logs");
            
            // Save logs
            saveLogs();
        }
    }
    
    /**
     * Logs an action.
     * @param category The log category
     * @param action The action
     * @param playerId The player ID
     * @param playerName The player name
     * @param target The target (optional)
     * @param location The location (optional)
     * @param details Additional details (optional)
     */
    public void logAction(LogCategory category, String action, UUID playerId, String playerName, 
            String target, Location location, Map<String, Object> details) {
        
        if (!enabled) {
            return;
        }
        
        // Create key
        String key = category.name().toLowerCase();
        
        // Get or create logs list
        List<LogEntry> logs = logEntries.computeIfAbsent(key, k -> new ArrayList<>());
        
        // Create log entry
        LogEntry entry = new LogEntry();
        entry.timestamp = Instant.now().getEpochSecond();
        entry.category = category.name();
        entry.action = action;
        entry.playerId = playerId != null ? playerId.toString() : null;
        entry.playerName = playerName;
        entry.target = target;
        
        if (location != null) {
            entry.world = location.getWorld().getName();
            entry.x = location.getBlockX();
            entry.y = location.getBlockY();
            entry.z = location.getBlockZ();
        }
        
        if (details != null) {
            entry.details = new HashMap<>(details);
        }
        
        // Add log entry
        logs.add(entry);
        
        // Limit list size to prevent memory issues (keep last 10,000 entries)
        if (logs.size() > 10000) {
            logs = logs.subList(logs.size() - 10000, logs.size());
            logEntries.put(key, logs);
        }
    }
    
    /**
     * Gets logs by category.
     * @param category The category
     * @param limit The maximum number of logs to return
     * @return The logs
     */
    public List<LogEntry> getLogsByCategory(LogCategory category, int limit) {
        if (!enabled) {
            return new ArrayList<>();
        }
        
        String key = category.name().toLowerCase();
        List<LogEntry> logs = logEntries.get(key);
        
        if (logs == null) {
            return new ArrayList<>();
        }
        
        // Sort by timestamp (newest first) and limit
        return logs.stream()
                .sorted(Comparator.comparingLong(log -> -log.timestamp))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets logs by player.
     * @param playerId The player ID
     * @param limit The maximum number of logs to return
     * @return The logs
     */
    public List<LogEntry> getLogsByPlayer(UUID playerId, int limit) {
        if (!enabled || playerId == null) {
            return new ArrayList<>();
        }
        
        List<LogEntry> result = new ArrayList<>();
        
        // Collect logs from all categories
        for (List<LogEntry> logs : logEntries.values()) {
            for (LogEntry log : logs) {
                if (playerId.toString().equals(log.playerId)) {
                    result.add(log);
                }
            }
        }
        
        // Sort by timestamp (newest first) and limit
        return result.stream()
                .sorted(Comparator.comparingLong(log -> -log.timestamp))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets logs by action.
     * @param action The action
     * @param limit The maximum number of logs to return
     * @return The logs
     */
    public List<LogEntry> getLogsByAction(String action, int limit) {
        if (!enabled || action == null) {
            return new ArrayList<>();
        }
        
        List<LogEntry> result = new ArrayList<>();
        
        // Collect logs from all categories
        for (List<LogEntry> logs : logEntries.values()) {
            for (LogEntry log : logs) {
                if (action.equalsIgnoreCase(log.action)) {
                    result.add(log);
                }
            }
        }
        
        // Sort by timestamp (newest first) and limit
        return result.stream()
                .sorted(Comparator.comparingLong(log -> -log.timestamp))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets logs by time period.
     * @param hours The number of hours
     * @param limit The maximum number of logs to return
     * @return The logs
     */
    public List<LogEntry> getLogsByTime(int hours, int limit) {
        if (!enabled) {
            return new ArrayList<>();
        }
        
        // Calculate cutoff time
        long now = Instant.now().getEpochSecond();
        long cutoff = now - (hours * 60 * 60);
        
        List<LogEntry> result = new ArrayList<>();
        
        // Collect logs from all categories
        for (List<LogEntry> logs : logEntries.values()) {
            for (LogEntry log : logs) {
                if (log.timestamp >= cutoff) {
                    result.add(log);
                }
            }
        }
        
        // Sort by timestamp (newest first) and limit
        return result.stream()
                .sorted(Comparator.comparingLong(log -> -log.timestamp))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets logs by location radius.
     * @param location The center location
     * @param radius The radius
     * @param limit The maximum number of logs to return
     * @return The logs
     */
    public List<LogEntry> getLogsByRadius(Location location, int radius, int limit) {
        if (!enabled || location == null) {
            return new ArrayList<>();
        }
        
        String world = location.getWorld().getName();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        
        List<LogEntry> result = new ArrayList<>();
        
        // Collect logs from all categories
        for (List<LogEntry> logs : logEntries.values()) {
            for (LogEntry log : logs) {
                if (world.equals(log.world)) {
                    // Calculate distance (approximation)
                    double distance = Math.sqrt(
                            Math.pow(x - log.x, 2) +
                            Math.pow(y - log.y, 2) +
                            Math.pow(z - log.z, 2)
                    );
                    
                    if (distance <= radius) {
                        result.add(log);
                    }
                }
            }
        }
        
        // Sort by timestamp (newest first) and limit
        return result.stream()
                .sorted(Comparator.comparingLong(log -> -log.timestamp))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Creates a simple log message for a player.
     * @param entry The log entry
     * @return The log message
     */
    public String formatLogEntry(LogEntry entry) {
        if (entry == null) {
            return "Invalid log entry";
        }
        
        // Format timestamp
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(entry.timestamp),
                ZoneId.systemDefault()
        );
        String time = dateTime.format(DATE_FORMAT);
        
        // Build message
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.GRAY).append("[").append(time).append("] ");
        message.append(ChatColor.YELLOW).append(entry.action);
        
        if (entry.playerName != null) {
            message.append(ChatColor.GRAY).append(" by ").append(ChatColor.AQUA).append(entry.playerName);
        }
        
        if (entry.target != null) {
            message.append(ChatColor.GRAY).append(" | Target: ").append(ChatColor.GREEN).append(entry.target);
        }
        
        if (entry.world != null) {
            message.append(ChatColor.GRAY).append(" | Location: ").append(ChatColor.WHITE)
                    .append(entry.world).append(" (").append(entry.x).append(", ")
                    .append(entry.y).append(", ").append(entry.z).append(")");
        }
        
        return message.toString();
    }
    
    /**
     * Registers the logging commands.
     */
    private void registerCommands() {
        // Register a command handler for the logs command
        CommandExecutor logsCommandExecutor = new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!sender.hasPermission("bdcraft.admin.logs")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.GOLD + "=== BD Logs Help ===");
                    sender.sendMessage(ChatColor.YELLOW + "/bdlogs list [limit]" + ChatColor.GRAY + " - List recent logs");
                    sender.sendMessage(ChatColor.YELLOW + "/bdlogs player <name> [limit]" + ChatColor.GRAY + " - Player logs");
                    sender.sendMessage(ChatColor.YELLOW + "/bdlogs action <type> [limit]" + ChatColor.GRAY + " - Logs by action");
                    sender.sendMessage(ChatColor.YELLOW + "/bdlogs time <hours> [limit]" + ChatColor.GRAY + " - Logs by time");
                    sender.sendMessage(ChatColor.YELLOW + "/bdlogs radius <blocks> [limit]" + ChatColor.GRAY + " - Logs by location");
                    return true;
                }
                
                String subCommand = args[0].toLowerCase();
                
                if (subCommand.equals("list")) {
                    int limit = 10;
                    
                    if (args.length > 1) {
                        try {
                            limit = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid limit: " + args[1]);
                            return true;
                        }
                    }
                    
                    List<LogEntry> allLogs = new ArrayList<>();
                    
                    for (List<LogEntry> logs : logEntries.values()) {
                        allLogs.addAll(logs);
                    }
                    
                    List<LogEntry> recentLogs = allLogs.stream()
                            .sorted(Comparator.comparingLong(log -> -log.timestamp))
                            .limit(limit)
                            .collect(Collectors.toList());
                    
                    sender.sendMessage(ChatColor.GOLD + "=== Recent Logs ===");
                    
                    if (recentLogs.isEmpty()) {
                        sender.sendMessage(ChatColor.YELLOW + "No logs found.");
                        return true;
                    }
                    
                    for (LogEntry log : recentLogs) {
                        sender.sendMessage(formatLogEntry(log));
                    }
                    
                    return true;
                } else if (subCommand.equals("player")) {
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Usage: /bdlogs player <name> [limit]");
                        return true;
                    }
                    
                    String playerName = args[1];
                    int limit = 10;
                    
                    if (args.length > 2) {
                        try {
                            limit = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid limit: " + args[2]);
                            return true;
                        }
                    }
                    
                    Player player = Bukkit.getPlayer(playerName);
                    UUID playerId = player != null ? player.getUniqueId() : null;
                    
                    List<LogEntry> playerLogs;
                    
                    if (playerId != null) {
                        playerLogs = getLogsByPlayer(playerId, limit);
                    } else {
                        List<LogEntry> allLogs = new ArrayList<>();
                        
                        for (List<LogEntry> logs : logEntries.values()) {
                            for (LogEntry log : logs) {
                                if (playerName.equalsIgnoreCase(log.playerName)) {
                                    allLogs.add(log);
                                }
                            }
                        }
                        
                        playerLogs = allLogs.stream()
                                .sorted(Comparator.comparingLong(log -> -log.timestamp))
                                .limit(limit)
                                .collect(Collectors.toList());
                    }
                    
                    sender.sendMessage(ChatColor.GOLD + "=== Logs for " + playerName + " ===");
                    
                    if (playerLogs.isEmpty()) {
                        sender.sendMessage(ChatColor.YELLOW + "No logs found for this player.");
                        return true;
                    }
                    
                    for (LogEntry log : playerLogs) {
                        sender.sendMessage(formatLogEntry(log));
                    }
                    
                    return true;
                } else if (subCommand.equals("action")) {
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Usage: /bdlogs action <type> [limit]");
                        return true;
                    }
                    
                    String action = args[1];
                    int limit = 10;
                    
                    if (args.length > 2) {
                        try {
                            limit = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid limit: " + args[2]);
                            return true;
                        }
                    }
                    
                    List<LogEntry> actionLogs = getLogsByAction(action, limit);
                    
                    sender.sendMessage(ChatColor.GOLD + "=== Logs for action: " + action + " ===");
                    
                    if (actionLogs.isEmpty()) {
                        sender.sendMessage(ChatColor.YELLOW + "No logs found for this action.");
                        return true;
                    }
                    
                    for (LogEntry log : actionLogs) {
                        sender.sendMessage(formatLogEntry(log));
                    }
                    
                    return true;
                } else if (subCommand.equals("time")) {
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Usage: /bdlogs time <hours> [limit]");
                        return true;
                    }
                    
                    int hours;
                    try {
                        hours = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Invalid hours: " + args[1]);
                        return true;
                    }
                    
                    int limit = 10;
                    
                    if (args.length > 2) {
                        try {
                            limit = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid limit: " + args[2]);
                            return true;
                        }
                    }
                    
                    List<LogEntry> timeLogs = getLogsByTime(hours, limit);
                    
                    sender.sendMessage(ChatColor.GOLD + "=== Logs for the last " + hours + " hours ===");
                    
                    if (timeLogs.isEmpty()) {
                        sender.sendMessage(ChatColor.YELLOW + "No logs found for this time period.");
                        return true;
                    }
                    
                    for (LogEntry log : timeLogs) {
                        sender.sendMessage(formatLogEntry(log));
                    }
                    
                    return true;
                } else if (subCommand.equals("radius")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                        return true;
                    }
                    
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Usage: /bdlogs radius <blocks> [limit]");
                        return true;
                    }
                    
                    int radius;
                    try {
                        radius = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "Invalid radius: " + args[1]);
                        return true;
                    }
                    
                    int limit = 10;
                    
                    if (args.length > 2) {
                        try {
                            limit = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid limit: " + args[2]);
                            return true;
                        }
                    }
                    
                    Player player = (Player) sender;
                    Location location = player.getLocation();
                    
                    List<LogEntry> radiusLogs = getLogsByRadius(location, radius, limit);
                    
                    sender.sendMessage(ChatColor.GOLD + "=== Logs within " + radius + " blocks ===");
                    
                    if (radiusLogs.isEmpty()) {
                        sender.sendMessage(ChatColor.YELLOW + "No logs found in this area.");
                        return true;
                    }
                    
                    for (LogEntry log : radiusLogs) {
                        sender.sendMessage(formatLogEntry(log));
                    }
                    
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Unknown logs subcommand: " + subCommand);
                    return true;
                }
            }
        };
        
        // Register the command executor
        plugin.getCommand("bdlogs").setExecutor(logsCommandExecutor);
    }
    
    /**
     * An enum for log categories.
     */
    public enum LogCategory {
        MARKET,
        ECONOMY,
        PROGRESSION,
        ADMIN,
        PLAYER,
        VILLAGER,
        ITEM
    }
    
    /**
     * A class representing a log entry.
     */
    public static class LogEntry {
        public long timestamp;
        public String category;
        public String action;
        public String playerId;
        public String playerName;
        public String target;
        public String world;
        public int x;
        public int y;
        public int z;
        public Map<String, Object> details;
    }
}