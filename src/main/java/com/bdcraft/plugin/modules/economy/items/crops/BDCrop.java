package com.bdcraft.plugin.modules.economy.items.crops;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed.SeedType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class representing a special BD crop.
 */
public class BDCrop {
    private final ItemStack itemStack;
    private final CropType type;
    private final int value;
    
    /**
     * Creates a new BD crop.
     * 
     * @param itemStack The item stack
     * @param type The crop type
     * @param value The crop value
     */
    public BDCrop(ItemStack itemStack, CropType type, int value) {
        this.itemStack = itemStack;
        this.type = type;
        this.value = value;
    }
    
    /**
     * Gets the item stack.
     * 
     * @return The item stack
     */
    public ItemStack getItemStack() {
        return itemStack.clone();
    }
    
    /**
     * Gets the crop type.
     * 
     * @return The crop type
     */
    public CropType getType() {
        return type;
    }
    
    /**
     * Gets the crop value.
     * 
     * @return The crop value
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Gets the corresponding crop type for a seed type.
     * 
     * @param seedType The seed type
     * @return The crop type
     */
    public static CropType getCropTypeFromSeedType(SeedType seedType) {
        if (seedType == null) {
            return CropType.REGULAR;
        }
        
        switch (seedType) {
            case GREEN:
                return CropType.GREEN;
            case BLUE:
                return CropType.BLUE;
            case PURPLE:
                return CropType.PURPLE;
            case LEGENDARY:
                return CropType.LEGENDARY;
            default:
                return CropType.REGULAR;
        }
    }
    
    /**
     * Checks if an ItemStack is a BD crop.
     * 
     * @param item The ItemStack to check
     * @return True if it's a BD crop
     */
    public static boolean isBDCrop(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return false;
        }
        
        List<String> lore = item.getItemMeta().getLore();
        if (lore == null || lore.isEmpty()) {
            return false;
        }
        
        // Check for BD crop identifier in lore
        for (String line : lore) {
            if (line.contains("Special") && line.contains("quality crop")) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets the crop type from an ItemStack.
     * 
     * @param item The ItemStack
     * @return The crop type, or null if not a BD crop
     */
    public static CropType getCropType(ItemStack item) {
        if (!isBDCrop(item)) {
            return null;
        }
        
        String displayName = item.getItemMeta().getDisplayName();
        
        if (displayName.contains("Quality Crop")) {
            return CropType.GREEN;
        } else if (displayName.contains("Premium Crop")) {
            return CropType.BLUE;
        } else if (displayName.contains("Exceptional Crop")) {
            return CropType.PURPLE;
        } else if (displayName.contains("Legendary Crop")) {
            return CropType.LEGENDARY;
        } else {
            return CropType.REGULAR;
        }
    }
    
    /**
     * Gets the value of a crop type.
     * 
     * @param type The crop type
     * @return The value
     */
    public static int getValueForType(CropType type) {
        switch (type) {
            case REGULAR:
                return 10;
            case GREEN:
                return 30;
            case BLUE:
                return 60;
            case PURPLE:
                return 100;
            case LEGENDARY:
                return 200;
            default:
                return 5;
        }
    }
    
    /**
     * Creates an ItemStack for this crop.
     * 
     * @param quantity The quantity
     * @return The ItemStack
     */
    public ItemStack createItemStack(int quantity) {
        Material material = Material.WHEAT;
        ItemStack crop = new ItemStack(material, quantity);
        ItemMeta meta = crop.getItemMeta();
        
        if (meta != null) {
            String displayName;
            ChatColor color;
            
            switch (type) {
                case GREEN:
                    displayName = "Quality Crop";
                    color = ChatColor.GREEN;
                    break;
                case BLUE:
                    displayName = "Premium Crop";
                    color = ChatColor.BLUE;
                    break;
                case PURPLE:
                    displayName = "Exceptional Crop";
                    color = ChatColor.LIGHT_PURPLE;
                    break;
                case LEGENDARY:
                    displayName = "Legendary Crop";
                    color = ChatColor.GOLD;
                    break;
                default:
                    displayName = "Crop";
                    color = ChatColor.WHITE;
                    break;
            }
            
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Special " + color + type.name().toLowerCase() + 
                    ChatColor.GRAY + " quality crop.");
            lore.add(ChatColor.GRAY + "Can be sold to BD collectors for");
            lore.add(ChatColor.GOLD + String.valueOf(value) + " BD" + 
                    ChatColor.GRAY + " each.");
            lore.add("");
            lore.add(ChatColor.GRAY + "ID: " + UUID.randomUUID().toString().substring(0, 8));
            
            meta.setLore(lore);
            
            // Add enchant glow for better crops
            if (type != CropType.REGULAR) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            
            crop.setItemMeta(meta);
        }
        
        return crop;
    }
    
    /**
     * Enum for crop types.
     */
    public enum CropType {
        /**
         * Regular crop type.
         */
        REGULAR,
        
        /**
         * Green quality crop type.
         */
        GREEN,
        
        /**
         * Blue premium crop type.
         */
        BLUE,
        
        /**
         * Purple exceptional crop type.
         */
        PURPLE,
        
        /**
         * Legendary gold crop type.
         */
        LEGENDARY
    }
}