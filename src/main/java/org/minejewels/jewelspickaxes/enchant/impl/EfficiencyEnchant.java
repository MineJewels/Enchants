package org.minejewels.jewelspickaxes.enchant.impl;

import org.bukkit.entity.Player;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

public class EfficiencyEnchant extends Enchant {

    public EfficiencyEnchant(final JewelsPickaxes plugin) {
        super(
                "efficiency",
                plugin.getEnchantsConfig().getColoredString("enchants.efficiency.lore.format"),
                plugin.getEnchantsConfig().getBoolean("enchants.efficiency.requirements.enabled"),
                plugin.getEnchantsConfig().getInt("enchants.efficiency.requirements.enabled"),
                plugin.getEnchantsConfig().getInt("enchants.efficiency.levels.max-level"),
                plugin.getEnchantsConfig().getLong("enchants.efficiency.pricing.base-price"),
                plugin.getEnchantsConfig().getDouble("enchants.efficiency.pricing.price-increase")
        );
    }

    @Override
    public void onEquip(final Player player, final int level) {

    }

    @Override
    public void onUnequip(Player player, int level) {

    }

    @Override
    public void onBreak(CobbleCubeBreakEvent event, Player player, int level) {

    }
}
