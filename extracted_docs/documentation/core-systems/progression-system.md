# BD Progression System

The BDCraft progression system provides players with a comprehensive advancement path through the plugin's features. This system combines ranks, rebirth levels, reputation, and special abilities to create a long-term engagement structure.

## System Overview

The BDCraft progression consists of several interconnected systems:

1. **Rank System**: 5 ranks that unlock new seeds, tools, and benefits
2. **Rebirth System**: Prestige system that adds permanent multipliers
3. **Reputation System**: Village-specific standing that affects trading
4. **Achievement System**: In-game recognition for BD-related accomplishments

## Rank Progression

The rank system is the primary progression path for players:

### Rank 1: Newcomer

- **Starting Point**: Default rank for all players
- **Access To**: Regular BD Seeds, BD Stick crafting
- **Trading Capabilities**: Basic villager trading
- **Cost**: None (default rank)

### Rank 2: Farmer

- **Access To**: Green BD Seeds + all Newcomer features
- **Benefits**: 10% increased crop value, slightly faster reputation gain
- **Cost**: 5,000 server currency
- **Command**: `/bdrank up`

### Rank 3: Expert Farmer

- **Access To**: BD Harvester + all Farmer features
- **Benefits**: 25% increased crop value, faster reputation gain, 5% chance for double seeds when purchasing
- **Cost**: 15,000 server currency
- **Command**: `/bdrank up`

### Rank 4: Master Farmer

- **Access To**: Purple BD Seeds + all Expert Farmer features
- **Benefits**: 40% increased crop value, much faster reputation gain, 10% chance for double seeds
- **Cost**: 30,000 server currency
- **Command**: `/bdrank up`

### Rank 5: Agricultural Expert

- **Access To**: Ultimate BD Harvester + all Master Farmer features
- **Benefits**: 60% increased crop value, maximum reputation gain, 15% chance for double seeds
- **Cost**: 60,000 server currency
- **Command**: `/bdrank up`

## Advancement Strategy

The optimal progression strategy follows these steps:

1. **Early Game (Newcomer)**:
   - Focus on farming regular BD crops
   - Sell to collectors to earn server currency
   - Save up for the Farmer rank

2. **Mid Game (Farmer & Expert Farmer)**:
   - Start growing green BD seeds
   - Invest in a BD Harvester as soon as possible
   - Build reputation in a main village

3. **Late Game (Master Farmer & Agricultural Expert)**:
   - Focus on purple BD crops for maximum profit
   - Utilize the Ultimate BD Harvester
   - Prepare for rebirth

## Rebirth System

Once players reach Agricultural Expert rank, they can choose to rebirth:

### Rebirth Requirements

- Agricultural Expert rank (Rank 5)
- 100,000 server currency accumulated
- 500 completed trades with BD villagers

### Rebirth Benefits

Each rebirth level provides:

- Permanent +10% crop value multiplier (stacks with each level)
- Special cosmetic effects
- Unique gameplay advantages

### Rebirth Progression

| Rebirth Level | Permanent Bonus | Special Effect | Gameplay Advantage |
|---------------|----------------|----------------|--------------------|
| 1 | +10% crop value | Gold name prefix | +5% chance for double harvest |
| 2 | +20% crop value | Gold particles when farming | Retained benefit from level 1 |
| 3 | +30% crop value | Enhanced gold particles | +10% chance for double harvest |
| 5 | +50% crop value | Green tint and aura effect | +5% reputation gain |
| 7 | +70% crop value | Rainbow-cycling name | +10% chance to pay 25% less for seeds |
| 10 | +100% crop value | "Agricultural Deity" title | Special deity abilities |

### Agricultural Deity Status

After 10 rebirths, players achieve the ultimate "Agricultural Deity" status with special abilities:

- **Golden Touch**: 25% chance to convert regular seeds to green seeds when harvesting
- **Harvester's Blessing**: 50% chance tool usage doesn't consume durability
- **Divine Favor**: 25% better trades with all villagers
- **Seasonal Insight**: Preview next day's seasonal trader items
- **Abundance Aura**: Nearby players get +10% crop value (20 block radius)

## Reputation System

Reputation is a village-specific progression system:

### Reputation Tiers

- **Hostile** (-100 to -50): +50% prices when buying, -50% value when selling
- **Unfriendly** (-49 to -10): +25% prices when buying, -25% value when selling
- **Neutral** (-9 to 9): Standard prices
- **Friendly** (10 to 49): -10% prices when buying, +10% value when selling
- **Hero** (50 to 100): -20% prices when buying, +20% value when selling

### Building Reputation

- Each successful trade increases reputation based on villager type:
  - Dealers: +2 reputation
  - Collectors: +3 reputation
  - Seasonal Traders: +4 reputation

- Rank bonuses add extra reputation per trade:
  - Farmer: +0.5 extra
  - Expert Farmer: +1.0 extra
  - Master Farmer: +1.5 extra
  - Agricultural Expert: +2.0 extra

## Achievement System

The plugin includes in-game achievements for progression milestones:

### Farming Achievements

