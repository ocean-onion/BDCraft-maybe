# BDCraft Permission System

BDCraft includes a comprehensive built-in permission system that allows server administrators to control access to the plugin's features. This document explains how to use and configure this system.

## Overview

The BDCraft permission system offers:
- Complete permission management without external plugins
- Group-based permission assignment
- Permission inheritance
- Default permissions for new players
- Admin override functionality

## Permission Structure

BDCraft uses a hierarchical permission structure with the following format:

```
bdcraft.<module>.<submodule>.<action>
```

For example:
- `bdcraft.economy.use` - Basic economy usage
- `bdcraft.market.founder` - Market creation ability
- `bdcraft.admin` - Full administrative access

## Permission Configuration

BDCraft's permissions are configured in the `permissions.yml` file:

```yaml
# Permission Groups
groups:
  default:
    permissions:
      - bdcraft.economy.use
      - bdcraft.economy.balance
      - bdcraft.market.use
      - bdcraft.auction.use
      - bdcraft.villager.use
      - bdcraft.home.use
      - bdcraft.home.set
      - bdcraft.home.multiple.1
      - bdcraft.teleport.use
      - bdcraft.teleport.tpa
      - bdcraft.teleport.spawn
      - bdcraft.chat.global
      - bdcraft.chat.local
      
  vip:
    inherit: default
    permissions:
      - bdcraft.home.multiple.3
      - bdcraft.teleport.back
      - bdcraft.chat.color
      
  admin:
    inherit: vip
    permissions:
      - bdcraft.admin

# Player-specific permissions
players:
  00000000-0000-0000-0000-000000000000:  # Example UUID
    group: admin
    permissions:
      - -bdcraft.economy.admin  # Negated permission
```

## How to Use

### Assigning Groups

To assign a player to a permission group:

1. Find the player's UUID using the in-game command `/bdadmin player info <player>`
2. Add the player to the `players` section in `permissions.yml`:
   ```yaml
   players:
     <uuid>:
       group: vip
   ```
3. Reload the plugin with `/bdadmin reload permissions`

### Adding Custom Groups

To create a new permission group:

1. Add the group to the `groups` section in `permissions.yml`:
   ```yaml
   groups:
     custom_group:
       inherit: default
       permissions:
         - bdcraft.custom.permission1
         - bdcraft.custom.permission2
   ```
2. Reload the plugin with `/bdadmin reload permissions`

### Inheritance

Groups can inherit permissions from other groups. For example, if `vip` inherits from `default`, then all permissions granted to `default` are also granted to `vip`.

### Negated Permissions

You can negate a permission by prefixing it with a minus sign (`-`). This is useful for removing specific permissions from users or groups that would otherwise have them through inheritance.

```yaml
permissions:
  - bdcraft.economy.use
  - -bdcraft.economy.admin  # Negated permission
```

## Permission Commands

BDCraft provides several commands for managing permissions in-game:

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdadmin perms group list` | List all permission groups | bdcraft.admin.permissions |
| `/bdadmin perms group info <group>` | View a group's permissions | bdcraft.admin.permissions |
| `/bdadmin perms group create <group>` | Create a new permission group | bdcraft.admin.permissions |
| `/bdadmin perms group delete <group>` | Delete a permission group | bdcraft.admin.permissions |
| `/bdadmin perms group addperm <group> <permission>` | Add a permission to a group | bdcraft.admin.permissions |
| `/bdadmin perms group delperm <group> <permission>` | Remove a permission from a group | bdcraft.admin.permissions |
| `/bdadmin perms player info <player>` | View a player's permissions | bdcraft.admin.permissions |
| `/bdadmin perms player setgroup <player> <group>` | Set a player's group | bdcraft.admin.permissions |
| `/bdadmin perms player addperm <player> <permission>` | Add a permission to a player | bdcraft.admin.permissions |
| `/bdadmin perms player delperm <player> <permission>` | Remove a permission from a player | bdcraft.admin.permissions |
| `/bdadmin perms reload` | Reload permissions configuration | bdcraft.admin.permissions |

## Default Setup

By default, BDCraft comes with the following permission groups:

### Default Group
All new players are automatically assigned to this group. It includes basic permissions for general usage:

```yaml
default:
  permissions:
    - bdcraft.economy.use
    - bdcraft.economy.balance
    - bdcraft.economy.pay
    - bdcraft.market.use
    - bdcraft.auction.use
    - bdcraft.villager.use
    - bdcraft.rank.use
    - bdcraft.home.use
    - bdcraft.home.set
    - bdcraft.home.multiple.1
    - bdcraft.teleport.use
    - bdcraft.teleport.tpa
    - bdcraft.teleport.spawn
    - bdcraft.chat.global
    - bdcraft.chat.local
    - bdcraft.chat.msg
    - bdcraft.chat.reply
    - bdcraft.chat.ignore
```

### VIP Group
A premium tier with extra features:

```yaml
vip:
  inherit: default
  permissions:
    - bdcraft.home.multiple.3
    - bdcraft.teleport.back
    - bdcraft.chat.color
```

### Admin Group
Full administrative access:

```yaml
admin:
  inherit: vip
  permissions:
    - bdcraft.admin
```

## Operator Overrides

Server operators (players listed in the server's `ops.json` file) automatically receive the `bdcraft.admin` permission, regardless of their assigned group. This ensures that server administrators always have full access to the plugin.

## Best Practices

1. **Use Groups**: Instead of assigning individual permissions to players, assign them to groups and then assign players to those groups.

2. **Inheritance**: Utilize the inheritance system to create a hierarchy of permission groups.

3. **Regular Backups**: Make backups of your `permissions.yml` file before making significant changes.

4. **Test Changes**: After making changes to permissions, test them with the `/bdadmin checkperm <player> <permission>` command to ensure they are working as expected.

5. **Document Custom Groups**: Keep documentation of custom groups and their purposes for future reference.

## Troubleshooting

### Common Issues

1. **Permission Not Working**: Use `/bdadmin checkperm <player> <permission>` to check if a player has a specific permission. This will show the effective permission status, including the source (group, individual, or operator).

2. **Group Not Applied**: If a player's group is not applied, ensure that their UUID is correct in the configuration and that the group exists.

3. **Inheritance Issues**: If inherited permissions are not working, check that the parent group is correctly specified and exists.

4. **Negated Permissions**: Remember that negated permissions override inherited permissions. If a player has `-bdcraft.command.use` directly or through their group, they won't be able to use the command even if they inherit the positive permission.

### Support Commands

- `/bdadmin perms debug <player>` - Shows detailed permission information for a player, including all calculated permissions, inheritance chains, and negated permissions.

- `/bdadmin perms check <player> <permission>` - Checks a specific permission for a player, showing whether they have it and how it was determined.