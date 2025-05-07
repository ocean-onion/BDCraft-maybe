# BDCraft Installation Guide

This guide covers the installation process for the BDCraft plugin on your Minecraft server. BDCraft requires the Paper server platform and is designed for Minecraft version 1.20.1 or newer.

## Prerequisites

Before installing BDCraft, ensure you have:

- A Minecraft server running Paper 1.20.1 or newer
- Server operator privileges
- At least 1GB of free RAM (2GB recommended)
- Java 17 or newer

## Supported Platforms

- **Paper** (fully supported, recommended)
- **Spigot** (supported, but some features may be limited)
- **Purpur** (supported, derived from Paper)

## Installation Steps

### 1. Download the Plugin

1. Download the latest version of BDCraft from one of these sources:
   - [Official BDCraft Website](https://bdcraft.example.com/downloads)
   - [SpigotMC](https://www.spigotmc.org/resources/bdcraft.12345/)
   - [Hangar](https://hangar.papermc.io/BDCraft/BDCraft)

2. Save the `.jar` file to your downloads folder

### 2. Install the Plugin

1. Stop your Minecraft server if it's running
2. Navigate to your server's `plugins` folder
3. Copy the downloaded `BDCraft-X.X.X.jar` file into the plugins folder
4. Start your server

The plugin will create its configuration files on first startup.

### 3. Verify Installation

After your server has fully started, you can verify the installation:

1. Run the command `/bdcraft version` in-game or from the console
2. Check the server logs for the message "BDCraft vX.X.X has been enabled"
3. Check that the following folders have been created in `plugins/BDCraft/`:
   - `config`
   - `data`
   - `logs`

## Configuration Files

BDCraft will generate several configuration files on first startup:

- `config.yml` - Core plugin settings
- `messages.yml` - Customizable messages
- `permissions.yml` - Permission setup
- `economy.yml` - Economy settings
- `players.yml` - Player data storage

These files can be edited to customize the plugin to your needs. See the [Configuration Basics](./configuration.md) guide for details.

## Fully Self-Contained Plugin

BDCraft is designed to be fully self-contained with no external dependencies required:

- **Built-in Economy System** - Complete economy functionality without Vault
- **Integrated Permissions (BDPerms)** - No external permission plugins needed
- **Internal Protection System** - Market protection without WorldGuard
- **Comprehensive Logging** - Built-in activity tracking

The plugin handles all functionality internally, making installation and maintenance simpler and eliminating compatibility issues with other plugins.

## Permission Setup

Grant the necessary permissions to players using the built-in BDPerms system:

```
# Grant basic permissions to the default group
/bdperms group default add bdcraft.use

# Grant admin permissions to a specific user
/bdperms user <username> add bdcraft.admin

# List all permissions for a player
/bdperms user <username> list

# Create a new permission group
/bdperms group create <group_name>
```

All permission management is handled through in-game commands with no external interfaces required.

For a complete list of permissions, see the [Permissions Reference](../technical/configuration-reference.md#permissions).

## Upgrading from Previous Versions

When upgrading BDCraft to a newer version:

1. Stop your server
2. Make a backup of your `plugins/BDCraft` folder
3. Replace the old `.jar` file with the new one
4. Start your server

The plugin will automatically update its data structures if necessary.

## Common Installation Issues

### Plugin Doesn't Load

**Possible causes:**
- Incompatible server version (requires Paper 1.20.1+)
- Java version too old (requires Java 17+)
- Insufficient server memory

**Solution:** Check server logs for specific error messages. Ensure you're using Paper 1.20.1 or newer with Java 17 or higher, and that your server has sufficient memory allocated.

### Configuration Errors

**Possible causes:**
- Invalid YAML syntax in configuration files
- Outdated configuration from previous version

**Solution:** Check the server logs for configuration errors. Reset the configuration files and reconfigure, or use a YAML validator to check syntax.

### Permission Issues

**Possible causes:**
- Permission nodes not assigned correctly in BDPerms
- Conflict with other permission plugins
- Corrupted permissions data file

**Solution:** 
- Check BDPerms settings using `/bdperms debug`
- Ensure no other permission plugins are installed
- View all permissions with `/bdperms list all`
- Reset permissions with `/bdperms reset` (caution: this resets all permissions)

## Support

If you encounter issues with installation:

- Check the [Troubleshooting Guide](../technical/troubleshooting.md)
- Visit our [Discord Server](https://discord.gg/bdcraft) for community support
- Open a support ticket on our [GitHub repository](https://github.com/bdcraft/bdcraft-plugin/issues)

After installation, continue to the [Quick Start Guide](./quick-start.md) for initial setup instructions.