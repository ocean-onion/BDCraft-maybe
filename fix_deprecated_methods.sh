#!/bin/bash

# Fix all instances of deprecated Bukkit.getPlayer(UUID) method
echo "Fixing Bukkit.getPlayer(UUID) methods..."
find src -type f -name "*.java" -exec sed -i 's/Player \([a-zA-Z0-9]*\) = Bukkit.getPlayer(\([a-zA-Z0-9_.()]*\));/Player \1 = Bukkit.getServer().getPlayer(\2);\n        if (\1 != null \&\& !\1.isOnline()) {\n            \1 = null; \/\/ Ensure consistency with old behavior\n        }/g' {} \;

# Fix all instances of deprecated Player.getItemInHand() method
echo "Fixing getItemInHand methods..."
find src -type f -name "*.java" -exec sed -i 's/\(event\|player\).getItemInHand()/\1.getInventory().getItemInMainHand()/g' {} \;

# Fix all instances of deprecated player.updateInventory() method
echo "Fixing updateInventory methods..."
find src -type f -name "*.java" -exec sed -i 's/player.updateInventory()/player.getInventory().setContents(player.getInventory().getContents())/g' {} \;

# Fix all instances of setting null in inventory.setItem
echo "Fixing inventory null references..."
find src -type f -name "*.java" -exec sed -i 's/inventory.setItem([^,]*,[ ]*null)/inventory.setItem(\1, new ItemStack(Material.AIR))/g' {} \;

echo "Done fixing deprecated methods."