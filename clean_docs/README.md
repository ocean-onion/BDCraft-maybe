# BDCraft Documentation

Welcome to the BDCraft documentation. This comprehensive guide contains everything you need to know about installing, configuring, and using the BDCraft plugin - a complete economic and progression system for Minecraft Paper 1.21.x servers.

## Getting Started

- [Introduction](getting-started/introduction.md) - Overview of BDCraft's features and capabilities
- [Installation](getting-started/installation.md) - How to install and set up BDCraft

## Core Economy System

BDCraft provides a complete economic ecosystem centered around player-built markets and specialized villager trading.

### [Economy System](economy/README.md)
- **[Currency System](economy/currency.md)** - Dual currency system (emeralds + server currency) with village reputation mechanics
- **[BD Crops](economy/crops.md)** - Complete crop growing system with three tiers and yield bonuses
- **[Trading System](economy/trading.md)** - Comprehensive villager trading with bonuses and reputation mechanics
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
- **[Rank System](progression/ranks.md)** - 5-tier progression from Newcomer to Agricultural Expert with achievement requirements
- **[Achievement System](progression/achievements.md)** - Comprehensive achievement tracking with GUI interface for meaningful progression
- **[Rebirth System](progression/rebirth.md)** - Prestige system with permanent bonuses and exclusive features (5 rebirth levels)

## Essential Utilities

### [Vital Features](vital/README.md)
- **[Chat System](vital/chat.md)** - Multi-channel chat with Global, Local, Market, and Admin channels plus private messaging
- **[Messaging System](vital/messaging.md)** - Private messages, mail system, and item storage with 7-day retention
- **[Home System](vital/home.md)** - Personal teleportation points with rank-based limits and donor benefits
- **[Teleport System](vital/teleport.md)** - Player teleportation, warp management, and location systems
- **[Tab List System](vital/tab.md)** - Customizable player listings with rank-based sorting and headers

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
Quick command reference organized by module and function

### [Complete Command Reference](admin/complete-command-reference.md)
Comprehensive guide with detailed explanations for every command:
- **Usage Examples** - Real-world scenarios and proper syntax
- **Permission Requirements** - Exact permissions needed for each command
- **Detailed Descriptions** - What each command does and when to use it
- **Administrative Tools** - Full admin command suite with explanations

### [FAQ & Troubleshooting](faq.md)
Complete guide covering:
- **Common Questions** - General plugin information and usage
- **Troubleshooting** - Solutions for startup, economy, and performance issues
- **Achievement System** - How to use the achievement GUI and understand progression
- **Getting Help** - How to report issues and get support

## Configuration

### [Configuration System](configuration/configuration.md)
Multi-file configuration covering:
- **Module Control** - Enable/disable Economy, Progression, and Vital modules
- **Economy Settings** - Currency, market, auction, and villager configurations
- **Progression Tuning** - Rank requirements, rebirth costs, and benefit multipliers
- **Vital Customization** - Chat formats, teleport delays, home limits

### [Permission System](configuration/permissions.md)
- **Built-in Permissions** - Complete permission management without external plugins
- **GUI Management** - Interactive permission editing with `/bdpermissions`
- **Command Control** - Quick permission changes with `/bdperm` and `/bdgroup`
- **YAML Configuration** - Direct file editing for bulk operations
- **Group-Based Access** - Default, donor, and admin permission groups with inheritance

### [Permission Management](admin/permission-management.md)
Comprehensive permission management system:
- **Interactive GUI** - Visual permission editing and group management
- **Command System** - 30+ commands for permission and group management
- **Advanced Features** - Import/export, backups, bulk operations, and templates
- **Integration** - Seamless integration with existing rank and chat systems

### [Permission Quick Reference](admin/permission-quick-reference.md)
Essential commands and common setups for quick permission management

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

If you encounter any issues not covered in this documentation, please contact your server administrators. For troubleshooting steps, consult the [FAQ & Troubleshooting](faq.md) guide first.

---

**BDCraft provides everything needed for a complete economic Minecraft experience - from basic currency to advanced market systems, all integrated into one powerful plugin.**