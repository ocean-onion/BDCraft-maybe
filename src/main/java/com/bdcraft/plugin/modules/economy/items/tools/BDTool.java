package com.bdcraft.plugin.modules.economy.items.tools;

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
 * Represents a BD tool item.
 */
public class BDTool extends BDItem {
    public static final String BDSTICK_KEY = "bd_stick";
    public static final String HARVESTER_KEY = "bd_harvester";
    public static final String ULTIMATE_HARVESTER_KEY = "bd_ultimate_harvester";
    
    private final ToolType toolType;
    private final int requiredRank;
    private final int durability;
    
    /**
     * Creates a new BD tool.
     * 
     * @param plugin The plugin instance
     * @param toolType The type of tool
     */
    public BDTool(BDCraft plugin, ToolType toolType) {
        super(plugin);
        this.toolType = toolType;
        
        switch (toolType) {
            case BDSTICK:
                this.requiredRank = 0; // No rank requirement
                this.durability = 5; // 5 uses before breaking
                break;
            case HARVESTER:
                this.requiredRank = 3; // Expert Farmer rank (3rd rank)
                this.durability = 20; // 20 uses before breaking
                break;
            case ULTIMATE_HARVESTER:
                this.requiredRank = 5; // Agricultural Expert rank (5th rank)
                this.durability = 60; // 60 uses before breaking
                break;
            default:
                this.requiredRank = 0;
                this.durability = 1;
                break;
        }
    }
    
    @Override
    public ItemStack createItemStack(int amount) {
        Material material;
        String displayName;
        String itemKey;
        ChatColor color;
        List<String> loreLines = new ArrayList<>();
        
        switch (toolType) {
            case BDSTICK:
                material = Material.BLAZE_ROD;
                displayName = "BD Stick";
                itemKey = BDSTICK_KEY;
                color = ChatColor.GOLD;
                loreLines.add(ChatColor.GRAY + "Applies random potion effects when used");
                loreLines.add(ChatColor.GRAY + "Required for market creation");
                loreLines.add(ChatColor.GRAY + "Durability: " + durability + " uses");
                break;
            case HARVESTER:
                material = Material.IRON_HOE;
                displayName = "BD Harvester";
                itemKey = HARVESTER_KEY;
                color = ChatColor.AQUA;
                loreLines.add(ChatColor.GRAY + "+50% bonus yield when harvesting BD crops");
                loreLines.add(ChatColor.GRAY + "Creates blue particles when harvesting");
                loreLines.add(ChatColor.GRAY + "Durability: " + durability + " uses");
                break;
            case ULTIMATE_HARVESTER:
                material = Material.DIAMOND_HOE;
                displayName = "Ultimate BD Harvester";
                itemKey = ULTIMATE_HARVESTER_KEY;
                color = ChatColor.LIGHT_PURPLE;
                loreLines.add(ChatColor.GRAY + "+100% bonus yield when harvesting BD crops");
                loreLines.add(ChatColor.GRAY + "Creates purple particles when harvesting");
                loreLines.add(ChatColor.GRAY + "Durability: " + durability + " uses");
                break;
            default:
                material = Material.WOODEN_HOE;
                displayName = "Unknown BD Tool";
                itemKey = "unknown_tool";
                color = ChatColor.GRAY;
                loreLines.add(ChatColor.RED + "This tool has no effect");
                break;
        }
        
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set display name
            meta.setDisplayName(color + displayName);
            
            // Add enchantment glow
            Enchantment unbreaking = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft("unbreaking"));
            if (unbreaking != null) {
                meta.addEnchant(unbreaking, 1, true);
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            
            // Add lore
            if (requiredRank > 0) {
                loreLines.add(ChatColor.RED + "Requires rank: " + getRequiredRankName());
            }
            
            meta.setLore(loreLines);
            
            // Add custom NBT data
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(getNamespacedKey(itemKey), PersistentDataType.STRING, itemKey);
            container.set(getNamespacedKey(itemKey + "_durability"), PersistentDataType.INTEGER, durability);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Gets the type of tool.
     * 
     * @return The tool type
     */
    public ToolType getToolType() {
        return toolType;
    }
    
    /**
     * Gets the required rank to use this tool.
     * 
     * @return The required rank
     */
    public int getRequiredRank() {
        return requiredRank;
    }
    
    /**
     * Gets the durability of this tool.
     * 
     * @return The durability
     */
    public int getDurability() {
        return durability;
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
                return "Expert Farmer";
            case 4:
                return "Master Farmer";
            case 5:
                return "Agricultural Expert";
            default:
                return "Unknown";
        }
    }
    
