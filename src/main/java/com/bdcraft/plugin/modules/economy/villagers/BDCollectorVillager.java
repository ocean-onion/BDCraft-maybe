package com.bdcraft.plugin.modules.economy.villagers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop;
import com.bdcraft.plugin.modules.economy.items.crops.BDCrop.CropType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a BD Collector villager that buys crops from players.
 */
public class BDCollectorVillager extends BDVillager {
    private final UUID houseId;
    
    /**
     * Creates a new BD Collector villager.
     * 
     * @param plugin The plugin instance
     * @param uuid The UUID of the villager
     * @param houseId The UUID of the house this villager belongs to
     * @param entity The villager entity (or null if not spawned yet)
     */
    public BDCollectorVillager(BDCraft plugin, UUID uuid, UUID houseId, Villager entity) {
        super(plugin, uuid, entity, "BD Collector", VillagerType.BD_COLLECTOR);
        this.houseId = houseId;
    }
    
    @Override
    public Villager spawn(Location location) {
        Villager villager = super.spawn(location);
        setupTrades();
        return villager;
    }
    
    @Override
    protected Profession getBukkitProfession() {
        return Profession.LIBRARIAN; // According to documentation
    }
    
    @Override
    public void setupTrades() {
        if (entity == null) {
            return;
        }
        
        // Clear existing trades
        entity.setRecipes(new ArrayList<>());
        
        // Add the standard crop trades
        List<MerchantRecipe> trades = new ArrayList<>();
        
        // Regular BD Crops: 10 crops for 2 emeralds + 50 server currency
        trades.add(createRegularCropTrade());
        
        // Green BD Crops: 5 crops for 10 emeralds + 150 server currency
        trades.add(createGreenCropTrade());
        
        // Purple BD Crops: 3 crops for 20 emeralds + 400 server currency
        trades.add(createPurpleCropTrade());
        
        // Bulk Trade: 50 regular BD crops for 1 diamond
        trades.add(createBulkRegularCropTrade());
        
        // Apply trades to villager
        entity.setRecipes(trades);
    }
    
    /**
     * Creates a trade for regular BD crops.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createRegularCropTrade() {
        // Result: 2 emeralds (server currency is handled separately)
        ItemStack result = new ItemStack(Material.EMERALD, 2);
        
        MerchantRecipe recipe = new MerchantRecipe(result, 0, 16, true);
        recipe.addIngredient(plugin.getEconomyModule().getItemManager().createBDCrop(CropType.REGULAR, 10));
        
        return recipe;
    }
    
    /**
     * Creates a trade for green BD crops.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createGreenCropTrade() {
        // Result: 10 emeralds (server currency is handled separately)
        ItemStack result = new ItemStack(Material.EMERALD, 10);
        
        MerchantRecipe recipe = new MerchantRecipe(result, 0, 12, true);
        recipe.addIngredient(plugin.getEconomyModule().getItemManager().createBDCrop(CropType.GREEN, 5));
        
        return recipe;
    }
    
    /**
     * Creates a trade for purple BD crops.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createPurpleCropTrade() {
        // Result: 20 emeralds (server currency is handled separately)
        ItemStack result = new ItemStack(Material.EMERALD, 20);
        
        MerchantRecipe recipe = new MerchantRecipe(result, 0, 8, true);
        recipe.addIngredient(plugin.getEconomyModule().getItemManager().createBDCrop(CropType.PURPLE, 3));
        
        return recipe;
    }
    
    /**
     * Creates a bulk trade for regular BD crops.
     * 
     * @return The trade recipe
     */
    private MerchantRecipe createBulkRegularCropTrade() {
        // Result: 1 diamond
        ItemStack result = new ItemStack(Material.DIAMOND, 1);
        
        MerchantRecipe recipe = new MerchantRecipe(result, 0, 5, true);
        recipe.addIngredient(plugin.getEconomyModule().getItemManager().createBDCrop(CropType.REGULAR, 50));
        
        return recipe;
    }
    
    /**
     * Gets the house ID of this collector.
     * 
     * @return The house ID
     */
    public UUID getHouseId() {
        return houseId;
    }
    
    /**
     * Updates the villager's trades.
     */
    public void updateTrades() {
        setupTrades();
    }
}