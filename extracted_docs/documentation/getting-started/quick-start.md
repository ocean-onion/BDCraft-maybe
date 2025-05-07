# BDCraft Quick Start Guide

This guide will help you quickly set up and start using the BDCraft plugin on your Minecraft server. Follow these steps to get the basic BD economy system functioning.

## Prerequisites

Before beginning:

- The BDCraft plugin should be [installed](./installation.md)
- You should have operator status on the server
- Vault should be installed for economy functions

## Initial Setup Checklist

1. Verify plugin installation
2. Set up essential permissions
3. Configure the market system settings
4. Help players establish their first markets
5. Test the BD farming and market system

## Step 1: Verify Plugin Installation

1. Log in to your Minecraft server
2. Run the command: `/bdcraft version`
3. You should see the current plugin version displayed

If this command fails, check the [installation guide](./installation.md) again.

## Step 2: Set Up Essential Permissions

Set up basic permissions for your players:

1. Grant basic player permissions:
   - Using BDPerms: `/bdperms group default add bdcraft.use`
   - For regular Ops: Already granted by default

2. Grant admin permissions to yourself and other admins:
   - Using BDPerms: `/bdperms user <username> add bdcraft.admin`
   - For regular Ops: Already granted by default

3. Grant market creation permissions to players:
   - Using BDPerms: `/bdperms group default add bdcraft.market.create`
   - For regular players: Already granted with bdcraft.use

## Step 3: Configure Market System Settings

Configure the market system which is the foundation of the BD economy:

1. Adjust market settings in config.yml:
   ```yaml
   markets:
     enabled: true
     min-distance: 30  # Minimum distance between markets (blocks)
     max-per-player: 1  # Maximum markets per player (increases with rank)
     token-craft-enabled: true  # Allow players to craft market tokens
   ```

2. Configure villager settings in economy.yml:
   ```yaml
   villagers:
     dealer:
       enabled: true
       spawn-delay: 60  # Seconds after market creation until dealer spawns
     collector:
       enabled: true
       spawn-delay: 120  # Seconds after market creation until collector spawns
   ```

3. Run the command: `/bdadmin reload` to apply the configuration

You can manually spawn villagers in player markets using admin commands:
- `/bdadmin market adddealer <market_id>`
- `/bdadmin market addcollector <market_id>`
- `/bdadmin market addseasonal <market_id>`

## Step 4: Help Players Establish Their First Markets

Guide your players in creating their first markets:

1. Provide initial resources for market creation:
   - `/bdadmin give <player> bdstarter` gives a starter kit with essential BD items 
   - `/bdadmin give <player> bdmarkettoken` gives a market token directly

2. Show players how to create a market structure:
   - Build a structure with walls, roof, bed, and a door
   - Place the BD Market Token in an item frame above the door
   - The market will be automatically created when the token is placed

3. Guide them through the activation process:
   - After activation, a dealer villager will spawn inside their market
   - Players can then purchase seeds from this dealer

## Step 5: Market System Testing

Test the full cycle of BD economy using player markets:

1. Create a market as described in Step 4

2. Purchase BD seeds from the dealer in the market
   - Regular Seeds (represented by Wheat Seeds)
   - Green Seeds (represented by Beetroot Seeds) - requires Farmer rank
   - Purple Seeds (represented by Pumpkin Seeds) - requires Master Farmer rank

3. Plant the seeds within the market's farming area for maximum efficiency

4. Wait for the crops to grow and harvest them (appear as Ferns or Large Ferns)

5. Sell the crops to the collector in the market for emeralds + server currency

6. Craft BD Sticks using the harvested crops (Paper + BD crop + Flint, has 5 uses)

7. Use server currency to purchase higher ranks, unlocking better seeds and tools

Players now have their own market, which provides a 49x49 block area where they can grow crops more efficiently. Markets must be at least 30 blocks from other markets and can be upgraded as players progress.

## Basic Admin Commands

### Market Management

- List markets: `/bdadmin market list`
- Delete a market: `/bdadmin market delete <id>`
- Teleport to market: `/bdadmin market tp <id>`
- Add villager: `/bdadmin market adddealer <id>`
- Reset market: `/bdadmin market reset <id>`

### Villager Management

- List villagers in market: `/bdadmin villager list <market_id>`
- Remove a villager: `/bdadmin villager remove <uuid>`
- Restock villager: `/bdadmin villager restock <uuid>`

### Economy Management

- Give server currency: `/bdadmin economy give <player> <amount>`
- Check plugin economy status: `/bdadmin economy status`

### Item Management

- Give BD items: `/bdadmin give <player> <item> [amount]`
  - Example: `/bdadmin give Steve bdseed 10`

## Basic Player Commands

### Economy Commands
- Check balance: `/bdbal`
- Check rank: `/bdrank`
- View reputation: `/bdrep`
- Purchase rank: `/bdrank buy <rank_name>`

### Market Commands
- List markets: `/bdmarket list`
- Teleport to market: `/bdmarket tp <id>` (owner only)
- Add member: `/bdmarket addmember <player>` (owner only)
- Market info: `/bdmarket info <id>`
- Upgrade market: `/bdmarket upgrade` (requires currency and higher rank)

## Configuration Basics

The main configuration files are in the `plugins/BDCraft/` directory:

- `config.yml` - Core settings
- `economy.yml` - Economy settings
- `messages.yml` - Customizable messages

After editing configuration files, use `/bdadmin reload` to apply changes without restarting the server.

## Next Steps

After completing the quick start setup:

1. Review the [Configuration Basics](./configuration.md) guide to tailor settings to your server
2. Learn about the [Player Markets](../player-features/player-markets.md) system
3. Explore the [BD Economy Overview](../core-systems/economy-overview.md) to understand the complete system
4. Check the [Admin Commands](../administration/admin-commands.md) reference for detailed administration options

## Getting Help

If you encounter any issues during setup:

- Use `/bdadmin debug on` to enable debug mode for detailed logs
- Check the server console for error messages
- Reference the [Troubleshooting Guide](../technical/troubleshooting.md)
- Join our Discord community for support

Congratulations! You now have a basic BDCraft economy set up on your server.