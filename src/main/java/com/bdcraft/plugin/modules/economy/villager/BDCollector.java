package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a BD Collector villager.
 */
public class BDCollector extends BDVillager {
    private static final String TYPE = "COLLECTOR";
    private final BDCraft plugin;
    
    /**
     * Creates a new BD Collector.
     * @param plugin The plugin instance
     * @param entity The villager entity
     */
    public BDCollector(BDCraft plugin, Villager entity) {
        super(entity, TYPE);
        this.plugin = plugin;
        
        // Set collector appearance
        entity.setProfession(Villager.Profession.LIBRARIAN);
        entity.setVillagerType(Villager.Type.PLAINS);
        entity.setVillagerLevel(3);
        entity.setCustomName(ChatColor.AQUA + "BD Collector");
        entity.setCustomNameVisible(true);
        
        // Set up trades
        setupTrades();
    }
    
    /**
     * Sets up the trades for this collector.
     */
    private void setupTrades() {
        BDItemFactory itemFactory = plugin.getEconomyModule().getItemManager().getItemFactory();
        List<MerchantRecipe> recipes = new ArrayList<>();
        
        // Regular BD Crops (10 crops for 2 emeralds + 50 server currency)
        ItemStack emeraldReward = new ItemStack(Material.EMERALD, 2);
        ItemMeta emeraldMeta = emeraldReward.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "+" + ChatColor.GREEN + "50 BD Currency");
        emeraldMeta.setLore(lore);
        emeraldReward.setItemMeta(emeraldMeta);
        
        MerchantRecipe bdCropsRecipe = new MerchantRecipe(emeraldReward, 0, 1000, true);
        bdCropsRecipe.addIngredient(itemFactory.createBDCrops(10));
        recipes.add(bdCropsRecipe);
        
        // Green BD Crops (5 crops for 10 emeralds + 150 server currency)
        ItemStack greenEmeraldReward = new ItemStack(Material.EMERALD, 10);
        ItemMeta greenEmeraldMeta = greenEmeraldReward.getItemMeta();
        List<String> greenLore = new ArrayList<>();
        greenLore.add(ChatColor.GRAY + "+" + ChatColor.GREEN + "150 BD Currency");
        greenEmeraldMeta.setLore(greenLore);
        greenEmeraldReward.setItemMeta(greenEmeraldMeta);
        
        MerchantRecipe greenBDCropsRecipe = new MerchantRecipe(greenEmeraldReward, 0, 100, true);
        greenBDCropsRecipe.addIngredient(itemFactory.createGreenBDCrops(5));
        recipes.add(greenBDCropsRecipe);
        
        // Purple BD Crops (3 crops for 20 emeralds + 400 server currency)
        ItemStack purpleEmeraldReward = new ItemStack(Material.EMERALD, 20);
        ItemMeta purpleEmeraldMeta = purpleEmeraldReward.getItemMeta();
        List<String> purpleLore = new ArrayList<>();
        purpleLore.add(ChatColor.GRAY + "+" + ChatColor.GREEN + "400 BD Currency");
        purpleEmeraldMeta.setLore(purpleLore);
        purpleEmeraldReward.setItemMeta(purpleEmeraldMeta);
        
        MerchantRecipe purpleBDCropsRecipe = new MerchantRecipe(purpleEmeraldReward, 0, 50, true);
        purpleBDCropsRecipe.addIngredient(itemFactory.createPurpleBDCrops(3));
        recipes.add(purpleBDCropsRecipe);
        
        // Bulk trade - 50 regular BD for 1 diamond
        ItemStack diamondReward = new ItemStack(Material.DIAMOND, 1);
        MerchantRecipe bulkBDCropsRecipe = new MerchantRecipe(diamondReward, 0, 100, true);
        bulkBDCropsRecipe.addIngredient(itemFactory.createBDCrops(50));
        recipes.add(bulkBDCropsRecipe);
        
        entity.setRecipes(recipes);
    }
    
    /**
     * Updates trades based on the player's level and reputation.
     */
    public void updateTrades() {
        // Re-apply the trades with potentially modified values
        setupTrades();
    }
    
    @Override
    public int changeReputation(UUID playerUuid, int amount) {
        // Collectors give +3 reputation per trade
        int newRep = super.changeReputation(playerUuid, amount);
        
        // Update trades if reputation changes significantly
        if (amount != 0 && Math.abs(amount) >= 5) {
            updateTrades();
        }
        
        return newRep;
    }
    
    @Override
    public double getPriceModifier(UUID playerUuid) {
        int rep = getReputation(playerUuid);
        if (rep <= 0) {
            return 1.0;
        }
        
        // For collectors, higher reputation means better sell prices
        // Every 10 reputation points gives a 1% bonus, max 30%
        double bonus = Math.min(0.3, rep / 1000.0);
        return 1.0 + bonus;
    }
}