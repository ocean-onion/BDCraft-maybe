package com.bdcraft.plugin.modules.economy.items;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages BD items.
 */
public class BDItemManager {
    private final BDCraft plugin;
    private final NamespacedKey bdItemKey;
    private final NamespacedKey bdItemTypeKey;
    private final NamespacedKey bdItemValueKey;
    
    private final Map<String, BDItemInfo> itemInfoMap;
    
    /**
     * Creates a new BD item manager.
     * @param plugin The plugin instance
     */
    public BDItemManager(BDCraft plugin) {
        this.plugin = plugin;
        this.bdItemKey = new NamespacedKey(plugin, "bd_item");
        this.bdItemTypeKey = new NamespacedKey(plugin, "bd_item_type");
        this.bdItemValueKey = new NamespacedKey(plugin, "bd_item_value");
        
        this.itemInfoMap = new HashMap<>();
        
        // Initialize item definitions
        initializeItemInfo();
    }
    
    /**
     * Initializes item info.
     */
    private void initializeItemInfo() {
        // Seeds
        registerItemInfo("bd_seed", Material.WHEAT_SEEDS, "BD Seed", 
                Arrays.asList("A special seed from BD Craft", "Grows into a valuable crop"), 
                5, ChatColor.YELLOW);
        
        registerItemInfo("green_bd_seed", Material.WHEAT_SEEDS, "Green BD Seed", 
                Arrays.asList("A rare green seed from BD Craft", "Grows faster and produces more valuable crops", 
                        "Requires Farmer rank"), 
                25, ChatColor.GREEN);
        
        registerItemInfo("purple_bd_seed", Material.WHEAT_SEEDS, "Purple BD Seed", 
                Arrays.asList("An extremely rare purple seed from BD Craft", "Grows very fast and produces highly valuable crops", 
                        "Requires Master Farmer rank"), 
                100, ChatColor.LIGHT_PURPLE);
        
        // Crops
        registerItemInfo("bd_crop", Material.WHEAT, "BD Crop", 
                Arrays.asList("A special crop from BD Craft", "Can be sold to BD Collectors for currency"), 
                10, ChatColor.YELLOW);
        
        registerItemInfo("green_bd_crop", Material.WHEAT, "Green BD Crop", 
                Arrays.asList("A rare green crop from BD Craft", "Can be sold to BD Collectors for more currency"), 
                50, ChatColor.GREEN);
        
        registerItemInfo("purple_bd_crop", Material.WHEAT, "Purple BD Crop", 
                Arrays.asList("An extremely rare purple crop from BD Craft", "Can be sold to BD Collectors for lots of currency"), 
                200, ChatColor.LIGHT_PURPLE);
        
        // Tokens
        registerItemInfo("market_token", Material.EMERALD, "Market Token", 
                Arrays.asList("Place this token to create a market", "Requires a 3x3 flat area", 
                        "Must be at least Expert Farmer rank"), 
                1000, ChatColor.GOLD, true);
        
        registerItemInfo("house_token", Material.EMERALD, "House Token", 
                Arrays.asList("Place this token in your market to add a Collector", "Each market has a limit on houses based on level"), 
                500, ChatColor.AQUA, true);
        
        // Tools
        registerItemInfo("bd_stick", Material.BLAZE_ROD, "BD Stick", 
                Arrays.asList("A special stick with magical properties", "Right-click to use"), 
                250, ChatColor.LIGHT_PURPLE, true);
        
        registerItemInfo("bd_hoe", Material.DIAMOND_HOE, "BD Hoe", 
                Arrays.asList("A special hoe for BD farming", "Increased range when tilling soil"), 
                750, ChatColor.AQUA, true);
    }
    
    /**
     * Registers item info.
     * @param itemType The item type
     * @param material The material
     * @param displayName The display name
     * @param lore The lore
     * @param value The value
     * @param color The color
     */
    private void registerItemInfo(String itemType, Material material, String displayName, List<String> lore, 
            int value, ChatColor color) {
        registerItemInfo(itemType, material, displayName, lore, value, color, false);
    }
    
    /**
     * Registers item info.
     * @param itemType The item type
     * @param material The material
     * @param displayName The display name
     * @param lore The lore
     * @param value The value
     * @param color The color
     * @param glowing Whether the item should glow
     */
    private void registerItemInfo(String itemType, Material material, String displayName, List<String> lore, 
            int value, ChatColor color, boolean glowing) {
        BDItemInfo itemInfo = new BDItemInfo(itemType, material, displayName, lore, value, color, glowing);
        itemInfoMap.put(itemType, itemInfo);
    }
    
