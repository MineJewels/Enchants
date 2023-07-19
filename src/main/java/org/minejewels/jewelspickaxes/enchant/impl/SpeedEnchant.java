package org.minejewels.jewelspickaxes.enchant.impl;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

public class SpeedEnchant extends Enchant {

    public SpeedEnchant(final JewelsPickaxes plugin) {
        super(
                "speed",
                plugin.getEnchantsConfig().getColoredString("enchants.speed.lore.format"),
                plugin.getEnchantsConfig().getInt("enchants.speed.levels.max-level"),
                plugin.getEnchantsConfig().getLong("enchants.speed.pricing.base-price"),
                plugin.getEnchantsConfig().getDouble("enchants.speed.pricing.price-increase"),
                plugin.getEnchantsConfig().getDouble("enchants.speed.chance.base-chance"),
                plugin.getEnchantsConfig().getDouble("enchants.speed.chance.chance-increase")
        );
    }

    @Override
    public void onEquip(final Player player, final ItemStack item, final int level) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, level - 1, true, true));
    }

    @Override
    public void onUnequip(Player player, final ItemStack item,  int level) {
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public void onBreak(CobbleCubeBreakEvent event, Player player, int level) {
        
    }
}
