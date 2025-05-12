#!/bin/bash

# Fix indentation issues in TeleportCommands.java
sed -i '72s/^        /                /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '73s/^            /                    /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '74s/^        /                /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java

sed -i '228s/^        /                /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '229s/^            /                    /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java
sed -i '230s/^        /                /' src/main/java/com/bdcraft/plugin/modules/vital/commands/TeleportCommands.java

echo "Indentation fix complete."