package com.bdcraft.plugin.modules.vital;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.commands.CommandBase;
import com.bdcraft.plugin.commands.SubCommand;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.ModuleManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Module that provides essential utilities and commands.
 */
public class BDVitalModule implements BDModule {
    private final BDCraft plugin;
    private final ModuleManager moduleManager;
    private final Logger logger;
    private FileConfiguration config;
    
    /**
     * Creates a new vital module.
     * @param plugin The plugin instance
     * @param moduleManager The module manager
     */
    public BDVitalModule(BDCraft plugin, ModuleManager moduleManager) {
        this.plugin = plugin;
        this.moduleManager = moduleManager;
        this.logger = plugin.getLogger();
    }
    
    @Override
    public void onEnable() {
        logger.info("Enabling Vital Module...");
        
        // Load configuration
        config = plugin.getConfigManager().getModuleConfig("vital");
        
        // Register commands
        registerCommands();
        
        // Register event listeners
        // registerListeners();
        
        logger.info("Vital Module enabled!");
    }
    
    @Override
    public void onDisable() {
        logger.info("Disabling Vital Module...");
        
        // Save data
        // saveData();
        
        logger.info("Vital Module disabled!");
    }
    
    @Override
    public void onReload() {
        logger.info("Reloading Vital Module...");
        
        // Reload configuration
        config = plugin.getConfigManager().getModuleConfig("vital");
        
        // Apply configuration changes
        
        logger.info("Vital Module reloaded!");
    }
    
    @Override
    public String getName() {
        return "vital";
    }
    
    @Override
    public List<String> getDependencies() {
        // Vital module depends on perms
        return Collections.singletonList("perms");
    }
    
    /**
     * Registers commands for this module.
     */
    private void registerCommands() {
        // Main plugin command
        new CommandBase(plugin, "bdcraft", "bdcraft.admin") {
            {
                // Add subcommands
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "help";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Shows help information";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "[command]";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.help";
                    }
                    
                    @Override
                    public List<String> getAliases() {
                        return Arrays.asList("h", "?");
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        showHelp(sender);
                        return true;
                    }
                });
                
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "reload";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Reloads the plugin configuration";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.admin.reload";
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        // Reload the plugin configuration
                        plugin.getConfigManager().reloadConfig();
                        
                        // Reload modules
                        plugin.getModuleManager().reloadModules();
                        
