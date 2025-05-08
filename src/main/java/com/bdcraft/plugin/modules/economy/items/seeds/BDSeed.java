package com.bdcraft.plugin.modules.economy.items.seeds;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a BD seed item.
 */
public class BDSeed extends BDItem {
    public static final String BD_SEED_KEY = "bd_seed";
    public static final String GREEN_BD_SEED_KEY = "green_bd_seed";
    public static final String PURPLE_BD_SEED_KEY = "purple_bd_seed";
    
    private final SeedType seedType;
    private final int requiredRank;
    private final double emeraldValue;
    
    /**
     * Creates a new BD seed.
     * 
     * @param plugin The plugin instance
     * @param seedType The type of seed
     */
    public BDSeed(BDCraft plugin, SeedType seedType) {
        super(plugin);
        this.seedType = seedType;
        
        switch (seedType) {
            case REGULAR:
                this.requiredRank = 0;
                this.emeraldValue = 0.2; // 1 emerald for 5 seeds
                break;
            case GREEN:
                this.requiredRank = 2;
                this.emeraldValue = 9;
                break;
            case PURPLE:
                this.requiredRank = 4;
                this.emeraldValue = 25;
                break;
            default:
                this.requiredRank = 0;
                this.emeraldValue = 0;
                break;
        }
    }
    
    @Override
    public ItemStack createItemStack(int amount) {
        Material material;
        String displayName;
        String itemKey;
        ChatColor color;
        
        switch (seedType) {
            case REGULAR:
                material = Material.WHEAT_SEEDS;
                displayName = "BD Seed";
                itemKey = BD_SEED_KEY;
                color = ChatColor.GOLD;
                break;
            case GREEN:
                material = Material.BEETROOT_SEEDS;
                displayName = "Green BD Seed";
                itemKey = GREEN_BD_SEED_KEY;
                color = ChatColor.GREEN;
                break;
            case PURPLE:
                material = Material.PUMPKIN_SEEDS;
                displayName = "Purple BD Seed";
                itemKey = PURPLE_BD_SEED_KEY;
                color = ChatColor.DARK_PURPLE;
                break;
            default:
                material = Material.WHEAT_SEEDS;
                displayName = "Unknown BD Seed";
                itemKey = BD_SEED_KEY;
                color = ChatColor.GRAY;
                break;
        }
        
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set display name
            meta.setDisplayName(color + displayName);
            
            // Add enchantment glow
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            
            // Add lore
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "A special seed for BD farming");
            lore.add(ChatColor.GRAY + "Value: " + ChatColor.YELLOW + emeraldValue + " emeralds");
            
            if (requiredRank > 0) {
                lore.add(ChatColor.RED + "Requires rank: " + getRequiredRankName());
            }
            
            meta.setLore(lore);
            
            // Add custom NBT data
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(getNamespacedKey(itemKey), PersistentDataType.STRING, itemKey);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Gets the type of seed.
     * 
     * @return The seed type
     */
    public SeedType getSeedType() {
        return seedType;
    }
    
    /**
     * Gets the required rank to use this seed.
     * 
     * @return The required rank
     */
    public int getRequiredRank() {
        return requiredRank;
    }
    
    /**
     * Gets the emerald value of this seed.
     * 
     * @return The emerald value
     */
    public double getEmeraldValue() {
        return emeraldValue;
    }
    
    /**
     * Gets the name of the required rank.
     * 
     * @return The rank name
     */
    public String getRequiredRankName() {
        switch (requiredRank) {
            case 0:
                return "None";
            case 1:
                return "Newcomer";
            case 2:
                return "Farmer";
            case 3:
                return "Experienced Farmer";
            case 4:
                return "Master Farmer";
            case 5:
                return "BD Expert";
            default:
                return "Unknown";
        }
    }
    
    /**
     * Gets the growth speed multiplier.
     * 
     * @return The growth speed multiplier
     */
    public double getGrowthSpeedMultiplier() {
        switch (seedType) {
            case GREEN:
                return 1.3; // 30% faster
            default:
                return 1.0;
        }
    }
    
    /**
     * Checks if an item is a BD seed.
     * 
     * @param item The item to check
     * @return True if the item is a BD seed
     */
    public static boolean isBDSeed(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        return container.has(BDItem.getStaticNamespacedKey(BD_SEED_KEY), PersistentDataType.STRING) ||
               container.has(BDItem.getStaticNamespacedKey(GREEN_BD_SEED_KEY), PersistentDataType.STRING) ||
               container.has(BDItem.getStaticNamespacedKey(PURPLE_BD_SEED_KEY), PersistentDataType.STRING);
    }
    
    /**
     * Gets the seed type from an item.
     * 
     * @param item The item to check
     * @return The seed type, or null if the item is not a BD seed
     */
    public static SeedType getSeedType(ItemStack item) {
        if (!isBDSeed(item)) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        if (container.has(BDItem.getStaticNamespacedKey(BD_SEED_KEY), PersistentDataType.STRING)) {
            return SeedType.REGULAR;
        } else if (container.has(BDItem.getStaticNamespacedKey(GREEN_BD_SEED_KEY), PersistentDataType.STRING)) {
            return SeedType.GREEN;
        } else if (container.has(BDItem.getStaticNamespacedKey(PURPLE_BD_SEED_KEY), PersistentDataType.STRING)) {
            return SeedType.PURPLE;
        }
        
        return null;
    }
    
    /**
     * Enum for different seed types.
     */
    public enum SeedType {
        REGULAR,
        GREEN,
        PURPLE
    }
}