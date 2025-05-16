# Installation Guide

This guide will walk you through the process of installing BDCraft on your Minecraft server.

## Requirements

- Java 17 or higher
- Minecraft Paper server 1.21.x
- At least 1GB of RAM dedicated to the server
- No other economy or teleport plugins installed

## Installation Steps

1. **Download the Plugin**
   - Download the latest version of BDCraft from the official site
   - Make sure you download the correct version for your Minecraft server version

2. **Place the Plugin File**
   - Locate your server's `plugins` folder
   - Copy the `BDCraft.jar` file into the `plugins` folder

3. **Start or Restart Your Server**
   - Start your server if it's not running
   - If your server is already running, use the `/stop` command to shut it down, then start it again

4. **Verify Installation**
   - Check the server console for messages from BDCraft
   - You should see a message indicating that BDCraft has successfully loaded
   - If you see any error messages, check the Troubleshooting section for help

5. **Initial Configuration**
   - BDCraft will generate default configuration files in a new folder called `plugins/BDCraft`
   - Stop the server using the `/stop` command
   - Edit the configuration files as needed (see the [Configuration Guide](configuration.md))
   - Start the server again

## First-Time Setup

After installing BDCraft, follow these steps to set up the plugin for the first time:

1. **Assign Permissions**
   - Give yourself admin permissions by adding yourself to the ops.json file or using the built-in BDCraft permission system
   - The main admin permission is `bdcraft.admin`

2. **Initialize Economy**
   - Use the command `/bdadmin economy init` to initialize the economy system
   - This will set up the default currency and economy settings

3. **Create Spawn Market**
   - Use the command `/bdadmin market create spawn` to create a default market at your server's spawn
   - This market will serve as a central trading hub for players

4. **Test Basic Functionality**
   - Try using basic commands like `/bdmarket list` and `/bdauction`
   - Verify that the commands work and the plugin is functioning correctly

## Updating the Plugin

To update BDCraft to a newer version:

1. Stop your server using the `/stop` command
2. Back up your `plugins/BDCraft` folder
3. Replace the old `BDCraft.jar` with the new version
4. Start your server
5. Check the console for any migration or update messages

## Common Installation Issues

### Plugin Conflicts

BDCraft is designed to be self-contained and will block competing plugins to prevent conflicts. If you see error messages about plugin conflicts, you'll need to remove the conflicting plugins.

Common conflicting plugins include:
- Other economy plugins (Vault-based plugins, CMI Economy, etc.)
- Other teleportation plugins (EssentialsX, CMI, etc.)
- Other chat formatting plugins (EssentialsX Chat, ChatControl, etc.)

### Performance Considerations

BDCraft is designed to be efficient, but it does require some server resources. If you experience performance issues:

1. Make sure your server meets the minimum requirements
2. Consider increasing the memory allocation for your server
3. Disable any modules you don't need in the configuration

### Database Issues

By default, BDCraft stores data in flat files. If you choose to use MySQL:

1. Make sure your MySQL server is running and accessible
2. Check that the database credentials in the configuration are correct
3. Verify that the specified database exists and is accessible by the user

## Next Steps

After successfully installing BDCraft, check out the [Configuration Guide](configuration.md) to learn how to customize the plugin to your needs.