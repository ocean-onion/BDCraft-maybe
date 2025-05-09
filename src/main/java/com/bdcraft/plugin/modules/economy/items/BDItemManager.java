package com.bdcraft.plugin.modules.economy.items;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop;
import com.bdcraft.plugin.modules.economy.items.seeds.BDSeed;
import com.bdcraft.plugin.modules.economy.items.tokens.TokenType;
import com.bdcraft.plugin.modules.economy.items.tools.ToolType;
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
 * Manages special BD items like tokens, crops, etc.
 */
public class BDItemManager {
    private final BDCraft plugin;
    
    /**
     * Creates a new BD item manager.
     * 
     * @param plugin The plugin instance
     */
    public BDItemManager(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Creates a market token with the specified name and owner.
     * 
     * @param name The market name
     * @param ownerName The owner name
     * @param level The market level (1-3)
     * @return The market token
     */
    public ItemStack createMarketToken(String name, String ownerName, int level) {
        ItemStack token = new ItemStack(Material.EMERALD);
        ItemMeta meta = token.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Market Token: " + ChatColor.GOLD + name);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Owner: " + ChatColor.WHITE + ownerName);
            lore.add(ChatColor.GRAY + "Level: " + ChatColor.YELLOW + level);
            lore.add("");
            lore.add(ChatColor.YELLOW + "Place this token in the center of a");
            lore.add(ChatColor.YELLOW + "3x3 obsidian platform to create a market.");
            lore.add("");
            lore.add(ChatColor.GRAY + "ID: " + UUID.randomUUID().toString().substring(0, 8));
            
            meta.setLore(lore);
            
            // Add enchant glow
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            
            token.setItemMeta(meta);
        }
        
