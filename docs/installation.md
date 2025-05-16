# Installation Guide

## Requirements

- Minecraft server running Paper 1.21
- Java 17 or higher
- At least 2GB of RAM dedicated to the server
- Recommended: SSD storage for improved performance

## Installation Steps

1. Download the latest version of the BDCraft plugin JAR file from the releases page.

2. Place the JAR file in your server's `plugins` directory.

3. Restart your server or use a plugin manager to load the plugin.

4. On first run, BDCraft will create its configuration files in the `plugins/BDCraft` directory.

5. The plugin will automatically disable any detected conflicting plugins to ensure proper functionality.

6. Customize the configuration files as needed (see the [Configuration](configuration.md) guide).

7. Restart your server again to apply any configuration changes.

## Post-Installation

After installing BDCraft, you should:

1. Set up administrator permissions using your server's permission system. Admins should have the `bdcraft.admin` permission.

2. Review the default configuration files and adjust them to suit your server's needs.

3. Familiarize yourself with the available commands (see the [Commands](commands.md) guide).

4. Test the different modules to ensure they're working as expected.

## Upgrading from Previous Versions

When upgrading from a previous version of BDCraft:

1. Back up your current configuration files and any player data.

2. Replace the old JAR file with the new one.

3. Restart your server.

4. Check the changelog for any configuration changes that might be needed.

## Plugin Conflicts

BDCraft is designed to replace several common plugins and will automatically attempt to disable conflicting plugins. If a plugin cannot be automatically disabled, you will see warnings in the console.

Common plugins that should be removed before installing BDCraft:

- Economy plugins (Vault, EssentialsX Economy, etc.)
- Shop plugins (ChestShop, ShopKeepers, etc.)
- Teleportation plugins (EssentialsX, HyperDrive, etc.)
- Home plugins (EssentialsX, HomeSpawnPlus, etc.)
- Chat formatting plugins (EssentialsX Chat, ChatManager, etc.)

## Troubleshooting

If you encounter issues during installation:

1. Check the server logs for any error messages.

2. Ensure you're using a compatible version of Paper (1.21+).

3. Verify that no conflicting plugins are still active.

4. See the [Troubleshooting](troubleshooting.md) guide for more information.

## Next Steps

After installation, see the [Configuration](configuration.md) guide to customize BDCraft for your server.