# BD Seeds and Crops

BD seeds and crops are the foundation of the BDCraft economy system. This guide details the different types of BD items, their properties, and how they function within the game.

## BD Seeds

### Regular BD Seeds
- **Appearance**: Wheat Seeds (Material.WHEAT_SEEDS)
- **Display Name**: "BD Seed" (gold color)
- **Enchantment Glow**: Yes
- **Custom Metadata**: Tagged with bdItemKey (prevents crafting)
- **Price**: 1 emerald for 5 seeds
- **Required Rank**: None (available to all players)
- **Acquisition**: From BD Dealers in natural villages (45% chance to spawn)
- **Description**: Basic BD seeds, the entry point to BD farming

### Green BD Seeds
- **Appearance**: Beetroot Seeds (Material.BEETROOT_SEEDS)
- **Display Name**: "Green BD Seed" (green color)
- **Enchantment Glow**: Yes
- **Custom Metadata**: Tagged with greenBDItemKey (prevents crafting)
- **Price**: 9 emeralds each
- **Required Rank**: Farmer (or equivalent rank 2+)
- **Acquisition**: From BD Dealers in players' own markets or admin villages
- **Alternate Acquisition**: 5% chance to drop when harvesting regular BD crops
- **Description**: Premium seeds with faster growth time (30% faster than regular)

### Purple BD Seeds
- **Appearance**: Pumpkin Seeds (Material.PUMPKIN_SEEDS)
- **Display Name**: "Purple BD Seed" (purple color)
- **Enchantment Glow**: Yes
- **Custom Metadata**: Tagged with purpleBDItemKey (prevents crafting)
- **Price**: 25 emeralds each
- **Required Rank**: Master Farmer (or equivalent rank 4+)
- **Acquisition**: From Seasonal Traders or high-level market dealers
- **Alternate Acquisition**: 1% chance to drop when harvesting green BD crops
- **Description**: Rare seeds that produce the most valuable BD crops

## Growth Process

When planted, all BD seeds appear as standard wheat plants in the world but are tracked by the plugin as special BD plants:

1. **Visual Appearance**: Standard wheat plant (no color tint)
2. **Visual Indicators**:
   - Regular Seeds: Few green particles
   - Green Seeds: More green particles
   - Purple Seeds: Most green particles + slight glow effect
3. **Growth Speed**:
   - Green seeds grow 30% faster than regular
   - Purple seeds grow at the same rate as regular
4. **Tracker System**: Plugin tracks planted BD seeds using block metadata

## BD Crops (Harvested Items)

### Regular BD Crops
- **Appearance**: Fern (Material.FERN)
- **Display Name**: "BD Crop" (gold color)
- **Custom Metadata**: Tagged with bdCropKey
- **Trade Value**: 10 crops = 2 emeralds + 50 server currency
- **Description**: Standard BD crop harvested from regular BD seeds

### Green BD Crops
- **Appearance**: Large Fern (Material.LARGE_FERN)
- **Display Name**: "Green BD Crop" (green color) 
- **Custom Metadata**: Tagged with greenBDCropKey
- **Trade Value**: 5 crops = 10 emeralds + 150 server currency
- **Description**: Premium BD crop, worth 5x more than regular BD

### Purple BD Crops
- **Appearance**: Large Fern with enchantment glow (Material.LARGE_FERN)
- **Display Name**: "Purple BD Crop" (purple color)
- **Custom Metadata**: Tagged with purpleBDCropKey
- **Trade Value**: 3 crops = 20 emeralds + 400 server currency
- **Description**: Rare high-value BD crop, worth over 10x more than regular BD

## Trading Restrictions

- **Seeds**: Can be sold on auction house (player-to-player)
- **Crops**: Can only be sold to collector villagers (not on auction house)

## Special Properties

- BD items cannot be used in crafting regular Minecraft recipes
- BD seeds will still look like wheat when planted, but have special properties
- Harvesting BD crops without a proper tool gives standard yield
- Using BD Harvester or Ultimate BD Harvester increases yield significantly
- Players can sell 50 regular BD crops to collectors for 1 diamond

This specialized farming system creates a unique economy loop for the BDCraft server ecosystem, with clear progression paths from regular to premium crops.