package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.SeedType;
import com.bdcraft.plugin.modules.economy.items.tools.ToolType;
import com.bdcraft.plugin.modules.economy.market.Market;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
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
    private Market market;
    
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
    
    /**
     * Creates a new BD dealer associated with a market.
     * @param plugin The plugin instance
     * @param location The spawn location
     * @param market The associated market
     */
    public BDDealer(BDCraft plugin, Location location, Market market) {
        super(plugin, location, "BD Dealer", Villager.Profession.FARMER, Villager.Type.PLAINS);
        this.market = market;
    }
    
    /**
     * Creates a new BD dealer from an existing villager with a market.
     * @param plugin The plugin instance
     * @param villager The villager
     * @param market The associated market
     */
    public BDDealer(BDCraft plugin, Villager villager, Market market) {
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
        
        // Get configuration values from economy.yml
        FileConfiguration economyConfig = plugin.getConfigManager().getConfig("economy.yml");
        
        // Get trade configuration values
        int regularSeedAmount = economyConfig.getInt("villager.trades.regular-seeds.amount", 5);
        int regularSeedCost = economyConfig.getInt("villager.trades.regular-seeds.cost", 1);
        
        int greenSeedAmount = economyConfig.getInt("villager.trades.green-seeds.amount", 2);
        int greenSeedCost = economyConfig.getInt("villager.trades.green-seeds.cost", 5);
        
        int purpleSeedAmount = economyConfig.getInt("villager.trades.purple-seeds.amount", 1);
        int purpleSeedCost = economyConfig.getInt("villager.trades.purple-seeds.cost", 25);
        
        int harvesterUses = economyConfig.getInt("villager.trades.harvester.uses", 10);
        int harvesterEmeralds = economyConfig.getInt("villager.trades.harvester.emerald-cost", 16);
        int harvesterDiamonds = economyConfig.getInt("villager.trades.harvester.diamond-cost", 1);
        
        int ultimateHarvesterUses = economyConfig.getInt("villager.trades.ultimate-harvester.uses", 5);
        int ultimateHarvesterEmeralds = economyConfig.getInt("villager.trades.ultimate-harvester.emerald-cost", 32);
        int ultimateHarvesterDiamonds = economyConfig.getInt("villager.trades.ultimate-harvester.diamond-cost", 5);
        
        // Add regular BD seeds trade
        MerchantRecipe regularSeedRecipe = new MerchantRecipe(
                createBDSeedItem("Regular BD Seeds", regularSeedAmount), 
                Integer.MAX_VALUE); // Unlimited uses
        
        // Set ingredients
        regularSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, regularSeedCost));
        
        // Add green BD seeds trade
        MerchantRecipe greenSeedRecipe = new MerchantRecipe(
                createBDSeedItem("Green BD Seeds", greenSeedAmount), 
                Integer.MAX_VALUE);
        
        // Set ingredients
        greenSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, greenSeedCost));
        
        // Add purple BD seeds trade
        MerchantRecipe purpleSeedRecipe = new MerchantRecipe(
                createBDSeedItem("Purple BD Seeds", purpleSeedAmount), 
                Integer.MAX_VALUE);
        
        // Set ingredients
        purpleSeedRecipe.addIngredient(new ItemStack(Material.EMERALD, purpleSeedCost));
        
        // Add BD harvester trade
        MerchantRecipe harvesterRecipe = new MerchantRecipe(
                createBDHarvesterItem("BD Harvester", 1), 
                harvesterUses);
        
        // Set ingredients
        harvesterRecipe.addIngredient(new ItemStack(Material.EMERALD, harvesterEmeralds));
        harvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, harvesterDiamonds));
        
        // Add Ultimate BD harvester trade
        MerchantRecipe ultimateHarvesterRecipe = new MerchantRecipe(
                createBDHarvesterItem("Ultimate BD Harvester", 1), 
                ultimateHarvesterUses);
        
        // Set ingredients
        ultimateHarvesterRecipe.addIngredient(new ItemStack(Material.EMERALD, ultimateHarvesterEmeralds));
        ultimateHarvesterRecipe.addIngredient(new ItemStack(Material.DIAMOND, ultimateHarvesterDiamonds));
        
        // Add recipes to list
        recipes.add(regularSeedRecipe);
        recipes.add(greenSeedRecipe);
        recipes.add(purpleSeedRecipe);
        recipes.add(harvesterRecipe);
        recipes.add(ultimateHarvesterRecipe);
        
        // Set recipes
        getVillager().setRecipes(recipes);
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
        // Get configuration values from economy.yml
        FileConfiguration economyConfig = plugin.getConfigManager().getConfig("economy.yml");
        
        switch (type) {
            case REGULAR:
                return economyConfig.getInt("seeds.values.regular", 5);
            case GREEN:
                return economyConfig.getInt("seeds.values.green", 15);
            case BLUE:
                return economyConfig.getInt("seeds.values.blue", 30);
            case PURPLE:
                return economyConfig.getInt("seeds.values.purple", 50);
            case LEGENDARY:
                return economyConfig.getInt("seeds.values.legendary", 100);
            default:
                return economyConfig.getInt("seeds.values.default", 1);
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
        // Get configuration values from economy.yml
        FileConfiguration economyConfig = plugin.getConfigManager().getConfig("economy.yml");
        
        if (toolType == ToolType.ULTIMATE_HARVESTER) {
            int ultimateBonus = economyConfig.getInt("tools.ultimate-harvester.bonus-yield", 100);
            int ultimateDurability = economyConfig.getInt("tools.ultimate-harvester.durability", 60);
            String ultimateRank = economyConfig.getString("tools.ultimate-harvester.required-rank", "Agricultural Expert");
            
            lore.add(ChatColor.GRAY + "+" + ultimateBonus + "% bonus yield when harvesting BD crops");
            lore.add(ChatColor.GRAY + "Creates purple particles when harvesting");
            lore.add(ChatColor.GRAY + "Durability: " + ultimateDurability + " uses");
            lore.add(ChatColor.RED + "Requires rank: " + ultimateRank);
        } else {
            int regularBonus = economyConfig.getInt("tools.harvester.bonus-yield", 50);
            int regularDurability = economyConfig.getInt("tools.harvester.durability", 20);
            String regularRank = economyConfig.getString("tools.harvester.required-rank", "Expert Farmer");
            
            lore.add(ChatColor.GRAY + "+" + regularBonus + "% bonus yield when harvesting BD crops");
            lore.add(ChatColor.GRAY + "Creates blue particles when harvesting");
            lore.add(ChatColor.GRAY + "Durability: " + regularDurability + " uses");
            lore.add(ChatColor.RED + "Requires rank: " + regularRank);
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
    
    /**
     * Gets the name color for this villager type.
     * 
     * @return The name color
     */
    @Override
    protected ChatColor getNameColor() {
        return ChatColor.GOLD;
    }
    
    /**
     * Gets the villager level.
     * 
     * @return The villager level
     */
    @Override
    protected int getVillagerLevel() {
        return 1;  // Default level
    }
    
    /**
     * Gets the villager type name.
     * 
     * @return The villager type name
     */
    @Override
    public String getVillagerTypeName() {
        return "Dealer";
    }
    
    /**
     * Gets the Bukkit profession for this villager.
     * 
     * @return The Bukkit profession
     */
    @Override
    protected Profession getBukkitProfession() {
        return Profession.FARMER; // Using FARMER for dealers
    }
}