package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.CropType;
import com.bdcraft.plugin.modules.economy.market.Market;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * BD Collector villager type.
 */
public class BDCollector extends BDVillager {
    public static final String TYPE = "COLLECTOR";
    private Market market;
    
    /**
     * Creates a new BD collector.
     * @param plugin The plugin instance
     * @param location The spawn location
     */
    public BDCollector(BDCraft plugin, Location location) {
        super(plugin, location, "BD Collector", Villager.Profession.LIBRARIAN, Villager.Type.PLAINS);
    }
    
    /**
     * Creates a new BD collector from an existing villager.
     * @param plugin The plugin instance
     * @param villager The existing villager
     */
    public BDCollector(BDCraft plugin, Villager villager) {
        super(plugin, villager);
    }
    
    /**
     * Creates a new BD collector associated with a market.
     * @param plugin The plugin instance
     * @param location The spawn location
     * @param market The associated market
     */
    public BDCollector(BDCraft plugin, Location location, Market market) {
        super(plugin, location, "BD Collector", Villager.Profession.LIBRARIAN, Villager.Type.PLAINS);
        this.market = market;
    }
    
    /**
     * Creates a new BD collector from an existing villager with a market.
     * @param plugin The plugin instance
     * @param villager The villager
     * @param market The associated market
     */
    public BDCollector(BDCraft plugin, Villager villager, Market market) {
        super(plugin, villager);
        this.market = market;
    }
    
    /**
     * Gets the associated market.
     * @return The market, or null if none
     */
    public Market getMarket() {
        return market;
    }
    
    /**
     * Sets the associated market.
     * @param market The market
     */
    public void setMarket(Market market) {
        this.market = market;
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
        
        // Regular BD Crops trade (player gives 10 regular crops and receives 2 emeralds)
        MerchantRecipe regularCropRecipe = new MerchantRecipe(
                new ItemStack(Material.EMERALD, 2), 
                Integer.MAX_VALUE); // Unlimited uses
        
        // Set ingredients (10 regular BD crops - represented as ferns)
        regularCropRecipe.addIngredient(createBDCropItem("Regular BD Crop", 10));
        
        // Green BD Crops trade (player gives 5 green crops and receives 10 emeralds)
        MerchantRecipe greenCropRecipe = new MerchantRecipe(
                new ItemStack(Material.EMERALD, 10), 
                Integer.MAX_VALUE);
        
        // Set ingredients (5 green BD crops - represented as large ferns)
        greenCropRecipe.addIngredient(createBDCropItem("Green BD Crop", 5));
        
        // Purple BD Crops trade (player gives 3 purple crops and receives 20 emeralds)
        MerchantRecipe purpleCropRecipe = new MerchantRecipe(
                new ItemStack(Material.EMERALD, 20), 
                Integer.MAX_VALUE);
        
        // Set ingredients (3 purple BD crops)
        purpleCropRecipe.addIngredient(createBDCropItem("Purple BD Crop", 3));
        
        // Bulk Regular BD Crops trade (player gives 50 regular crops and receives 1 diamond)
        MerchantRecipe bulkRegularCropRecipe = new MerchantRecipe(
                new ItemStack(Material.DIAMOND, 1), 
                Integer.MAX_VALUE);
        
        // Set ingredients (50 regular BD crops)
        bulkRegularCropRecipe.addIngredient(createBDCropItem("Regular BD Crop", 50));
        
        // Add recipes to list
        recipes.add(regularCropRecipe);
        recipes.add(greenCropRecipe);
        recipes.add(purpleCropRecipe);
        recipes.add(bulkRegularCropRecipe);
        
        // Set recipes
        getVillager().setRecipes(recipes);
    }
    
    /**
     * Creates a BD crop item for trading.
     * @param name The item name
     * @param amount The amount
     * @return The item
     */
    private ItemStack createBDCropItem(String name, int amount) {
        // Create a properly recognized BD crop item with metadata
        CropType cropType;
        
        if (name.contains("Regular")) {
            cropType = CropType.REGULAR;
        } else if (name.contains("Green")) {
            cropType = CropType.GREEN;
        } else if (name.contains("Blue")) {
            cropType = CropType.BLUE;
        } else if (name.contains("Purple")) {
            cropType = CropType.PURPLE;
        } else {
            cropType = CropType.LEGENDARY;
        }
        
        // Until BDCraft exposes getItemManager() or we inject itemManager into this class
        // We'll use a simpler implementation that matches the item system
        ItemStack item = new ItemStack(cropType == CropType.REGULAR ? Material.WHEAT : 
                cropType == CropType.GREEN ? Material.APPLE : 
                cropType == CropType.BLUE ? Material.GOLDEN_CARROT : 
                cropType == CropType.PURPLE ? Material.GOLDEN_APPLE : 
                Material.ENCHANTED_GOLDEN_APPLE, amount);
                
        var meta = item.getItemMeta();
        
        ChatColor color = cropType == CropType.REGULAR ? ChatColor.YELLOW :
                cropType == CropType.GREEN ? ChatColor.GREEN :
                cropType == CropType.BLUE ? ChatColor.BLUE :
                cropType == CropType.PURPLE ? ChatColor.LIGHT_PURPLE :
                ChatColor.GOLD;
                
        String displayName = cropType == CropType.REGULAR ? "Standard Crop" :
                cropType == CropType.GREEN ? "Quality Crop" :
                cropType == CropType.BLUE ? "Premium Crop" :
                cropType == CropType.PURPLE ? "Exceptional Crop" :
                "Legendary Crop";
                
        meta.setDisplayName(color + displayName);
        
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A special " + color + cropType.name().toLowerCase() + 
                ChatColor.GRAY + " crop grown with");
        lore.add(ChatColor.GRAY + "careful tending and agricultural expertise.");
        lore.add("");
        lore.add(ChatColor.GRAY + "Value: " + ChatColor.GOLD + getCropValue(cropType) + " BD each");
        lore.add(ChatColor.GRAY + "Can be sold to collectors or at markets.");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Gets the value of a crop type.
     * 
     * @param type The crop type
     * @return The value
     */
    private int getCropValue(CropType type) {
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
    
    @Override
    public String getVillagerType() {
        return TYPE;
    }
    
    /**
     * Gets the name color for this villager type.
     * 
     * @return The name color
     */
    @Override
    protected ChatColor getNameColor() {
        return ChatColor.BLUE;
    }
    
    /**
     * Gets the villager level.
     * 
     * @return The villager level
     */
    @Override
    protected int getVillagerLevel() {
        return 2;  // Collector level
    }
    
    /**
     * Gets the villager type name.
     * 
     * @return The villager type name
     */
    @Override
    public String getVillagerTypeName() {
        return "Collector";
    }
    
    /**
     * Gets the Bukkit profession for this villager.
     * 
     * @return The Bukkit profession
     */
    @Override
    protected Profession getBukkitProfession() {
        return Profession.LIBRARIAN; // Using LIBRARIAN for collectors
    }
}