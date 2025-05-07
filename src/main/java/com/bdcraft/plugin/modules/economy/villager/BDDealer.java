package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

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
        entity.setVillagerLevel(2);
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
        
        // Regular BD Seeds (5 for 1 emerald)
        MerchantRecipe bdSeedsRecipe = new MerchantRecipe(itemFactory.createBDSeeds(5), 0, 100, true);
        bdSeedsRecipe.addIngredient(new ItemStack(Material.EMERALD, 1));
        recipes.add(bdSeedsRecipe);
        
        // Green BD Seeds (1 for 9 emeralds)
        MerchantRecipe greenBDSeedsRecipe = new MerchantRecipe(itemFactory.createGreenBDSeeds(1), 0, 20, true);
        greenBDSeedsRecipe.addIngredient(new ItemStack(Material.EMERALD, 9));
        recipes.add(greenBDSeedsRecipe);
        
        // Purple BD Seeds (1 for 25 emeralds, very limited)
        MerchantRecipe purpleBDSeedsRecipe = new MerchantRecipe(itemFactory.createPurpleBDSeeds(1), 0, 5, true);
        purpleBDSeedsRecipe.addIngredient(new ItemStack(Material.EMERALD, 25));
        recipes.add(purpleBDSeedsRecipe);
        
        // BD Harvester (1 for 16 diamonds)
        MerchantRecipe bdHarvesterRecipe = new MerchantRecipe(itemFactory.createBDHarvester(), 0, 10, true);
        bdHarvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 16));
        recipes.add(bdHarvesterRecipe);
        
        // Ultimate BD Harvester (1 for 32 diamonds, very limited)
        MerchantRecipe ultimateBDHarvesterRecipe = new MerchantRecipe(itemFactory.createUltimateBDHarvester(), 0, 3, true);
        ultimateBDHarvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, 32));
        recipes.add(ultimateBDHarvesterRecipe);
        
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
        // Dealers give +2 reputation per trade
        int newRep = super.changeReputation(playerUuid, amount);
        
        // Update trades if reputation changes significantly
        if (amount != 0 && Math.abs(amount) >= 5) {
            updateTrades();
        }
        
        return newRep;
    }
}