package com.bdcraft.plugin.modules.perms;

/**
 * Enum containing all permission nodes used in the plugin.
 */
public enum PermNode {
    // Admin permissions
    ADMIN("bdcraft.admin"),
    RELOAD("bdcraft.admin.reload"),
    
    // Economy permissions
    ECONOMY_ADMIN("bdcraft.economy.admin"),
    ECONOMY_USE("bdcraft.economy.use"),
    
    // Market permissions
    MARKET_ADMIN("bdcraft.market.admin"),
    MARKET_USE("bdcraft.market.use"),
    
    // Auction permissions
    AUCTION_ADMIN("bdcraft.auction.admin"),
    AUCTION_USE("bdcraft.auction.use"),
    
    // Villager permissions
    VILLAGER_ADMIN("bdcraft.villager.admin"),
    VILLAGER_USE("bdcraft.villager.use"),
    
    // Progression permissions
    PROGRESSION_ADMIN("bdcraft.progression.admin"),
    RANK_ADMIN("bdcraft.rank.admin"),
    REBIRTH_ADMIN("bdcraft.rebirth.admin"),
    
    // Teleport permissions
    TELEPORT_GENERAL("bdcraft.teleport"),
    TELEPORT_TPA("bdcraft.teleport.tpa"),
    TELEPORT_BACK("bdcraft.teleport.back"),
    TELEPORT_ADMIN("bdcraft.teleport.admin"),
    TELEPORT_WARP("bdcraft.teleport.warp"),
    TELEPORT_HOME("bdcraft.teleport.home"),
    TELEPORT_BYPASS_COOLDOWN("bdcraft.teleport.bypass.cooldown"),
    TELEPORT_BYPASS_WARMUP("bdcraft.teleport.bypass.warmup"),
    TELEPORT_BYPASS_WORLDS("bdcraft.teleport.bypass.worlds"),
    TELEPORT_BYPASS_DIMENSION("bdcraft.teleport.bypass.dimension"),
    
    // Message permissions
    MSG_ADMIN("bdcraft.message.admin"),
    MSG_USE("bdcraft.message.use"),
    MSG_BYPASS_COOLDOWN("bdcraft.message.bypass.cooldown"),
    MSG_BYPASS_FILTER("bdcraft.message.bypass.filter"),
    MSG_FORMAT_CODES("bdcraft.message.format"),
    SOCIALSPY("bdcraft.socialspy");
    
    private final String node;
    
    PermNode(String node) {
        this.node = node;
    }
    
    /**
     * Gets the permission node string.
     *
     * @return The permission node string
     */
    public String getNode() {
        return node;
    }
    
    @Override
    public String toString() {
        return node;
    }
}