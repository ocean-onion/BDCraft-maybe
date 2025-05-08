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
import java.util.List;

/**
 * Factory for creating BD items.
 */
public class BDItemFactory {
    private final BDCraft plugin;
    private final NamespacedKey bdItemKey;
    private final NamespacedKey bdItemTypeKey;
    private final NamespacedKey bdItemDurabilityKey;
    private final NamespacedKey bdItemValueKey;
    
    /**
     * Creates a new BD item factory.
     * @param plugin The plugin instance
     */
    public BDItemFactory(BDCraft plugin) {
        this.plugin = plugin;
        this.bdItemKey = new NamespacedKey(plugin, "bd_item");
        this.bdItemTypeKey = new NamespacedKey(plugin, "bd_item_type");
        this.bdItemDurabilityKey = new NamespacedKey(plugin, "bd_item_durability");
        this.bdItemValueKey = new NamespacedKey(plugin, "bd_item_value");
    }
    
    /**
     * Creates a regular BD seed item.
     * @param amount The amount
     * @return The item
     */
    public ItemStack createRegularBDSeed(int amount) {
        ItemStack item = new ItemStack(Material.WHEAT_SEEDS, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Regular BD Seeds");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Plant these seeds to grow regular BD crops.");
        lore.add(ChatColor.GRAY + "Can be sold to BD Collectors for emeralds.");
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "REGULAR_BD_SEED");
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates a green BD seed item.
     * @param amount The amount
     * @return The item
     */
    public ItemStack createGreenBDSeed(int amount) {
        ItemStack item = new ItemStack(Material.BEETROOT_SEEDS, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Green BD Seeds");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Plant these seeds to grow green BD crops.");
        lore.add(ChatColor.GRAY + "More valuable than regular BD crops.");
        lore.add(ChatColor.GRAY + "Can be sold to BD Collectors for emeralds.");
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "GREEN_BD_SEED");
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates a purple BD seed item.
     * @param amount The amount
     * @return The item
     */
    public ItemStack createPurpleBDSeed(int amount) {
        ItemStack item = new ItemStack(Material.PUMPKIN_SEEDS, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Purple BD Seeds");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Plant these seeds to grow purple BD crops.");
        lore.add(ChatColor.GRAY + "The most valuable BD crop type.");
        lore.add(ChatColor.GRAY + "Can be sold to BD Collectors for many emeralds.");
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "PURPLE_BD_SEED");
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates a regular BD crop item.
     * @param amount The amount
     * @return The item
     */
    public ItemStack createRegularBDCrop(int amount) {
        ItemStack item = new ItemStack(Material.FERN, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Regular BD Crop");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Harvested from regular BD seeds.");
        lore.add(ChatColor.GRAY + "Can be sold to BD Collectors for emeralds.");
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "REGULAR_BD_CROP");
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates a green BD crop item.
     * @param amount The amount
     * @return The item
     */
    public ItemStack createGreenBDCrop(int amount) {
        ItemStack item = new ItemStack(Material.LARGE_FERN, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Green BD Crop");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Harvested from green BD seeds.");
        lore.add(ChatColor.GRAY + "More valuable than regular BD crops.");
        lore.add(ChatColor.GRAY + "Can be sold to BD Collectors for emeralds.");
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "GREEN_BD_CROP");
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates a purple BD crop item.
     * @param amount The amount
     * @return The item
     */
    public ItemStack createPurpleBDCrop(int amount) {
        ItemStack item = new ItemStack(Material.TALL_GRASS, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Purple BD Crop");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Harvested from purple BD seeds.");
        lore.add(ChatColor.GRAY + "The most valuable BD crop type.");
        lore.add(ChatColor.GRAY + "Can be sold to BD Collectors for many emeralds.");
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "PURPLE_BD_CROP");
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates a BD stick item.
     * @param amount The amount
     * @param durability The durability
     * @return The item
     */
    public ItemStack createBDStick(int amount, int durability) {
        ItemStack item = new ItemStack(Material.STICK, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "BD Stick");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A special stick used to harvest BD crops.");
        lore.add(ChatColor.GRAY + "Durability: " + durability + "/5");
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "BD_STICK");
        container.set(bdItemDurabilityKey, PersistentDataType.INTEGER, durability);
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates a BD harvester item.
     * @param amount The amount
     * @param durability The durability
     * @return The item
     */
    public ItemStack createBDHarvester(int amount, int durability) {
        ItemStack item = new ItemStack(Material.SHEARS, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "BD Harvester");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A special tool for harvesting BD crops efficiently.");
        lore.add(ChatColor.GRAY + "Durability: " + durability + "/20");
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "BD_HARVESTER");
        container.set(bdItemDurabilityKey, PersistentDataType.INTEGER, durability);
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates an ultimate BD harvester item.
     * @param amount The amount
     * @param durability The durability
     * @return The item
     */
    public ItemStack createUltimateBDHarvester(int amount, int durability) {
        ItemStack item = new ItemStack(Material.GOLDEN_HOE, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "Ultimate BD Harvester");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "The ultimate tool for harvesting BD crops.");
        lore.add(ChatColor.GRAY + "25% chance to get extra crops when harvesting.");
        lore.add(ChatColor.GRAY + "Durability: " + durability + "/60");
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "ULTIMATE_BD_HARVESTER");
        container.set(bdItemDurabilityKey, PersistentDataType.INTEGER, durability);
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates a market token item.
     * @param amount The amount
     * @return The item
     */
    public ItemStack createMarketToken(int amount) {
        ItemStack item = new ItemStack(Material.ITEM_FRAME, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "Market Token");
        List<String> lore = Arrays.asList(
            ChatColor.GRAY + "Place this token in an item frame above a door",
            ChatColor.GRAY + "to create a BD Market."
        );
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "MARKET_TOKEN");
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Creates a house token item.
     * @param amount The amount
     * @return The item
     */
    public ItemStack createHouseToken(int amount) {
        ItemStack item = new ItemStack(Material.ITEM_FRAME, amount);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.AQUA + "House Token");
        List<String> lore = Arrays.asList(
            ChatColor.GRAY + "Place this token in an item frame above a door",
            ChatColor.GRAY + "to create a BD Collector house."
        );
        meta.setLore(lore);
        
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemKey, PersistentDataType.STRING, "true");
        container.set(bdItemTypeKey, PersistentDataType.STRING, "HOUSE_TOKEN");
        
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1); // Adds enchant glow
        
        return item;
    }
    
    /**
     * Checks if an item is a BD item.
     * @param item The item
     * @return True if the item is a BD item, false otherwise
     */
    public boolean isBDItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(bdItemKey, PersistentDataType.STRING);
    }
    
    /**
     * Gets the BD item type from an item.
     * @param item The item
     * @return The item type, or null if not a BD item
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
     * Gets the BD item durability from an item.
     * @param item The item
     * @return The durability, or -1 if the item has no durability
     */
    public int getBDItemDurability(ItemStack item) {
        if (!isBDItem(item)) {
            return -1;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        if (!container.has(bdItemDurabilityKey, PersistentDataType.INTEGER)) {
            return -1;
        }
        
        return container.get(bdItemDurabilityKey, PersistentDataType.INTEGER);
    }
    
    /**
     * Sets the BD item durability on an item.
     * @param item The item
     * @param durability The durability
     * @return The updated item
     */
    public ItemStack setBDItemDurability(ItemStack item, int durability) {
        if (!isBDItem(item)) {
            return item;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        if (!container.has(bdItemDurabilityKey, PersistentDataType.INTEGER)) {
            return item;
        }
        
        container.set(bdItemDurabilityKey, PersistentDataType.INTEGER, durability);
        
        // Update lore
        List<String> lore = meta.getLore();
        if (lore != null && !lore.isEmpty()) {
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                if (line.contains("Durability:")) {
                    String itemType = container.get(bdItemTypeKey, PersistentDataType.STRING);
                    int maxDurability;
                    
                    switch (itemType) {
                        case "BD_STICK":
                            maxDurability = 5;
                            break;
                        case "BD_HARVESTER":
                            maxDurability = 20;
                            break;
                        case "ULTIMATE_BD_HARVESTER":
                            maxDurability = 60;
                            break;
                        default:
                            maxDurability = 0;
                    }
                    
                    lore.set(i, ChatColor.GRAY + "Durability: " + durability + "/" + maxDurability);
                    break;
                }
            }
            meta.setLore(lore);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Gets the BD item value from an item.
     * @param item The item
     * @return The value, or -1 if the item has no value
     */
    public int getBDItemValue(ItemStack item) {
        if (!isBDItem(item)) {
            return -1;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        if (!container.has(bdItemValueKey, PersistentDataType.INTEGER)) {
            return -1;
        }
        
        return container.get(bdItemValueKey, PersistentDataType.INTEGER);
    }
    
    /**
     * Sets the BD item value on an item.
     * @param item The item
     * @param value The value
     * @return The updated item
     */
    public ItemStack setBDItemValue(ItemStack item, int value) {
        if (!isBDItem(item)) {
            return item;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(bdItemValueKey, PersistentDataType.INTEGER, value);
        item.setItemMeta(meta);
        return item;
    }
}