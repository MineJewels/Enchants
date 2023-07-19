package org.minejewels.jewelspickaxes.enchant;

import lombok.Data;
import org.bukkit.entity.Player;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;

import java.util.concurrent.ThreadLocalRandom;

@Data
public abstract class Enchant {

    private final String name, loreFormat;
    private final int maxLevel;
    private final long basePrice;
    private final double priceIncrease, baseChance, chanceIncrease;

    public abstract void onEquip(final Player player, final int level);
    public abstract void onUnequip(final Player player, final int level);
    public abstract void onBreak(final CobbleCubeBreakEvent event, final Player player, final int level);

    private double getChance(final double level) {
        return this.baseChance * (this.chanceIncrease + (level - 1));
    }

    private boolean hasChance(final int level) {
        if (this.baseChance == 100.0) return true;
        return this.getChance(level) >= ThreadLocalRandom.current().nextDouble(100);
    }
}
