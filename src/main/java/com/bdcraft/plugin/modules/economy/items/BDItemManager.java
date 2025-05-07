package com.bdcraft.plugin.modules.economy.items;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages BD custom items like seeds, crops, and special tools.
 */
public class BDItemManager {
    // Keys for marking BD items
    private final NamespacedKey bdItemKey;
    private final NamespacedKey greenBDItemKey;
    private final NamespacedKey purpleBDItemKey;
    private final NamespacedKey bdCropKey;
    private final NamespacedKey greenBDCropKey;
    private final NamespacedKey purpleBDCropKey;
    private final NamespacedKey bdStickKey;
    private final NamespacedKey bdHarvesterKey;
    private final NamespacedKey ultimateBDHarvesterKey;
    private final NamespacedKey bdMarketTokenKey;
    private final NamespacedKey bdHouseTokenKey;
    private final NamespacedKey durabilityKey;
    
    private final BDCraft plugin;
    
    /**
     * Creates a new BD item manager.
     * @param plugin The plugin instance
     */
    public BDItemManager(BDCraft plugin) {
        this.plugin = plugin;
        
        // Create keys for item identification
        this.bdItemKey = new NamespacedKey(plugin, "bd_seed");
        this.greenBDItemKey = new NamespacedKey(plugin, "green_bd_seed");
        this.purpleBDItemKey = new NamespacedKey(plugin, "purple_bd_seed");
        this.bdCropKey = new NamespacedKey(plugin, "bd_crop");
        this.greenBDCropKey = new NamespacedKey(plugin, "green_bd_crop");
        this.purpleBDCropKey = new NamespacedKey(plugin, "purple_bd_crop");
        this.bdStickKey = new NamespacedKey(plugin, "bd_stick");
        this.bdHarvesterKey = new NamespacedKey(plugin, "bd_harvester");
        this.ultimateBDHarvesterKey = new NamespacedKey(plugin, "ultimate_bd_harvester");
        this.bdMarketTokenKey = new NamespacedKey(plugin, "bd_market_token");
        this.bdHouseTokenKey = new NamespacedKey(plugin, "bd_house_token");
        this.durabilityKey = new NamespacedKey(plugin, "durability");
    }
    
