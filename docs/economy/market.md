# Market System

The BDCraft Market System allows players to create and manage physical market locations in the game world where they can sell items to other players.

## Overview

The Market System provides:
- Physical market locations where players can sell items
- Custom pricing for each item
- Market teleportation capabilities
- Market ownership and permissions
- Market listing and discovery

## Creating a Market

Players with the appropriate permissions can create a market at their current location:

1. Find a suitable location for your market
2. Use the `/bdmarket create` command
3. Set up signs and chests for item display and storage
4. Configure prices for your items

## Market Management

Market owners can:
- Set prices for items in their market
- Remove their market when no longer needed
- See statistics about their market's performance
- Control who can access specific features of their market

## Finding Markets

Players can:
- List all available markets using `/bdmarket list`
- Teleport to a market using `/bdmarket tp <name>`
- Get information about a market using `/bdmarket info`

## Market Items

Markets deal with physical items in the Minecraft world:
- Items are displayed in the market area using item frames, signs, or containers
- Prices are set using signs or through commands
- Transactions occur when players interact with the market elements

## Permissions

| Permission | Description |
|------------|-------------|
| `bdcraft.market.use` | Allow basic market usage |
| `bdcraft.market.create` | Allow creating markets |
| `bdcraft.market.admin` | Administrative market commands |

## Commands

See the [Commands Reference](../commands.md#physical-market-commands) for a complete list of market-related commands.