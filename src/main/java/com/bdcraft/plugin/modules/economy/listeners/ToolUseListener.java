package com.bdcraft.plugin.modules.economy.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.items.tools.BDStickHandler;
import com.bdcraft.plugin.modules.economy.items.tools.BDTool;
import com.bdcraft.plugin.modules.economy.items.tools.ToolType;

/**
 * Listens for BD tool use events.
 */
public class ToolUseListener implements Listener {
    private final BDCraft plugin;
    private final BDItemManager itemManager;
    private final BDStickHandler stickHandler;
    
    /**
     * Creates a new tool use listener.
     * 
     * @param plugin The plugin instance
     */
    public ToolUseListener(BDCraft plugin) {
        this.plugin = plugin;
        this.itemManager = plugin.getModuleManager().getEconomyModule().getItemManager();
        this.stickHandler = new BDStickHandler(plugin);
    }
    
    /**
     * Handles player interact events for BD tools.
     * 
     * @param event The event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Only process right click air or right click block events
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        
        // Check if the item is a BD tool
        if (!itemManager.isBDTool(item)) {
            return;
        }
        
        ToolType toolType = BDTool.getToolType(item);
        
        if (toolType == ToolType.BDSTICK) {
            // Handle BD Stick use
            event.setCancelled(true);
            stickHandler.handleUse(player, item);
        }
    }
}