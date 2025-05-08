package com.bdcraft.plugin.modules.economy.items;

import org.bukkit.inventory.ItemStack;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop.CropType;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed.SeedType;

/**
 * Manages BD items.
 */
public class BDItemManager {
    private final BDCraft plugin;
    
    /**
     * Creates a new BD item manager.
     * 
     * @param plugin The plugin instance
     */
    public BDItemManager(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Creates a BD seed.
     * 
     * @param type The seed type
     * @param amount The amount of seeds
     * @return The BD seed item
     */
    public ItemStack createBDSeed(SeedType type, int amount) {
        return new BDSeed(plugin, type).createItemStack(amount);
    }
    
    /**
     * Creates a BD crop.
     * 
     * @param type The crop type
     * @param amount The amount of crops
     * @return The BD crop item
     */
    public ItemStack createBDCrop(CropType type, int amount) {
        return new BDCrop(plugin, type).createItemStack(amount);
    }
    
    /**
     * Checks if an item is a BD seed.
     * 
     * @param item The item to check
     * @return True if the item is a BD seed
     */
    public boolean isBDSeed(ItemStack item) {
        return BDSeed.isBDSeed(item);
    }
    
    /**
     * Checks if an item is a BD crop.
     * 
     * @param item The item to check
     * @return True if the item is a BD crop
     */
    public boolean isBDCrop(ItemStack item) {
        return BDCrop.isBDCrop(item);
    }
    
    /**
     * Gets the seed type of an item.
     * 
     * @param item The item to check
     * @return The seed type, or null if the item is not a BD seed
     */
    public SeedType getBDSeedType(ItemStack item) {
        return BDSeed.getSeedType(item);
    }
    
    /**
     * Gets the crop type of an item.
     * 
     * @param item The item to check
     * @return The crop type, or null if the item is not a BD crop
     */
    public CropType getBDCropType(ItemStack item) {
        return BDCrop.getCropType(item);
    }
}