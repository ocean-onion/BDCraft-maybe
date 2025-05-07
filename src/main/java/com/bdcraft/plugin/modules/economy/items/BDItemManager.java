package com.bdcraft.plugin.modules.economy.items;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Handles the creation and management of special BD items.
 */
public class BDItemManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final NamespacedKey bdItemKey;
    private final NamespacedKey bdItemTypeKey;
    private final NamespacedKey bdItemValueKey;
    
    private final Map<String, BDItemTemplate> itemTemplates;
    
    /**
     * Creates a new BD item manager.
     * @param plugin The plugin instance
     */
    public BDItemManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.bdItemKey = new NamespacedKey(plugin, "bd_item");
        this.bdItemTypeKey = new NamespacedKey(plugin, "bd_item_type");
        this.bdItemValueKey = new NamespacedKey(plugin, "bd_item_value");
        
        this.itemTemplates = new HashMap<>();
        
        // Load item templates
        loadItemTemplates();
    }
    
    /**
     * Loads item templates from configuration.
     */
    private void loadItemTemplates() {
        // Market token
        registerItemTemplate("market_token", Material.EMERALD, 
                "&aMarket Token", 
                Arrays.asList(
                    "&7Creates a BD Market when placed in",
                    "&7a properly configured 3x3 area."
                ));
        
        // House token
        registerItemTemplate("house_token", Material.DIAMOND, 
                "&bHouse Token", 
                Arrays.asList(
                    "&7Places a BD Collector House when",
                    "&7used in a BD Market area."
                ));
        
        // Special seeds
        registerItemTemplate("tier1_seed", Material.WHEAT_SEEDS, 
                "&2Tier 1 Special Seeds", 
                Arrays.asList(
                    "&7High-quality seeds that produce",
                    "&7better crops than normal seeds."
                ));
                
        registerItemTemplate("tier2_seed", Material.PUMPKIN_SEEDS, 
                "&aTier 2 Special Seeds", 
                Arrays.asList(
                    "&7Premium seeds that produce",
                    "&7exceptional crops."
                ));
                
        registerItemTemplate("tier3_seed", Material.MELON_SEEDS, 
                "&bTier 3 Special Seeds", 
                Arrays.asList(
                    "&7Extraordinary seeds that produce",
                    "&7the highest quality crops."
                ));
        
        // Special tools
        registerItemTemplate("harvester_tool", Material.GOLDEN_HOE, 
                "&eAutomatic Harvester", 
                Arrays.asList(
                    "&7Right-click crops to automatically",
                    "&7harvest and replant in a 3x3 area."
                ));
                
        registerItemTemplate("fertilizer", Material.BONE_MEAL, 
                "&dEnhanced Fertilizer", 
                Arrays.asList(
                    "&7Right-click crops to accelerate growth",
                    "&7and increase yields."
                ));
                
        registerItemTemplate("bd_stick", Material.STICK, 
                "&6BD Stick", 
                Arrays.asList(
                    "&7A special stick that can be used",
                    "&7to craft BD Market Tokens and",
                    "&7BD House Tokens.",
                    "&7Also provides a temporary farming buff."
                ));
                
        // BD Market Token (used to create player markets)
        registerItemTemplate("market_token", Material.EMERALD, 
                "&2BD Market Token", 
                Arrays.asList(
                    "&7Place this token in the center of a 3x3",
                    "&7stone platform to create a BD Market.",
                    "&7Creates a 49x49 block market area.",
                    "&7Spawns a Market Owner and BD Dealer."
                ));
                
        // BD House Token (used to add collector houses to markets)
        registerItemTemplate("house_token", Material.DIAMOND, 
                "&bBD House Token", 
                Arrays.asList(
                    "&7Place this token in your market to",
                    "&7create a Collector House.",
                    "&7Spawns a BD Collector who will buy crops.",
                    "&7Multiple collectors can exist in one market."
                ));
    }
    
    /**
     * Registers a new item template.
     * @param id The item ID
     * @param material The item material
     * @param name The item name
     * @param lore The item lore
     */
    private void registerItemTemplate(String id, Material material, String name, List<String> lore) {
        BDItemTemplate template = new BDItemTemplate(id, material, name, lore);
        itemTemplates.put(id.toLowerCase(), template);
    }
    
    /**
     * Creates a BD item from a template.
     * @param id The template ID
     * @param amount The amount
     * @param value The value (optional)
     * @return The created item, or null if the template doesn't exist
     */
    public ItemStack createItem(String id, int amount, int value) {
        BDItemTemplate template = itemTemplates.get(id.toLowerCase());
        if (template == null) {
            return null;
        }
        
        ItemStack item = new ItemStack(template.getMaterial(), amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set display name
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', template.getName()));
            
            // Set lore
            List<String> lore = template.getLore().stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList());
            meta.setLore(lore);
            
            // Add enchant glow
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            
            // Store metadata
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(bdItemKey, PersistentDataType.BYTE, (byte) 1);
            pdc.set(bdItemTypeKey, PersistentDataType.STRING, id.toLowerCase());
            
            if (value > 0) {
                pdc.set(bdItemValueKey, PersistentDataType.INTEGER, value);
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Creates a BD item from a template.
     * @param id The template ID
     * @param amount The amount
     * @return The created item, or null if the template doesn't exist
     */
    public ItemStack createItem(String id, int amount) {
        return createItem(id, amount, 0);
    }
    
    /**
     * Checks if an item is a BD item.
     * @param item The item to check
     * @return Whether the item is a BD item
     */
    public boolean isBDItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(bdItemKey, PersistentDataType.BYTE);
    }
    
    /**
     * Gets the BD item type of an item.
     * @param item The item
     * @return The BD item type, or null if the item is not a BD item
     */
    public String getBDItemType(ItemStack item) {
        if (!isBDItem(item)) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        
        return meta.getPersistentDataContainer().get(bdItemTypeKey, PersistentDataType.STRING);
    }
    
    /**
     * Gets the value of a BD item.
     * @param item The item
     * @return The value, or 0 if not set
     */
    public int getBDItemValue(ItemStack item) {
        if (!isBDItem(item)) {
            return 0;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return 0;
        }
        
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(bdItemValueKey, PersistentDataType.INTEGER) ?
                pdc.get(bdItemValueKey, PersistentDataType.INTEGER) : 0;
    }
    
    /**
     * Gives a BD item to a player.
     * @param player The player
     * @param id The BD item ID
     * @param amount The amount
     * @param value The value (optional)
     * @return Whether the item was given
     */
    public boolean giveItem(Player player, String id, int amount, int value) {
        ItemStack item = createItem(id, amount, value);
        if (item == null) {
            return false;
        }
        
        Map<Integer, ItemStack> overflow = player.getInventory().addItem(item);
        
        // Drop overflow items
        if (!overflow.isEmpty()) {
            for (ItemStack overflowItem : overflow.values()) {
                player.getWorld().dropItem(player.getLocation(), overflowItem);
            }
            
            player.sendMessage(ChatColor.YELLOW + "Some items were dropped because your inventory is full.");
        }
        
        return true;
    }
    
    /**
     * Gives a BD item to a player.
     * @param player The player
     * @param id The BD item ID
     * @param amount The amount
     * @return Whether the item was given
     */
    public boolean giveItem(Player player, String id, int amount) {
        return giveItem(player, id, amount, 0);
    }
    
    /**
     * Inner class that represents a BD item template.
     */
    private static class BDItemTemplate {
        private final String id;
        private final Material material;
        private final String name;
        private final List<String> lore;
        
        /**
         * Creates a new BD item template.
         * @param id The template ID
         * @param material The material
         * @param name The name
         * @param lore The lore
         */
        public BDItemTemplate(String id, Material material, String name, List<String> lore) {
            this.id = id;
            this.material = material;
            this.name = name;
            this.lore = new ArrayList<>(lore);
        }
        
        /**
         * Gets the ID.
         * @return The ID
         */
        public String getId() {
            return id;
        }
        
        /**
         * Gets the material.
         * @return The material
         */
        public Material getMaterial() {
            return material;
        }
        
        /**
         * Gets the name.
         * @return The name
         */
        public String getName() {
            return name;
        }
        
        /**
         * Gets the lore.
         * @return The lore
         */
        public List<String> getLore() {
            return lore;
        }
    }
}