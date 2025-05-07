package com.example.paperplugin.modules.economy;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.module.AbstractBDModule;
import com.example.paperplugin.module.ModuleManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Economy module implementation for the BDCraft plugin.
 * Handles the BD economy system, including crops, villagers, and currency.
 */
public class EconomyModule extends AbstractBDModule {

    private FileConfiguration economyConfig;
    private File economyConfigFile;
    private final Map<UUID, Double> playerBalances;
    
    // Constants for the module
    private static final String MODULE_NAME = "economy";
    private static final String CONFIG_FILE_NAME = "economy.yml";

    /**
     * Creates a new EconomyModule.
     *
     * @param plugin The main plugin instance
     * @param moduleManager The module manager
     */
    public EconomyModule(PaperPlugin plugin, ModuleManager moduleManager) {
        super(plugin, moduleManager);
        this.playerBalances = new HashMap<>();
    }

    /**
     * Called when the module is enabled.
     * Initializes the economy system.
     */
    @Override
    public void onEnable() {
        plugin.getLogger().info("Enabling Economy Module...");
        
        // Load economy configuration
        loadEconomyConfig();
        
        // Load player balances from storage
        loadPlayerBalances();
        
        // Register the economy service
        registerEconomyService();
        
        plugin.getLogger().info("Economy Module enabled successfully!");
    }

    /**
     * Called when the module is disabled.
     * Saves all economy data.
     */
    @Override
    public void onDisable() {
        plugin.getLogger().info("Disabling Economy Module...");
        
        // Save player balances
        savePlayerBalances();
        
        plugin.getLogger().info("Economy Module disabled successfully!");
    }

    /**
     * Called when the module is reloaded.
     * Reloads economy configuration.
     */
    @Override
    public void onReload() {
        plugin.getLogger().info("Reloading Economy Module...");
        
        // Reload economy configuration
        reloadEconomyConfig();
        
        plugin.getLogger().info("Economy Module reloaded successfully!");
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
     * Loads the economy configuration.
     */
    private void loadEconomyConfig() {
        economyConfigFile = new File(plugin.getDataFolder(), CONFIG_FILE_NAME);
        
        if (!economyConfigFile.exists()) {
            plugin.saveResource(CONFIG_FILE_NAME, false);
        }
        
        economyConfig = YamlConfiguration.loadConfiguration(economyConfigFile);
        plugin.getLogger().info("Economy configuration loaded!");
    }

    /**
     * Reloads the economy configuration.
     */
    private void reloadEconomyConfig() {
        economyConfig = YamlConfiguration.loadConfiguration(economyConfigFile);
        plugin.getLogger().info("Economy configuration reloaded!");
    }

    /**
     * Saves the economy configuration.
     */
    private void saveEconomyConfig() {
        try {
            economyConfig.save(economyConfigFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save economy config!", e);
        }
    }

    /**
     * Loads player balances from storage.
     */
    private void loadPlayerBalances() {
        // For this example, we'll load from the config
        // In a real plugin, this might use a database
        if (economyConfig.contains("balances")) {
            for (String uuidString : economyConfig.getConfigurationSection("balances").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidString);
                double balance = economyConfig.getDouble("balances." + uuidString);
                playerBalances.put(uuid, balance);
            }
        }
        
        plugin.getLogger().info("Loaded balances for " + playerBalances.size() + " players");
    }

    /**
     * Saves player balances to storage.
     */
    private void savePlayerBalances() {
        // Clear existing balances in config
        economyConfig.set("balances", null);
        
        // Save current balances
        for (Map.Entry<UUID, Double> entry : playerBalances.entrySet()) {
            economyConfig.set("balances." + entry.getKey().toString(), entry.getValue());
        }
        
        saveEconomyConfig();
        plugin.getLogger().info("Saved balances for " + playerBalances.size() + " players");
    }

    /**
     * Registers the economy service with the module manager.
     */
    private void registerEconomyService() {
        // Create the economy service implementation
        EconomyService economyService = new EconomyServiceImpl(this);
        
        // Register it with the module manager
        moduleManager.registerService(EconomyService.class, economyService);
        
        plugin.getLogger().info("Economy service registered!");
    }

    /**
     * Gets a player's balance.
     *
     * @param playerId The player UUID
     * @return The player's balance
     */
    public double getBalance(UUID playerId) {
        return playerBalances.getOrDefault(playerId, 0.0);
    }

    /**
     * Sets a player's balance.
     *
     * @param playerId The player UUID
     * @param amount The new balance amount
     */
    public void setBalance(UUID playerId, double amount) {
        playerBalances.put(playerId, Math.max(0, amount));
    }

    /**
     * Adds to a player's balance.
     *
     * @param playerId The player UUID
     * @param amount The amount to add
     * @return The new balance
     */
    public double addBalance(UUID playerId, double amount) {
        double balance = getBalance(playerId) + amount;
        setBalance(playerId, balance);
        return balance;
    }

    /**
     * Subtracts from a player's balance.
     *
     * @param playerId The player UUID
     * @param amount The amount to subtract
     * @return true if the transaction was successful, false if insufficient funds
     */
    public boolean subtractBalance(UUID playerId, double amount) {
        double balance = getBalance(playerId);
        
        if (balance >= amount) {
            setBalance(playerId, balance - amount);
            return true;
        }
        
        return false;
    }

    /**
     * Gets the economy configuration.
     *
     * @return The economy configuration
     */
    public FileConfiguration getEconomyConfig() {
        return economyConfig;
    }
}