    /**
     * Gets the crop yield multiplier for this tool.
     * 
     * @return The yield multiplier
     */
    public double getYieldMultiplier() {
        switch (toolType) {
            case HARVESTER:
                return 1.5; // 50% increase (from documentation)
            case ULTIMATE_HARVESTER:
                return 2.0; // 100% increase (from documentation)
            default:
                return 1.0;
        }
    }
    
    /**
     * Checks if an item is a BD tool.
     * 
     * @param item The item to check
     * @return True if the item is a BD tool
     */
    public static boolean isBDTool(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        return container.has(BDItem.getStaticNamespacedKey(BDSTICK_KEY), PersistentDataType.STRING) ||
               container.has(BDItem.getStaticNamespacedKey(HARVESTER_KEY), PersistentDataType.STRING) ||
               container.has(BDItem.getStaticNamespacedKey(ULTIMATE_HARVESTER_KEY), PersistentDataType.STRING);
    }
    
    /**
     * Gets the tool type from an item.
     * 
     * @param item The item to check
     * @return The tool type, or null if the item is not a BD tool
     */
    public static ToolType getToolType(ItemStack item) {
        if (!isBDTool(item)) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        if (container.has(BDItem.getStaticNamespacedKey(BDSTICK_KEY), PersistentDataType.STRING)) {
            return ToolType.BDSTICK;
        } else if (container.has(BDItem.getStaticNamespacedKey(HARVESTER_KEY), PersistentDataType.STRING)) {
            return ToolType.HARVESTER;
        } else if (container.has(BDItem.getStaticNamespacedKey(ULTIMATE_HARVESTER_KEY), PersistentDataType.STRING)) {
            return ToolType.ULTIMATE_HARVESTER;
        }
        
        return null;
    }
    
    /**
     * Gets the remaining durability of a tool.
     * 
     * @param item The item to check
     * @return The remaining durability, or 0 if the item is not a BD tool
     */
    public static int getRemainingDurability(ItemStack item) {
        if (!isBDTool(item)) {
            return 0;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        ToolType type = getToolType(item);
        
        if (type == null) {
            return 0;
        }
        
        String key;
        switch (type) {
            case BDSTICK:
                key = BDSTICK_KEY;
                break;
            case HARVESTER:
                key = HARVESTER_KEY;
                break;
            case ULTIMATE_HARVESTER:
                key = ULTIMATE_HARVESTER_KEY;
                break;
            default:
                return 0;
        }
        
        return container.getOrDefault(BDItem.getStaticNamespacedKey(key + "_durability"), PersistentDataType.INTEGER, 0);
    }
    
    /**
     * Damages a BD tool.
     * 
     * @param item The item to damage
     * @param amount The amount of damage
     * @return True if the tool is still usable, false if it broke
     */
    public static boolean damageTool(ItemStack item, int amount) {
        if (!isBDTool(item)) {
            return false;
        }
        
        int durability = getRemainingDurability(item);
        int newDurability = durability - amount;
        
        if (newDurability <= 0) {
            return false; // Tool broke
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        ToolType type = getToolType(item);
        
        if (type == null) {
            return false;
        }
        
        String key;
        switch (type) {
            case BDSTICK:
                key = BDSTICK_KEY;
                break;
            case HARVESTER:
                key = HARVESTER_KEY;
                break;
            case ULTIMATE_HARVESTER:
                key = ULTIMATE_HARVESTER_KEY;
                break;
            default:
                return false;
        }
        
        container.set(BDItem.getStaticNamespacedKey(key + "_durability"), PersistentDataType.INTEGER, newDurability);
        
        // Update lore
        List<String> lore = meta.getLore();
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).contains("Durability:")) {
                    lore.set(i, ChatColor.GRAY + "Durability: " + newDurability);
                    break;
                }
            }
            meta.setLore(lore);
        }
        
        item.setItemMeta(meta);
        return true;
    }
}