    /**
     * Creates a BD seed item.
     * @param amount The amount of seeds
     * @return The seed item
     */
    public ItemStack createBDSeed(int amount) {
        ItemStack item = new ItemStack(Material.WHEAT_SEEDS, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "BD Seed");
        
        // Add glow effect
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Plant to grow special BD crops");
        lore.add(ChatColor.GRAY + "Can be sold to BD Dealers");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(bdItemKey, PersistentDataType.BYTE, (byte) 1);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a green BD seed item.
     * @param amount The amount of seeds
     * @return The seed item
     */
    public ItemStack createGreenBDSeed(int amount) {
        ItemStack item = new ItemStack(Material.BEETROOT_SEEDS, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Green BD Seed");
        
        // Add glow effect
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Premium seed for faster growth");
        lore.add(ChatColor.GRAY + "Grows 30% faster than regular BD seeds");
        lore.add(ChatColor.GRAY + "Produces Green BD Crops");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(greenBDItemKey, PersistentDataType.BYTE, (byte) 1);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a purple BD seed item.
     * @param amount The amount of seeds
     * @return The seed item
     */
    public ItemStack createPurpleBDSeed(int amount) {
        ItemStack item = new ItemStack(Material.PUMPKIN_SEEDS, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Purple BD Seed");
        
        // Add glow effect
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Rare premium BD seed");
        lore.add(ChatColor.GRAY + "Produces valuable Purple BD Crops");
        lore.add(ChatColor.GRAY + "Highly sought after by collectors");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(purpleBDItemKey, PersistentDataType.BYTE, (byte) 1);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a BD crop item.
     * @param amount The amount of crops
     * @return The crop item
     */
    public ItemStack createBDCrop(int amount) {
        ItemStack item = new ItemStack(Material.FERN, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "BD Crop");
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Harvested from BD seeds");
        lore.add(ChatColor.GRAY + "Can be sold to BD Collectors");
        lore.add(ChatColor.GRAY + "Used in crafting BD Sticks");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(bdCropKey, PersistentDataType.BYTE, (byte) 1);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a green BD crop item.
     * @param amount The amount of crops
     * @return The crop item
     */
    public ItemStack createGreenBDCrop(int amount) {
        ItemStack item = new ItemStack(Material.LARGE_FERN, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Green BD Crop");
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Premium BD crop");
        lore.add(ChatColor.GRAY + "Worth 5x more than regular BD crops");
        lore.add(ChatColor.GRAY + "Can be sold to BD Collectors");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(greenBDCropKey, PersistentDataType.BYTE, (byte) 1);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a purple BD crop item.
     * @param amount The amount of crops
     * @return The crop item
     */
    public ItemStack createPurpleBDCrop(int amount) {
        ItemStack item = new ItemStack(Material.LARGE_FERN, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Purple BD Crop");
        
        // Add glow effect
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Rare premium BD crop");
        lore.add(ChatColor.GRAY + "Worth more than 10x regular BD crops");
        lore.add(ChatColor.GRAY + "Highly sought after by collectors");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(purpleBDCropKey, PersistentDataType.BYTE, (byte) 1);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a BD stick item.
     * @return The BD stick
     */
    public ItemStack createBDStick() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "BD Stick");
        
        // Add glow effect
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Right-click to use (5 uses)");
        lore.add(ChatColor.GRAY + "Applies random potion effects");
        lore.add(ChatColor.GRAY + "Used in crafting BD Market Tokens");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(bdStickKey, PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer().set(durabilityKey, PersistentDataType.INTEGER, 5);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a BD harvester tool.
     * @return The BD harvester
     */
    public ItemStack createBDHarvester() {
        ItemStack item = new ItemStack(Material.IRON_HOE, 1);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.AQUA + "BD Harvester");
        
        // Add glow effect
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Harvests 50% more BD crops");
        lore.add(ChatColor.GRAY + "20 uses before breaking");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(bdHarvesterKey, PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer().set(durabilityKey, PersistentDataType.INTEGER, 20);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates an ultimate BD harvester tool.
     * @return The ultimate BD harvester
     */
    public ItemStack createUltimateBDHarvester() {
        ItemStack item = new ItemStack(Material.DIAMOND_HOE, 1);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Ultimate BD Harvester");
        
        // Add glow effect
        meta.addEnchant(Enchantment.DURABILITY, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Harvests 100% more BD crops");
        lore.add(ChatColor.GRAY + "60 uses before breaking");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(ultimateBDHarvesterKey, PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer().set(durabilityKey, PersistentDataType.INTEGER, 60);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a BD market token.
     * @return The market token
     */
    public ItemStack createBDMarketToken() {
        ItemStack item = new ItemStack(Material.STICK, 1);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "BD Market Token");
        
        // Add glow effect
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Place in an item frame above a door");
        lore.add(ChatColor.GRAY + "to create a BD Market");
        lore.add("");
        lore.add(ChatColor.GRAY + "Requirements:");
        lore.add(ChatColor.GRAY + "- 3x3 roof minimum");
        lore.add(ChatColor.GRAY + "- Walls with a door");
        lore.add(ChatColor.GRAY + "- Bed inside");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(bdMarketTokenKey, PersistentDataType.BYTE, (byte) 1);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Creates a BD house token.
     * @return The house token
     */
    public ItemStack createBDHouseToken() {
        ItemStack item = new ItemStack(Material.RED_BED, 1);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.AQUA + "BD House Token");
        
        // Add glow effect
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Place in an item frame above a door");
        lore.add(ChatColor.GRAY + "to create a BD Collector house");
        lore.add("");
        lore.add(ChatColor.GRAY + "Requirements:");
        lore.add(ChatColor.GRAY + "- Must be in an existing market");
        lore.add(ChatColor.GRAY + "- 3x3 roof minimum");
        lore.add(ChatColor.GRAY + "- Walls with a door");
        lore.add(ChatColor.GRAY + "- Bed inside");
        meta.setLore(lore);
        
        // Add custom data
        meta.getPersistentDataContainer().set(bdHouseTokenKey, PersistentDataType.BYTE, (byte) 1);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Checks if an item is a BD item.
     * @param item The item to check
     * @return True if it's a BD item
     */
    public boolean isBDItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(bdItemKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(greenBDItemKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(purpleBDItemKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(bdCropKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(greenBDCropKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(purpleBDCropKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(bdStickKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(bdHarvesterKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(ultimateBDHarvesterKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(bdMarketTokenKey, PersistentDataType.BYTE) ||
               meta.getPersistentDataContainer().has(bdHouseTokenKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a BD seed.
     * @param item The item to check
     * @return True if it's a BD seed
     */
    public boolean isBDSeed(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(bdItemKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a green BD seed.
     * @param item The item to check
     * @return True if it's a green BD seed
     */
    public boolean isGreenBDSeed(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(greenBDItemKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a purple BD seed.
     * @param item The item to check
     * @return True if it's a purple BD seed
     */
    public boolean isPurpleBDSeed(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(purpleBDItemKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a BD crop.
     * @param item The item to check
     * @return True if it's a BD crop
     */
    public boolean isBDCrop(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(bdCropKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a green BD crop.
     * @param item The item to check
     * @return True if it's a green BD crop
     */
    public boolean isGreenBDCrop(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(greenBDCropKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a purple BD crop.
     * @param item The item to check
     * @return True if it's a purple BD crop
     */
    public boolean isPurpleBDCrop(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(purpleBDCropKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a BD stick.
     * @param item The item to check
     * @return True if it's a BD stick
     */
    public boolean isBDStick(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(bdStickKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a BD harvester.
     * @param item The item to check
     * @return True if it's a BD harvester
     */
    public boolean isBDHarvester(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(bdHarvesterKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is an ultimate BD harvester.
     * @param item The item to check
     * @return True if it's an ultimate BD harvester
     */
    public boolean isUltimateBDHarvester(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(ultimateBDHarvesterKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a BD market token.
     * @param item The item to check
     * @return True if it's a BD market token
     */
    public boolean isBDMarketToken(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(bdMarketTokenKey, PersistentDataType.BYTE);
    }
    
    /**
     * Checks if an item is a BD house token.
     * @param item The item to check
     * @return True if it's a BD house token
     */
    public boolean isBDHouseToken(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(bdHouseTokenKey, PersistentDataType.BYTE);
    }
    
    /**
     * Gets the durability of a BD tool.
     * @param item The item to check
     * @return The durability, or 0 if not a valid BD tool
     */
    public int getBDToolDurability(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(durabilityKey, PersistentDataType.INTEGER)) return 0;
        
        return meta.getPersistentDataContainer().get(durabilityKey, PersistentDataType.INTEGER);
    }
    
    /**
     * Sets the durability of a BD tool.
     * @param item The item to modify
     * @param durability The new durability
     * @return True if the durability was set successfully
     */
    public boolean setBDToolDurability(ItemStack item, int durability) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(durabilityKey, PersistentDataType.INTEGER)) return false;
        
        if (durability <= 0) {
            return false; // Item would break, return false to indicate this
        }
        
        meta.getPersistentDataContainer().set(durabilityKey, PersistentDataType.INTEGER, durability);
        
        // Update lore to reflect new durability
        List<String> lore = meta.getLore();
        if (lore != null && !lore.isEmpty()) {
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                if (line.contains("uses")) {
                    if (meta.getPersistentDataContainer().has(bdStickKey, PersistentDataType.BYTE)) {
                        lore.set(i, ChatColor.GRAY + "Right-click to use (" + String.valueOf(durability) + " uses)");
                    } else if (meta.getPersistentDataContainer().has(bdHarvesterKey, PersistentDataType.BYTE)) {
                        lore.set(i, ChatColor.GRAY + String.valueOf(durability) + " uses before breaking");
                    } else if (meta.getPersistentDataContainer().has(ultimateBDHarvesterKey, PersistentDataType.BYTE)) {
                        lore.set(i, ChatColor.GRAY + String.valueOf(durability) + " uses before breaking");
                    }
                    break;
                }
            }
            meta.setLore(lore);
        }
        
        item.setItemMeta(meta);
        return true;
    }
    
    /**
     * Decreases the durability of a BD tool by 1.
     * @param item The item to modify
     * @return True if the durability was decreased successfully, false if the item should break
     */
    public boolean decreaseBDToolDurability(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        int durability = getBDToolDurability(item);
        if (durability <= 1) {
            return false; // Item should break
        }
        
        return setBDToolDurability(item, durability - 1);
    }
}