                        sender.sendMessage(ChatColor.GREEN + "BDCraft configuration reloaded!");
                        return true;
                    }
                });
                
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "version";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Shows the plugin version";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.admin";
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        sender.sendMessage(ChatColor.GREEN + "BDCraft v" + plugin.getDescription().getVersion());
                        return true;
                    }
                });
            }
        };
        
        // Help command
        new CommandBase(plugin, "bdhelp", "bdcraft.help") {
            @Override
            protected void showHelp(CommandSender sender) {
                sender.sendMessage(ChatColor.GOLD + "=== " + ChatColor.YELLOW + "BDCraft Help" + 
                        ChatColor.GOLD + " ===");
                
                sender.sendMessage(ChatColor.YELLOW + "/bdcraft" + ChatColor.GRAY + " - Main plugin command");
                sender.sendMessage(ChatColor.YELLOW + "/bdhelp" + ChatColor.GRAY + " - Shows this help message");
                sender.sendMessage(ChatColor.YELLOW + "/bdreload" + ChatColor.GRAY + " - Reloads the plugin");
                sender.sendMessage(ChatColor.YELLOW + "/bdstatus" + ChatColor.GRAY + " - Shows plugin status");
                sender.sendMessage(ChatColor.YELLOW + "/bdhome" + ChatColor.GRAY + " - Home management");
                sender.sendMessage(ChatColor.YELLOW + "/bdmarket" + ChatColor.GRAY + " - Market management");
                sender.sendMessage(ChatColor.YELLOW + "/bdrank" + ChatColor.GRAY + " - Rank management");
                sender.sendMessage(ChatColor.YELLOW + "/bdcurrency" + ChatColor.GRAY + " - Currency commands");
                
                if (sender.hasPermission("bdcraft.admin")) {
                    sender.sendMessage(ChatColor.YELLOW + "/bdvillager" + ChatColor.GRAY + " - Villager management");
                }
            }
        };
        
        // Reload command
        new CommandBase(plugin, "bdreload", "bdcraft.admin.reload") {
            @Override
            protected void showHelp(CommandSender sender) {
                sender.sendMessage(ChatColor.GOLD + "=== " + ChatColor.YELLOW + "BDReload Help" + 
                        ChatColor.GOLD + " ===");
                
                sender.sendMessage(ChatColor.YELLOW + "/bdreload" + ChatColor.GRAY + " - Reloads the plugin configuration");
            }
            
            @Override
            public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                // Reload the plugin configuration
                plugin.getConfigManager().reloadConfig();
                
                // Reload modules
                plugin.getModuleManager().reloadModules();
                
                sender.sendMessage(ChatColor.GREEN + "BDCraft configuration reloaded!");
                return true;
            }
        };
        
        // Status command
        new CommandBase(plugin, "bdstatus", "bdcraft.admin.status") {
            @Override
            protected void showHelp(CommandSender sender) {
                sender.sendMessage(ChatColor.GOLD + "=== " + ChatColor.YELLOW + "BDStatus Help" + 
                        ChatColor.GOLD + " ===");
                
                sender.sendMessage(ChatColor.YELLOW + "/bdstatus" + ChatColor.GRAY + " - Shows status information about the plugin");
            }
            
            @Override
            public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                sender.sendMessage(ChatColor.GOLD + "=== " + ChatColor.YELLOW + "BDCraft Status" + 
                        ChatColor.GOLD + " ===");
                
                sender.sendMessage(ChatColor.YELLOW + "Version: " + ChatColor.WHITE + 
                        plugin.getDescription().getVersion());
                
                // Show module status
                sender.sendMessage(ChatColor.YELLOW + "Modules:");
                
                ModuleManager moduleManager = plugin.getModuleManager();
                sender.sendMessage(ChatColor.YELLOW + "  - Economy: " + ChatColor.GREEN + 
                        (moduleManager.getModule("economy") != null ? "Enabled" : "Disabled"));
                sender.sendMessage(ChatColor.YELLOW + "  - Permissions: " + ChatColor.GREEN + 
                        (moduleManager.getModule("perms") != null ? "Enabled" : "Disabled"));
                sender.sendMessage(ChatColor.YELLOW + "  - Vital: " + ChatColor.GREEN + 
                        (moduleManager.getModule("vital") != null ? "Enabled" : "Disabled"));
                
                return true;
            }
        };
        
        // Home command (basic implementation)
        new CommandBase(plugin, "bdhome", "bdcraft.home") {
            {
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "set";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Sets your home location";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "[name]";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.home.set";
                    }
                    
                    @Override
                    public boolean isPlayerOnly() {
                        return true;
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        Player player = (Player) sender;
                        String homeName = args.length > 0 ? args[0] : "default";
                        
                        // This would be implemented with actual home setting logic
                        sender.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' set to your current location!");
                        return true;
                    }
                });
                
                addSubCommand(new SubCommand() {
                    @Override
                    public String getName() {
                        return "list";
                    }
                    
                    @Override
                    public String getDescription() {
                        return "Lists your homes";
                    }
                    
                    @Override
                    public String getUsage() {
                        return "";
                    }
                    
                    @Override
                    public String getPermission() {
                        return "bdcraft.home.list";
                    }
                    
                    @Override
                    public boolean isPlayerOnly() {
                        return true;
                    }
                    
                    @Override
                    public boolean execute(CommandSender sender, String[] args) {
                        // This would be implemented with actual home listing logic
                        sender.sendMessage(ChatColor.YELLOW + "You have no homes set.");
                        return true;
                    }
                });
            }
            
            @Override
            protected void showHelp(CommandSender sender) {
                sender.sendMessage(ChatColor.GOLD + "=== " + ChatColor.YELLOW + "BDHome Help" + 
                        ChatColor.GOLD + " ===");
                
                sender.sendMessage(ChatColor.YELLOW + "/bdhome" + ChatColor.GRAY + " - Teleport to your default home");
                sender.sendMessage(ChatColor.YELLOW + "/bdhome set [name]" + ChatColor.GRAY + " - Set a home");
                sender.sendMessage(ChatColor.YELLOW + "/bdhome remove [name]" + ChatColor.GRAY + " - Remove a home");
                sender.sendMessage(ChatColor.YELLOW + "/bdhome list" + ChatColor.GRAY + " - List your homes");
            }
            
            @Override
            public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
                // Check permission
                if (!sender.hasPermission(permission)) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                
                // Check player-only
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                    return true;
                }
                
                // If no arguments, teleport to default home
                if (args.length == 0) {
                    Player player = (Player) sender;
                    
                    // This would be implemented with actual teleportation logic
                    sender.sendMessage(ChatColor.RED + "You don't have a default home set.");
                    return true;
                }
                
                // Otherwise, delegate to subcommands
                return super.onCommand(sender, command, label, args);
            }
        };
    }
}