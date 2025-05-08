package com.bdcraft.plugin.modules.economy.listeners;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.progression.BDRankManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

/**
 * Listener for BD crop growth.
 */
public class CropGrowthListener implements Listener {
    private final BDCraft plugin;
    private final Random random;
    
    /**
     * Creates a new crop growth listener.
     * @param plugin The plugin instance
     */
    public CropGrowthListener(BDCraft plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onSeedPlant(PlayerInteractEvent event) {
        // Only handle right clicks on blocks with items
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getItem() == null) {
            return;
        }
        
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        ItemStack item = event.getItem();
        
        // Check if the item is a BD seed
        BDItemManager itemManager = plugin.getEconomyModule().getItemManager();
        if (!itemManager.isBDItem(item)) {
            return;
        }
        
        String itemType = itemManager.getBDItemType(item);
        if (!itemType.contains("_bd_seed")) {
            return;
        }
        
        // Check if the block is farmland
        if (clickedBlock.getType() != Material.FARMLAND) {
            return;
        }
        
        // Check player rank requirements for special seeds
        BDRankManager rankManager = plugin.getProgressionModule().getRankManager();
        int playerRank = rankManager.getPlayerRank(player);
        String playerRankName = rankManager.getPlayerRankName(player);
        
        if (itemType.equals("green_bd_seed") && playerRank < BDRankManager.RANK_FARMER) {
            player.sendMessage(ChatColor.RED + "You need to be a Farmer to plant green BD seeds.");
            event.setCancelled(true);
            return;
        }
        
        if (itemType.equals("purple_bd_seed") && playerRank < BDRankManager.RANK_MASTER_FARMER) {
            player.sendMessage(ChatColor.RED + "You need to be a Master Farmer to plant purple BD seeds.");
            event.setCancelled(true);
            return;
        }
        
        // Get the block above the farmland
        Block soilBlock = clickedBlock;
        Block cropBlock = soilBlock.getRelative(BlockFace.UP);
        
        // If the block above is not air, we can't plant
        if (!cropBlock.getType().isAir()) {
            return;
        }
        
        // Cancel the vanilla event
        event.setCancelled(true);
        
        // Plant the BD crop
        cropBlock.setType(Material.WHEAT);
        
        // Set metadata to identify as a BD crop
        cropBlock.setMetadata("bd_crop_type", new FixedMetadataValue(plugin, itemType));
        cropBlock.setMetadata("bd_crop_owner", new FixedMetadataValue(plugin, player.getUniqueId().toString()));
        
        // Consume the seed
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
        
        // Notify player
        player.sendMessage(ChatColor.GREEN + "You've planted a special BD crop!");
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCropGrow(BlockGrowEvent event) {
        Block block = event.getBlock();
        
        // Check if it's a BD crop
        if (!block.hasMetadata("bd_crop_type")) {
            return;
        }
        
        // Get crop data
        Ageable ageable = (Ageable) event.getNewState().getBlockData();
        
        // Apply growth boost based on crop type
        String cropType = block.getMetadata("bd_crop_type").get(0).asString();
        
        if (cropType.equals("green_bd_seed")) {
            // Green crops grow 20% faster
            if (random.nextFloat() < 0.2f) {
                ageable.setAge(Math.min(ageable.getAge() + 1, ageable.getMaximumAge()));
                event.getNewState().setBlockData(ageable);
            }
        } else if (cropType.equals("purple_bd_seed")) {
            // Purple crops grow 40% faster
            if (random.nextFloat() < 0.4f) {
                ageable.setAge(Math.min(ageable.getAge() + 1, ageable.getMaximumAge()));
                event.getNewState().setBlockData(ageable);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onCropHarvest(BlockBreakEvent event) {
        Block block = event.getBlock();
        
        // Check if it's a wheat crop and a BD crop
        if (block.getType() != Material.WHEAT || !block.hasMetadata("bd_crop_type")) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // Check if crop is fully grown
        Ageable ageable = (Ageable) block.getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge()) {
            // Not fully grown, notify player
            player.sendMessage(ChatColor.YELLOW + "This BD crop is not fully grown yet!");
            return;
        }
        
        // Cancel the vanilla event to handle custom drops
        event.setCancelled(true);
        
        // Remove the block manually
        block.setType(Material.AIR);
        
        // Determine crop type and drops
        String cropType = block.getMetadata("bd_crop_type").get(0).asString();
        BDItemManager itemManager = plugin.getEconomyModule().getItemManager();
        
        // Create the reward item based on crop type
        ItemStack cropItem = null;
        int expAmount = 0;
        
        if (cropType.equals("bd_seed")) {
            // Regular BD crop
            cropItem = itemManager.createItem("bd_crop", 1);
            expAmount = 2;
        } else if (cropType.equals("green_bd_seed")) {
            // Green BD crop
            cropItem = itemManager.createItem("green_bd_crop", 1);
            expAmount = 5;
        } else if (cropType.equals("purple_bd_seed")) {
            // Purple BD crop
            cropItem = itemManager.createItem("purple_bd_crop", 1);
            expAmount = 10;
        }
        
        // Drop the item
        if (cropItem != null) {
            Location dropLocation = block.getLocation().add(0.5, 0.5, 0.5);
            block.getWorld().dropItemNaturally(dropLocation, cropItem);
            
            // Also drop a seed for replanting (50% chance)
            if (random.nextFloat() < 0.5f) {
                ItemStack seedItem = itemManager.createItem(cropType, 1);
                block.getWorld().dropItemNaturally(dropLocation, seedItem);
            }
            
            // Award experience
            BDRankManager rankManager = plugin.getProgressionModule().getRankManager();
            rankManager.addExperience(player, expAmount);
            
            player.sendMessage(ChatColor.GREEN + "You harvested a " + 
                    getColorForCropType(cropType) + getCropNameForType(cropType) + ChatColor.GREEN + "!");
            player.sendMessage(ChatColor.AQUA + "+" + expAmount + " progression experience.");
        }
    }
    
    /**
     * Gets the color for a crop type.
     * @param cropType The crop type
     * @return The color
     */
    private ChatColor getColorForCropType(String cropType) {
        if (cropType.equals("bd_seed")) {
            return ChatColor.YELLOW;
        } else if (cropType.equals("green_bd_seed")) {
            return ChatColor.GREEN;
        } else if (cropType.equals("purple_bd_seed")) {
            return ChatColor.LIGHT_PURPLE;
        }
        
        return ChatColor.WHITE;
    }
    
    /**
     * Gets the name for a crop type.
     * @param cropType The crop type
     * @return The name
     */
    private String getCropNameForType(String cropType) {
        if (cropType.equals("bd_seed")) {
            return "BD Crop";
        } else if (cropType.equals("green_bd_seed")) {
            return "Green BD Crop";
        } else if (cropType.equals("purple_bd_seed")) {
            return "Purple BD Crop";
        }
        
        return "Unknown Crop";
    }
}