package com.bdcraft.plugin.modules.economy.villagers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.villagers.BDVillager.VillagerType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all BD villagers.
 */
public class VillagerManager implements Listener {
    private final BDCraft plugin;
    private final Map<UUID, BDVillager> villagers;
    
    /**
     * Creates a new villager manager.
     * 
     * @param plugin The plugin instance
     */
    public VillagerManager(BDCraft plugin) {
        this.plugin = plugin;
        this.villagers = new HashMap<>();
        
        // Register events
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Creates a new market owner villager.
     * 
     * @param market The market
     * @param location The spawn location
     * @return The created villager
     */
    public MarketOwnerVillager createMarketOwner(BDMarket market, Location location) {
        MarketOwnerVillager villager = new MarketOwnerVillager(plugin, location, market);
        
        villagers.put(villager.getUuid(), villager);
        return villager;
    }
    
    /**
     * Creates a new BD dealer villager.
     * 
     * @param market The market
     * @param location The spawn location
     * @return The created villager
     */
    public BDDealerVillager createBDDealer(BDMarket market, Location location) {
        UUID uuid = UUID.randomUUID();
        BDDealerVillager villager = new BDDealerVillager(plugin, uuid, market, null);
        villager.spawn(location);
        
        villagers.put(uuid, villager);
        return villager;
    }
    
    /**
     * Gets a BD villager by UUID.
     * 
     * @param uuid The UUID
     * @return The villager, or null if not found
     */
    public BDVillager getVillager(UUID uuid) {
        return villagers.get(uuid);
    }
    
    /**
     * Gets a BD villager from a Bukkit entity.
     * 
     * @param entity The entity
     * @return The villager, or null if the entity is not a BD villager
     */
    public BDVillager getVillager(Entity entity) {
        if (!(entity instanceof Villager)) {
            return null;
        }
        
        PersistentDataContainer container = entity.getPersistentDataContainer();
        if (!container.has(new NamespacedKey(plugin, "bd_villager_uuid"), PersistentDataType.STRING)) {
            return null;
        }
        
        String uuidString = container.get(new NamespacedKey(plugin, "bd_villager_uuid"), PersistentDataType.STRING);
        return villagers.get(UUID.fromString(uuidString));
    }
    
    /**
     * Gets all villagers of a specific type.
     * 
     * @param type The villager type
     * @return A list of villagers of the specified type
     */
    public List<BDVillager> getVillagers(VillagerType type) {
        List<BDVillager> result = new ArrayList<>();
        
        for (BDVillager villager : villagers.values()) {
            if (villager.getBDType() == type) {
                result.add(villager);
            }
        }
        
        return result;
    }
    
    /**
     * Gets all market owner villagers.
     * 
     * @return A list of market owner villagers
     */
    public List<MarketOwnerVillager> getMarketOwners() {
        List<MarketOwnerVillager> result = new ArrayList<>();
        
        for (BDVillager villager : getVillagers(VillagerType.MARKET_OWNER)) {
            result.add((MarketOwnerVillager) villager);
        }
        
        return result;
    }
    
    /**
     * Gets all BD dealer villagers.
     * 
     * @return A list of BD dealer villagers
     */
    public List<BDDealerVillager> getBDDealers() {
        List<BDDealerVillager> result = new ArrayList<>();
        
        for (BDVillager villager : getVillagers(VillagerType.BD_DEALER)) {
            result.add((BDDealerVillager) villager);
        }
        
        return result;
    }
    
    /**
     * Removes a villager.
     * 
     * @param uuid The UUID of the villager to remove
     */
    public void removeVillager(UUID uuid) {
        BDVillager villager = villagers.get(uuid);
        if (villager != null) {
            villager.despawn();
            villagers.remove(uuid);
        }
    }
    
    /**
     * Removes all villagers.
     */
    public void removeAllVillagers() {
        for (BDVillager villager : new ArrayList<>(villagers.values())) {
            villager.despawn();
        }
        
        villagers.clear();
    }
    
    /**
     * Handles a market upgrade.
     * 
     * @param market The market that was upgraded
     */
    public void handleMarketUpgrade(BDMarket market) {
        // Update trades for all villagers in this market
        for (MarketOwnerVillager owner : getMarketOwners()) {
            if (owner.getMarket().equals(market)) {
                owner.updateTrades();
            }
        }
        
        for (BDDealerVillager dealer : getBDDealers()) {
            if (dealer.getMarket().equals(market)) {
                dealer.updateTrades();
            }
        }
    }
    
    /**
     * Handles villager interaction events.
     */
    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager)) {
            return;
        }
        
        BDVillager bdVillager = getVillager(event.getRightClicked());
        if (bdVillager == null) {
            return;
        }
        
        // Handle specific villager interactions here if needed
        if (bdVillager.getBDType() == VillagerType.MARKET_OWNER) {
            // Additional market owner interaction logic can go here
        }
    }
    
    /**
     * Prevents BD villagers from acquiring new trades.
     */
    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        BDVillager bdVillager = getVillager(event.getEntity());
        if (bdVillager != null) {
            event.setCancelled(true);
        }
    }
    
    /**
     * Prevents BD villagers from taking damage.
     */
    @EventHandler
    public void onVillagerDamage(EntityDamageEvent event) {
        BDVillager bdVillager = getVillager(event.getEntity());
        if (bdVillager != null) {
            event.setCancelled(true);
        }
    }
    
    /**
     * Handles trade result processing.
     */
    @EventHandler
    public void onTradeProcess(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof MerchantInventory)) {
            return;
        }
        
        // Only process when clicking the result slot in a merchant inventory
        if (event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }
        
        MerchantInventory merchantInv = (MerchantInventory) event.getInventory();
        Villager villager = (Villager) merchantInv.getMerchant();
        
        BDVillager bdVillager = getVillager(villager);
        if (bdVillager == null) {
            return;
        }
        
        // Handle specific trade results here
        if (bdVillager.getBDType() == VillagerType.MARKET_OWNER) {
            MarketOwnerVillager marketOwner = (MarketOwnerVillager) bdVillager;
            MerchantRecipe recipe = merchantInv.getMerchant().getRecipe(merchantInv.getSelectedRecipeIndex());
            
            // Trade is happening - player has provided diamonds, now check BD currency
            ItemStack result = recipe.getResult();
            if (result.getType() == Material.PAPER) {
                ItemMeta meta = result.getItemMeta();
                if (meta != null && meta.getPersistentDataContainer().has(
                        plugin.getNamespacedKey("market_upgrade_certificate"), PersistentDataType.STRING)) {
                    
                    // Get the certificate data
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    String data = container.get(plugin.getNamespacedKey("market_upgrade_certificate"), 
                            PersistentDataType.STRING);
                    String[] parts = data.split(":");
                    
                    if (parts.length != 3) {
                        return;
                    }
                    
                    // Parse certificate data
                    String marketId = parts[0];
                    int targetLevel = Integer.parseInt(parts[1]);
                    int bdCost = Integer.parseInt(parts[2]);
                    
                    // Verify this certificate is for this market
                    if (!marketId.equals(marketOwner.getMarket().getId().toString())) {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(ChatColor.RED + "This certificate is for a different market!");
                        return;
                    }
                    
                    Player player = (Player) event.getWhoClicked();
                    
                    // Check if player has enough BD currency
                    if (!hasEnoughBDCurrency(player, bdCost)) {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You need " + bdCost + " BD currency to complete this upgrade!");
                        return;
                    }
                    
                    // Take the BD currency cost
                    removeBDCurrency(player, bdCost);
                    
                    // Allow the trade to continue
                    player.sendMessage(ChatColor.GREEN + "Upgrade certificate received! Right-click to upgrade the market.");
                    
                    // Update the certificate to mark it as paid
                    ItemStack certificate = result.clone();
                    ItemMeta certMeta = certificate.getItemMeta();
                    
                    // Update lore to show it's a paid certificate
                    certMeta.setLore(Arrays.asList(
                        ChatColor.YELLOW + "Market Upgrade Certificate: Level " + targetLevel,
                        ChatColor.GREEN + "✓ Payment Completed",
                        "",
                        ChatColor.GRAY + "Right-click while holding to apply upgrade."
                    ));
                    
                    // Change persistent data to show it's paid
                    certMeta.getPersistentDataContainer().set(
                        plugin.getNamespacedKey("market_upgrade"),
                        PersistentDataType.STRING,
                        marketId + ":" + targetLevel
                    );
                    
                    // Apply the metadata to the certificate
                    certificate.setItemMeta(certMeta);
                    
                    // Create a new recipe with our modified result
                    MerchantRecipe newRecipe = new MerchantRecipe(certificate, recipe.getUses(), 
                            recipe.getMaxUses(), recipe.hasExperienceReward(), 
                            recipe.getVillagerExperience(), recipe.getPriceMultiplier());
                    
                    // Copy ingredients from old recipe
                    for (ItemStack ingredient : recipe.getIngredients()) {
                        if (ingredient != null) {
                            newRecipe.addIngredient(ingredient.clone());
                        }
                    }
                    
                    // Replace the recipe in the villager's inventory with our new one
                    List<MerchantRecipe> recipes = merchantInv.getMerchant().getRecipes();
                    recipes.set(merchantInv.getSelectedRecipeIndex(), newRecipe);
                }
            }
        }
    }
    
    /**
     * Processes a market upgrade through the two-step verification.
     * First checks if the player has enough diamonds, then checks if they have enough BD currency.
     * 
     * @param player The player trying to upgrade
     * @param market The market to upgrade
     * @param targetLevel The level to upgrade to
     * @param diamondCost The diamond cost
     * @param bdCost The BD currency cost
     */
    private void processMarketUpgrade(Player player, BDMarket market, int targetLevel, int diamondCost, int bdCost) {
        // Check if market is already at the target level or higher
        if (market.getLevel() >= targetLevel) {
            player.sendMessage(ChatColor.RED + "This market is already at level " + market.getLevel() + "!");
            return;
        }
        
        // Check if player has enough diamonds
        if (!hasEnoughDiamonds(player, diamondCost)) {
            player.sendMessage(ChatColor.RED + "You need " + diamondCost + " diamonds to upgrade this market!");
            return;
        }
        
        // Check if player has enough BD currency
        if (!hasEnoughBDCurrency(player, bdCost)) {
            player.sendMessage(ChatColor.RED + "You need " + bdCost + " BD currency to upgrade this market!");
            return;
        }
        
        // Remove diamonds from inventory
        removeDiamonds(player, diamondCost);
        
        // Remove BD currency from player
        removeBDCurrency(player, bdCost);
        
        // Upgrade the market
        market.upgrade();
        
        // Give the actual upgrade certificate
        ItemStack certificate = createMarketUpgradeCertificate(market, targetLevel);
        player.getInventory().addItem(certificate);
        
        // Notify player
        player.sendMessage(ChatColor.GREEN + "Your market has been upgraded to level " + market.getLevel() + "!");
        
        // Update trades
        handleMarketUpgrade(market);
    }
    
    /**
     * Checks if a player has enough diamonds.
     * 
     * @param player The player
     * @param count The number of diamonds required
     * @return True if the player has enough diamonds
     */
    private boolean hasEnoughDiamonds(Player player, int count) {
        int total = 0;
        
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.DIAMOND) {
                total += item.getAmount();
                if (total >= count) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Removes diamonds from a player's inventory.
     * 
     * @param player The player
     * @param count The number of diamonds to remove
     */
    private void removeDiamonds(Player player, int count) {
        int remaining = count;
        
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.DIAMOND) {
                int amount = item.getAmount();
                
                if (amount > remaining) {
                    item.setAmount(amount - remaining);
                    break;
                } else {
                    item.setAmount(0);
                    remaining -= amount;
                    if (remaining <= 0) {
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Checks if a player has enough BD currency.
     * This would check the player's BD currency score on the scoreboard.
     * 
     * @param player The player
     * @param amount The amount of BD currency required
     * @return True if the player has enough BD currency
     */
    private boolean hasEnoughBDCurrency(Player player, int amount) {
        // This would be implemented to check the player's BD currency score
        // For now, return true for testing purposes
        // In production, this would check a scoreboard or other tracking system
        
        // Example implementation:
        // Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        // Objective objective = scoreboard.getObjective("bdcurrency");
        // if (objective == null) {
        //     return false;
        // }
        // Score score = objective.getScore(player.getName());
        // return score.getScore() >= amount;
        
        // Temporary implementation:
        // Note: This should be replaced with actual BD currency checking
        return true;
    }
    
    /**
     * Removes BD currency from a player.
     * This would deduct from the player's BD currency score on the scoreboard.
     * 
     * @param player The player
     * @param amount The amount of BD currency to remove
     */
    private void removeBDCurrency(Player player, int amount) {
        // This would be implemented to deduct from the player's BD currency score
        // For now, do nothing for testing purposes
        // In production, this would update a scoreboard or other tracking system
        
        // Example implementation:
        // Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        // Objective objective = scoreboard.getObjective("bdcurrency");
        // if (objective != null) {
        //     Score score = objective.getScore(player.getName());
        //     int newValue = score.getScore() - amount;
        //     score.setScore(newValue);
        // }
    }
    
    /**
     * Creates a market upgrade certificate.
     * 
     * @param market The market
     * @param newLevel The new level after upgrade
     * @return The certificate item
     */
    private ItemStack createMarketUpgradeCertificate(BDMarket market, int newLevel) {
        ItemStack certificate = new ItemStack(Material.PAPER);
        ItemMeta meta = certificate.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Market Upgrade Certificate");
            meta.setLore(Arrays.asList(
                ChatColor.YELLOW + "Use this to upgrade your market",
                ChatColor.YELLOW + "from level " + (market.getLevel() - 1) + " to level " + market.getLevel(),
                "",
                ChatColor.GRAY + "This certificate has already been applied."
            ));
            
            // Add persistent data to identify this as a valid upgrade certificate
            meta.getPersistentDataContainer().set(
                plugin.getNamespacedKey("market_upgrade"),
                PersistentDataType.STRING,
                market.getId().toString() + ":" + newLevel
            );
            
            certificate.setItemMeta(meta);
        }
        return certificate;
    }
    
    /**
     * Handles players using market upgrade certificates.
     */
    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        // Only process right-click with an item
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_AIR && 
                event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.PAPER) {
            return;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        
        // Check if this is a market upgrade certificate
        PersistentDataContainer container = meta.getPersistentDataContainer();
        // Check both types of certificates - the unpaid one and the paid one
        boolean isUnpaidCertificate = container.has(plugin.getNamespacedKey("market_upgrade_certificate"), PersistentDataType.STRING);
        boolean isPaidCertificate = container.has(plugin.getNamespacedKey("market_upgrade"), PersistentDataType.STRING);
        
        if (!isUnpaidCertificate && !isPaidCertificate) {
            return;
        }
        
        // Cancel the event to prevent using the item normally
        event.setCancelled(true);
        
        Player player = event.getPlayer();
        
        if (isUnpaidCertificate) {
            // This is still an unpaid certificate - tell them they need to trade with the villager
            player.sendMessage(ChatColor.RED + "You need to trade this with the Market Owner villager first!");
            return;
        }
        
        // This is a paid certificate - process the upgrade
        String data = container.get(plugin.getNamespacedKey("market_upgrade"), PersistentDataType.STRING);
        String[] parts = data.split(":");
        
        if (parts.length != 2) {
            return;
        }
        
        // Parse certificate data
        String marketId = parts[0]; 
        int targetLevel = Integer.parseInt(parts[1]);
        
        // Get market - convert market ID string to UUID
        BDMarket market = plugin.getEconomyModule().getMarketManager().getMarket(UUID.fromString(marketId));
        if (market == null) {
            player.sendMessage(ChatColor.RED + "This certificate is for a market that no longer exists!");
            return;
        }
        
        // Check if player has permission to upgrade this market
        boolean isFounder = player.getUniqueId().equals(market.getFounderId());
        boolean isAssociate = market.isAssociate(player.getUniqueId());
        boolean isAdmin = player.hasPermission("bdcraft.admin.market");
        
        if (!isFounder && !isAssociate && !isAdmin) {
            player.sendMessage(ChatColor.RED + "Only the market founder and associates can upgrade this market.");
            return;
        }
        
        // Check if market is already at or beyond this level
        if (market.getLevel() >= targetLevel) {
            player.sendMessage(ChatColor.RED + "This market is already at level " + market.getLevel() + "!");
            return;
        }
        
        // Upgrade the market
        market.upgrade();
        
        // Remove the certificate (decrement stack size)
        int amount = item.getAmount();
        if (amount > 1) {
            item.setAmount(amount - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
        
        // Notify player
        player.sendMessage(ChatColor.GREEN + "Your market has been upgraded to level " + market.getLevel() + "!");
        
        // Update trades for market villagers
        handleMarketUpgrade(market);
    }
}