# Troubleshooting Guide

This guide provides solutions to common issues encountered when using the BDCraft plugin. If you're experiencing problems, check this guide before reaching out for support.

## Startup Issues

### Plugin Fails to Load

**Symptoms**:
- Error messages in server console during startup
- BDCraft commands don't work
- Plugin appears in `/plugins` list but shows as disabled

**Possible Causes and Solutions**:

1. **Incompatible Server Version**
   - BDCraft requires Paper 1.20.1 or newer
   - Solution: Upgrade your server to a supported version

2. **Missing Dependencies**
   - BDCraft requires Vault to function correctly
   - Solution: Install Vault plugin from [SpigotMC](https://www.spigotmc.org/resources/vault.34315/)

3. **Java Version Too Old**
   - BDCraft requires Java 17 or newer
   - Solution: Update your Java installation

4. **Plugin Conflict**
   - Other economy plugins might conflict with BDCraft
   - Solution: Temporarily disable other economy plugins to identify conflicts

### Configuration Errors

**Symptoms**:
- Error messages about invalid configuration
- Plugin loads but features don't work correctly

**Possible Causes and Solutions**:

1. **Invalid YAML Syntax**
   - YAML is sensitive to indentation and formatting
   - Solution: Use a YAML validator to check your configuration files
   - Solution: Reset to default configurations and reconfigure

2. **Outdated Configuration**
   - After updates, configuration files may need updating
   - Solution: Rename your old configuration files and let the plugin generate new ones
   - Solution: Use `/bdadmin config update` to merge old settings with new structure

## Economy Issues

### Currency Not Working

**Symptoms**:
- Players can't earn or spend currency
- Balance shows as 0 regardless of actions

**Possible Causes and Solutions**:

1. **Vault Integration Issue**
   - BDCraft needs proper Vault registration
   - Solution: Check that Vault is installed and running correctly
   - Solution: Run `/bdadmin economy reload` to reinitialize the economy system

2. **Economy Provider Conflict**
   - Another plugin might be registered as the primary economy provider
   - Solution: Check server startup logs for economy provider registration
   - Solution: Configure other economy plugins to not register with Vault

3. **Data Storage Corruption**
   - Economy data files might be corrupted
   - Solution: Check `plugins/BDCraft/data/economy.json` for validity
   - Solution: Restore from a backup or use `/bdadmin economy repair`

### Rank System Problems

**Symptoms**:
- Players can't purchase ranks
- Rank benefits not applying

**Possible Causes and Solutions**:

1. **Rank Not Available**
   - Ranks might be disabled in configuration
   - Solution: Check `economy.yml` for `rank-enabled: true`

2. **Insufficient Funds**
   - Players need enough currency to purchase ranks
   - Solution: Check player balance with `/bdbal`
   - Solution: Temporarily reduce rank costs for testing

3. **Permission Issues**
   - Players might not have permission to upgrade
   - Solution: Ensure players have `bdcraft.rank.up` permission

## Villager System Issues

### Villagers Not Spawning

**Symptoms**:
- Villager spawn commands don't work
- No error messages, but no villagers appear

**Possible Causes and Solutions**:

1. **Invalid Village ID**
   - Village might not exist or ID is incorrect
   - Solution: Check village list with `/bdadmin village list`
   - Solution: Create a new village with `/bdadmin village create <id>`

2. **Dealer Restriction**
   - Dealers can only spawn in every third village
   - Solution: Try spawning a dealer in a different village
   - Solution: Use `/bdadmin village dealer-eligible` to check eligibility

3. **World Protection**
   - WorldGuard or similar plugins might block entity spawning
   - Solution: Temporarily disable protection or add spawn exception

### Villagers Not Trading

**Symptoms**:
- Villagers exist but don't open trade menu
- Trades show but can't be completed

**Possible Causes and Solutions**:

1. **Villager Not Registered**
   - Villager entity exists but isn't registered as a BD villager
   - Solution: Remove the villager and spawn a new one with proper commands

2. **Trade Requirements Not Met**
   - Players might not have required rank for certain trades
   - Solution: Check player rank with `/bdrank`
   - Solution: Temporarily disable rank requirements in configuration

3. **Inventory Issues**
   - Full player inventory can prevent trade completion
   - Solution: Ensure players have inventory space

## BD Farming Issues

### Seeds Not Growing

**Symptoms**:
- BD seeds planted but don't grow
- Seeds grow very slowly or inconsistently

**Possible Causes and Solutions**:

1. **Growth Conditions**
   - Seeds need proper conditions (light, water)
   - Solution: Ensure farming area has light level 9+ and water nearby

2. **Growth Tracking Issue**
   - Plugin might not be tracking planted BD seeds
   - Solution: Try replanting seeds
   - Solution: Use `/bdadmin debug on` to see seed registration logs

3. **Server Lag**
   - High server lag can slow or prevent growth events
   - Solution: Check server TPS and optimize if necessary

### Incorrect Crop Yields

**Symptoms**:
- Harvesting doesn't give BD crops
- Incorrect number of crops received

**Possible Causes and Solutions**:

1. **Item Identification Issue**
   - Plugin might not identify BD crops correctly
   - Solution: Use BD Harvester or Ultimate BD Harvester to ensure proper harvesting

2. **Configuration Mismatch**
   - Yield settings might be incorrectly configured
   - Solution: Check `economy.yml` for proper yield settings

3. **Permission Issue**
   - Special yields might require permissions
   - Solution: Check that players have proper permissions

## Player Market Issues

### Market Tokens Not Working

**Symptoms**:
- Placing market token doesn't create a market
- No villagers spawn when token is placed

**Possible Causes and Solutions**:

1. **Market Requirements Not Met**
   - Market structure may not meet requirements
   - Solution: Verify 3x3 roof, walls with door, bed inside
   - Solution: Ensure item frame is directly above door

2. **Market Distance Restrictions**
   - Market might be too close to another market
   - Solution: Use `/bdmarket check` to see nearby markets
   - Solution: Move at least 30 blocks away from other markets

3. **Player Market Limit Reached**
   - Player might have reached their market limit
   - Solution: Check limit in config (`max-markets-per-player`)
   - Solution: Remove an existing market before creating a new one

### Market Boundary Visualization Issues

**Symptoms**:
- `/bdmarket check` command doesn't show boundaries
- Temporary blocks don't appear or disappear too quickly

**Possible Causes and Solutions**:

1. **Visualization Time Too Short**
   - Temporary blocks might disappear too quickly
   - Solution: Adjust `market-visualize-time` in configuration

2. **Block Replacement Issues**
   - Some blocks might not be replaceable temporarily
   - Solution: Stand on flat ground when using the command
   - Solution: Try running the command in a different location

3. **Permission Issues**
   - Player might not have required permissions
   - Solution: Ensure player has `bdcraft.market.check` permission

## Database Issues

### MySQL Connection Problems

**Symptoms**:
- Error messages about database connection
- Plugin falls back to file storage

**Possible Causes and Solutions**:

1. **Incorrect Credentials**
   - Database username or password might be wrong
   - Solution: Verify credentials in `config.yml`

2. **Database Not Accessible**
   - MySQL server might be down or unreachable
   - Solution: Check MySQL server status
   - Solution: Verify network connectivity

3. **Missing Tables**
   - Database tables might not exist
   - Solution: Let plugin create tables automatically
   - Solution: Run `/bdadmin database init` to force table creation

## Integration Issues

### Vault Integration Problems

**Symptoms**:
- Economy features don't work with other plugins
- Permission features don't synchronize

**Possible Causes and Solutions**:

1. **Load Order**
   - Vault might load after BDCraft
   - Solution: Add Vault to `depend` section in plugin.yml

2. **Multiple Economy Providers**
   - Other plugins might compete for economy provider status
   - Solution: Disable economy features in other plugins

### PlaceholderAPI Problems

**Symptoms**:
- BDCraft placeholders don't work
- Placeholder errors in console

**Possible Causes and Solutions**:

1. **Expansion Not Registered**
   - BDCraft expansion might not be registered
   - Solution: Run `/papi reload` to refresh PlaceholderAPI

2. **Incorrect Placeholder Format**
   - Placeholder syntax might be incorrect
   - Solution: Verify correct placeholder format in config

## Performance Issues

### Lag When Using BD Features

**Symptoms**:
- Server lag when players trade with villagers
- TPS drops during BD farming activities

**Possible Causes and Solutions**:

1. **Too Many Villagers**
   - Large numbers of BD villagers can cause lag
   - Solution: Reduce number of villagers per village
   - Solution: Spread villages across multiple worlds

2. **Excessive Growth Checks**
   - Growth monitoring can be resource-intensive
   - Solution: Adjust growth check frequency in advanced configuration

3. **Database Queries**
   - Frequent database access can cause lag
   - Solution: Enable caching in configuration
   - Solution: Switch to local file storage for small servers

## Getting Help

If you're still experiencing issues after trying these solutions:

1. **Enable Debug Mode**
   - Run `/bdadmin debug on` to enable detailed logging
   - Check console and log files for error messages

2. **Check Latest Logs**
   - Examine `plugins/BDCraft/logs/latest.log` for errors
   - Look for specific error codes and messages

3. **Gather Information**
   - Server version (`/version`)
   - BDCraft version (`/bdcraft version`)
   - List of plugins (`/plugins`)
   - Relevant configuration settings

4. **Seek Support**
   - Join our Discord server for community support
   - Create a detailed issue on our GitHub repository
   - Include all gathered information and error logs

## Common Error Codes

| Error Code | Description | Solution |
|------------|-------------|----------|
| BD-E001 | Configuration Load Error | Check YAML syntax in configuration files |
| BD-E002 | Database Connection Failure | Verify database credentials and connectivity |
| BD-E003 | Villager Registration Error | Remove and respawn the villager |
| BD-E004 | Economy Provider Conflict | Resolve conflicts with other economy plugins |
| BD-E005 | Permission System Failure | Check permission configuration and dependencies |
| BD-E006 | Market Structure Validation Error | Verify market structure meets requirements |
| BD-E007 | Growth Tracking Failure | Clear and replant BD crops |
| BD-E008 | Data File Corruption | Restore from backup or repair data files |
| BD-E009 | Integration Initialization Error | Ensure dependency plugins are correctly installed |
| BD-E010 | Token Authentication Failure | Generate new authentication tokens |

Remember to always back up your data before making significant changes to configuration or attempting repairs.