package com.bdcraft.plugin.modules.economy.items.tokens;

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
 * Represents a BD token item.
 */
public class BDToken extends BDItem {
    public static final String MARKET_TOKEN_KEY = "market_token";
    public static final String HOUSE_TOKEN_KEY = "house_token";
    public static final String TRADE_TOKEN_KEY = "trade_token";
    
    private final TokenType tokenType;
    private final int requiredRank;
    
    /**
     * Creates a new BD token.
     * 
     * @param plugin The plugin instance
     * @param tokenType The type of token
     */
    public BDToken(BDCraft plugin, TokenType tokenType) {
        super(plugin);
        this.tokenType = tokenType;
        
        switch (tokenType) {
            case MARKET:
                this.requiredRank = 2; // Farmer rank
                break;
            case HOUSE:
                this.requiredRank = 1; // Newcomer rank
                break;
            case TRADE:
                this.requiredRank = 3; // Experienced Farmer rank
                break;
            default:
                this.requiredRank = 0;
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
        
        switch (tokenType) {
            case MARKET:
                material = Material.EMERALD;
                displayName = "Market Token";
                itemKey = MARKET_TOKEN_KEY;
                color = ChatColor.GREEN;
                loreLines.add(ChatColor.GRAY + "Use to create a BD Market");
                loreLines.add(ChatColor.GRAY + "Place in the center of a");
                loreLines.add(ChatColor.GRAY + "3x3 dirt square surrounded");
                loreLines.add(ChatColor.GRAY + "by fence posts");
                break;
            case HOUSE:
                material = Material.DIAMOND;
                displayName = "House Token";
                itemKey = HOUSE_TOKEN_KEY;
                color = ChatColor.AQUA;
                loreLines.add(ChatColor.GRAY + "Use to create a BD House");
                loreLines.add(ChatColor.GRAY + "Place at the entrance of");
                loreLines.add(ChatColor.GRAY + "your house to claim it");
                break;
            case TRADE:
                material = Material.GOLD_INGOT;
                displayName = "Trade Token";
                itemKey = TRADE_TOKEN_KEY;
                color = ChatColor.GOLD;
                loreLines.add(ChatColor.GRAY + "Use to access special trades");
                loreLines.add(ChatColor.GRAY + "Give to a Seasonal Trader");
                loreLines.add(ChatColor.GRAY + "to unlock rare items");
                break;
            default:
                material = Material.IRON_INGOT;
                displayName = "Unknown Token";
                itemKey = "unknown_token";
                color = ChatColor.GRAY;
                loreLines.add(ChatColor.RED + "This token has no effect");
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
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Gets the type of token.
     * 
     * @return The token type
     */
    public TokenType getTokenType() {
        return tokenType;
    }
    
    /**
     * Gets the required rank to use this token.
     * 
     * @return The required rank
     */
    public int getRequiredRank() {
        return requiredRank;
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
     * Checks if an item is a BD token.
     * 
     * @param item The item to check
     * @return True if the item is a BD token
     */
    public static boolean isBDToken(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        return container.has(BDItem.getStaticNamespacedKey(MARKET_TOKEN_KEY), PersistentDataType.STRING) ||
               container.has(BDItem.getStaticNamespacedKey(HOUSE_TOKEN_KEY), PersistentDataType.STRING) ||
               container.has(BDItem.getStaticNamespacedKey(TRADE_TOKEN_KEY), PersistentDataType.STRING);
    }
    
    /**
     * Gets the token type from an item.
     * 
     * @param item The item to check
     * @return The token type, or null if the item is not a BD token
     */
    public static TokenType getTokenType(ItemStack item) {
        if (!isBDToken(item)) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        
        if (container.has(BDItem.getStaticNamespacedKey(MARKET_TOKEN_KEY), PersistentDataType.STRING)) {
            return TokenType.MARKET;
        } else if (container.has(BDItem.getStaticNamespacedKey(HOUSE_TOKEN_KEY), PersistentDataType.STRING)) {
            return TokenType.HOUSE;
        } else if (container.has(BDItem.getStaticNamespacedKey(TRADE_TOKEN_KEY), PersistentDataType.STRING)) {
            return TokenType.TRADE;
        }
        
        return null;
    }
}