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
    private final BDItemFactory itemFactory;
    
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
        this.itemFactory = new BDItemFactory(plugin);
        
        // Load item templates
        loadItemTemplates();
    }
    
    /**
     * Loads item templates from configuration.
     */
    private void loadItemTemplates() {
        // BD Seeds (Regular, Green, Purple)
        registerItemTemplate("bd_seed", Material.WHEAT_SEEDS, 
                "&6BD Seed", 
                Arrays.asList(
                    "&7Special seeds for growing BD crops.",
                    "&7Plant like normal wheat seeds.",
                    "&7Produces BD Crops when harvested."
                ));
        
        registerItemTemplate("green_bd_seed", Material.BEETROOT_SEEDS, 
                "&aGreen BD Seed", 
                Arrays.asList(
                    "&7Premium seeds that grow 30% faster.",
                    "&7Plant like normal seeds.",
                    "&7Produces Green BD Crops when harvested.",
                    "&7Requires Farmer rank or higher."
                ));
        
        registerItemTemplate("purple_bd_seed", Material.PUMPKIN_SEEDS, 
                "&5Purple BD Seed", 
                Arrays.asList(
                    "&7Rare seeds that produce valuable crops.",
                    "&7Plant like normal seeds.",
                    "&7Produces Purple BD Crops when harvested.",
                    "&7Requires Master Farmer rank or higher."
                ));
        
        // BD Crops (Regular, Green, Purple)
        registerItemTemplate("bd_crop", Material.FERN, 
                "&6BD Crop", 
                Arrays.asList(
                    "&7Standard BD crop harvested from BD Seeds.",
                    "&7Sell to BD Collectors for emeralds.",
                    "&710 crops = 2 emeralds + 50 server currency"
                ));
        
        registerItemTemplate("green_bd_crop", Material.LARGE_FERN, 
                "&aGreen BD Crop", 
                Arrays.asList(
                    "&7Premium BD crop, worth 5x more than regular.",
                    "&7Sell to BD Collectors for emeralds.",
                    "&75 crops = 10 emeralds + 150 server currency"
                ));
        
        registerItemTemplate("purple_bd_crop", Material.LARGE_FERN, 
                "&5Purple BD Crop", 
                Arrays.asList(
                    "&7Rare high-value BD crop, worth 10x more than regular.",
                    "&7Sell to BD Collectors for emeralds.",
                    "&73 crops = 20 emeralds + 400 server currency"
                ));
        
        // BD Tools
        registerItemTemplate("bd_stick", Material.BLAZE_ROD, 
                "&6BD Stick", 
                Arrays.asList(
                    "&7A special enchanted blaze rod that can be used",
                    "&7to craft BD Market Tokens and",
                    "&7BD House Tokens.",
                    "&7Has 5 uses before breaking."
                ));
        
        registerItemTemplate("bd_harvester", Material.GOLDEN_HOE, 
                "&eBD Harvester", 
                Arrays.asList(
                    "&7Special tool for harvesting BD crops.",
                    "&7Increases yield by 25%.",
                    "&7Has 20 uses before breaking.",
                    "&7Requires Expert Farmer rank or higher."
                ));
        
        registerItemTemplate("ultimate_bd_harvester", Material.DIAMOND_HOE, 
                "&bUltimate BD Harvester", 
                Arrays.asList(
                    "&7Premium tool for harvesting BD crops.",
                    "&7Increases yield by 50%.",
                    "&7Has 60 uses before breaking.",
                    "&7Requires Agricultural Expert rank or higher."
                ));
        
        // BD Tokens
        registerItemTemplate("market_token", Material.EMERALD, 
                "&2BD Market Token", 
                Arrays.asList(
                    "&7Place this token in the center of a 3x3",
                    "&7platform to create a BD Market.",
                    "&7Creates a 49x49 block market area.",
                    "&7Spawns a Market Owner and BD Dealer."
                ));
                
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
    
    // Specialized BD item creation methods
    
    /**
     * Creates regular BD seeds.
     * @param amount The amount
     * @return The BD seeds
     */
    public ItemStack createBDSeed(int amount) {
        return createItem("bd_seed", amount);
    }
    
    /**
     * Creates green BD seeds.
     * @param amount The amount
     * @return The green BD seeds
     */
    public ItemStack createGreenBDSeed(int amount) {
        return createItem("green_bd_seed", amount);
    }
    
    /**
     * Creates purple BD seeds.
     * @param amount The amount
     * @return The purple BD seeds
     */
    public ItemStack createPurpleBDSeed(int amount) {
        return createItem("purple_bd_seed", amount);
    }
    
    /**
     * Creates regular BD crops.
     * @param amount The amount
     * @return The BD crops
     */
    public ItemStack createBDCrop(int amount) {
        return createItem("bd_crop", amount);
    }
    
    /**
     * Creates green BD crops.
     * @param amount The amount
     * @return The green BD crops
     */
    public ItemStack createGreenBDCrop(int amount) {
        return createItem("green_bd_crop", amount);
    }
    
    /**
     * Creates purple BD crops.
     * @param amount The amount
     * @return The purple BD crops
     */
    public ItemStack createPurpleBDCrop(int amount) {
        return createItem("purple_bd_crop", amount);
    }
    
    /**
     * Creates a BD stick.
     * @return The BD stick
     */
    public ItemStack createBDStick() {
        return createItem("bd_stick", 1, 5); // 5 uses
    }
    
    /**
     * Creates a BD harvester.
     * @return The BD harvester
     */
    public ItemStack createBDHarvester() {
        return createItem("bd_harvester", 1, 20); // 20 uses
    }
    
    /**
     * Creates an ultimate BD harvester.
     * @return The ultimate BD harvester
     */
    public ItemStack createUltimateBDHarvester() {
        return createItem("ultimate_bd_harvester", 1, 60); // 60 uses
    }
    
    /**
     * Creates a market token.
     * @return The market token
     */
    public ItemStack createMarketToken() {
        return createItem("market_token", 1);
    }
    
    /**
     * Creates a house token.
     * @return The house token
     */
    public ItemStack createHouseToken() {
        return createItem("house_token", 1);
    }
    
    /**
     * Gets the item factory for creating specialized BD items.
     * @return The item factory
     */
    public BDItemFactory getItemFactory() {
        return itemFactory;
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