- **First Harvest**: Harvest your first BD crop
- **Green Thumb**: Harvest 100 BD crops
- **Farming Expert**: Harvest 1,000 BD crops
- **Master Cultivator**: Harvest 10,000 BD crops
- **Agricultural Legend**: Harvest 100,000 BD crops

### Trading Achievements

- **First Deal**: Complete your first trade with a BD villager
- **Trade Network**: Trade with 10 different BD villagers
- **Business Partner**: Complete 100 trades with BD villagers
- **Trade Magnate**: Complete 1,000 trades with BD villagers
- **Economic Force**: Complete 10,000 trades with BD villagers

### Special Achievements

- **Tool Master**: Craft your first BD Stick
- **Premium Harvester**: Acquire a BD Harvester
- **Ultimate Harvester**: Acquire an Ultimate BD Harvester
- **Purple Pioneer**: Grow your first Purple BD crop
- **Village Hero**: Reach Hero status in any village
- **Multi-Village Hero**: Reach Hero status in 5 different villages
- **Rebirth**: Complete your first rebirth
- **Deity Status**: Reach Agricultural Deity status (10 rebirths)

## Leaderboards

Players can view various leaderboards to track progression:

- **Economy Leaderboard**: `/bdtop economy` - Top players by currency
- **Rank Leaderboard**: `/bdtop rank` - Players at each rank
- **Rebirth Leaderboard**: `/bdtop rebirth` - Top players by rebirth level
- **Crops Leaderboard**: `/bdtop crops` - Most crops harvested
- **Trades Leaderboard**: `/bdtop trades` - Most trades completed

## GUI Interfaces

The plugin provides GUI interfaces to visualize progression:

### Rank GUI

- Access with: `/bdrank`
- Shows current rank and progress
- Displays benefits of current rank
- Shows requirements for next rank
- Provides upgrade button if eligible

### Rebirth GUI

- Access with: `/bdrebirth`
- Shows current rebirth level and benefits
- Displays requirements for rebirth
- Shows rebirth confirmation if eligible

### Statistics GUI

- Access with: `/bdstats`
- Shows overall BD statistics
- Displays achievements earned and locked
- Tracks progression metrics

## Custom Events and Challenges

Server administrators can create special events to enhance progression:

### Seasonal Challenges

- **Harvest Festival**: Double crop yields for a limited time
- **Trading Bonanza**: Extra reputation from trades
- **Market Crash**: Sudden change in crop values

### Progression Boosters

- **Education Grant**: One-time currency grant to new players
- **Catch-up Mechanics**: Temporary bonuses for lower-rank players
- **BD Development Grant**: Server-wide economic stimulus

## Cross-Player Progression Features

The progression system includes features that encourage player interaction:

### Sponsorship System

- Agricultural Experts can sponsor newer players
- Command: `/bdsponsor <player>`
- Effect: Sponsored player gets +15% crop value for 2 hours
- Cooldown: 6 hours between sponsorships
- Maximum: 3 players sponsored at once

### Deity Blessings

- Agricultural Deities can bless other players
- Command: `/bdbless <player>`
- Effect: Blessed player gets +25% crop value for 1 hour
- Cooldown: 24 hours between blessings per player

## Technical Implementation

### Data Storage

Progression data is stored in several files:

- **Player Ranks**: In economy data file
- **Rebirth Levels**: In economy data file
- **Reputation**: In village reputation file
- **Achievements**: In player achievement file

### Commands and Permissions

#### Player Commands

- `/bdrank` - View current rank (Permission: `bdcraft.rank.view`)
- `/bdrank up` - Upgrade to next rank (Permission: `bdcraft.rank.up`)
- `/bdrebirth` - View rebirth information (Permission: `bdcraft.rebirth.view`)
- `/bdrebirth check` - Check eligibility (Permission: `bdcraft.rebirth.check`)
- `/bdrebirth confirm` - Confirm rebirth (Permission: `bdcraft.rebirth.use`)
- `/bdrep` - Check reputation (Permission: `bdcraft.reputation`)
- `/bdstats` - View statistics and achievements (Permission: `bdcraft.stats`)

#### Admin Commands

- `/bdadmin rank set <player> <rank>` - Set player rank (Permission: `bdcraft.admin.rank`)
- `/bdadmin rebirth <player> <level>` - Set rebirth level (Permission: `bdcraft.admin.rebirth`)
- `/bdadmin rep set <player> <village> <value>` - Set reputation (Permission: `bdcraft.admin.reputation`)

## Best Practices for Server Owners

1. **Balance Economy**: Ensure currency flow is balanced to maintain progression pace
2. **Monitor Progression**: Check how quickly players advance through ranks
3. **Adjust Settings**: Fine-tune rank costs and benefits for server population
4. **Create Events**: Implement special events to boost engagement
5. **Gather Feedback**: Listen to player feedback about progression pacing

The BDCraft progression system provides a comprehensive advancement path that keeps players engaged long-term. From newcomers just starting out to agricultural deities at the height of power, there's always something to work toward in the BD economy.