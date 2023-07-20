package org.minejewels.jewelspickaxes.enchant.impl;

import net.abyssdev.abysslib.economy.registry.impl.DefaultEconomyRegistry;
import net.abyssdev.abysslib.location.LocationSerializer;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Region;
import net.abyssdev.abysslib.utils.Utils;
import net.abyssdev.me.lucko.helper.Events;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelscobblecubes.JewelsCobbleCubes;
import org.minejewels.jewelscobblecubes.cube.block.CobbleCubeBlock;
import org.minejewels.jewelscobblecubes.cube.player.PlayerCobbleCube;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelscobblecubes.events.CobbleCubeSellEvent;
import org.minejewels.jewelscobblecubes.upgrade.CubeUpgrade;
import org.minejewels.jewelscobblecubes.utils.RegionUtils;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JackhammerEnchant extends Enchant {

    private final JewelsPickaxes plugin;

    public JackhammerEnchant(final JewelsPickaxes plugin) {
        super(
                "jackhammer",
                plugin.getEnchantsConfig().getColoredString("enchants.jackhammer.lore.format"),
                plugin.getEnchantsConfig().getInt("enchants.jackhammer.levels.max-level"),
                plugin.getEnchantsConfig().getLong("enchants.jackhammer.pricing.base-price"),
                plugin.getEnchantsConfig().getDouble("enchants.jackhammer.pricing.price-increase"),
                plugin.getEnchantsConfig().getDouble("enchants.jackhammer.chance.base-chance"),
                plugin.getEnchantsConfig().getDouble("enchants.jackhammer.chance.chance-increase")
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

       if (!cobbleCube.isAutosellEnabled()) {
           for (final Block block : this.getBlocksAtLayer(cobbleCube.getCubeRegion(), event.getEvent().getBlock().getY())) {
               cobbleCube.addDrop(block.getType());
           }
       }

       JewelsCobbleCubes.get().getBlockHandler().clearBlocksFast(this.getLocationsAtLayer(cobbleCube.getCubeRegion(), event.getEvent().getBlock().getY()));

        for (final Location location : this.getLocationsAtLayer(cobbleCube.getCubeRegion(), event.getEvent().getBlock().getY())) {
            cobbleCube.getBrokenLocations().add(LocationSerializer.serialize(location));
        }
    }

    private Set<Block> getBlocksAtLayer(Region region, int layerY) {
        Set<Block> blocks = new HashSet<>();

        for (int x = region.getMinX(); x <= region.getMaxX(); ++x) {
            for (int z = region.getMinZ(); z <= region.getMaxZ(); ++z) {
                Block block = region.getWorld().getBlockAt(x, layerY, z);
                blocks.add(block);
            }
        }

        return blocks;
    }

    private Set<Location> getLocationsAtLayer(Region region, int layerY) {
        Set<Location> blocks = new HashSet<>();

        for (int x = region.getMinX(); x <= region.getMaxX(); ++x) {
            for (int z = region.getMinZ(); z <= region.getMaxZ(); ++z) {
                Location block = new Location(region.getWorld(), x, layerY, z);
                blocks.add(block);
            }
        }

        return blocks;
    }

}
