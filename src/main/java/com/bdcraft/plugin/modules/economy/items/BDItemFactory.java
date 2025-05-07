package com.bdcraft.plugin.modules.economy.items;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Factory for creating specialized BD items with consistent formatting.
 * This ensures all items follow the same styling and metadata patterns.
 */
public class BDItemFactory {
    private final BDCraft plugin;
    
    /**
     * Creates a new BD item factory.
     * @param plugin The plugin instance
     */
    public BDItemFactory(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Creates a custom item with the specified properties.
     * @param material The item material
     * @param name The display name
     * @param amount The amount
     * @param lore The item lore
     * @param glow Whether the item should have enchantment glow
     * @return The created item
     */
    private ItemStack createCustomItem(Material material, String name, int amount, List<String> lore, boolean glow) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set name
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            
            // Set lore
            List<String> coloredLore = new ArrayList<>();
            for (String line : lore) {
                coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(coloredLore);
            
            // Add glow effect if needed
            if (glow) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Creates BD crops.
     * @param amount The amount
     * @return The BD crops
     */
    public ItemStack createBDCrops(int amount) {
        return createCustomItem(
                Material.FERN,
                "&6BD Crop",
                amount,
                Arrays.asList(
                    "&7Standard BD crop harvested from BD Seeds.",
                    "&7Sell to BD Collectors for emeralds.",
                    "&710 crops = 2 emeralds + 50 server currency"
                ),
                true
        );
    }
    
    /**
     * Creates green BD crops.
     * @param amount The amount
     * @return The green BD crops
     */
    public ItemStack createGreenBDCrops(int amount) {
        return createCustomItem(
                Material.LARGE_FERN,
                "&aGreen BD Crop",
                amount,
                Arrays.asList(
                    "&7Premium BD crop, worth 5x more than regular.",
                    "&7Sell to BD Collectors for emeralds.",
                    "&75 crops = 10 emeralds + 150 server currency"
                ),
                true
        );
    }
    
    /**
     * Creates purple BD crops.
     * @param amount The amount
     * @return The purple BD crops
     */
    public ItemStack createPurpleBDCrops(int amount) {
        return createCustomItem(
                Material.LARGE_FERN,
                "&5Purple BD Crop",
                amount,
                Arrays.asList(
                    "&7Rare high-value BD crop, worth 10x more than regular.",
                    "&7Sell to BD Collectors for emeralds.",
                    "&73 crops = 20 emeralds + 400 server currency"
                ),
                true
        );
    }
    
    /**
     * Creates BD seeds.
     * @param amount The amount
     * @return The BD seeds
     */
    public ItemStack createBDSeeds(int amount) {
        return createCustomItem(
                Material.WHEAT_SEEDS,
                "&6BD Seed",
                amount,
                Arrays.asList(
                    "&7Special seeds for growing BD crops.",
                    "&7Plant like normal wheat seeds.",
                    "&7Produces BD Crops when harvested."
                ),
                true
        );
    }
    
    /**
     * Creates green BD seeds.
     * @param amount The amount
     * @return The green BD seeds
     */
    public ItemStack createGreenBDSeeds(int amount) {
        return createCustomItem(
                Material.BEETROOT_SEEDS,
                "&aGreen BD Seed",
                amount,
                Arrays.asList(
                    "&7Premium seeds that grow 30% faster.",
                    "&7Plant like normal seeds.",
                    "&7Produces Green BD Crops when harvested.",
                    "&7Requires Farmer rank or higher."
                ),
                true
        );
    }
    
    /**
     * Creates purple BD seeds.
     * @param amount The amount
     * @return The purple BD seeds
     */
    public ItemStack createPurpleBDSeeds(int amount) {
        return createCustomItem(
                Material.PUMPKIN_SEEDS,
                "&5Purple BD Seed",
                amount,
                Arrays.asList(
                    "&7Rare seeds that produce valuable crops.",
                    "&7Plant like normal seeds.",
                    "&7Produces Purple BD Crops when harvested.",
                    "&7Requires Master Farmer rank or higher."
                ),
                true
        );
    }
    
    /**
     * Creates a BD Stick (special blaze rod).
     * @return The BD stick
     */
    public ItemStack createBDStick() {
        return createCustomItem(
                Material.BLAZE_ROD,
                "&6BD Stick",
                1,
                Arrays.asList(
                    "&7A special enchanted blaze rod that can be used",
                    "&7to craft BD Market Tokens and",
                    "&7BD House Tokens.",
                    "&7Has 5 uses before breaking."
                ),
                true
        );
    }
    
    /**
     * Creates a Market Token.
     * @return The market token
     */
    public ItemStack createMarketToken() {
        return createCustomItem(
                Material.EMERALD,
                "&2BD Market Token",
                1,
                Arrays.asList(
                    "&7Place this token in the center of a 3x3",
                    "&7platform to create a BD Market.",
                    "&7Creates a 49x49 block market area.",
                    "&7Spawns a Market Owner and BD Dealer."
                ),
                true
        );
    }
    
    /**
     * Creates a House Token.
     * @return The house token
     */
    public ItemStack createHouseToken() {
        return createCustomItem(
                Material.DIAMOND,
                "&bBD House Token",
                1,
                Arrays.asList(
                    "&7Place this token in your market to",
                    "&7create a Collector House.",
                    "&7Spawns a BD Collector who will buy crops.",
                    "&7Multiple collectors can exist in one market."
                ),
                true
        );
    }
    
    /**
     * Creates a BD Harvester.
     * @return The BD harvester
     */
    public ItemStack createBDHarvester() {
        return createCustomItem(
                Material.GOLDEN_HOE,
                "&eBD Harvester",
                1,
                Arrays.asList(
                    "&7Special tool for harvesting BD crops.",
                    "&7Increases yield by 25%.",
                    "&7Has 20 uses before breaking.",
                    "&7Requires Expert Farmer rank or higher."
                ),
                true
        );
    }
    
    /**
     * Creates an Ultimate BD Harvester.
     * @return The ultimate BD harvester
     */
    public ItemStack createUltimateBDHarvester() {
        return createCustomItem(
                Material.DIAMOND_HOE,
                "&bUltimate BD Harvester",
                1,
                Arrays.asList(
                    "&7Premium tool for harvesting BD crops.",
                    "&7Increases yield by 50%.",
                    "&7Has 60 uses before breaking.",
                    "&7Requires Agricultural Expert rank or higher."
                ),
                true
        );
    }
}