        return token;
    }
    
    /**
     * Creates a special BD crop item.
     * 
     * @param type The crop type
     * @param quantity The quantity
     * @return The crop item
     */
    public ItemStack createBDCrop(CropType type, int quantity) {
        Material material;
        String displayName;
        ChatColor color;
        
        switch (type) {
            case REGULAR:
                material = Material.WHEAT;
                displayName = "Standard Crop";
                color = ChatColor.YELLOW;
                break;
            case GREEN:
                material = Material.APPLE;
                displayName = "Quality Crop";
                color = ChatColor.GREEN;
                break;
            case BLUE:
                material = Material.GOLDEN_CARROT;
                displayName = "Premium Crop";
                color = ChatColor.BLUE;
                break;
            case PURPLE:
                material = Material.GOLDEN_APPLE;
                displayName = "Exceptional Crop";
                color = ChatColor.LIGHT_PURPLE;
                break;
            case LEGENDARY:
                material = Material.ENCHANTED_GOLDEN_APPLE;
                displayName = "Legendary Crop";
                color = ChatColor.GOLD;
                break;
            default:
                material = Material.WHEAT;
                displayName = "Crop";
                color = ChatColor.WHITE;
                break;
        }
        
        ItemStack crop = new ItemStack(material, quantity);
        ItemMeta meta = crop.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "A special " + color + type.name().toLowerCase() + 
                    ChatColor.GRAY + " crop grown with");
            lore.add(ChatColor.GRAY + "careful tending and agricultural expertise.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Value: " + ChatColor.GOLD + getCropValue(type) + " BD each");
            lore.add(ChatColor.GRAY + "Can be sold to collectors or at markets.");
            
            meta.setLore(lore);
            
            // Add enchant glow for better crops
            if (type != CropType.REGULAR) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            
            crop.setItemMeta(meta);
        }
        
        return crop;
    }
    
    /**
     * Gets the value of a crop type.
     * 
     * @param type The crop type
     * @return The value
     */
    public int getCropValue(CropType type) {
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
     * Creates a special seed item.
     * 
     * @param type The seed type (e.g., GREEN, BLUE)
     * @param quantity The quantity
     * @return The seed item
     */
    public ItemStack createSpecialSeed(CropType type, int quantity) {
        Material material = Material.WHEAT_SEEDS;
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
        
        ItemStack seeds = new ItemStack(material, quantity);
        ItemMeta meta = seeds.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Special " + color + type.name().toLowerCase() + 
                    ChatColor.GRAY + " quality seeds that");
            lore.add(ChatColor.GRAY + "grow into higher value crops.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Plant these to grow special crops");
            lore.add(ChatColor.GRAY + "worth " + ChatColor.GOLD + getCropValue(type) + " BD each" + 
                    ChatColor.GRAY + " when harvested.");
            
            meta.setLore(lore);
            
            // Add enchant glow
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            
            seeds.setItemMeta(meta);
        }
        
        return seeds;
    }
    
    /**
     * Creates a villager token.
     * 
     * @param type The villager type (e.g., "collector", "vendor")
     * @param level The villager level (1-3)
     * @return The villager token
     */
    public ItemStack createVillagerToken(String type, int level) {
        Material material = Material.EMERALD;
        String displayName;
        ChatColor color = ChatColor.GREEN;
        
        if ("collector".equalsIgnoreCase(type)) {
            displayName = "Collector Villager Token";
        } else if ("vendor".equalsIgnoreCase(type)) {
            displayName = "Vendor Villager Token";
            material = Material.DIAMOND;
            color = ChatColor.AQUA;
        } else {
            displayName = "Villager Token";
        }
        
        ItemStack token = new ItemStack(material);
        ItemMeta meta = token.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Level: " + ChatColor.YELLOW + level);
            lore.add("");
            lore.add(ChatColor.YELLOW + "Place this token to spawn a " + type);
            lore.add(ChatColor.YELLOW + "villager at your market.");
            lore.add("");
            lore.add(ChatColor.GRAY + "ID: " + UUID.randomUUID().toString().substring(0, 8));
            
            meta.setLore(lore);
            
            // Add enchant glow
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            
            token.setItemMeta(meta);
        }
        
        return token;
    }
    
    /**
     * Creates a crop item using the BDCrop.CropType.
     * This is a wrapper to maintain compatibility.
     * 
     * @param type The crop type
     * @param quantity The quantity
     * @return The crop item
     */
    public ItemStack createBDCrop(BDCrop.CropType type, int quantity) {
        // Convert BDCrop.CropType to CropType
        CropType cropType;
        switch (type) {
            case REGULAR:
                cropType = CropType.REGULAR;
                break;
            case GREEN:
                cropType = CropType.GREEN;
                break;
            case BLUE:
                cropType = CropType.BLUE;
                break;
            case PURPLE:
                cropType = CropType.PURPLE;
                break;
            case LEGENDARY:
                cropType = CropType.LEGENDARY;
                break;
            default:
                cropType = CropType.REGULAR;
                break;
        }
        
        // Use existing method
        return createBDCrop(cropType, quantity);
    }
    
    /**
     * Creates a BD seed with the specified seed type.
     * 
     * @param type The seed type
     * @param quantity The quantity
     * @return The seed item
     */
    public ItemStack createBDSeed(SeedType type, int quantity) {
        Material material = Material.WHEAT_SEEDS;
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
        
        ItemStack seeds = new ItemStack(material, quantity);
        ItemMeta meta = seeds.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Special " + color + type.name().toLowerCase() + 
                    ChatColor.GRAY + " quality seeds that");
            lore.add(ChatColor.GRAY + "grow into higher value crops.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Plant these to grow special crops");
            lore.add(ChatColor.GRAY + "worth " + ChatColor.GOLD + getSeedValue(type) + " BD each" + 
                    ChatColor.GRAY + " when harvested.");
            
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
     * Creates a BD seed with the specified BDSeed.SeedType.
     * 
     * @param type The seed type
     * @param quantity The quantity
     * @return The seed item
     */
    public ItemStack createBDSeed(BDSeed.SeedType type, int quantity) {
        Material material = Material.WHEAT_SEEDS;
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
        
        ItemStack seeds = new ItemStack(material, quantity);
        ItemMeta meta = seeds.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Special " + color + type.name().toLowerCase() + 
                    ChatColor.GRAY + " quality seeds that");
            lore.add(ChatColor.GRAY + "grow into higher value crops.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Plant these to grow special crops");
            lore.add(ChatColor.GRAY + "worth " + ChatColor.GOLD + getSeedValue(type) + " BD each" + 
                    ChatColor.GRAY + " when harvested.");
            
            meta.setLore(lore);
            
            // Add enchant glow for better seeds
            if (type != BDSeed.SeedType.REGULAR) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            
            seeds.setItemMeta(meta);
        }
        
        return seeds;
    }
    
    /**
     * Gets the value of a seed type.
     * 
     * @param type The seed type
     * @return The value
     */
    public int getSeedValue(SeedType type) {
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
     * Gives an item to a player based on an item ID.
     * 
     * @param player The player to give the item to
     * @param itemId The item identifier (seed, token, crop, etc)
     * @param amount The amount of the item to give
     * @param value Optional specific value (for some custom items)
     * @return True if the item was given successfully
     */
    public boolean giveItem(org.bukkit.entity.Player player, String itemId, int amount, int value) {
        if (player == null || itemId == null) {
            return false;
        }
        
        ItemStack item = null;
        
        // Handle tokens
        if (itemId.equalsIgnoreCase("markettoken")) {
            item = createMarketToken("Market #" + UUID.randomUUID().toString().substring(0, 4), 
                    player.getName(), 1);
        } else if (itemId.equalsIgnoreCase("collectortoken")) {
            item = createVillagerToken("collector", value > 0 ? value : 1);
        } else if (itemId.equalsIgnoreCase("vendortoken")) {
            item = createVillagerToken("vendor", value > 0 ? value : 1);
        } 
        // Handle seeds
        else if (itemId.equalsIgnoreCase("seed") || itemId.equalsIgnoreCase("seeds")) {
            item = createBDSeed(SeedType.REGULAR, amount);
        } else if (itemId.equalsIgnoreCase("greenseed") || itemId.equalsIgnoreCase("greenseeds")) {
            item = createBDSeed(SeedType.GREEN, amount);
        } else if (itemId.equalsIgnoreCase("blueseed") || itemId.equalsIgnoreCase("blueseeds")) {
            item = createBDSeed(SeedType.BLUE, amount);
        } else if (itemId.equalsIgnoreCase("purpleseed") || itemId.equalsIgnoreCase("purpleseeds")) {
            item = createBDSeed(SeedType.PURPLE, amount);
        } else if (itemId.equalsIgnoreCase("legendaryseed") || itemId.equalsIgnoreCase("legendaryseeds")) {
            item = createBDSeed(SeedType.LEGENDARY, amount);
        }
        // Handle crops
        else if (itemId.equalsIgnoreCase("crop") || itemId.equalsIgnoreCase("crops")) {
            item = createBDCrop(CropType.REGULAR, amount);
        } else if (itemId.equalsIgnoreCase("greencrop") || itemId.equalsIgnoreCase("greencrops")) {
            item = createBDCrop(CropType.GREEN, amount);
        } else if (itemId.equalsIgnoreCase("bluecrop") || itemId.equalsIgnoreCase("bluecrops")) {
            item = createBDCrop(CropType.BLUE, amount);
        } else if (itemId.equalsIgnoreCase("purplecrop") || itemId.equalsIgnoreCase("purplecrops")) {
            item = createBDCrop(CropType.PURPLE, amount);
        } else if (itemId.equalsIgnoreCase("legendarycrop") || itemId.equalsIgnoreCase("legendarycrops")) {
            item = createBDCrop(CropType.LEGENDARY, amount);
        }
        
        // If item was created, give it to the player
        if (item != null) {
            player.getInventory().addItem(item);
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets the value of a BDSeed.SeedType.
     * 
     * @param type The seed type
     * @return The value
     */
    public int getSeedValue(BDSeed.SeedType type) {
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
     * Creates a BD crop with the specified crop type.
     * 
     * @param type The crop type
     * @param quantity The quantity
     * @return The crop item
     */
    public ItemStack createBDCrop(BDCrop.CropType type, int quantity) {
        Material material = Material.WHEAT;
        String displayName;
        ChatColor color;
        
        switch (type) {
            case GREEN:
                displayName = "Quality Crop";
                color = ChatColor.GREEN;
                break;
            case BLUE:
                displayName = "Premium Crop";
                color = ChatColor.BLUE;
                break;
            case PURPLE:
                displayName = "Exceptional Crop";
                color = ChatColor.LIGHT_PURPLE;
                break;
            case LEGENDARY:
                displayName = "Legendary Crop";
                color = ChatColor.GOLD;
                break;
            default:
                displayName = "BD Crop";
                color = ChatColor.WHITE;
                break;
        }
        
        ItemStack crop = new ItemStack(material, quantity);
        ItemMeta meta = crop.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "A special " + color + type.name().toLowerCase() + 
                    ChatColor.GRAY + " quality crop");
            lore.add(ChatColor.GRAY + "grown from special seeds.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Worth " + ChatColor.GOLD + getCropValue(type) + " BD each" + 
                    ChatColor.GRAY + " when sold.");
            lore.add("");
            lore.add(ChatColor.GRAY + "ID: " + UUID.randomUUID().toString().substring(0, 8));
            
            meta.setLore(lore);
            
            // Add enchant glow for better crops
            if (type != BDCrop.CropType.REGULAR) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            
            crop.setItemMeta(meta);
        }
        
        return crop;
    }
    
    /**
     * Gets the value of a crop type using BDCrop.CropType.
     * This is a wrapper for compatibility.
     * 
     * @param type The crop type
     * @return The value
     */
    public int getCropValue(BDCrop.CropType type) {
        // Convert BDCrop.CropType to CropType
        CropType cropType;
        switch (type) {
            case REGULAR:
                cropType = CropType.REGULAR;
                break;
            case GREEN:
                cropType = CropType.GREEN;
                break;
            case BLUE:
                cropType = CropType.BLUE;
                break;
            case PURPLE:
                cropType = CropType.PURPLE;
                break;
            case LEGENDARY:
                cropType = CropType.LEGENDARY;
                break;
            default:
                cropType = CropType.REGULAR;
                break;
        }
        
        // Use existing method
        return getCropValue(cropType);
    }
    
    /**
     * Checks if an item is a BD tool.
     * 
     * @param item The item to check
     * @return True if the item is a BD tool
     */
    public boolean isBDTool(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        // Check for lore containing something like "BD Tool"
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            if (lore != null) {
                for (String line : lore) {
                    if (line.contains("BD Tool") || line.contains("BD Harvester")) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Creates a BD token of the specified type.
     * 
     * @param type The token type
     * @param level The token level (1-3)
     * @return The token
     */
    public ItemStack createBDToken(TokenType type, int level) {
        Material material;
        String displayName;
        ChatColor color;
        
        switch (type) {
            case MARKET:
                material = Material.EMERALD;
                displayName = "Market Token";
                color = ChatColor.GREEN;
                break;
            case TRADE:
                material = Material.DIAMOND;
                displayName = "Trade Token";
                color = ChatColor.AQUA;
                break;
            case HOUSE:
                material = Material.GOLD_INGOT;
                displayName = "House Token";
                color = ChatColor.GOLD;
                break;
            case COLLECTOR:
                material = Material.EMERALD;
                displayName = "Collector Token";
                color = ChatColor.GREEN;
                break;
            case VENDOR:
                material = Material.DIAMOND;
                displayName = "Vendor Token";
                color = ChatColor.BLUE;
                break;
            default:
                material = Material.IRON_INGOT;
                displayName = "Basic Token";
                color = ChatColor.WHITE;
                break;
        }
        
        ItemStack token = new ItemStack(material);
        ItemMeta meta = token.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Level: " + ChatColor.YELLOW + level);
            lore.add("");
            
            switch (type) {
                case MARKET:
                    lore.add(ChatColor.YELLOW + "Place this token in the center of a");
                    lore.add(ChatColor.YELLOW + "3x3 obsidian platform to create a market.");
                    break;
                case TRADE:
                    lore.add(ChatColor.YELLOW + "Use this token to establish a trade");
                    lore.add(ChatColor.YELLOW + "route between two markets.");
                    break;
                case HOUSE:
                    lore.add(ChatColor.YELLOW + "Place this token to claim a plot");
                    lore.add(ChatColor.YELLOW + "for your house or shop.");
                    break;
                case COLLECTOR:
                case VENDOR:
                    lore.add(ChatColor.YELLOW + "Place this token to spawn a special");
                    lore.add(ChatColor.YELLOW + "villager in your market.");
                    break;
                default:
                    lore.add(ChatColor.YELLOW + "A basic token with unknown powers.");
                    break;
            }
            
            lore.add("");
            lore.add(ChatColor.GRAY + "ID: " + UUID.randomUUID().toString().substring(0, 8));
            
            meta.setLore(lore);
            
            // Add enchant glow
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            
            token.setItemMeta(meta);
        }
        
        return token;
    }
    
    /**
     * Checks if an item is a BD item.
     * 
     * @param item The item to check
     * @return True if it's a BD item
     */
    public boolean isBDItem(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return false;
        }
        
        List<String> lore = item.getItemMeta().getLore();
        if (lore == null || lore.isEmpty()) {
            return false;
        }
        
        // Check for ID in lore which is unique to BD items
        for (String line : lore) {
            if (line.startsWith(ChatColor.GRAY + "ID:")) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets the type of a BD item.
     * 
     * @param item The item to check
     * @return The item type as a string, or null if not a BD item
     */
    public String getBDItemType(ItemStack item) {
        if (!isBDItem(item)) {
            return null;
        }
        
        String displayName = item.getItemMeta().getDisplayName();
        Material material = item.getType();
        
        // Check for tokens
        if (material == Material.EMERALD && displayName.contains("Market Token")) {
            return "market_token";
        } else if (material == Material.DIAMOND && displayName.contains("Trade Token")) {
            return "trade_token";
        } else if (material == Material.GOLD_INGOT && displayName.contains("House Token")) {
            return "house_token";
        } else if (material == Material.EMERALD && displayName.contains("Collector Token")) {
            return "collector_token";
        } else if (material == Material.DIAMOND && displayName.contains("Vendor Token")) {
            return "vendor_token";
        }
        
        // Check for crops/seeds
        if (material == Material.WHEAT_SEEDS) {
            if (displayName.contains("Quality Seeds")) {
                return "green_bd_seed";
            } else if (displayName.contains("Premium Seeds")) {
                return "blue_bd_seed";
            } else if (displayName.contains("Exceptional Seeds")) {
                return "purple_bd_seed";
            } else if (displayName.contains("Legendary Seeds")) {
                return "legendary_bd_seed";
            } else {
                return "bd_seed";
            }
        } else if (material == Material.WHEAT) {
            if (displayName.contains("Quality Crop")) {
                return "green_bd_crop";
            } else if (displayName.contains("Premium Crop")) {
                return "blue_bd_crop";
            } else if (displayName.contains("Exceptional Crop")) {
                return "purple_bd_crop";
            } else if (displayName.contains("Legendary Crop")) {
                return "legendary_bd_crop";
            } else {
                return "bd_crop";
            }
        }
        
        // Check for tools
        if (displayName.contains("BD Stick")) {
            return "bd_stick";
        } else if (displayName.contains("BD Shovel")) {
            return "bd_shovel";
        } else if (displayName.contains("BD Hoe")) {
            return "bd_hoe";
        } else if (displayName.contains("BD Pickaxe")) {
            return "bd_pickaxe";
        } else if (displayName.contains("BD Axe")) {
            return "bd_axe";
        } else if (displayName.contains("Ultimate BD Harvester")) {
            return "ultimate_harvester";
        } else if (displayName.contains("BD Harvester")) {
            return "harvester";
        }
        
        return "unknown_bd_item";
    }
    
    /**
     * Creates a BD item by type string and quantity.
     * 
     * @param itemType The item type string
     * @param quantity The quantity
     * @return The created item
     */
    public ItemStack createItem(String itemType, int quantity) {
        // Handle tokens
        if (itemType.equals("market_token")) {
            return createBDToken(TokenType.MARKET, 1);
        } else if (itemType.equals("trade_token")) {
            return createBDToken(TokenType.TRADE, 1);
        } else if (itemType.equals("house_token")) {
            return createBDToken(TokenType.HOUSE, 1);
        } else if (itemType.equals("collector_token")) {
            return createBDToken(TokenType.COLLECTOR, 1);
        } else if (itemType.equals("vendor_token")) {
            return createBDToken(TokenType.VENDOR, 1);
        }
        
        // Handle seeds
        if (itemType.equals("bd_seed")) {
            return createBDSeed(SeedType.REGULAR, quantity);
        } else if (itemType.equals("green_bd_seed")) {
            return createBDSeed(SeedType.GREEN, quantity);
        } else if (itemType.equals("blue_bd_seed")) {
            return createBDSeed(SeedType.BLUE, quantity);
        } else if (itemType.equals("purple_bd_seed")) {
            return createBDSeed(SeedType.PURPLE, quantity);
        } else if (itemType.equals("legendary_bd_seed")) {
            return createBDSeed(SeedType.LEGENDARY, quantity);
        }
        
        // Handle crops
        if (itemType.equals("bd_crop")) {
            return createBDCrop(BDCrop.CropType.REGULAR, quantity);
        } else if (itemType.equals("green_bd_crop")) {
            return createBDCrop(BDCrop.CropType.GREEN, quantity);
        } else if (itemType.equals("blue_bd_crop")) {
            return createBDCrop(BDCrop.CropType.BLUE, quantity);
        } else if (itemType.equals("purple_bd_crop")) {
            return createBDCrop(BDCrop.CropType.PURPLE, quantity);
        } else if (itemType.equals("legendary_bd_crop")) {
            return createBDCrop(BDCrop.CropType.LEGENDARY, quantity);
        }
        
        // Handle tools
        if (itemType.equals("bd_stick")) {
            return createBDTool(ToolType.BDSTICK);
        } else if (itemType.equals("bd_shovel")) {
            return createBDTool(ToolType.BDSHOVEL);
        } else if (itemType.equals("bd_hoe")) {
            return createBDTool(ToolType.BDHOE);
        } else if (itemType.equals("bd_pickaxe")) {
            return createBDTool(ToolType.BDPICKAXE);
        } else if (itemType.equals("bd_axe")) {
            return createBDTool(ToolType.BDAXE);
        } else if (itemType.equals("harvester")) {
            return createBDTool(ToolType.HARVESTER);
        } else if (itemType.equals("ultimate_harvester")) {
            return createBDTool(ToolType.ULTIMATE_HARVESTER);
        }
        
        // Default fallback
        ItemStack defaultItem = new ItemStack(Material.PAPER, quantity);
        ItemMeta meta = defaultItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Unknown BD Item");
            defaultItem.setItemMeta(meta);
        }
        return defaultItem;
    }
    
    /**
     * Creates a special BD tool.
     * 
     * @param type The tool type
     * @return The tool
     */
    public ItemStack createBDTool(com.bdcraft.plugin.modules.economy.items.tools.ToolType type) {
        Material material;
        String displayName;
        ChatColor color = ChatColor.AQUA;
        
        switch (type) {
            case BDSTICK:
                material = Material.STICK;
                displayName = "BD Stick";
                break;
            case BDSHOVEL:
                material = Material.DIAMOND_SHOVEL;
                displayName = "BD Shovel";
                break;
            case BDHOE:
                material = Material.DIAMOND_HOE;
                displayName = "BD Hoe";
                break;
            case BDPICKAXE:
                material = Material.DIAMOND_PICKAXE;
                displayName = "BD Pickaxe";
                break;
            case BDAXE:
                material = Material.DIAMOND_AXE;
                displayName = "BD Axe";
                break;
            case HARVESTER:
                material = Material.GOLDEN_HOE;
                displayName = "BD Harvester";
                break;
            case ULTIMATE_HARVESTER:
                material = Material.NETHERITE_HOE;
                displayName = "Ultimate BD Harvester";
                break;
            default:
                material = Material.STICK;
                displayName = "BD Tool";
                break;
        }
        
        ItemStack tool = new ItemStack(material);
        ItemMeta meta = tool.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(color + displayName);
            
            List<String> lore = new ArrayList<>();
            
            switch (type) {
                case BDSTICK:
                    lore.add(ChatColor.GRAY + "A magical stick that can be used");
                    lore.add(ChatColor.GRAY + "for crafting special BD items.");
                    break;
                case BDSHOVEL:
                    lore.add(ChatColor.GRAY + "A special shovel that harvests");
                    lore.add(ChatColor.GRAY + "crops with higher efficiency.");
                    break;
                case BDHOE:
                    lore.add(ChatColor.GRAY + "A powerful hoe that tills multiple");
                    lore.add(ChatColor.GRAY + "blocks at once and increases crop yield.");
                    break;
                case BDPICKAXE:
                    lore.add(ChatColor.GRAY + "A magical pickaxe that finds");
                    lore.add(ChatColor.GRAY + "special resources occasionally.");
                    break;
                case BDAXE:
                    lore.add(ChatColor.GRAY + "An enchanted axe that harvests");
                    lore.add(ChatColor.GRAY + "wood more efficiently.");
                    break;
                case HARVESTER:
                    lore.add(ChatColor.GRAY + "A legendary harvester that collects");
                    lore.add(ChatColor.GRAY + "crops with magical efficiency.");
                    lore.add(ChatColor.GRAY + "Grants bonus crops when harvesting.");
                    break;
                case ULTIMATE_HARVESTER:
                    lore.add(ChatColor.GOLD + "The ultimate harvesting tool,");
                    lore.add(ChatColor.GOLD + "blessed by agricultural deities.");
                    lore.add(ChatColor.GOLD + "Significantly increases crop yields");
                    lore.add(ChatColor.GOLD + "and has a chance to produce rare crops.");
                    break;
                default:
                    lore.add(ChatColor.GRAY + "A basic BD tool.");
                    break;
            }
            
            lore.add("");
            lore.add(ChatColor.GRAY + "ID: " + UUID.randomUUID().toString().substring(0, 8));
            
            meta.setLore(lore);
            
            // Add enchant glow
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            
            tool.setItemMeta(meta);
        }
        
        return tool;
    }
}