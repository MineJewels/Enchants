package org.minejewels.jewelspickaxes.enchant.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

public class EfficiencyEnchant extends Enchant {

    public EfficiencyEnchant(final JewelsPickaxes plugin) {
        super(
                "efficiency",
                plugin.getEnchantsConfig().getColoredString("enchants.efficiency.lore.format"),
                plugin.getEnchantsConfig().getInt("enchants.efficiency.levels.max-level"),
                plugin.getEnchantsConfig().getLong("enchants.efficiency.pricing.base-price"),
                plugin.getEnchantsConfig().getDouble("enchants.efficiency.pricing.price-increase"),
                plugin.getEnchantsConfig().getDouble("enchants.efficiency.chance.base-chance"),
                plugin.getEnchantsConfig().getDouble("enchants.efficiency.chance.chance-increase")
        );
    }

    @Override
    public void onEquip(final Player player, final ItemStack item, final int level) {
        player.sendMessage("Equipped");
    }

    @Override
    public void onUnequip(Player player, final ItemStack item,  int level) {
        player.sendMessage("Unequipped");
    }

    @Override
    public void onBreak(CobbleCubeBreakEvent event, Player player, int level) {
        player.sendMessage("Mined");
    }
}
