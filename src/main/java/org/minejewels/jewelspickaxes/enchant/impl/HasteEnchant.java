package org.minejewels.jewelspickaxes.enchant.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

public class HasteEnchant extends Enchant {

    public HasteEnchant(final JewelsPickaxes plugin) {
        super(
                "haste",
                plugin.getEnchantsConfig().getColoredString("enchants.haste.lore.format"),
                plugin.getEnchantsConfig().getInt("enchants.haste.levels.max-level"),
                plugin.getEnchantsConfig().getLong("enchants.haste.pricing.base-price"),
                plugin.getEnchantsConfig().getDouble("enchants.haste.pricing.price-increase"),
                plugin.getEnchantsConfig().getDouble("enchants.haste.chance.base-chance"),
                plugin.getEnchantsConfig().getDouble("enchants.haste.chance.chance-increase")
        );
    }

    @Override
    public void onEquip(final Player player, final ItemStack item, final int level) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level - 1, true, true));
    }

    @Override
    public void onUnequip(Player player, final ItemStack item,  int level) {
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }

    @Override
    public void onBreak(CobbleCubeBreakEvent event, Player player, int level) {
        
    }
}
