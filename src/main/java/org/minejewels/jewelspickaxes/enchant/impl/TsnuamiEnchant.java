package org.minejewels.jewelspickaxes.enchant.impl;

import net.abyssdev.abysslib.economy.registry.impl.DefaultEconomyRegistry;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import net.abyssdev.me.lucko.helper.Events;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.minejewels.jewelscobblecubes.JewelsCobbleCubes;
import org.minejewels.jewelscobblecubes.cube.block.CobbleCubeBlock;
import org.minejewels.jewelscobblecubes.cube.player.PlayerCobbleCube;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelscobblecubes.events.CobbleCubeSellEvent;
import org.minejewels.jewelscobblecubes.upgrade.CubeUpgrade;
import org.minejewels.jewelscobblecubes.utils.RegionUtils;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

import java.util.Map;

public class TsnuamiEnchant extends Enchant {

    private final JewelsPickaxes plugin;

    public TsnuamiEnchant(final JewelsPickaxes plugin) {
        super(
                "tsunami",
                plugin.getEnchantsConfig().getColoredString("enchants.tsunami.lore.format"),
                plugin.getEnchantsConfig().getInt("enchants.tsunami.levels.max-level"),
                plugin.getEnchantsConfig().getLong("enchants.tsunami.pricing.base-price"),
                plugin.getEnchantsConfig().getDouble("enchants.tsunami.pricing.price-increase"),
                plugin.getEnchantsConfig().getDouble("enchants.tsunami.chance.base-chance"),
                plugin.getEnchantsConfig().getDouble("enchants.tsunami.chance.chance-increase")
        );

        this.plugin = plugin;
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

        final PlayerCobbleCube cobbleCube = event.getPlayerCobbleCube();

        if (!cobbleCube.getBrokenLocations().isEmpty()) {
            cobbleCube.reset(JewelsCobbleCubes.get());
        }

        final CubeUpgrade storageUpgrade = JewelsCobbleCubes.get().getUpgradeRegistry().get("STORAGE").get();

        final double maxStorage = storageUpgrade.getAmount(cobbleCube.getLevel(storageUpgrade));

        int destoryCount = 1;
        int storageCount = 1;

        for (Map.Entry<CobbleCubeBlock, Long> entry : cobbleCube.getBlockStorage().entrySet()) {
            storageCount += entry.getValue();
        }

        for (final Block block : RegionUtils.getBlocksWithinRegion(cobbleCube.getCubeRegion())) {
            storageCount++;
            if (storageCount > maxStorage) continue;

            if (!cobbleCube.isAutosellEnabled()) {
                cobbleCube.addDrop(block.getType());
            }

            destoryCount++;
        }

        long currentValue = 0;

        for (Map.Entry<CobbleCubeBlock, Long> entry : cobbleCube.getBlockStorage().entrySet()) {
            currentValue += entry.getKey().getPrice() * entry.getValue();
        }

        if (cobbleCube.isAutosellEnabled()) {
            DefaultEconomyRegistry.get().getEconomy("vault").addBalance(player, currentValue);

            final CobbleCubeSellEvent sellEvent = new CobbleCubeSellEvent(
                    player,
                    cobbleCube,
                    currentValue
            );

            Events.call(sellEvent);
        }

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%amount%", Utils.format(destoryCount));

        this.plugin.getMessageCache().sendMessage(player, "messages.tsunami-activated", replacer);

        player.performCommand("realm");
    }
}
