package com.bdcraft.plugin.modules.progression;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.progression.modules.rebirth.BDRebirthModule;
import com.bdcraft.plugin.modules.progression.modules.rank.BDRankModule;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Main progression module that manages all progression-related features.
 * This class implements the ProgressionAPI interface to provide a standardized way 
 * for other plugins to interact with the progression system.
 */
public class BDProgressionModule implements ModuleManager, ProgressionManager, com.bdcraft.plugin.api.ProgressionAPI {
    private final BDCraft plugin;
    private boolean enabled = false;
    private final Map<String, SubmoduleBase> submodules = new HashMap<>();
    
    /**
     * Creates a new progression module.
     * 
     * @param plugin The plugin instance
     */
    public BDProgressionModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Progression";
    }
    
    @Override
    public void enable(BDCraft plugin) {
        if (enabled) {
            return;
        }
        
        plugin.getLogger().info("Enabling BDProgression module");
        
        // Register and enable submodules
        registerSubmodules();
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling BDProgression module");
        
        // Disable submodules
        for (SubmoduleBase submodule : submodules.values()) {
            try {
                submodule.disable();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error disabling submodule " + submodule.getName(), e);
            }
        }
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        // Reload submodules
        for (SubmoduleBase submodule : submodules.values()) {
            try {
                submodule.reload();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error reloading submodule " + submodule.getName(), e);
            }
        }
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public Object getSubmodule(String name) {
        return submodules.get(name);
    }
    
    @Override
    public void registerSubmodule(Object submodule) {
        if (!(submodule instanceof SubmoduleBase)) {
            throw new IllegalArgumentException("Submodule must implement SubmoduleBase");
        }
        
        SubmoduleBase base = (SubmoduleBase) submodule;
        submodules.put(base.getName(), base);
        
        if (enabled) {
            // Enable the submodule if the parent module is already enabled
            base.enable(this);
        }
    }
    
    /**
     * Registers all submodules.
     */
    private void registerSubmodules() {
        // Register rebirth module
        registerSubmodule(new BDRebirthModule(plugin));
        
        // Register rank module
        registerSubmodule(new BDRankModule(plugin));
        
        // Enable all submodules
        for (SubmoduleBase submodule : submodules.values()) {
            boolean enableSubmodule = plugin.getConfig(ConfigType.CONFIG)
                    .getBoolean("modules.progression." + submodule.getName().toLowerCase() + ".enabled", true);
            
            if (enableSubmodule) {
                try {
                    submodule.enable(this);
                    plugin.getLogger().info("Enabled " + submodule.getName() + " submodule");
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error enabling submodule " + submodule.getName(), e);
                }
            }
        }
    }
    
    // ProgressionManager implementation
    
    @Override
    public Rank getPlayerRank(UUID playerId) {
        BDRankModule rankModule = (BDRankModule) getSubmodule("Rank");
        if (rankModule != null && rankModule.isEnabled()) {
            return rankModule.getPlayerRank(playerId);
        }
        return Rank.DEFAULT; // Return default rank if module not available
    }
    
    @Override
    public int getRebirthLevel(UUID playerId) {
        BDRebirthModule rebirthModule = (BDRebirthModule) getSubmodule("Rebirth");
        if (rebirthModule != null && rebirthModule.isEnabled()) {
            return rebirthModule.getRebirthLevel(playerId);
        }
        return 0; // Return 0 if module not available
    }
    
    @Override
    public boolean performRebirth(UUID playerId) {
        BDRebirthModule rebirthModule = (BDRebirthModule) getSubmodule("Rebirth");
        if (rebirthModule != null && rebirthModule.isEnabled()) {
            return rebirthModule.performRebirth(playerId);
        }
        return false; // Return false if module not available
    }
    
    @Override
    public boolean setRank(UUID playerId, Rank rank) {
        BDRankModule rankModule = (BDRankModule) getSubmodule("Rank");
        if (rankModule != null && rankModule.isEnabled()) {
            return rankModule.setPlayerRank(playerId, rank);
        }
        return false; // Return false if module not available
    }
    
    /**
     * Adds experience to a player.
     * 
     * @param playerId The player's UUID
     * @param experienceToAdd The amount of experience to add
     * @return The new experience total
     */
    public int addPlayerExperience(UUID playerId, int experienceToAdd) {
        BDRankModule rankModule = (BDRankModule) getSubmodule("Rank");
        if (rankModule != null && rankModule.isEnabled()) {
            return rankModule.addPlayerExperience(playerId, experienceToAdd);
        }
        return 0; // Return 0 if module not available
    }
    
    /**
     * Adds experience to a player.
     * 
     * @param player The player
     * @param experienceToAdd The amount of experience to add
     * @return The new experience total
     */
    public int addPlayerExperience(org.bukkit.entity.Player player, int experienceToAdd) {
        return addPlayerExperience(player.getUniqueId(), experienceToAdd);
    }
    
    /**
     * Gets a player's experience.
     * 
     * @param playerId The player's UUID
     * @return The player's experience
     */
    public int getPlayerExperience(UUID playerId) {
        BDRankModule rankModule = (BDRankModule) getSubmodule("Rank");
        if (rankModule != null && rankModule.isEnabled()) {
            return rankModule.getPlayerExperience(playerId);
        }
        return 0; // Return 0 if module not available
    }
    
    /**
     * Sets a player's experience.
     * 
     * @param playerId The player's UUID
     * @param experience The experience to set
     */
    public void setPlayerExperience(UUID playerId, int experience) {
        BDRankModule rankModule = (BDRankModule) getSubmodule("Rank");
        if (rankModule != null && rankModule.isEnabled()) {
            rankModule.setPlayerExperience(playerId, experience);
        }
    }
    
    /**
     * Gets the player's progress percentage to the next rank.
     * 
     * @param playerId The player's UUID
     * @return The progress percentage (0-100)
     */
    public double getProgressPercentage(UUID playerId) {
        BDRankModule rankModule = (BDRankModule) getSubmodule("Rank");
        if (rankModule != null && rankModule.isEnabled()) {
            return rankModule.getProgressPercentage(playerId);
        }
        return 0.0; // Return 0 if module not available
    }
    
    /**
     * Gets the rank module.
     * 
     * @return The rank module
     */
    public BDRankModule getRankModule() {
        return (BDRankModule) getSubmodule("Rank");
    }
    
    /**
     * Gets the rebirth module.
     * 
     * @return The rebirth module
     */
    public BDRebirthModule getRebirthModule() {
        return (BDRebirthModule) getSubmodule("Rebirth");
    }
    
    // ProgressionAPI interface implementation methods
    
    /**
     * Gets a player's rank in the external API format.
     *
     * @param playerUuid The player UUID
     * @return The rank in API format
     */
    @Override
    public com.bdcraft.plugin.api.ProgressionAPI.Rank getPlayerRank(UUID playerUuid) {
        Rank internalRank = getPlayerRank(playerUuid);
        
        // Map internal ranks to API ranks
        switch (internalRank) {
            case DEFAULT: return com.bdcraft.plugin.api.ProgressionAPI.Rank.NEWCOMER;
            case NOVICE: return com.bdcraft.plugin.api.ProgressionAPI.Rank.FARMER;
            case APPRENTICE: return com.bdcraft.plugin.api.ProgressionAPI.Rank.EXPERT_FARMER;
            case EXPERT: return com.bdcraft.plugin.api.ProgressionAPI.Rank.MASTER_FARMER;
            default: return com.bdcraft.plugin.api.ProgressionAPI.Rank.AGRICULTURAL_EXPERT;
        }
    }
    
    /**
     * Gets a display name for a rank in API format.
     *
     * @param rank The rank in API format
     * @return The display name
     */
    @Override
    public String getRankDisplayName(com.bdcraft.plugin.api.ProgressionAPI.Rank rank) {
        // Convert API rank to internal rank for display
        Rank internalRank;
        switch (rank) {
            case NEWCOMER: internalRank = Rank.DEFAULT; break;
            case FARMER: internalRank = Rank.NOVICE; break;
            case EXPERT_FARMER: internalRank = Rank.APPRENTICE; break;
            case MASTER_FARMER: internalRank = Rank.EXPERT; break;
            case AGRICULTURAL_EXPERT: internalRank = Rank.MASTER; break;
            default: internalRank = Rank.DEFAULT; break;
        }
        
        return internalRank.getFormattedName();
    }
    
    /**
     * Gets a player's rebirth level.
     *
     * @param playerUuid The player UUID
     * @return The rebirth level
     */
    @Override
    public int getPlayerRebirthLevel(UUID playerUuid) {
        return getRebirthLevel(playerUuid);
    }
    
    /**
     * Gets the next rank for a player in API format.
     *
     * @param playerUuid The player UUID
     * @return The next rank, or null if at max rank
     */
    @Override
    public com.bdcraft.plugin.api.ProgressionAPI.Rank getNextRank(UUID playerUuid) {
        Rank currentRank = getPlayerRank(playerUuid);
        Rank nextRank = currentRank.getNext();
        
        // If player is at max rank, return null
        if (nextRank == currentRank) {
            return null;
        }
        
        // Convert internal rank to API rank
        switch (nextRank) {
            case DEFAULT: return com.bdcraft.plugin.api.ProgressionAPI.Rank.NEWCOMER;
            case NOVICE: return com.bdcraft.plugin.api.ProgressionAPI.Rank.FARMER;
            case APPRENTICE: return com.bdcraft.plugin.api.ProgressionAPI.Rank.EXPERT_FARMER;
            case EXPERT: return com.bdcraft.plugin.api.ProgressionAPI.Rank.MASTER_FARMER;
            default: return com.bdcraft.plugin.api.ProgressionAPI.Rank.AGRICULTURAL_EXPERT;
        }
    }
}