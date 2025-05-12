#!/bin/bash

# Update Bukkit.getServer().getPlayer() to Bukkit.getPlayerExact() in TeleportCommands.java
sed -i '71s/Bukkit.getServer().getPlayer(targetName)/Bukkit.getPlayerExact(targetName)/' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java 
sed -i '227s/Bukkit.getServer().getPlayer(targetName)/Bukkit.getPlayerExact(targetName)/' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java

# Add comment above each line
sed -i '70a\                \/\/ Use the more specific method for exact player name matching' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '226a\                \/\/ Use the more specific method for exact player name matching' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java

# Remove the now redundant isOnline check since getPlayerExact already guarantees an online player
sed -i '72,74d' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '228,230d' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java

echo "Updated to getPlayerExact() in TeleportCommands.java"