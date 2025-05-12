#!/bin/bash

# Fix Enchantment.getById() calls (deprecated in newer Bukkit versions)
# This script replaces Enchantment.getById() with Registry.ENCHANTMENT.get(NamespacedKey)

echo "Checking for Enchantment.getById() usage..."

# Look for Enchantment.getById in java files
FILES=$(grep -l "Enchantment\.getById" $(find src -name "*.java"))

if [ -z "$FILES" ]; then
    echo "No files found using Enchantment.getById"
    exit 0
fi

for FILE in $FILES; do
    echo "Processing file: $FILE"
    
    # Add necessary imports
    if ! grep -q "org.bukkit.NamespacedKey" $FILE; then
        sed -i '1,/^$/s/^import org.bukkit.Enchantment;/import org.bukkit.Enchantment;\nimport org.bukkit.NamespacedKey;\nimport org.bukkit.Registry;/' $FILE
    fi
    
    # Replace getById usage with Registry
    # Find all Enchantment.getById(ID) calls and convert them to Registry.ENCHANTMENT.get(NamespacedKey.minecraft("name"))
    # This is a basic conversion and might need manual adjustment for complex cases
    
    # Common enchantment IDs to names mapping for Minecraft
    # This is a simplified set - full migration might require checking the complete enchantment list
    sed -i 's/Enchantment\.getById(0)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("protection"))/g' $FILE
    sed -i 's/Enchantment\.getById(1)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("fire_protection"))/g' $FILE
    sed -i 's/Enchantment\.getById(2)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("feather_falling"))/g' $FILE
    sed -i 's/Enchantment\.getById(3)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("blast_protection"))/g' $FILE
    sed -i 's/Enchantment\.getById(4)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("projectile_protection"))/g' $FILE
    sed -i 's/Enchantment\.getById(5)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("respiration"))/g' $FILE
    sed -i 's/Enchantment\.getById(6)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("aqua_affinity"))/g' $FILE
    sed -i 's/Enchantment\.getById(7)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("thorns"))/g' $FILE
    sed -i 's/Enchantment\.getById(8)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("depth_strider"))/g' $FILE
    sed -i 's/Enchantment\.getById(9)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("frost_walker"))/g' $FILE
    sed -i 's/Enchantment\.getById(16)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("sharpness"))/g' $FILE
    sed -i 's/Enchantment\.getById(17)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("smite"))/g' $FILE
    sed -i 's/Enchantment\.getById(18)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("bane_of_arthropods"))/g' $FILE
    sed -i 's/Enchantment\.getById(19)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("knockback"))/g' $FILE
    sed -i 's/Enchantment\.getById(20)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("fire_aspect"))/g' $FILE
    sed -i 's/Enchantment\.getById(21)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("looting"))/g' $FILE
    sed -i 's/Enchantment\.getById(32)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("efficiency"))/g' $FILE
    sed -i 's/Enchantment\.getById(33)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("silk_touch"))/g' $FILE
    sed -i 's/Enchantment\.getById(34)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("unbreaking"))/g' $FILE
    sed -i 's/Enchantment\.getById(35)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("fortune"))/g' $FILE
    sed -i 's/Enchantment\.getById(48)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("power"))/g' $FILE
    sed -i 's/Enchantment\.getById(49)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("punch"))/g' $FILE
    sed -i 's/Enchantment\.getById(50)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("flame"))/g' $FILE
    sed -i 's/Enchantment\.getById(51)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("infinity"))/g' $FILE
    sed -i 's/Enchantment\.getById(61)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("luck_of_the_sea"))/g' $FILE
    sed -i 's/Enchantment\.getById(62)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("lure"))/g' $FILE
    sed -i 's/Enchantment\.getById(70)/Registry.ENCHANTMENT.get(NamespacedKey.minecraft("mending"))/g' $FILE
    
    echo "Updated $FILE"
done

echo "Enchantment fixes completed."