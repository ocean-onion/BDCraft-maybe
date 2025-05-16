# Auction System

The BDCraft Auction System provides a server-wide platform for players to sell items through a bidding process.

## Overview

The Auction System offers:
- Time-limited item auctions
- Bidding functionality
- Instant buyout options
- Auction history tracking
- Featured auction slots

## Creating an Auction

Players with the appropriate permissions can create auctions:

1. Hold the item you want to auction in your main hand
2. Use `/auction create <starting-bid> <duration>` 
3. Optionally set a buyout price with `/auction create <starting-bid> <duration> <buyout>`
4. The item will be removed from your inventory and placed in the auction

## Bidding on Auctions

To bid on an auction:

1. View available auctions with `/auction` or `/ah`
2. Find an auction you're interested in
3. Place a bid using `/auction bid <id> <amount>`
4. If you're outbid, you'll receive a notification
5. If you win, the item will be delivered to your inventory

## Buyout

For auctions with a buyout option:

1. View the auction details
2. If a buyout price is set, you can use `/auction buyout <id>`
3. The auction will immediately end and you'll receive the item

## Featured Auctions

Players with the appropriate permissions can create featured auctions:

1. Create an auction normally
2. Use `/auction featured <id>` to highlight it
3. Featured auctions appear prominently in the auction house

## Auction Management

Auction creators can:
- Cancel their own auctions before any bids using `/auction cancel <id>`
- View their auction history with `/auction history`
- See the status of their active auctions

Administrators can:
- Cancel any auction with `/auction admin cancel <id>`
- Manage the auction system with `/auction admin`

## Permissions

| Permission | Description |
|------------|-------------|
| `bdcraft.auction.use` | Allow basic auction usage |
| `bdcraft.auction.create` | Allow creating auctions |
| `bdcraft.auction.featured` | Allow creating featured auctions |
| `bdcraft.auction.admin` | Administrative auction commands |

## Commands

See the [Commands Reference](../commands.md#auction-commands) for a complete list of auction-related commands.