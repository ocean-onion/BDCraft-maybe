# Frequently Asked Questions

## General Questions

### What is BDCraft?
BDCraft is a comprehensive Minecraft plugin that provides a complete economic and progression system for your server. It includes features such as player-built markets, custom villagers, an auction house, rank progression, and essential utilities like teleportation and chat management.

### What server versions is BDCraft compatible with?
BDCraft is designed for Paper 1.21.x servers. It is not compatible with older versions or other server implementations like Spigot or Bukkit.

### Does BDCraft require any other plugins?
No. BDCraft is designed to be completely self-contained and does not require any other plugins to function. In fact, it will block competing plugins to prevent conflicts and ensure stability.

### Can I use BDCraft alongside other plugins?
Yes, as long as they don't conflict with BDCraft's features. Plugins that modify economy, teleportation, chat, or villager behavior may conflict and be blocked by BDCraft. However, plugins that add completely different features (like WorldEdit, CoreProtect, etc.) should work fine.

### How do I update BDCraft?
Simply replace the BDCraft.jar file with the new version and restart your server. The plugin will handle any necessary data migrations automatically.

## Economy System

### How do players earn money in BDCraft?
Players can earn money through various activities:
- Selling crops to collector villagers in markets
- Selling items to other players through the auction house
- Receiving payments from other players

### Can I customise the currency name and symbol?
Yes, you can customise the currency name, plural form, and symbol in the `economy.yml` configuration file.

### How do I give money to players?
As an administrator, you can use the command `/bdadmin economy give <player> <amount>` to give money to players.

## Market System

### How do players create markets?
Players need to:
1. Build a valid market structure (minimum 3x3 roof, walls, bed, item frame)
2. Obtain a BD Market Token (craftable or admin-given)
3. Place the token in an item frame above the door
4. The market will initialize automatically, spawning the necessary villagers

### How many markets can a player have?
By default, players can have one market. This can be adjusted in the configuration, or permissions can be granted to allow multiple markets.

### What are collector houses?
Collector houses are additional structures within a market area that spawn special collector villagers who buy crops from players. Each market can have a limited number of collectors based on its level.

### How do players upgrade their markets?
Market upgrades are purchased through the Market Owner villager by right-clicking and selecting the Upgrades tab in the GUI. Upgrades require both currency and materials.

## Auction System

### How do players use the auction house?
Players can access the auction house using the `/bdah` command. This opens a GUI where they can browse listings, buy items, and manage their own listings.

### How do players sell items in the auction house?
To sell an item, players hold it in their main hand and use the command `/bdah sell <price> [amount]`. They can also access selling options through the auction house GUI.

### Is there a fee for listing items?
Yes, there is a listing fee (default 5% of the listing price) to prevent auction house spam. This fee is not refunded if the listing is canceled.

### How long do auction listings last?
By default, auction listings expire after 3 days if not sold. This can be adjusted in the configuration.

## Progression System

### How do players progress through ranks?
Players automatically earn experience toward ranks by:
- Harvesting BD crops
- Trading with BD villagers
- Using market and auction systems

The experience requirements for each rank can be configured in the `progression.yml` file.

### What benefits do higher ranks provide?
Higher ranks provide various benefits:
- Increased crop yields
- Better trading prices
- Extended auction listing durations
- Reduced auction fees
- Access to special features and abilities
- Custom chat prefixes

### What is the rebirth system?
The rebirth system allows players who have reached the maximum rank to reset their progression in exchange for permanent bonuses. Players can go through multiple rebirth cycles, each providing additional benefits.

### How do players perform a rebirth?
Once eligible, players can use the `/bdrebirth` command to view their status and requirements. When ready, they can use `/bdrebirth confirm` to initiate the rebirth process.

## Vital Features

### How many homes can players set?
The default home limit is configurable, but typically starts at 1. Players can receive permission to set more homes based on rank or permissions.

### How do teleport cooldowns work?
Teleportation commands have configurable cooldowns to prevent abuse. The default cooldown is 30 seconds between teleports, but this can be adjusted in the configuration.

### Can players use colored chat?
By default, only players with the `bdcraft.chat.color` permission can use colored chat. This permission can be granted based on rank or other criteria.

### What chat channels are available?
BDCraft includes several chat channels:
- Global chat (server-wide)
- Local chat (range-based)
- Market chat (only within a market area)
- Admin chat (only visible to administrators)

## Administration

### How do I give players admin access?
Grant the `bdcraft.admin` permission using BDCraft's built-in permission system to give full administrative access. For more granular control, you can grant specific admin permissions like `bdcraft.economy.admin` or `bdcraft.market.admin`.

### How do I reset a player's data?
Use the command `/bdadmin player reset <player>` to reset a player's data. This will reset their balance, rank, homes, and other plugin-related data.

### Can I back up BDCraft data?
Yes, you can use the command `/bdadmin backup` to create a backup of all plugin data. This will create a backup file in the `plugins/BDCraft/backups` folder.

### How do I configure the plugin?
BDCraft uses multiple configuration files located in the `plugins/BDCraft` folder. See the [Configuration Guide](configuration.md) for detailed information.

## Technical Questions

### Does BDCraft use a database?
By default, BDCraft stores data in flat files. However, it can be configured to use MySQL for more robust data storage, especially recommended for larger servers.

### How resource-intensive is BDCraft?
BDCraft is designed to be efficient, but it does include many features that require server resources. For smaller servers (under 20 players), the default configuration should work fine. For larger servers, some performance optimizations may be needed.

### Can I disable modules I don't need?
Yes, you can disable any module (economy, progression, vital) or specific submodules in the main configuration file. This can help reduce resource usage if you don't need all features.

### Is BDCraft compatible with server proxies like BungeeCord or Velocity?
Yes, BDCraft works with proxy setups, but its data is server-specific by default. If you want to share data across multiple servers, you should configure BDCraft to use a shared MySQL database.

## Troubleshooting

### What do I do if I encounter an error?
Check the [Troubleshooting Guide](troubleshooting.md) for common issues and their solutions. If your issue isn't covered, enable debug mode in the configuration and check the console for more detailed error messages.

### How do I report a bug?
If you believe you've found a bug, please provide detailed information including:
1. Your server version
2. BDCraft version
3. Detailed description of the issue
4. Steps to reproduce the issue
5. Any error messages from the console

### Can I request a feature?
Feature requests are welcome, but BDCraft is designed to be a comprehensive, self-contained plugin. Not all feature requests will be implemented, especially if they would require integration with external plugins.