# BD Economy System Overview

The BDCraft economy is built around a unique farming and trading system that creates a complete economic loop for players. This system is the core of the plugin, allowing players to grow special crops, trade with villagers, and progress through ranks.

## Economic Flow

1. **Player Markets**: Players build and establish their own BD market to access the economy
   - Markets are player-created trading centers where all BD economy activity takes place
   - Each market requires BD Market Tokens to establish

2. **Market Villagers**: Each market contains specialized villagers:
   - **Dealers**: FARMER profession villagers with green name tags who sell seeds
   - **Collectors**: LIBRARIAN profession villagers with blue name tags who buy crops

3. **Acquisition of BD Seeds**: Purchase seeds from dealers in player markets

4. **Farming**: Plant and grow seeds into BD crops within player markets for maximum efficiency

5. **Harvesting**: Collect BD crops when plants are mature (appear as Ferns and Large Ferns)

6. **Trading**: Sell BD crops to collectors for emeralds + server currency

7. **Crafting BD Sticks**: Create BD Sticks (Paper + BD crop + Flint) to expand market capabilities

8. **Market Development**: Upgrade markets using BD Market Tokens crafted from BD sticks

9. **Progression**: Use server currency to purchase ranks, use emeralds to buy more seeds

## Currency Types

### In-Game Items as Currency
- **Emeralds**: Primary tradeable currency, used for buying seeds and tools from dealers
- **Diamonds**: Premium currency for special tools (BD Harvester and Ultimate BD Harvester)

### Plugin Economy
- **Server Currency**: Earned from trading with collectors, used for purchasing ranks

## Rank Progression

Players can spend server currency to purchase ranks that enhance their BD farming:

1. **Newcomer** (Default)
   - Access to: Regular BD seeds, BD Stick crafting

2. **Farmer** (Rank 2)
   - Unlocks: Green BD seed purchases
   - Cost: 5,000 server currency
   - Benefit: 10% increased BD crop value

3. **Expert Farmer** (Rank 3)
   - Unlocks: BD Harvester purchase
   - Cost: 15,000 server currency
   - Benefit: 25% increased BD crop value

4. **Master Farmer** (Rank 4)
   - Unlocks: Purple BD seed purchases
   - Cost: 30,000 server currency
   - Benefit: 40% increased BD crop value

5. **Agricultural Expert** (Rank 5)
   - Unlocks: Ultimate BD Harvester purchase
   - Cost: 60,000 server currency
   - Benefit: 60% increased BD crop value

## Rebirth System

After reaching a currency cap, players can choose to rebirth, which:
- Resets their rank to Newcomer
- Removes all their server currency
- Provides a permanent multiplier to BD crop values
- Grants special cosmetic benefits

After 10 rebirths, players become an "Agricultural Deity" with special abilities.

## Village Reputation System

The village reputation system affects player interactions with BD villagers:

- Higher reputation = Better prices from villagers
- Reputation is village-specific
- Successful trades increase reputation
- Failed trades decrease reputation
- Different villager types give different reputation gains:
  - Dealers: +2 reputation
  - Collectors: +3 reputation
  - Seasonal: +4 reputation

## Player Markets

Player markets are the foundation of the BD economy system, providing the only way for players to access BD villagers and trading. Markets allow players to:

- Build and manage their own BD trading village/market
- Create a market using BD Market Tokens (crafted from BD sticks)
- Spawn dealers to purchase seeds from
- Build collector houses using House Tokens
- Control a farming area up to 49x49 blocks (expandable with upgrades)
- Upgrade their market to increase capacity and functionality

To establish a market, players must:
1. Craft a BD Market Token
2. Build a proper structure with walls, roof, bed, and a door
3. Place the token in an item frame above the door
4. Complete the market activation process with the `/bdmarket create` command

Markets must be at least 30 blocks apart, and players can only own one market until reaching higher ranks.

## Price Dynamics

The BDCraft economy includes dynamic pricing factors:

- Market fluctuations over time
- Village reputation modifiers
- Rank modifiers
- Rebirth multipliers
- Market upgrade level benefits

This creates a rich economic system with multiple paths for progression and encourages player interaction with all aspects of the plugin. Players can focus on creating efficient markets, farming rare crops, or advancing through the rank and rebirth systems.