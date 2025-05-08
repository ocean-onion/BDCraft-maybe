package com.bdcraft.plugin.modules.economy.items.crops;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItem;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed.SeedType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a BD crop item.
 */
public class BDCrop extends BDItem {
    public static final String BD_CROP_KEY = "bd_crop";
    public static final String GREEN_BD_CROP_KEY = "green_bd_crop";
    public static final String PURPLE_BD_CROP_KEY = "purple_bd_crop";
    
    private final CropType cropType;
    private final double emeraldValue;
    private final double currencyValue;
    
    /**
     * Creates a new BD crop.
     * 
     * @param plugin The plugin instance
     * @param cropType The type of crop
     */
    public BDCrop(BDCraft plugin, CropType cropType) {
        super(plugin);
        this.cropType = cropType;
        
        switch (cropType) {
            case REGULAR:
                this.emeraldValue = 0.2; // 10 crops = 2 emeralds
                this.currencyValue = 5; // 10 crops = 50 server currency
                break;
            case GREEN:
                this.emeraldValue = 2.0; // 5 crops = 10 emeralds
                this.currencyValue = 30; // 5 crops = 150 server currency
                break;
            case PURPLE:
                this.emeraldValue = 6.67; // 3 crops = 20 emeralds
                this.currencyValue = 133.33; // 3 crops = 400 server currency
                break;
            default:
                this.emeraldValue = 0;
                this.currencyValue = 0;
                break;
        }
    }
    
    @Override
    public ItemStack createItemStack(int amount) {
        Material material;
        String displayName;
        String itemKey;
        ChatColor color;
        boolean hasGlow = false;
        
        switch (cropType) {
            case REGULAR:
                material = Material.FERN;
                displayName = "BD Crop";
                itemKey = BD_CROP_KEY;
                color = ChatColor.GOLD;
                break;
            case GREEN:
                material = Material.LARGE_FERN;
                displayName = "Green BD Crop";
                itemKey = GREEN_BD_CROP_KEY;
                color = ChatColor.GREEN;
                break;
            case PURPLE:
                material = Material.LARGE_FERN;
                displayName = "Purple BD Crop";
                itemKey = PURPLE_BD_CROP_KEY;
                color = ChatColor.DARK_PURPLE;
                hasGlow = true;
                break;
            default:
                material = Material.FERN;
                displayName = "Unknown BD Crop";
                itemKey = BD_CROP_KEY;
                color = ChatColor.GRAY;
                break;
        }
        
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set display name
            meta.setDisplayName(color + displayName);
            
            // Add enchantment glow if needed
            if (hasGlow) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            
            // Add lore
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "A special BD crop");
            lore.add(ChatColor.GRAY + "Value per crop: " + ChatColor.YELLOW + emeraldValue + " emeralds");
            lore.add(ChatColor.GRAY + "Currency value: " + ChatColor.YELLOW + currencyValue + " coins");
            
            if (cropType == CropType.REGULAR) {
                lore.add(ChatColor.GRAY + "50 crops can be traded for 1 diamond");
            }
            
            lore.add(ChatColor.RED + "Can only be sold to Collector Villagers");
            
            meta.setLore(lore);
            
            // Add custom NBT data
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(getNamespacedKey(itemKey), PersistentDataType.STRING, itemKey);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Gets the type of crop.
     * 
     * @return The crop type
     */
    public CropType getCropType() {
        return cropType;
    }
    
    /**
     * Gets the emerald value of this crop.
     * 
     * @return The emerald value per crop
     */
    public double getEmeraldValue() {
        return emeraldValue;
    }
    
    /**
     * Gets the server currency value of this crop.
     * 
     * @return The currency value per crop
     */
    public double getCurrencyValue() {
        return currencyValue;
    }
    
    /**
     * Gets the corresponding seed type for this crop.
     * 
     * @return The seed type
     */
    public SeedType getCorrespondingSeedType() {
        switch (cropType) {
            case REGULAR:
                return SeedType.REGULAR;
            case GREEN:
                return SeedType.GREEN;
            case PURPLE:
                return SeedType.PURPLE;
            default:
                return SeedType.REGULAR;
        }
    }
    
    /**
     * Checks if an item is a BD crop.
     * 
     * @param item The item to check
     * @return True if the item is a BD crop
     */
    public static boolean isBDCrop(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        return container.has(BDItem.getNamespacedKey(BD_CROP_KEY), PersistentDataType.STRING) ||
               container.has(BDItem.getNamespacedKey(GREEN_BD_CROP_KEY), PersistentDataType.STRING) ||
               container.has(BDItem.getNamespacedKey(PURPLE_BD_CROP_KEY), PersistentDataType.STRING);
    }
    
    /**
     * Gets the crop type from an item.
     * 
     * @param item The item to check
     * @return The crop type, or null if the item is not a BD crop
     */
    public static CropType getCropType(ItemStack item) {
        if (!isBDCrop(item)) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        if (container.has(BDItem.getNamespacedKey(BD_CROP_KEY), PersistentDataType.STRING)) {
            return CropType.REGULAR;
        } else if (container.has(BDItem.getNamespacedKey(GREEN_BD_CROP_KEY), PersistentDataType.STRING)) {
            return CropType.GREEN;
        } else if (container.has(BDItem.getNamespacedKey(PURPLE_BD_CROP_KEY), PersistentDataType.STRING)) {
            return CropType.PURPLE;
        }
        
        return null;
    }
    
    /**
     * Gets a crop type from a seed type.
     * 
     * @param seedType The seed type
     * @return The corresponding crop type
     */
    public static CropType getCropTypeFromSeedType(SeedType seedType) {
        switch (seedType) {
            case REGULAR:
                return CropType.REGULAR;
            case GREEN:
                return CropType.GREEN;
            case PURPLE:
                return CropType.PURPLE;
            default:
                return CropType.REGULAR;
        }
    }
    
    /**
     * Enum for different crop types.
     */
    public enum CropType {
        REGULAR,
        GREEN,
        PURPLE
    }
}