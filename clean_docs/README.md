# BDCraft Documentation

Welcome to the BDCraft documentation. This comprehensive guide contains everything you need to know about installing, configuring, and using the BDCraft plugin - a complete economic and progression system for Minecraft Paper 1.21.x servers.

## Getting Started

- [Introduction](getting-started/introduction.md) - Overview of BDCraft's features and capabilities
- [Installation](getting-started/installation.md) - How to install and set up BDCraft

## Core Economy System

BDCraft provides a complete economic ecosystem centered around player-built markets and specialized villager trading.

### [Economy System](economy/README.md)
- **[Currency System](economy/currency.md)** - Dual currency system (emeralds + server currency) with village reputation mechanics
- **[Player Markets](economy/market.md)** - Player-built trading centers with physical structures, villager spawning, and upgrade systems
- **[BD Villagers](economy/villager.md)** - Specialized villagers including Dealers, Market Owners, Collectors, and Seasonal Traders
- **[Auction House](economy/auction.md)** - Server-wide marketplace for BD items with rank-based benefits and fee structures

### [Token Economy](tokens.md)
Complete token-based market creation system:
- **BD Market Tokens** - Create 98x98 block markets (128x128 for donors) with villager spawning
- **BD House Tokens** - Expand markets with collector houses (3-15 based on market level)
- **BD Stick System** - Precursor crafting item with durability and special effects

## Progression Systems

### [Progression System](progression/README.md)
- **[Rank System](progression/ranks.md)** - 5-tier progression (Newcomer → Agricultural Expert) with achievement requirements
- **[Rebirth System](progression/rebirth.md)** - Prestige system with permanent bonuses and exclusive features (5 rebirth levels)

## Essential Utilities

### [Vital Features](vital/README.md)
- **[Chat System](vital/chat.md)** - Multi-channel chat (Global/Local/Market/Admin), private messaging, and mail system
- **[Home System](vital/home.md)** - Personal teleportation points with rank-based limits (1-3 homes)
- **[Teleport System](vital/teleport.md)** - Player teleportation, warp system, and location management
- **[Tab List System](vital/tab.md)** - Customizable player listings with rank-based sorting

## Advanced Features

### Seasonal Trading System
- **Seasonal Traders** - Special villagers appearing in level 3+ markets every 2 weeks
- **Seasonal Variants** - Spring, Summer, Fall, Winter traders with unique themed items
- **Event Trading** - Special traders during server events with exclusive items

### Village Reputation System
- **Market-Specific Reputation** - Affects trading prices and currency generation
- **Reputation Gains** - Different rates for Dealers (+2), Collectors (+3), Seasonal (+4)
- **Dynamic Pricing** - Better prices with higher reputation levels

### Market Management
- **Market Visualization** - Commands to display market boundaries and information
- **Associate System** - Market owners can add up to 5 associates with building/management permissions
- **Security Controls** - Manage building permissions and trading access within market areas
- **Market Upgrades** - 5 upgrade levels affecting collector limits, trading bonuses, and special features

## Donor Benefits

### [Donor System](donor/donor-benefits.md)
Comprehensive donor perks across all systems:
- **Remote Market Access** - `/bdmarket` command for GUI access from anywhere
- **Enhanced Limits** - 3 homes, 5 markets maximum, 128x128 market size
- **Economic Benefits** - 2% auction fees, 20% currency retention on rebirth, daily balance growth
- **Rank Bonuses** - 10% boost to all rank perks and trading prices
- **Priority Features** - Auction listing priority and tool access regardless of rank

## Administration

### [Admin Commands](admin/commands.md)
Complete command reference including:
- **Economy Management** - Currency administration, market/auction oversight
- **Player Management** - Rank/rebirth control, data management, donor verification
- **System Administration** - Villager spawning, token distribution, debugging tools
- **Chat Management** - Moderation, channel control, muting systems

### [Troubleshooting](admin/troubleshooting.md)
Solutions for common issues including:
- Plugin conflicts and startup problems
- Economy and market functionality issues
- Database and permission problems
- Debug mode and advanced troubleshooting

## Configuration

### [Configuration System](configuration/configuration.md)
Multi-file configuration covering:
- **Module Control** - Enable/disable Economy, Progression, and Vital modules
- **Economy Settings** - Currency, market, auction, and villager configurations
- **Progression Tuning** - Rank requirements, rebirth costs, and benefit multipliers
- **Vital Customization** - Chat formats, teleport delays, home limits

### [Permission System](configuration/permissions.md)
- **[Built-in Permissions](configuration/permission-system.md)** - Complete permission management without external plugins
- **Group-Based Access** - Default, donor, and admin permission groups
- **Permission Inheritance** - Hierarchical permission structure for easy management

## Development

### [Technical Architecture](development/modular-architecture.md)
- **Modular Design** - Three main modules (Economy, Progression, Vital) with specialized submodules
- **API Structure** - Clean separation of concerns with well-defined module interactions
- **Extension Framework** - Easy addition of new features through consistent architecture

### [API Reference](development/api-reference.md)
Developer interfaces for:
- **Core API** - Configuration and data management
- **Economy API** - Currency, market, auction, and villager interactions
- **Progression API** - Rank and rebirth system access
- **Vital API** - Home, teleport, chat, and tab list management

## Additional Information

- **[Frequently Asked Questions](faq.md)** - Answers to common questions about features, compatibility, and troubleshooting
- **Self-Contained Design** - No external dependencies, automatically blocks competing plugins
- **Paper 1.21.x Compatible** - Designed specifically for modern Minecraft Paper servers
- **Complete Replacement** - Replaces multiple separate plugins with one cohesive system

## Support

If you encounter any issues not covered in this documentation, please contact our support team on Discord or through our website. For troubleshooting steps, consult the [Troubleshooting Guide](admin/troubleshooting.md) first.

---

**BDCraft provides everything needed for a complete economic Minecraft experience - from basic currency to advanced market systems, all integrated into one powerful plugin.**