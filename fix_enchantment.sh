#!/bin/bash
sed -i 's/meta.addEnchant(Enchantment.DURABILITY, 1, true);/Enchantment unbreaking = getUnbreakingEnchantment(); if (unbreaking != null) { meta.addEnchant(unbreaking, 1, true); }/' src/main/java/com/bdcraft/plugin/modules/economy/items/BDItemManager.java
chmod +x fix_enchantment.sh
