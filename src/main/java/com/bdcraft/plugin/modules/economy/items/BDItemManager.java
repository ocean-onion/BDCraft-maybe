package com.bdcraft.plugin.modules.economy.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop.CropType;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed.SeedType;
import com.bdcraft.plugin.modules.economy.items.tokens.BDToken;
import com.bdcraft.plugin.modules.economy.items.tokens.TokenType;
import com.bdcraft.plugin.modules.economy.items.tools.BDTool;
import com.bdcraft.plugin.modules.economy.items.tools.ToolType;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages BD items.
 */
public class BDItemManager {
    private final BDCraft plugin;
    private final Map<String, BDItem> itemRegistry;
    
    /**
     * Creates a new BD item manager.
     * 
     * @param plugin The plugin instance
     */
    public BDItemManager(BDCraft plugin) {
        this.plugin = plugin;
        this.itemRegistry = new HashMap<>();
        
        // Register all BD items
        registerItems();
    }
    
    /**
     * Registers all BD items.
     */
    private void registerItems() {
        // Register seeds
        for (SeedType seedType : SeedType.values()) {
            BDSeed seed = new BDSeed(plugin, seedType);
            String key = getKeyForSeedType(seedType);
            itemRegistry.put(key, seed);
        }
        
        // Register crops
        for (CropType cropType : CropType.values()) {
            BDCrop crop = new BDCrop(plugin, cropType);
            String key = getKeyForCropType(cropType);
            itemRegistry.put(key, crop);
        }
        
        // TODO: Register tokens and tools when they're implemented
    }
    
    /**
     * Creates an item by type.
     * 
     * @param itemType The item type
     * @param amount The amount
     * @return The item stack, or null if the item type is invalid
     */
    public ItemStack createItem(String itemType, int amount) {
        BDItem bdItem = itemRegistry.get(itemType);
        
        if (bdItem != null) {
            return bdItem.createItemStack(amount);
        }
        
        // Handle seed types
        if (itemType.equals("bd_seed")) {
            return createBDSeed(SeedType.REGULAR, amount);
        } else if (itemType.equals("green_bd_seed")) {
            return createBDSeed(SeedType.GREEN, amount);
        } else if (itemType.equals("purple_bd_seed")) {
            return createBDSeed(SeedType.PURPLE, amount);
        }
        
        // Handle crop types
        if (itemType.equals("bd_crop")) {
            return createBDCrop(CropType.REGULAR, amount);
        } else if (itemType.equals("green_bd_crop")) {
            return createBDCrop(CropType.GREEN, amount);
        } else if (itemType.equals("purple_bd_crop")) {
            return createBDCrop(CropType.PURPLE, amount);
        }
        
        // TODO: Handle token and tool types
        
        return null;
    }
    
    /**
     * Creates a BD seed.
     * 
     * @param type The seed type
     * @param amount The amount of seeds
     * @return The BD seed item
     */
    public ItemStack createBDSeed(SeedType type, int amount) {
        return new BDSeed(plugin, type).createItemStack(amount);
    }
    
    /**
     * Creates a BD crop.
     * 
     * @param type The crop type
     * @param amount The amount of crops
     * @return The BD crop item
     */
    public ItemStack createBDCrop(CropType type, int amount) {
        return new BDCrop(plugin, type).createItemStack(amount);
    }
    
    /**
     * Creates a BD token.
     * 
     * @param type The token type
     * @param amount The amount of tokens
     * @return The BD token item
     */
    public ItemStack createBDToken(TokenType type, int amount) {
        return new BDToken(plugin, type).createItemStack(amount);
    }
    
    /**
     * Creates a BD tool.
     * 
     * @param type The tool type
     * @return The BD tool item
     */
    public ItemStack createBDTool(ToolType type) {
        return new BDTool(plugin, type).createItemStack(1);
    }
    
