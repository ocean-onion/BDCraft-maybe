package com.bdcraft.plugin.modules.economy.items.seeds;

import com.bdcraft.plugin.BDCraft;
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
 * Class representing a special BD seed.
 */
public class BDSeed {
    private final ItemStack itemStack;
    private final SeedType type;
    private final int growthTime;
    
    /**
     * Creates a new BD seed.
     * 
     * @param itemStack The item stack
     * @param type The seed type
     * @param growthTime The growth time in ticks
     */
    public BDSeed(ItemStack itemStack, SeedType type, int growthTime) {
        this.itemStack = itemStack;
        this.type = type;
        this.growthTime = growthTime;
    }
    
    /**
     * Creates a new BD seed from a seed type.
     * 
     * @param plugin The plugin instance
     * @param type The seed type
     */
    public BDSeed(BDCraft plugin, SeedType type) {
        this.type = type;
        
        // Default growth times based on seed quality
        switch (type) {
            case LEGENDARY:
                this.growthTime = 2400; // 2 minutes
                break;
            case PURPLE:
                this.growthTime = 3600; // 3 minutes
                break; 
            case BLUE:
                this.growthTime = 4800; // 4 minutes
                break;
            case GREEN:
                this.growthTime = 6000; // 5 minutes
                break;
            default:
                this.growthTime = 8400; // 7 minutes
                break;
        }
        
        // Create the item stack
        this.itemStack = new ItemStack(Material.WHEAT_SEEDS);
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(getSeedDisplayName());
            this.itemStack.setItemMeta(meta);
        }
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
     * Gets the seed type.
     * 
     * @return The seed type
     */
    public SeedType getType() {
        return type;
    }
    
    /**
     * Gets the growth time.
     * 
     * @return The growth time in ticks
     */
    public int getGrowthTime() {
        return growthTime;
    }
    
    /**
     * Checks if an ItemStack is a BD seed.
     * 
     * @param item The ItemStack to check
     * @return True if it's a BD seed
     */
    public static boolean isBDSeed(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return false;
        }
        
        List<String> lore = item.getItemMeta().getLore();
        if (lore == null || lore.isEmpty()) {
            return false;
        }
        
        // Check for BD seed identifier in lore
        for (String line : lore) {
            if (line.contains("Special") && line.contains("quality seeds")) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets the seed type from an ItemStack.
     * 
     * @param item The ItemStack
     * @return The seed type, or null if not a BD seed
     */
    public static SeedType getSeedType(ItemStack item) {
        if (!isBDSeed(item)) {
            return null;
        }
        
        String displayName = item.getItemMeta().getDisplayName();
        
        if (displayName.contains("Quality Seeds")) {
            return SeedType.GREEN;
        } else if (displayName.contains("Premium Seeds")) {
            return SeedType.BLUE;
        } else if (displayName.contains("Exceptional Seeds")) {
            return SeedType.PURPLE;
        } else if (displayName.contains("Legendary Seeds")) {
            return SeedType.LEGENDARY;
        } else {
            return SeedType.REGULAR;
        }
    }
    
    /**
     * Creates an ItemStack for this seed.
     * 
     * @param quantity The quantity
     * @return The ItemStack
     */
    public ItemStack createItemStack(int quantity) {
        Material material = Material.WHEAT_SEEDS;
        ItemStack seeds = new ItemStack(material, quantity);
        ItemMeta meta = seeds.getItemMeta();
        
        if (meta != null) {
            String displayName;
            ChatColor color;
            
            switch (type) {
                case GREEN:
                    displayName = "Quality Seeds";
                    color = ChatColor.GREEN;
                    break;
                case BLUE:
                    displayName = "Premium Seeds";
                    color = ChatColor.BLUE;
                    break;
                case PURPLE:
                    displayName = "Exceptional Seeds";
                    color = ChatColor.LIGHT_PURPLE;
                    break;
                case LEGENDARY:
                    displayName = "Legendary Seeds";
                    color = ChatColor.GOLD;
                    break;
                default:
                    displayName = "Seeds";
                    color = ChatColor.WHITE;
                    break;
            }
            
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Special " + color + type.name().toLowerCase() + 
                    ChatColor.GRAY + " quality seeds that");
            lore.add(ChatColor.GRAY + "grow into higher value crops.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Plant these to grow special crops");
            lore.add(ChatColor.GRAY + "worth " + ChatColor.GOLD + getSeedValue() + " BD each" + 
                    ChatColor.GRAY + " when harvested.");
            lore.add("");
            lore.add(ChatColor.GRAY + "ID: " + UUID.randomUUID().toString().substring(0, 8));
            
            meta.setLore(lore);
            
            // Add enchant glow for better seeds
            if (type != SeedType.REGULAR) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            
            seeds.setItemMeta(meta);
        }
        
        return seeds;
    }
    
    /**
     * Gets the value of the seed.
     * 
     * @return The value
     */
    public int getSeedValue() {
        switch (type) {
            case REGULAR:
                return 5;
            case GREEN:
                return 15;
            case BLUE:
                return 30;
            case PURPLE:
                return 50;
            case LEGENDARY:
                return 100;
            default:
                return 1;
        }
    }
    
    /**
     * Gets the display name of the seed based on type.
     * 
     * @return The display name
     */
    public String getSeedDisplayName() {
        ChatColor color;
        String name;
        
        switch (type) {
            case GREEN:
                color = ChatColor.GREEN;
                name = "Quality Seeds";
                break;
            case BLUE:
                color = ChatColor.BLUE;
                name = "Premium Seeds";
                break;
            case PURPLE:
                color = ChatColor.LIGHT_PURPLE;
                name = "Exceptional Seeds";
                break;
            case LEGENDARY:
                color = ChatColor.GOLD;
                name = "Legendary Seeds";
                break;
            default:
                color = ChatColor.WHITE;
                name = "Seeds";
                break;
        }
        
        return color + name;
    }
    
    /**
     * Gets the required rank to use this seed.
     * 
     * @return The required rank
     */
    public int getRequiredRank() {
        switch (type) {
            case GREEN:
                return 1; // Farmer
            case BLUE: 
                return 2; // Expert Farmer
            case PURPLE:
                return 3; // Master Farmer
            case LEGENDARY:
                return 4; // Agricultural Expert
            default:
                return 0; // Newcomer
        }
    }
    
    /**
     * Gets the name of the required rank.
     * 
     * @return The required rank name
     */
    public String getRequiredRankName() {
        switch (getRequiredRank()) {
            case 1:
                return "Farmer";
            case 2:
                return "Expert Farmer";
            case 3:
                return "Master Farmer";
            case 4:
                return "Agricultural Expert";
            default:
                return "Newcomer";
        }
    }
    
    /**
     * Enum for seed types.
     */
    public enum SeedType {
        /**
         * Regular seed type.
         */
        REGULAR,
        
        /**
         * Green quality seed type.
         */
        GREEN,
        
        /**
         * Blue premium seed type.
         */
        BLUE,
        
        /**
         * Purple exceptional seed type.
         */
        PURPLE,
        
        /**
         * Legendary gold seed type.
         */
        LEGENDARY
    }
}