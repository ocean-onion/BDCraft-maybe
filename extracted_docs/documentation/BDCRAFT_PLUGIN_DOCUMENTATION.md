# BDCraft Plugin Documentation

## Overview

BDCraft is a comprehensive Minecraft server management plugin for Paper servers version 1.21+. It provides a suite of features designed to enhance gameplay with a unique farming-based economy system, custom villager trading, player progression, and server administration tools.

### Key Features

- **BD Economy System**: A custom economy based on farming special BD crops
- **Villager Trading System**: Enhanced villager mechanics with custom trades
- **Player Progression**: Rank system with custom rewards and abilities
- **Market System**: Player-created marketplaces with custom functionality
- **Administration Tools**: Comprehensive utilities for server management


## Core Systems

### BD Economy System

The plugin introduces a specialized Business Development (BD) economy with:

- Three tiers of BD seeds with increasing value
- Special BD crops (represented as ferns) that can be harvested with BD tools
- BD currency earned through selling crops to collector villagers
- Emerald-based trading with dealer villagers for seeds and special items

### Villager System

BDCraft enhances Minecraft's villager system with:

- **Dealers**: Found in natural villages, sell BD seeds for emeralds
- **Collectors**: Buy harvested BD crops for emeralds and BD currency
- **Seasonal Traders**: Appear periodically with unique items
- **Market Owners**: Manage player-created markets

### Player Markets

Players can create and manage their own markets with:

- Custom market creation using special tokens
- Territory management with protection features
- Market stall rentals and management
- Auction house functionality for player-to-player trading

### Progression System

The plugin includes a comprehensive progression system:

- Multiple player ranks with unique abilities
- Rank-up requirements based on BD currency and activities
- Rebirth mechanics for advanced players
- Special tools and capabilities unlocked at higher ranks

## Module Components

### BD Economy Module

Manages all economy-related functionality:

- BD seed and crop implementation
- Villager AI modifications
- Currency tracking and transactions
- Market economy mechanics
- Auction house functionality

### BD Vital Module

Provides essential server utilities:

- Teleportation system
- Home management
- Messaging systems
- Basic administration commands

### BD Perms Module

Handles permission management:

- Customizable permission groups
- Permission inheritance

- Chat formatting



## Installation and Configuration

See the [Installation Guide](getting-started/installation.md) for detailed setup instructions.

Key configuration files include:

- `config.yml`: Core plugin settings
- `economy.yml`: Economy settings
- `permissions.yml`: Permission configuration
- `messages.yml`: Customizable messages

## API and Integration

BDCraft provides extensive API access for developers:

- Economy API for currency and trade integration
- Villager API for custom villager creation
- Permission API for security integration
- Event API for monitoring plugin activities

See the [API Documentation](technical/api-documentation.md) for details.

## Module System

The plugin's modular architecture allows for:

- Easy maintenance and updates
- Feature toggling through configuration
- Extension through custom modules

See the [Module System](technical/module-system.md) documentation for details on the architecture.



## Documentation

Comprehensive documentation is available in the documentation directory, covering:

- [Getting Started Guides](getting-started/)
- [Core Systems Documentation](core-systems/)
- [BD Items and Trading](bd-items/)
- [Administration Guides](administration/)
- [Player Features](player-features/)
- [Technical Reference](technical/)

## Support and Community

- GitHub Issues: Bug reports and feature requests
- Discord Community: Real-time support and discussion
- Regular Updates: Ongoing development and improvements

## License

BDCraft is licensed under the MIT License. See the LICENSE file for details.
