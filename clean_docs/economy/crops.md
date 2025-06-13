# BD Crops System

BD Crops are the foundation of BDCraft's economy, providing the primary method for players to generate currency and progress through the system.

## Crop Types

BDCraft features three tiers of crops, each with different growth times, values, and unlock requirements:

### Regular BD Crops
- **Unlock Requirement**: Available to all players from Newcomer rank
- **Seed Appearance**: Wheat seeds with BD metadata in inventory
- **Growth Time**: 12 minutes to fully mature
- **Mature Appearance**: Ferns (stage 4)
- **Inventory Item**: Ferns with BD metadata when harvested
- **Base Value**: 1 emerald + 10 server currency per crop (as configured in economy.yml)

### Green BD Crops
- **Unlock Requirement**: Farmer rank (Rank 2) or higher
- **Seed Appearance**: Melon seeds with BD metadata in inventory
- **Growth Time**: 20 minutes to fully mature
- **Mature Appearance**: Double ferns (stage 4)
- **Inventory Item**: Double ferns with BD metadata when harvested
- **Base Value**: 2 emeralds + 25 server currency per crop (as configured in economy.yml)

### Purple BD Crops
- **Unlock Requirement**: Master Farmer rank (Rank 4) or higher
- **Seed Appearance**: Pumpkin seeds with BD metadata in inventory
- **Growth Time**: 32 minutes to fully mature
- **Mature Appearance**: Double ferns with purple particle effects (stage 4)
- **Inventory Item**: Enchanted ferns with BD metadata when harvested
- **Base Value**: 5 emeralds + 100 server currency per crop (as configured in economy.yml)

## Growth Mechanics

### Visual Growth Stages
All BD crop types follow identical visual growth patterns:
- **Stages 1-3**: Grow as melon stems (identical appearance regardless of crop type)
- **Stage 4**: Transform into their respective final forms (ferns, double ferns, double ferns with purple particles)

### Growth Requirements
- **Light Level**: Minimum level 9 required
- **Farmland**: Must be planted on tilled farmland
- **Water**: Water source within 4 blocks required
- **Bone Meal**: Does not work on BD crops
- **Biome**: Growth rate unaffected by biome type

### Growth Acceleration
- **Market Bonus**: 5% faster growth per market level (maximum 25% at level 5)
- **Outside Markets**: No growth bonuses apply
- **Rank Bonuses**: Expert Farmer (5%), Master Farmer (15%), Agricultural Expert (25%)

## Harvesting System

### Harvesting Methods
- **Manual Harvesting**: 1 crop per plant (standard)
- **BD Harvester**: 2 crops per plant (16 diamonds from Expert Farmer+ dealers)
- **Ultimate BD Harvester**: 3 crops per plant (32 diamonds from Agricultural Expert dealers)

### Yield Bonuses
Harvesting yields are enhanced by various factors:
- **Rank Multipliers**: 5% (Farmer) to 30% (Agricultural Expert)
- **Rebirth Bonuses**: +5% per rebirth level (maximum +25%)
- **Donor Benefits**: +10% boost to all rank bonuses
- **Market Level**: Additional trading value bonuses when selling to collectors

## Trading and Economics

### Collector Trading
BD crops can only be sold to Collector villagers within market areas:
- **Emerald Payment**: Physical emeralds added to inventory
- **Server Currency**: Digital currency automatically added to balance
- **Market Bonuses**: Better prices in higher-level markets
- **Reputation**: Trading increases village reputation for better future prices

### Prohibited Sales
- **Auction House**: BD crops cannot be listed in auction house
- **Player Trading**: Direct player-to-player crop sales not supported
- **Regular Villagers**: Only BD Collectors will purchase BD crops
- **Metadata Required**: Only crops with proper BD metadata are accepted by collectors

### Value Calculations
Final crop values are calculated using multiple modifiers:
```
Final Value = Base Value × (1 + Rank_Bonus + Rebirth_Bonus + Market_Level_Bonus + Reputation_Bonus)
```

## Crop Quality System

### Rare Crop Generation
Higher ranks have increased chances of generating higher-tier crops:
- **Master Farmer**: 15% chance to get higher-tier crop than planted
- **Agricultural Expert**: 25% chance to get higher-tier crop than planted
- **Example**: Planting Regular BD Seeds might yield Green BD Crops

### Quality Factors
- **Rank Level**: Higher ranks = better quality chances
- **Market Environment**: Crops grown in markets have enhanced quality potential
- **Tool Usage**: BD Harvesters may influence quality generation
- **Random Events**: Seasonal events can temporarily boost quality rates

## Seed Acquisition

### BD Dealers
Primary source for all BD seeds:
- **Regular Seeds**: 5 seeds for 1 emerald (all ranks)
- **Green Seeds**: 15 emeralds per batch (Farmer+ only)
- **Purple Seeds**: 30 emeralds per batch (Master Farmer+ only)

### Natural Spawning
BD Dealers appear in natural Minecraft villages:
- **Spawn Rate**: 45% chance in any village
- **Profession**: FARMER villager type
- **Identification**: Green "BD Dealer" name tag

### Market Creation
Every player-created market automatically spawns a BD Dealer:
- **Guaranteed Spawn**: Always created with Market Owner
- **Building Confined**: Cannot leave the market stall structure
- **Full Inventory**: Stocks all seed types appropriate for player rank

## Cultivation Tips

### Optimal Planting
- **Market Boundaries**: Always plant within market areas for growth bonuses
- **Water Efficiency**: Design farms with efficient water placement
- **Light Management**: Ensure adequate lighting for consistent growth
- **Space Planning**: Allow room for crop expansion and harvesting

### Progression Strategy
- **Start Small**: Begin with Regular BD Crops to build initial capital
- **Reinvest Profits**: Use emerald profits to buy more seeds
- **Save Currency**: Accumulate server currency for rank purchases
- **Market Investment**: Upgrade markets to improve growth rates and trading bonuses

### Advanced Techniques
- **Crop Rotation**: Plant different crop types based on availability and market demand
- **Timing Management**: Stagger planting times for consistent harvesting schedules
- **Tool Investment**: Prioritize BD Harvester purchases for doubled yields
- **Community Trading**: Coordinate with other players for seed sharing and market access

## Seasonal Considerations

### Seasonal Traders
Special seasonal variations may be available during trader visits:
- **Unique Variants**: Special themed crop types during seasons
- **Limited Availability**: Only available during specific seasonal trader appearances
- **Enhanced Values**: Seasonal crops often provide bonus currency or unique benefits

### Event Crops
During server events, special crop types may be introduced:
- **Event Seeds**: Available only during specific events
- **Bonus Multipliers**: Enhanced values during event periods
- **Exclusive Items**: Special crops that can't be obtained otherwise

---

BD Crops form the economic backbone of BDCraft, providing endless opportunities for growth, trade, and progression through strategic cultivation and market participation.