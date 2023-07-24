package org.minejewels.jewelspickaxes.enchant.impl;

import net.abyssdev.abysslib.caged.MathUtility;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.storage.Storage;
import net.abyssdev.abysslib.utils.PlayerUtils;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelsminerals.JewelsMinerals;
import org.minejewels.jewelsminerals.player.MineralPlayer;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

import java.util.List;
import java.util.UUID;

public class KeyFinderEnchant extends Enchant {

    private final JewelsPickaxes plugin;
    private final List<String> commands;

    public KeyFinderEnchant(final JewelsPickaxes plugin) {
        super(
                "key_finder",
                plugin.getEnchantsConfig().getColoredString("enchants.key_finder.lore.format"),
                plugin.getEnchantsConfig().getInt("enchants.key_finder.levels.max-level"),
                plugin.getEnchantsConfig().getInt("enchants.key_finder.levels.level-required"),
                plugin.getEnchantsConfig().getLong("enchants.key_finder.pricing.base-price"),
                plugin.getEnchantsConfig().getDouble("enchants.key_finder.pricing.price-increase"),
                plugin.getEnchantsConfig().getDouble("enchants.key_finder.chance.base-chance"),
                plugin.getEnchantsConfig().getDouble("enchants.key_finder.chance.chance-increase")
        );

        this.plugin = plugin;

        this.commands = plugin.getEnchantsConfig().getStringList("enchants.key_finder.rewards");
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

        final String command = this.commands.get(MathUtility.getRandomNumber(0, this.commands.size()-1));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
    }
}
