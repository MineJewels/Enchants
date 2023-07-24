package org.minejewels.jewelspickaxes.enchant.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

public class JumpEnchant extends Enchant {

    public JumpEnchant(final JewelsPickaxes plugin) {
        super(
                "jump",
                plugin.getEnchantsConfig().getColoredString("enchants.jump.lore.format"),
                plugin.getEnchantsConfig().getInt("enchants.jump.levels.max-level"),
                plugin.getEnchantsConfig().getInt("enchants.jump.levels.level-required"),
                plugin.getEnchantsConfig().getLong("enchants.jump.pricing.base-price"),
                plugin.getEnchantsConfig().getDouble("enchants.jump.pricing.price-increase"),
                plugin.getEnchantsConfig().getDouble("enchants.jump.chance.base-chance"),
                plugin.getEnchantsConfig().getDouble("enchants.jump.chance.chance-increase")
        );
    }

    @Override
    public void onEquip(final Player player, final ItemStack item, final int level) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, level - 1, true, true));
    }

    @Override
    public void onUnequip(Player player, final ItemStack item,  int level) {
        player.removePotionEffect(PotionEffectType.JUMP);
    }

    @Override
    public void onBreak(CobbleCubeBreakEvent event, Player player, int level) {
        
    }
}
