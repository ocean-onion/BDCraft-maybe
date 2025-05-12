#!/bin/bash

# Fix indentation for requester lines
sed -i '135s/^        /                /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '136s/^            /                    /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '137s/^        /                /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java

sed -i '134i\                // Use the server method for UUID-based player lookup' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java

# Fix the second instance
sed -i '176,184s/Bukkit.getPlayer/Bukkit.getServer().getPlayer/g' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '181s/^        /                /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '182s/^            /                    /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '183s/^        /                /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java

sed -i '180i\                // Use the server method for UUID-based player lookup' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java

echo "Fixed requester indentation and added comments"