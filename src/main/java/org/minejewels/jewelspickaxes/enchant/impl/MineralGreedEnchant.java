package org.minejewels.jewelspickaxes.enchant.impl;

import net.abyssdev.abysslib.caged.MathUtility;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.storage.Storage;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelsminerals.JewelsMinerals;
import org.minejewels.jewelsminerals.player.MineralPlayer;
import org.minejewels.jewelsminerals.player.storage.MineralPlayerStorage;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

import java.util.UUID;

public class MineralGreedEnchant extends Enchant {

    private final JewelsPickaxes plugin;
    private final Storage<UUID, MineralPlayer> playerStorage;
    private final long baseMinimum, baseMaximum;
    private final double amountIncrease;

    public MineralGreedEnchant(final JewelsPickaxes plugin) {
        super(
                "mineral_greed",
                plugin.getEnchantsConfig().getColoredString("enchants.mineral_greed.lore.format"),
                plugin.getEnchantsConfig().getInt("enchants.mineral_greed.levels.max-level"),
                plugin.getEnchantsConfig().getLong("enchants.mineral_greed.pricing.base-price"),
                plugin.getEnchantsConfig().getDouble("enchants.mineral_greed.pricing.price-increase"),
                plugin.getEnchantsConfig().getDouble("enchants.mineral_greed.chance.base-chance"),
                plugin.getEnchantsConfig().getDouble("enchants.mineral_greed.chance.chance-increase")
        );

        this.plugin = plugin;
        this.playerStorage = JewelsMinerals.get().getPlayerStorage();

        this.baseMinimum = plugin.getEnchantsConfig().getLong("enchants.mineral_greed.amount.base-minimum");
        this.baseMaximum = plugin.getEnchantsConfig().getLong("enchants.mineral_greed.amount.base-maximum");
        this.amountIncrease = plugin.getEnchantsConfig().getDouble("enchants.mineral_greed.amount.increase");
    }

    @Override
    public void onEquip(final Player player, final ItemStack item, final int level) {

    }

    @Override
    public void onUnequip(Player player, final ItemStack item,  int level) {

    }

    @Override
    public void onBreak(CobbleCubeBreakEvent event, Player player, int level) {
        if (!this.hasChance(level)) return;

        final long amount = this.getAmount(level);

        this.playerStorage.get(player.getUniqueId()).addTokens(amount);

        this.plugin.getMessageCache().sendMessage(player, "messages.mineral-greed-activated", new PlaceholderReplacer()
                .addPlaceholder("%amount%", Utils.format(amount)));
    }

    private long getAmount(final int level) {

        final long minimum = Math.round(this.baseMinimum * (this.amountIncrease * (level - 1)));
        final long maximum = Math.round(this.baseMaximum * (this.amountIncrease * (level - 1)));

        return Math.round(MathUtility.getRandomNumber(minimum, maximum));
    }
}
