package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a BD Dealer villager.
 */
public class BDDealer extends BDVillager {
    private static final String TYPE = "DEALER";
    private final BDCraft plugin;
    
    /**
     * Creates a new BD Dealer.
     * @param plugin The plugin instance
     * @param entity The villager entity
     */
    public BDDealer(BDCraft plugin, Villager entity) {
        super(entity, TYPE);
        this.plugin = plugin;
        
        // Set dealer appearance
        entity.setProfession(Villager.Profession.FARMER);
        entity.setVillagerType(Villager.Type.PLAINS);
        entity.setVillagerLevel(5);
        entity.setCustomName(ChatColor.GREEN + "BD Dealer");
        entity.setCustomNameVisible(true);
        
        // Set up trades
        setupTrades();
    }
    
    /**
     * Sets up the trades for this dealer.
     */
    private void setupTrades() {
        BDItemFactory itemFactory = plugin.getEconomyModule().getItemManager().getItemFactory();
        List<MerchantRecipe> recipes = new ArrayList<>();
        
        // Regular BD Seeds (2 emeralds for 5 seeds)
        MerchantRecipe bdSeedsRecipe = new MerchantRecipe(itemFactory.createBDSeeds(5), 0, 1000, true);
        bdSeedsRecipe.addIngredient(new ItemStack(Material.EMERALD, 2));
        recipes.add(bdSeedsRecipe);
        
        // Green BD Seeds (5 emeralds for 3 seeds)
        MerchantRecipe greenBDSeedsRecipe = new MerchantRecipe(itemFactory.createGreenBDSeeds(3), 0, 500, true);
        greenBDSeedsRecipe.addIngredient(new ItemStack(Material.EMERALD, 5));
        recipes.add(greenBDSeedsRecipe);
        
        // Purple BD Seeds (10 emeralds for 1 seed)
        MerchantRecipe purpleBDSeedsRecipe = new MerchantRecipe(itemFactory.createPurpleBDSeeds(1), 0, 100, true);
        purpleBDSeedsRecipe.addIngredient(new ItemStack(Material.EMERALD, 10));
        recipes.add(purpleBDSeedsRecipe);
        
        // BD Stick (25 emeralds for 1 stick)
        MerchantRecipe bdStickRecipe = new MerchantRecipe(itemFactory.createBDStick(), 0, 50, true);
        bdStickRecipe.addIngredient(new ItemStack(Material.EMERALD, 25));
        recipes.add(bdStickRecipe);
        
        // BD Harvester (15 emeralds for 1 harvester)
        MerchantRecipe bdHarvesterRecipe = new MerchantRecipe(itemFactory.createBDHarvester(), 0, 100, true);
        bdHarvesterRecipe.addIngredient(new ItemStack(Material.EMERALD, 15));
        recipes.add(bdHarvesterRecipe);
        
        // Ultimate BD Harvester (1 diamond + 30 emeralds for 1 ultimate harvester)
        MerchantRecipe ultimateBDHarvesterRecipe = new MerchantRecipe(itemFactory.createUltimateBDHarvester(), 0, 50, true);
        ultimateBDHarvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 1));
        ultimateBDHarvesterRecipe.addIngredient(new ItemStack(Material.EMERALD, 30));
        recipes.add(ultimateBDHarvesterRecipe);
        
        // Farmer Information Book (1 emerald for information book)
        ItemStack infoBook = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta infoBookMeta = infoBook.getItemMeta();
        if (infoBookMeta != null) {
            infoBookMeta.setDisplayName(ChatColor.GOLD + "BD Farming Guide");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Contains all information about BD farming.");
            lore.add(ChatColor.GRAY + "Learn about seeds, crops, and harvesting.");
            infoBookMeta.setLore(lore);
            infoBook.setItemMeta(infoBookMeta);
        }
        
        MerchantRecipe infoBookRecipe = new MerchantRecipe(infoBook, 0, 1000, true);
        infoBookRecipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        recipes.add(infoBookRecipe);
        
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
        // Dealers give +1 reputation per trade
        int newRep = super.changeReputation(playerUuid, amount);
        
        // Update trades if reputation changes significantly
        if (amount != 0 && Math.abs(amount) >= 10) {
            updateTrades();
        }
        
        return newRep;
    }
    
    @Override
    public double getPriceModifier(UUID playerUuid) {
        int rep = getReputation(playerUuid);
        
        // Dealers offer discounts more reluctantly
        if (rep >= 100) {
            return 0.9; // 10% discount
        } else if (rep >= 50) {
            return 0.95; // 5% discount
        } else if (rep <= -50) {
            return 1.25; // 25% higher prices
        } else if (rep <= -25) {
            return 1.1; // 10% higher prices
        }
        
        return 1.0;
    }
}