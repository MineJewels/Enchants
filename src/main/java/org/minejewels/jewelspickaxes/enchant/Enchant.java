package org.minejewels.jewelspickaxes.enchant;

import lombok.Data;
import org.bukkit.entity.Player;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;

@Data
public abstract class Enchant {

    private final String name, loreFormat;
    private final boolean levelRequired;
    private final int levelRequirement, maxLevel;
    private final long basePrice;
    private final double priceIncrease;

    public abstract void onEquip(final Player player, final int level);
    public abstract void onUnequip(final Player player, final int level);
    public abstract void onBreak(final CobbleCubeBreakEvent event, final Player player, final int level);

}
