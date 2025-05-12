#!/bin/bash

# Fix deprecated methods in the BDCraft codebase

echo "Checking for deprecated methods..."

# Define patterns and their replacements
declare -A replacements=(
  ["player\.getItemInHand()"]="player.getInventory().getItemInMainHand()"
  ["player\.setItemInHand"]="player.getInventory().setItemInMainHand"
  ["event\.getPlayer()\.getItemInHand()"]="event.getPlayer().getInventory().getItemInMainHand()"
  ["getInventory()\.addItem"]="getInventory().addItem" # No change, but we'll add proper return handling
)

# Loop through all Java files
find src -name "*.java" | while read FILE; do
  modified=false
  
  # Check each pattern
  for pattern in "${!replacements[@]}"; do
    replacement="${replacements[$pattern]}"
    
    # If the file contains the pattern
    if grep -q "$pattern" "$FILE"; then
      echo "Processing $FILE for pattern: $pattern"
      
      # Replace the pattern
      sed -i "s/$pattern/$replacement/g" "$FILE"
      
      # For inventory.addItem, add code to handle return HashMap
      if [[ "$pattern" == *"getInventory()\.addItem"* ]]; then
        # Find all lines with addItem and check if they handle the return
        while read -r line_num; do
          # Get the line content
          line=$(sed -n "${line_num}p" "$FILE")
          
          # If the line doesn't handle the return HashMap
          if ! echo "$line" | grep -q "HashMap<Integer, ItemStack>" && ! echo "$line" | grep -q "Map<Integer, ItemStack>"; then
            indent=$(echo "$line" | sed 's/[^ ].*//')
            
            # Add a comment above the line
            sed -i "${line_num}i\\${indent}// Handle inventory overflow return" "$FILE"
            
            # Add code to handle return below the line
            sed -i "${line_num}a\\${indent}// TODO: Add proper handling for items that couldn't fit in inventory" "$FILE"
          fi
        done < <(grep -n "getInventory()\.addItem" "$FILE" | cut -d: -f1)
      fi
      
      modified=true
    fi
  done
  
  # Check for getItemInHand method in event
  if grep -q "getItemInHand()" "$FILE" && ! grep -q "getInventory()\.getItemInMainHand()" "$FILE"; then
    echo "Adding import for EquipmentSlot in $FILE"
    sed -i '/^import/a import org.bukkit.inventory.EquipmentSlot;' "$FILE"
    modified=true
  fi
  
  # If any modifications were made
  if [ "$modified" = true ]; then
    echo "Updated $FILE"
  fi
done

echo "Deprecated method fixes completed."