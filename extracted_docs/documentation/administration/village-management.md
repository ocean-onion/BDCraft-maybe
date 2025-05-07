# Village Management

BD Villages are central to the BDCraft economy system, serving as hubs for BD villagers and player trading. This guide covers how to create, configure, and manage BD villages on your server.

## Village Basics

### What is a BD Village?

A BD Village is a designated area that:

- Serves as a hub for BD economy activities
- Hosts BD villagers (Dealers, Collectors, and Seasonal Traders)
- Tracks player reputation independently from other villages
- Creates economic diversity across your server

### Village Properties

Each village has the following properties:

- **Village ID**: Unique identifier (e.g., "farming_village1")
- **Center Location**: The central coordinates of the village
- **Radius**: The effective area of the village (configurable in config.yml)
- **Villagers**: Associated BD villagers
- **Player Reputations**: Individual player reputation scores

## Village Management Commands

### Creating Villages

```
/bdadmin village create <id>
```

**Example**:
```
/bdadmin village create farming_village1
```

This creates a new village with the specified ID at your current location, which becomes the village center.

### Setting Village Center

```
/bdadmin village setcenter <id>
```

**Example**:
```
/bdadmin village setcenter farming_village1
```

This updates the center of an existing village to your current location.

### Listing Villages

```
/bdadmin village list
```

Displays all registered BD villages with their IDs, center coordinates, and villager counts.

### Deleting Villages

```
/bdadmin village delete <id>
```

**Example**:
```
/bdadmin village delete farming_village1
```

This removes the specified village and all associated data. Note that this does not remove the villager entities, which must be removed separately.

## Villager Management

### Spawning Villagers

Each village can host different types of BD villagers:

#### BD Dealers

```
/bdadmin spawn dealer <village_id>
```

**Example**:
```
/bdadmin spawn dealer farming_village1
```

**Note**: Dealers can only be spawned in every third village due to design constraints.

#### BD Collectors

```
/bdadmin spawn collector <village_id>
```

**Example**:
```
/bdadmin spawn collector farming_village1
```

Each village can have up to 4 collectors.

#### Seasonal BD Traders

```
/bdadmin spawn seasonal <village_id>
```

**Example**:
```
/bdadmin spawn seasonal farming_village1
```

Seasonal traders are rare and offer limited-time trades.

### Managing Existing Villagers

#### Listing Villagers

```
/bdadmin villager list <village_id>
```

**Example**:
```
/bdadmin villager list farming_village1
```

Shows all BD villagers in the specified village with their types and UUID identifiers.

#### Removing Villagers

```
/bdadmin villager remove <uuid>
```

**Example**:
```
/bdadmin villager remove 550e8400-e29b-41d4-a716-446655440000
```

Removes a specific villager by UUID. Get the UUID from the villager list command.

## Village Configuration

### Global Settings

These settings are defined in `config.yml`:

```yaml
# Village Settings
village-radius: 50
max-villagers-per-village: 8
dealer-village-ratio: 3
village-discovery-range: 100
```

- **village-radius**: The effective radius of a village in blocks
- **max-villagers-per-village**: Maximum number of BD villagers per village
- **dealer-village-ratio**: Controls dealer distribution (1 dealer per X villages)
- **village-discovery-range**: How close a player must be to discover a village

### Village Distribution Strategy

For balanced gameplay, consider these distribution strategies:

1. **Economic Zones**: Create villages in different biomes with different specializations
2. **Trade Networks**: Space villages to encourage player travel and exploration
3. **Population Density**: Place more villages near player-dense areas

### Recommended Village Setup

A balanced server typically has:

- 1 village per 5-10 regular players
- Villages spaced 500-1000 blocks apart
- A mix of dealer and non-dealer villages
- Villages in a variety of biomes and locations

## Reputation System Management

Each village tracks player reputation independently:

### Viewing Reputation

Admins can view player reputation in a village:

```
/bdadmin rep view <player> <village_id>
```

**Example**:
```
/bdadmin rep view Steve farming_village1
```

### Setting Reputation

Admins can set player reputation in a village:

```
/bdadmin rep set <player> <village_id> <value>
```

**Example**:
```
/bdadmin rep set Steve farming_village1 50
```

Sets Steve's reputation in farming_village1 to 50 (Hero status).

### Reputation Tiers

Reputation affects trading with villagers:

- **Hostile** (-100 to -50): +50% prices when buying, -50% value when selling
- **Unfriendly** (-49 to -10): +25% prices when buying, -25% value when selling
- **Neutral** (-9 to 9): Standard prices
- **Friendly** (10 to 49): -10% prices when buying, +10% value when selling
- **Hero** (50 to 100): -20% prices when buying, +20% value when selling



## Technical Details

### Village Data Storage

Village data is stored in:

```
plugins/BDCraft/data/villages.json
```

This file contains all village definitions, center coordinates, and associated villagers.

### Reputation Data Storage

Reputation data is stored in:

```
plugins/BDCraft/data/reputation.json
```

This file contains all player reputation values for each village.

### Automated Backups

Village data is automatically backed up daily to:

```
plugins/BDCraft/backups/villages_YYYY-MM-DD.json
```

## Troubleshooting

### Common Issues

1. **Dealer Won't Spawn**:
   - Check if the village is eligible (every third village)
   - Use `/bdadmin village dealer-eligible` to check eligibility

2. **Villagers Disappearing**:
   - Ensure villagers are properly registered to the village
   - Make sure the village radius covers the villager location
   - Check if the villager was killed by a player or mob

3. **Village Not Recognized**:
   - Verify the village ID is correct (case-sensitive)
   - Check if the village was accidentally deleted
   - Ensure the village data file is not corrupted

### Fixing Corrupted Villages

If a village becomes corrupted:

1. Use `/bdadmin village repair <id>` to attempt automatic repair
2. If that fails, delete and recreate the village
3. Spawn new villagers in the recreated village

## Best Practices

1. **Regular Backups**: Regularly back up village data files
2. **Consistent Naming**: Use a consistent naming convention for villages
3. **Documentation**: Keep a server map or document of village locations
4. **Periodic Checks**: Regularly check villages to ensure villagers are present
5. **Player Input**: Consider player feedback for village placements

Effective village management is essential for a healthy BD economy. By strategically placing villages and managing their villagers, you create a balanced and engaging economic environment for your players.