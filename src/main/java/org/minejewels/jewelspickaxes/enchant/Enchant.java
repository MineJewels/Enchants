package org.minejewels.jewelspickaxes.enchant;

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;

import java.util.concurrent.ThreadLocalRandom;

@Data
public abstract class Enchant {

    private final String name, loreFormat;
    private final int maxLevel, requiredLevel;
    private final long basePrice;
    private final double priceIncrease, baseChance, chanceIncrease;

    public abstract void onEquip(final Player player, final ItemStack item, final int level);
    public abstract void onUnequip(final Player player, final ItemStack item, final int level);
    public abstract void onBreak(final CobbleCubeBreakEvent event, final Player player, final int level);

    public double getChance(final int level) {
        if (level == 0) return 0;
        return this.baseChance + (this.chanceIncrease * (level - 1));
    }

    public double getCost(final int level) {
        return this.basePrice * (this.priceIncrease + (level - 1));
    }

    public boolean hasChance(final int level) {
        if (this.baseChance == 100.0) return true;
        return this.getChance(level) >= ThreadLocalRandom.current().nextDouble(100);
    }
}