    /**
     * Creates a BD item.
     * @param itemType The item type
     * @param amount The amount
     * @return The item
     */
    public ItemStack createItem(String itemType, int amount) {
        // Check if item type exists
        BDItemInfo itemInfo = itemInfoMap.get(itemType);
        
        if (itemInfo == null) {
            return null;
        }
        
        // Create the item
        ItemStack item = new ItemStack(itemInfo.getMaterial(), amount);
        ItemMeta meta = item.getItemMeta();
        
        // Set display name
        meta.setDisplayName(itemInfo.getColor() + itemInfo.getDisplayName());
        
        // Set lore
        List<String> lore = new ArrayList<>();
        for (String loreLine : itemInfo.getLore()) {
            lore.add(ChatColor.GRAY + loreLine);
        }
        
        // Add value to lore
        lore.add(ChatColor.GRAY + "Value: " + itemInfo.getValue() + " BD Currency");
        
        meta.setLore(lore);
        
        // Add persistent data
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, itemType);
        container.set(bdItemValueKey, PersistentDataType.INTEGER, itemInfo.getValue());
        
        // Add glow effect if needed
        if (itemInfo.isGlowing()) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Checks if an item is a BD item.
     * @param item The item
     * @return Whether the item is a BD item
     */
    public boolean isBDItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        return container.has(bdItemKey, PersistentDataType.STRING) && 
                container.get(bdItemKey, PersistentDataType.STRING).equals("true");
    }
    
    /**
     * Gets the BD item type of an item.
     * @param item The item
     * @return The BD item type, or null if not a BD item
     */
    public String getBDItemType(ItemStack item) {
        if (!isBDItem(item)) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        return container.get(bdItemTypeKey, PersistentDataType.STRING);
    }
    
    /**
     * Gets the BD item value of an item.
     * @param item The item
     * @return The BD item value, or -1 if not a BD item
     */
    public int getBDItemValue(ItemStack item) {
        if (!isBDItem(item)) {
            return -1;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        return container.get(bdItemValueKey, PersistentDataType.INTEGER);
    }
    
    /**
     * Gets item info for a BD item type.
     * @param itemType The item type
     * @return The item info, or null if not found
     */
    public BDItemInfo getItemInfo(String itemType) {
        return itemInfoMap.get(itemType);
    }
    
    /**
     * Gets all registered BD item types.
     * @return The item types
     */
    public List<String> getAllItemTypes() {
        return new ArrayList<>(itemInfoMap.keySet());
    }
    
    /**
     * Inner class to hold BD item information.
     */
    public static class BDItemInfo {
        private final String itemType;
        private final Material material;
        private final String displayName;
        private final List<String> lore;
        private final int value;
        private final ChatColor color;
        private final boolean glowing;
        
        /**
         * Creates a new BD item info.
         * @param itemType The item type
         * @param material The material
         * @param displayName The display name
         * @param lore The lore
         * @param value The value
         * @param color The color
         * @param glowing Whether the item should glow
         */
        public BDItemInfo(String itemType, Material material, String displayName, List<String> lore, 
                int value, ChatColor color, boolean glowing) {
            this.itemType = itemType;
            this.material = material;
            this.displayName = displayName;
            this.lore = lore;
            this.value = value;
            this.color = color;
            this.glowing = glowing;
        }
        
        /**
         * Gets the item type.
         * @return The item type
         */
        public String getItemType() {
            return itemType;
        }
        
        /**
         * Gets the material.
         * @return The material
         */
        public Material getMaterial() {
            return material;
        }
        
        /**
         * Gets the display name.
         * @return The display name
         */
        public String getDisplayName() {
            return displayName;
        }
        
        /**
         * Gets the lore.
         * @return The lore
         */
        public List<String> getLore() {
            return lore;
        }
        
        /**
         * Gets the value.
         * @return The value
         */
        public int getValue() {
            return value;
        }
        
        /**
         * Gets the color.
         * @return The color
         */
        public ChatColor getColor() {
            return color;
        }
        
        /**
         * Checks if the item should glow.
         * @return Whether the item should glow
         */
        public boolean isGlowing() {
            return glowing;
        }
    }
}