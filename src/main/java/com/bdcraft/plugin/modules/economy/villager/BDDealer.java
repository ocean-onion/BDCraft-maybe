package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.SeedType;
import com.bdcraft.plugin.modules.economy.items.tools.ToolType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * BD Dealer villager type.
 */
public class BDDealer extends BDVillager {
    public static final String TYPE = "DEALER";
    
    /**
     * Creates a new BD dealer.
     * @param plugin The plugin instance
     * @param location The spawn location
     */
    public BDDealer(BDCraft plugin, Location location) {
        super(plugin, location, "BD Dealer", Villager.Profession.FARMER, Villager.Type.PLAINS);
    }
    
    /**
     * Creates a new BD dealer from an existing villager.
     * @param plugin The plugin instance
     * @param villager The villager
     */
    public BDDealer(BDCraft plugin, Villager villager) {
        super(plugin, villager);
    }
    
    @Override
    protected void initializeTrades() {
        updateTrades();
    }
    
    /**
     * Updates the villager's trades.
     */
    @Override
    public void updateTrades() {
        List<MerchantRecipe> recipes = new ArrayList<>();
        
        // Add regular BD seeds trade (5 seeds for 1 emerald)
        MerchantRecipe regularSeedRecipe = new MerchantRecipe(
                createBDSeedItem("Regular BD Seeds", 5), 
                Integer.MAX_VALUE); // Unlimited uses
        
        // Set ingredients (1 emerald)
        regularSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        
        // Add green BD seeds trade (2 seeds for 5 emeralds)
        MerchantRecipe greenSeedRecipe = new MerchantRecipe(
                createBDSeedItem("Green BD Seeds", 2), 
                Integer.MAX_VALUE);
        
        // Set ingredients (5 emeralds)
        greenSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 5));
        
        // Add purple BD seeds trade (1 seed for 25 emeralds)
        MerchantRecipe purpleSeedRecipe = new MerchantRecipe(
                createBDSeedItem("Purple BD Seeds", 1), 
                Integer.MAX_VALUE);
        
        // Set ingredients (25 emeralds)
        purpleSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, 25));
        
        // Add BD harvester trade (1 harvester for 16 emeralds + 1 diamond)
        MerchantRecipe harvesterRecipe = new MerchantRecipe(
                createBDHarvesterItem("BD Harvester", 1), 
                10); // Limited uses
        
        // Set ingredients (16 emeralds + 1 diamond)
        harvesterRecipe.addIngredient(new ItemStack(Material.EMERALD, 16));
        harvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 1));
        
        // Add Ultimate BD harvester trade (1 harvester for 32 emeralds + 5 diamonds)
        MerchantRecipe ultimateHarvesterRecipe = new MerchantRecipe(
                createBDHarvesterItem("Ultimate BD Harvester", 1), 
                5); // Very limited uses
        
        // Set ingredients (32 emeralds + 5 diamonds)
        ultimateHarvesterRecipe.addIngredient(new ItemStack(Material.EMERALD, 32));
        ultimateHarvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 5));
        
        // Add recipes to list
        recipes.add(regularSeedRecipe);
        recipes.add(greenSeedRecipe);
        recipes.add(purpleSeedRecipe);
        recipes.add(harvesterRecipe);
        recipes.add(ultimateHarvesterRecipe);
        
        // Set recipes
        villager.setRecipes(recipes);
    }
    
    /**
     * Creates a BD seed item for trading.
     * @param name The item name
     * @param amount The amount
     * @return The item
     */
    private ItemStack createBDSeedItem(String name, int amount) {
        // Create a properly recognized BD seed item with metadata
        SeedType seedType;
        
        if (name.contains("Regular")) {
            seedType = SeedType.REGULAR;
        } else if (name.contains("Green")) {
            seedType = SeedType.GREEN;
        } else if (name.contains("Blue")) {
            seedType = SeedType.BLUE;
        } else if (name.contains("Purple")) {
            seedType = SeedType.PURPLE;
        } else {
            seedType = SeedType.LEGENDARY;
        }
        
        // Create a proper seed with metadata
        ItemStack seeds = new ItemStack(Material.WHEAT_SEEDS, amount);
        var meta = seeds.getItemMeta();
        
        ChatColor color = seedType == SeedType.REGULAR ? ChatColor.WHITE :
                seedType == SeedType.GREEN ? ChatColor.GREEN :
                seedType == SeedType.BLUE ? ChatColor.BLUE :
                seedType == SeedType.PURPLE ? ChatColor.LIGHT_PURPLE :
                ChatColor.GOLD;
                
        String displayName = seedType == SeedType.REGULAR ? "Seeds" :
                seedType == SeedType.GREEN ? "Quality Seeds" :
                seedType == SeedType.BLUE ? "Premium Seeds" :
                seedType == SeedType.PURPLE ? "Exceptional Seeds" :
                "Legendary Seeds";
                
        meta.setDisplayName(color + displayName);
        
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Special " + color + seedType.name().toLowerCase() + 
                ChatColor.GRAY + " quality seeds that");
        lore.add(ChatColor.GRAY + "grow into higher value crops.");
        lore.add("");
        lore.add(ChatColor.GRAY + "Plant these to grow special crops");
        lore.add(ChatColor.GRAY + "worth " + ChatColor.GOLD + getSeedValue(seedType) + " BD each" + 
                ChatColor.GRAY + " when harvested.");
                
        meta.setLore(lore);
        
        // Add enchant glow for better seeds
        if (seedType != SeedType.REGULAR) {
            Enchantment unbreaking = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft("unbreaking"));
            if (unbreaking != null) {
                meta.addEnchant(unbreaking, 1, true);
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        
        seeds.setItemMeta(meta);
        return seeds;
    }
    
    /**
     * Gets the value of a seed type.
     * 
     * @param type The seed type
     * @return The value
     */
    private int getSeedValue(SeedType type) {
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
     * Creates a BD harvester item for trading.
     * @param name The item name
     * @param amount The amount
     * @return The item
     */
    private ItemStack createBDHarvesterItem(String name, int amount) {
        // Create a proper tool with metadata
        ToolType toolType;
        
        if (name.contains("Ultimate")) {
            toolType = ToolType.ULTIMATE_HARVESTER;
        } else {
            toolType = ToolType.HARVESTER;
        }
        
        // Create proper tool
        Material material = toolType == ToolType.ULTIMATE_HARVESTER ? 
                Material.DIAMOND_HOE : Material.IRON_HOE;
        
        ItemStack item = new ItemStack(material, amount);
        var meta = item.getItemMeta();
        
        ChatColor color = toolType == ToolType.ULTIMATE_HARVESTER ? 
                ChatColor.LIGHT_PURPLE : ChatColor.AQUA;
        
        meta.setDisplayName(color + name);
        
        // Add lore
        List<String> lore = new ArrayList<>();
        if (toolType == ToolType.ULTIMATE_HARVESTER) {
            lore.add(ChatColor.GRAY + "+100% bonus yield when harvesting BD crops");
            lore.add(ChatColor.GRAY + "Creates purple particles when harvesting");
            lore.add(ChatColor.GRAY + "Durability: 60 uses");
            lore.add(ChatColor.RED + "Requires rank: Agricultural Expert");
        } else {
            lore.add(ChatColor.GRAY + "+50% bonus yield when harvesting BD crops");
            lore.add(ChatColor.GRAY + "Creates blue particles when harvesting");
            lore.add(ChatColor.GRAY + "Durability: 20 uses");
            lore.add(ChatColor.RED + "Requires rank: Expert Farmer");
        }
        
        meta.setLore(lore);
        
        // Add enchantment glow
        Enchantment unbreaking = Enchantment.getByKey(org.bukkit.NamespacedKey.minecraft("unbreaking"));
        if (unbreaking != null) {
            meta.addEnchant(unbreaking, 1, true);
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        item.setItemMeta(meta);
        return item;
    }
    
    @Override
    public String getVillagerType() {
        return TYPE;
    }
}