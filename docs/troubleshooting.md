# Troubleshooting

This guide covers common issues you might encounter with BDCraft and their solutions.

## Startup Issues

### Plugin Fails to Load

**Symptoms:**
- Plugin doesn't appear in the `/plugins` command output
- Error messages in the server console during startup

**Possible Causes and Solutions:**

1. **Incompatible Server Version**
   - BDCraft requires Paper 1.21.x
   - Solution: Ensure you're using a compatible Paper server version

2. **Java Version Issues**
   - BDCraft requires Java 17 or higher
   - Solution: Update your Java installation to version 17 or higher

3. **Conflicting Plugins**
   - BDCraft is designed to be self-contained and will block competing plugins
   - Solution: Remove any conflicting plugins (economy, teleport, or chat plugins)

4. **Corrupted JAR File**
   - Solution: Re-download the BDCraft.jar file and ensure it was transferred properly

### Configuration Error on Startup

**Symptoms:**
- Plugin loads but reports configuration errors
- Features not working as expected

**Solutions:**
1. Delete the `plugins/BDCraft` folder and restart the server to generate default configurations
2. Check the console for specific error messages that indicate which configuration is causing issues
3. Make sure all required configuration sections are present and formatted correctly

## Economy Issues

### Players Cannot Earn Money

**Symptoms:**
- Players report not receiving money from activities
- Balance doesn't increase after selling items

**Solutions:**
1. Check if the economy module is enabled in the configuration
2. Make sure the market and villager trading modules are functioning correctly
3. Verify that players have the necessary permissions to use economy features

### Market Issues

**Symptoms:**
- Players can't create markets
- Market villagers not spawning
- Trading not working properly

**Solutions:**
1. Ensure the market module is enabled in the configuration
2. Check if players have the necessary permissions (`bdcraft.market.use`)
3. Verify that players are building valid market structures according to requirements
4. Check that the market tokens are being crafted correctly

### Auction House Issues

**Symptoms:**
- Auction house doesn't open
- Items can't be listed
- Purchases fail

**Solutions:**
1. Ensure the auction module is enabled in the configuration
2. Check if players have the necessary permissions
3. Verify that the database connection is working (if using MySQL)
4. Check for errors in the console when players try to use auction commands

## Progression Issues

### Rank Issues

**Symptoms:**
- Players not receiving rank-up notifications
- Rank benefits not applying
- Rank commands not working

**Solutions:**
1. Ensure the progression module is enabled in the configuration
2. Check that the rank-up requirements are configured correctly
3. Verify that players have the necessary permissions
4. Check for errors in the console when players try to use rank commands

### Rebirth Issues

**Symptoms:**
- Rebirth command not working
- Rebirth benefits not applying
- Errors during rebirth process

**Solutions:**
1. Ensure the rebirth module is enabled in the configuration
2. Check that the rebirth requirements are configured correctly
3. Verify that players have reached the required rank for rebirth
4. Check for errors in the console when players try to use rebirth commands

## Vital Module Issues

### Home Issues

**Symptoms:**
- Players can't set homes
- Teleporting to homes doesn't work
- Home limit not applying correctly

**Solutions:**
1. Ensure the home module is enabled in the configuration
2. Check that players have the necessary permissions
3. Verify that the home limits are configured correctly
4. Check for errors in the console when players try to use home commands

### Teleport Issues

**Symptoms:**
- Teleport commands not working
- Teleport requests not being sent
- Warps not functioning

**Solutions:**
1. Ensure the teleport module is enabled in the configuration
2. Check that players have the necessary permissions
3. Verify that teleport cooldowns and warmups are configured correctly
4. Check for errors in the console when players try to use teleport commands

### Chat Issues

**Symptoms:**
- Chat formatting not working
- Chat channels not functioning
- Private messages not being delivered

**Solutions:**
1. Ensure the chat module is enabled in the configuration
2. Check that players have the necessary permissions
3. Verify that chat formats are configured correctly
4. Check for errors in the console when players try to use chat commands

## Database Issues

### MySQL Connection Problems

**Symptoms:**
- Plugin reports database connection errors
- Data not being saved or loaded
- Errors in the console related to database operations

**Solutions:**
1. Verify that the MySQL server is running and accessible
2. Check that the database credentials in the configuration are correct
3. Ensure the specified database exists and is accessible by the user
4. Check for network issues between the Minecraft server and the database server

### Data Loss or Corruption

**Symptoms:**
- Player data missing
- Incorrect balances or permissions
- Markets or auctions disappearing

**Solutions:**
1. Restore from a backup if available
2. Check the database structure for errors
3. Run the repair commands to fix corrupted data:
   - `/bdadmin repair economy`
   - `/bdadmin repair markets`
   - `/bdadmin repair players`

## Permission Issues

### Commands Not Working

**Symptoms:**
- Players can't use certain commands
- Permission denied messages in chat
- Features not accessible

**Solutions:**
1. Verify that players have the necessary permissions in BDCraft's permission system
2. Check the permission structure in BDCraft's configuration
3. Ensure that the permission nodes are set up correctly
4. Use the command `/bdadmin checkperm <player> <permission>` to check specific permissions

### Admin Commands Not Available

**Symptoms:**
- Server operators can't use admin commands
- Permission denied messages for admin features

**Solutions:**
1. Ensure that operators have the `bdcraft.admin` permission
2. Check that BDCraft's permission system is configured correctly
3. Verify that the permission inheritance is working properly

## Advanced Troubleshooting

### Debug Mode

If you're experiencing issues that aren't covered here, you can enable debug mode to get more detailed information:

1. In the main configuration file, set `plugin.debug` to `true`
2. Restart the server
3. Reproduce the issue
4. Check the console for detailed debug messages
5. Share these messages with the support team when seeking help

### Plugin Conflicts

Although BDCraft is designed to block competing plugins, sometimes there can be unexpected conflicts:

1. Start with a clean server with only BDCraft installed
2. Verify that BDCraft works correctly
3. Add other plugins one by one, testing after each addition
4. If a conflict is found, report it to the support team

### Reset Plugin

As a last resort, you can completely reset the plugin:

1. Stop the server
2. Delete the `plugins/BDCraft` folder
3. Delete the plugin's data from the database (if using MySQL)
4. Start the server
5. Reconfigure the plugin

## Getting Help

If you've tried these troubleshooting steps and are still experiencing issues, please reach out for support:

1. Check if your issue is already addressed in the [FAQ](faq.md)
2. Provide detailed information about your server setup
3. Include any error messages from the console
4. Describe the steps you've taken to try to resolve the issue