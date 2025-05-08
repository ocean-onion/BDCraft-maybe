package com.bdcraft.plugin.modules.economy.farming;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop.CropType;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed.SeedType;
import com.bdcraft.plugin.modules.progression.BDRankManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Manages BD crop growth and harvesting.
 */
public class BDCropManager implements Listener {
    private static final String BD_CROP_METADATA = "bdCrop";
    private static final String BD_CROP_TYPE_METADATA = "bdCropType";
    private static final Random RANDOM = new Random();
    
    private final BDCraft plugin;
    private final Map<Location, SeedType> bdCropLocations;
    private final Map<UUID, Long> lastParticleMap;
    
    /**
     * Creates a new BD crop manager.
     * 
     * @param plugin The plugin instance
     */
    public BDCropManager(BDCraft plugin) {
        this.plugin = plugin;
        this.bdCropLocations = new HashMap<>();
        this.lastParticleMap = new HashMap<>();
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        // Start particle effect task
        new BukkitRunnable() {
            @Override
            public void run() {
                showParticlesForBDCrops();
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
    
    /**
     * Handles player interaction with blocks.
     * 
     * @param event The interaction event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.FARMLAND) {
            return;
        }
        
        ItemStack item = event.getItem();
        if (item == null || !BDSeed.isBDSeed(item)) {
            return;
        }
        
        Player player = event.getPlayer();
        SeedType seedType = BDSeed.getSeedType(item);
        
        if (seedType == null) {
            return;
        }
        
        // Check player rank for seed type
        BDRankManager rankManager = plugin.getProgressionModule().getRankManager();
        int playerRank = rankManager.getPlayerRank(player);
        
        if (new BDSeed(plugin, seedType).getRequiredRank() > playerRank) {
            player.sendMessage("§cYou need to be at least rank " + new BDSeed(plugin, seedType).getRequiredRankName() + 
                              " to use this type of BD seed!");
            event.setCancelled(true);
            return;
        }
        
        // Let vanilla handle the actual planting
    }
    
    /**
     * Handles block placement.
     * 
     * @param event The block place event
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!BDSeed.isBDSeed(item)) {
            return;
        }
        
        SeedType seedType = BDSeed.getSeedType(item);
        if (seedType == null) {
            return;
        }
        
        Block block = event.getBlock();
        if (block.getType() == Material.WHEAT) {
            // Mark this as a BD crop with metadata
            block.setMetadata(BD_CROP_METADATA, new FixedMetadataValue(plugin, true));
            block.setMetadata(BD_CROP_TYPE_METADATA, new FixedMetadataValue(plugin, seedType.name()));
            
            // Store location for particle effects
            bdCropLocations.put(block.getLocation(), seedType);
        }
    }
    
    /**
     * Handles crop growth.
     * 
     * @param event The block grow event
     */
    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        Block block = event.getBlock();
        if (!block.hasMetadata(BD_CROP_METADATA)) {
            return;
        }
        
        // If it's not fully grown, apply growth speed modifier
        if (block.getType() == Material.WHEAT) {
            String seedTypeStr = block.getMetadata(BD_CROP_TYPE_METADATA).get(0).asString();
            SeedType seedType = SeedType.valueOf(seedTypeStr);
            
            // Apply growth speed modifier for green seeds
            if (seedType == SeedType.GREEN) {
                Ageable ageable = (Ageable) block.getBlockData();
                if (ageable.getAge() < ageable.getMaximumAge()) {
                    // 30% chance to grow an extra stage for GREEN seeds
                    if (RANDOM.nextDouble() < 0.3) {
                        int newAge = Math.min(ageable.getAge() + 1, ageable.getMaximumAge());
                        ageable.setAge(newAge);
                        block.setBlockData(ageable);
                    }
                }
            }
        }
    }
    
    /**
     * Handles crop harvesting.
     * 
     * @param event The block break event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.WHEAT || !block.hasMetadata(BD_CROP_METADATA)) {
            return;
        }
        
        // Check if fully grown
        Ageable ageable = (Ageable) block.getBlockData();
        if (ageable.getAge() < ageable.getMaximumAge()) {
            return;
        }
        
        // Get the seed type and drop appropriate BD crops
        String seedTypeStr = block.getMetadata(BD_CROP_TYPE_METADATA).get(0).asString();
        SeedType seedType = SeedType.valueOf(seedTypeStr);
        CropType cropType = BDCrop.getCropTypeFromSeedType(seedType);
        
        // Remove default drops
        event.setDropItems(false);
        
        // Drop BD crops
        Location dropLocation = block.getLocation().add(0.5, 0.5, 0.5);
        BDCrop bdCrop = new BDCrop(plugin, cropType);
        dropLocation.getWorld().dropItemNaturally(dropLocation, bdCrop.createItemStack(1));
        
        // Chance to drop special seeds
        double seedDropChance = 0;
        SeedType specialSeedType = null;
        
        if (seedType == SeedType.REGULAR) {
            seedDropChance = 0.05; // 5% for green seeds from regular crops
            specialSeedType = SeedType.GREEN;
        } else if (seedType == SeedType.GREEN) {
            seedDropChance = 0.01; // 1% for purple seeds from green crops
            specialSeedType = SeedType.PURPLE;
        }
        
        if (specialSeedType != null && RANDOM.nextDouble() < seedDropChance) {
            BDSeed specialSeed = new BDSeed(plugin, specialSeedType);
            dropLocation.getWorld().dropItemNaturally(dropLocation, specialSeed.createItemStack(1));
        }
        
        // Also drop regular BD seeds (50% chance)
        if (RANDOM.nextDouble() < 0.5) {
            BDSeed regularSeed = new BDSeed(plugin, seedType);
            dropLocation.getWorld().dropItemNaturally(dropLocation, regularSeed.createItemStack(1));
        }
        
        // Remove from tracking
        bdCropLocations.remove(block.getLocation());
    }
    
    /**
     * Shows particles for all tracked BD crops.
     */
    private void showParticlesForBDCrops() {
        for (Map.Entry<Location, SeedType> entry : bdCropLocations.entrySet()) {
            Location loc = entry.getKey().clone().add(0.5, 0.2, 0.5);
            SeedType seedType = entry.getValue();
            
            int particleCount;
            switch (seedType) {
                case REGULAR:
                    particleCount = 1;
                    break;
                case GREEN:
                    particleCount = 3;
                    break;
                case PURPLE:
                    particleCount = 5;
                    break;
                default:
                    particleCount = 1;
                    break;
            }
            
            // Show green particles
            loc.getWorld().spawnParticle(Particle.COMPOSTER, loc, particleCount, 0.2, 0.1, 0.2, 0.01);
            
            // Show additional glow effect for purple seeds
            if (seedType == SeedType.PURPLE) {
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 1, 0.1, 0.1, 0.1, 0.01);
            }
        }
    }
    
    /**
     * Shows particle effects to a player standing near BD crops.
     * 
     * @param player The player
     */
    public void showParticlesToPlayer(Player player) {
        UUID playerUuid = player.getUniqueId();
        
        // Rate limit to once per 2 seconds per player
        if (lastParticleMap.containsKey(playerUuid)) {
            long lastTime = lastParticleMap.get(playerUuid);
            if (System.currentTimeMillis() - lastTime < 2000) {
                return;
            }
        }
        
        lastParticleMap.put(playerUuid, System.currentTimeMillis());
        
        // Check nearby blocks
        Location playerLoc = player.getLocation();
        
        for (Map.Entry<Location, SeedType> entry : bdCropLocations.entrySet()) {
            Location cropLoc = entry.getKey();
            
            if (cropLoc.getWorld().equals(playerLoc.getWorld()) && 
                cropLoc.distance(playerLoc) <= 16) {
                
                SeedType seedType = entry.getValue();
                Location particleLoc = cropLoc.clone().add(0.5, 0.2, 0.5);
                
                int particleCount;
                switch (seedType) {
                    case REGULAR:
                        particleCount = 1;
                        break;
                    case GREEN:
                        particleCount = 3;
                        break;
                    case PURPLE:
                        particleCount = 5;
                        break;
                    default:
                        particleCount = 1;
                        break;
                }
                
                // Show green particles just to this player
                player.spawnParticle(Particle.COMPOSTER, particleLoc, particleCount, 0.2, 0.1, 0.2, 0.01);
                
                // Show additional glow effect for purple seeds
                if (seedType == SeedType.PURPLE) {
                    player.spawnParticle(Particle.END_ROD, particleLoc, 1, 0.1, 0.1, 0.1, 0.01);
                }
            }
        }
    }
}