    /**
     * Gives an item to a player.
     * 
     * @param player The player
     * @param itemId The item ID
     * @param amount The amount
     * @param value The value (for value-based items)
     * @return True if the item was given successfully
     */
    public boolean giveItem(Player player, String itemId, int amount, int value) {
        ItemStack itemStack = createItem(itemId, amount);
        
        if (itemStack == null) {
            return false;
        }
        
        // Add the item to the player's inventory
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(itemStack);
        
        // If there were leftovers, drop them at the player's feet
        if (!leftovers.isEmpty()) {
            for (ItemStack leftover : leftovers.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), leftover);
            }
        }
        
        return true;
    }
    
    /**
     * Checks if an item is a BD item.
     * 
     * @param item The item to check
     * @return True if the item is a BD item
     */
    public boolean isBDItem(ItemStack item) {
        return isBDSeed(item) || isBDCrop(item) || isBDToken(item) || isBDTool(item);
    }
    
    /**
     * Gets the type of a BD item.
     * 
     * @param item The item to check
     * @return The BD item type, or null if the item is not a BD item
     */
    public String getBDItemType(ItemStack item) {
        if (isBDSeed(item)) {
            SeedType seedType = getBDSeedType(item);
            return getKeyForSeedType(seedType);
        } else if (isBDCrop(item)) {
            CropType cropType = getBDCropType(item);
            return getKeyForCropType(cropType);
        } else if (isBDToken(item)) {
            TokenType tokenType = getBDTokenType(item);
            return getKeyForTokenType(tokenType);
        } else if (isBDTool(item)) {
            ToolType toolType = getBDToolType(item);
            return getKeyForToolType(toolType);
        }
        
        return null;
    }
    
    /**
     * Checks if an item is a BD seed.
     * 
     * @param item The item to check
     * @return True if the item is a BD seed
     */
    public boolean isBDSeed(ItemStack item) {
        return BDSeed.isBDSeed(item);
    }
    
    /**
     * Checks if an item is a BD crop.
     * 
     * @param item The item to check
     * @return True if the item is a BD crop
     */
    public boolean isBDCrop(ItemStack item) {
        return BDCrop.isBDCrop(item);
    }
    
    /**
     * Checks if an item is a BD token.
     * 
     * @param item The item to check
     * @return True if the item is a BD token
     */
    public boolean isBDToken(ItemStack item) {
        return BDToken.isBDToken(item);
    }
    
    /**
     * Checks if an item is a BD tool.
     * 
     * @param item The item to check
     * @return True if the item is a BD tool
     */
    public boolean isBDTool(ItemStack item) {
        return BDTool.isBDTool(item);
    }
    
    /**
     * Gets the seed type of an item.
     * 
     * @param item The item to check
     * @return The seed type, or null if the item is not a BD seed
     */
    public SeedType getBDSeedType(ItemStack item) {
        return BDSeed.getSeedType(item);
    }
    
    /**
     * Gets the crop type of an item.
     * 
     * @param item The item to check
     * @return The crop type, or null if the item is not a BD crop
     */
    public CropType getBDCropType(ItemStack item) {
        return BDCrop.getCropType(item);
    }
    
    /**
     * Gets the token type of an item.
     * 
     * @param item The item to check
     * @return The token type, or null if the item is not a BD token
     */
    public TokenType getBDTokenType(ItemStack item) {
        return BDToken.getTokenType(item);
    }
    
    /**
     * Gets the tool type of an item.
     * 
     * @param item The item to check
     * @return The tool type, or null if the item is not a BD tool
     */
    public ToolType getBDToolType(ItemStack item) {
        return BDTool.getToolType(item);
    }
    
    /**
     * Gets the key for a seed type.
     * 
     * @param seedType The seed type
     * @return The key
     */
    private String getKeyForSeedType(SeedType seedType) {
        switch (seedType) {
            case REGULAR:
                return "bd_seed";
            case GREEN:
                return "green_bd_seed";
            case PURPLE:
                return "purple_bd_seed";
            default:
                return "unknown_seed";
        }
    }
    
    /**
     * Gets the key for a crop type.
     * 
     * @param cropType The crop type
     * @return The key
     */
    private String getKeyForCropType(CropType cropType) {
        switch (cropType) {
            case REGULAR:
                return "bd_crop";
            case GREEN:
                return "green_bd_crop";
            case PURPLE:
                return "purple_bd_crop";
            default:
                return "unknown_crop";
        }
    }
    
    /**
     * Gets the key for a token type.
     * 
     * @param tokenType The token type
     * @return The key
     */
    private String getKeyForTokenType(TokenType tokenType) {
        if (tokenType == null) {
            return "unknown_token";
        }
        
        switch (tokenType) {
            case MARKET:
                return "market_token";
            case HOUSE:
                return "house_token";
            case TRADE:
                return "trade_token";
            default:
                return "unknown_token";
        }
    }
    
    /**
     * Gets the key for a tool type.
     * 
     * @param toolType The tool type
     * @return The key
     */
    private String getKeyForToolType(ToolType toolType) {
        if (toolType == null) {
            return "unknown_tool";
        }
        
        switch (toolType) {
            case HARVESTER:
                return "bd_harvester";
            case ULTIMATE_HARVESTER:
                return "bd_ultimate_harvester";
            default:
                return "unknown_tool";
        }
    }
}