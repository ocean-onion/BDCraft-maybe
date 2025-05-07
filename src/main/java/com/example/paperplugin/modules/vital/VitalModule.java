package com.example.paperplugin.modules.vital;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.module.AbstractBDModule;
import com.example.paperplugin.module.ModuleManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * Vital module implementation for the BDCraft plugin.
 * Handles essential server functions like spawn points, warps, and home management.
 */
public class VitalModule extends AbstractBDModule {

    private FileConfiguration vitalConfig;
    private File vitalConfigFile;
    
    // Constants for the module
    private static final String MODULE_NAME = "vital";
    private static final String CONFIG_FILE_NAME = "vital.yml";

    /**
     * Creates a new VitalModule.
     *
     * @param plugin The main plugin instance
     * @param moduleManager The module manager
     */
    public VitalModule(PaperPlugin plugin, ModuleManager moduleManager) {
        super(plugin, moduleManager);
    }

    /**
     * Called when the module is enabled.
     * Initializes the vital system.
     */
    @Override
    public void onEnable() {
        plugin.getLogger().info("Enabling Vital Module...");
        
        // Load vital configuration
        loadVitalConfig();
        
        // Register vital commands
        registerCommands();
        
        // Register the vital service
        registerVitalService();
        
        plugin.getLogger().info("Vital Module enabled successfully!");
    }

    /**
     * Called when the module is disabled.
     * Saves all vital data.
     */
    @Override
    public void onDisable() {
        plugin.getLogger().info("Disabling Vital Module...");
        
        // Save any pending changes
        saveVitalConfig();
        
        plugin.getLogger().info("Vital Module disabled successfully!");
    }

    /**
     * Called when the module is reloaded.
     * Reloads vital configuration.
     */
    @Override
    public void onReload() {
        plugin.getLogger().info("Reloading Vital Module...");
        
        // Reload vital configuration
        reloadVitalConfig();
        
        plugin.getLogger().info("Vital Module reloaded successfully!");
    }

    /**
     * Gets the module name.
     *
     * @return The module name
     */
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    /**
     * Gets the module dependencies.
     * 
     * @return List of module dependencies
     */
    @Override
    public List<String> getDependencies() {
        // Depends on the permissions module
        return Collections.singletonList("perms");
    }

    /**
     * Loads the vital configuration.
     */
    private void loadVitalConfig() {
        vitalConfigFile = new File(plugin.getDataFolder(), CONFIG_FILE_NAME);
        
        if (!vitalConfigFile.exists()) {
            plugin.saveResource(CONFIG_FILE_NAME, false);
        }
        
        vitalConfig = YamlConfiguration.loadConfiguration(vitalConfigFile);
        plugin.getLogger().info("Vital configuration loaded!");
    }

    /**
     * Reloads the vital configuration.
     */
    private void reloadVitalConfig() {
        vitalConfig = YamlConfiguration.loadConfiguration(vitalConfigFile);
        plugin.getLogger().info("Vital configuration reloaded!");
    }

    /**
     * Saves the vital configuration.
     */
    private void saveVitalConfig() {
        try {
            vitalConfig.save(vitalConfigFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save vital config!", e);
        }
    }

    /**
     * Registers the vital service with the module manager.
     */
    private void registerVitalService() {
        // Create the vital service implementation
        VitalService vitalService = new VitalServiceImpl(this);
        
        // Register it with the module manager
        moduleManager.registerService(VitalService.class, vitalService);
        
        plugin.getLogger().info("Vital service registered!");
    }
    
    /**
     * Registers vital commands.
     */
    private void registerCommands() {
        // Register spawn command
        plugin.getCommand("spawn").setExecutor(new SpawnCommand(plugin, this));
        
        // Register home commands
        plugin.getCommand("sethome").setExecutor(new SetHomeCommand(plugin, this));
        plugin.getCommand("home").setExecutor(new HomeCommand(plugin, this));
        plugin.getCommand("delhome").setExecutor(new DelHomeCommand(plugin, this));
        
        // Register warp commands
        plugin.getCommand("setwarp").setExecutor(new SetWarpCommand(plugin, this));
        plugin.getCommand("warp").setExecutor(new WarpCommand(plugin, this));
        plugin.getCommand("delwarp").setExecutor(new DelWarpCommand(plugin, this));
        
        plugin.getLogger().info("Vital commands registered!");
    }
    
    /**
     * Gets the vital configuration.
     * 
     * @return The vital configuration
     */
    public FileConfiguration getVitalConfig() {
        return vitalConfig;
    }
}