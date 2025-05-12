#!/bin/bash

# Fix deprecated Player lookup methods
# This script replaces Bukkit.getPlayer(String) with Bukkit.getPlayerExact(String)

echo "Checking for Player lookup methods..."

# Look for Bukkit.getPlayer(String) patterns
FILES=$(grep -l "Bukkit\.getPlayer(" $(find src -name "*.java" | grep -v "TeleportCommands.java" | grep -v "BDEconomyAPI.java"))

if [ -z "$FILES" ]; then
    echo "No files found using deprecated player lookups."
    exit 0
fi

for FILE in $FILES; do
    echo "Processing file: $FILE"
    
    # Replace lookups with getPlayerExact
    # This only replaces simple string parameter cases, complex cases require manual review
    sed -i 's/Bukkit\.getPlayer(\([^)]*\))/Bukkit.getPlayerExact(\1)/g' $FILE
    
    # Add explanatory comment
    sed -i '/Bukkit\.getPlayerExact/i\\n    \/\/ Use getPlayerExact for exact name matching' $FILE
    
    echo "Updated $FILE"
done

echo "Player lookup fixes completed."