# Rebirth Mechanics

The BDCraft rebirth system offers endgame progression for dedicated players who have reached the highest rank in the regular progression system. This feature adds longevity to the plugin by providing permanent benefits for players willing to reset their progress.

## What is Rebirth?

Rebirth is a prestige system that allows players to reset their progress in exchange for permanent benefits. When a player rebirths:

- Their rank resets to Newcomer (Rank 1)
- All server currency is removed from their account
- They receive a permanent multiplier to crop values
- They gain access to special cosmetic features
- Their rebirth level increases by one

## Rebirth Requirements

To qualify for rebirth, a player must:

1. Reach the Agricultural Expert rank (Rank 5)
2. Accumulate at least 100,000 server currency
3. Have completed at least 500 trades with BD villagers

## How to Rebirth

1. Check eligibility with `/bdrebirth check`
2. If eligible, use `/bdrebirth confirm` to initiate the rebirth process
3. A confirmation dialog will appear - type `CONFIRM` to proceed
4. The rebirth process will reset progress and apply benefits

## Rebirth Benefits

### Permanent Multipliers

Each rebirth level provides a permanent +10% crop value multiplier:

| Rebirth Level | Permanent Crop Value Multiplier |
|---------------|--------------------------------|
| 1 | +10% |
| 2 | +20% |
| 3 | +30% |
| ... | ... |
| 10 | +100% |

These multipliers stack with rank bonuses, creating significant economic advantages.

### Cosmetic Benefits

Rebirth levels grant cosmetic benefits that increase with each level:

- **Rebirth 1**: Gold name prefix `[R1]`
- **Rebirth 2**: Gold name prefix with gold particles when farming
- **Rebirth 3**: Gold name prefix with enhanced gold particles
- **Rebirth 5**: Gold name prefix with green tint and special aura effect
- **Rebirth 7**: Rainbow-cycling name prefix and enhanced particle effects
- **Rebirth 10**: Special "Agricultural Deity" title with ultimate effects

### Gameplay Benefits

In addition to crop value multipliers, rebirth levels provide gameplay advantages:

- **Rebirth 1**: +5% chance for double harvest
- **Rebirth 3**: +10% chance for double harvest
- **Rebirth 5**: +5% reputation gain with villagers
- **Rebirth 7**: +10% chance to pay 25% less for seeds
- **Rebirth 10**: All of the above + special abilities

## Agricultural Deity Status (Rebirth 10)

Players who achieve 10 rebirths reach the ultimate "Agricultural Deity" status, unlocking:

### Deity Abilities

- **Golden Touch**: 25% chance to convert regular seeds into green seeds when harvesting
- **Harvester's Blessing**: Special tool usage doesn't consume durability (50% chance)
- **Divine Favor**: +25% better trades with all villagers
- **Seasonal Insight**: Can see the next seasonal trader items a day in advance
- **Abundance Aura**: Nearby players get +10% crop value (20 block radius)

### Deity Commands

- `/bdbless <player>`: Gives target player temporary trading boost (once per day)
- `/bdpredict`: Shows upcoming seasonal trader stock
- `/bdaura`: Toggles abundance aura visibility

## Rebirth Economy Integration

The rebirth system integrates with the broader BD economy in several ways:

- **Balanced Progression**: Each rebirth makes reaching rank 5 again easier, but still requires effort
- **Trading Advantage**: Higher rebirth levels make trading more profitable
- **Special Recognition**: Rebirth status visible to other players via prefixes
- **Auction House Impact**: Higher rebirth players can see auction listings earlier

## Rebirth Leaderboard

Players can view the server's rebirth leaderboard:

- Command: `/bdtop rebirth`
- Shows top 10 players by rebirth level
- Breaks ties using total crops harvested

## Commands

- `/bdrebirth` - Shows general rebirth information
- `/bdrebirth check` - Checks eligibility for rebirth
- `/bdrebirth confirm` - Initiates the rebirth process (requires confirmation)
- `/bdrebirth stats [player]` - Shows rebirth statistics for yourself or another player
- `/bdtop rebirth` - Displays the rebirth leaderboard

The rebirth system adds depth to the BDCraft economy by providing long-term goals for dedicated players, ensuring continued engagement with the plugin's features long after reaching the highest rank.