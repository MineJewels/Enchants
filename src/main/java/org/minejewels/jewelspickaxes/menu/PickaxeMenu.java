package org.minejewels.jewelspickaxes.menu;

import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.menu.templates.AbyssMenu;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;
import org.minejewels.jewelspickaxes.enchant.utils.EnchantUtils;
import org.minejewels.jewelspickaxes.menu.items.PickaxeMenuItem;

import java.util.Optional;
import java.util.UUID;

public class PickaxeMenu extends AbyssMenu {

    private final JewelsPickaxes plugin;
    private final EnchantUtils enchantUtils;

    public PickaxeMenu(final JewelsPickaxes plugin) {
        super(plugin.getMenusConfig(), "main-menu.");

        this.plugin = plugin;
        this.enchantUtils = plugin.getEnchantUtils();
    }

    public void open(final Player player, final UUID pickaxeUUID) {
        final MenuBuilder builder = this.createBase();

        if (this.enchantUtils.getPickaxeFromUUID(player, pickaxeUUID) ==  null) {
            this.plugin.getMessageCache().sendMessage(player, "messages.pickaxe-in-inventory");
            return;
        }

        final ItemStack pickaxe = this.enchantUtils.getPickaxeFromUUID(player, pickaxeUUID);

        for (final String key : this.plugin.getMenusConfig().getSectionKeys("main-menu.enchants")) {

            final Optional<Enchant> optionalEnchant = this.plugin.getEnchantRegistry().get(key.toUpperCase());

            if (!optionalEnchant.isPresent()) continue;

            final Enchant enchant = optionalEnchant.get();

            final PickaxeMenuItem item = new PickaxeMenuItem(
                    enchant,
                    this.plugin.getMenusConfig().getItemBuilder("main-menu.enchants." + key.toUpperCase() + ".item"),
                    this.plugin.getMenusConfig().getInt("main-menu.enchants." + key.toUpperCase() + ".slot")
            );

            final PlaceholderReplacer replacer = new PlaceholderReplacer()
                    .addPlaceholder("%chance%", Utils.format(enchant.getChance(this.enchantUtils.getLevel(pickaxe, enchant))))
                    .addPlaceholder("%cost%", Utils.format(enchant.getCost(this.enchantUtils.getLevel(pickaxe, enchant) + 1)))
                    .addPlaceholder("%current%", Utils.format(this.enchantUtils.getLevel(pickaxe, enchant)))
                    .addPlaceholder("%max%", Utils.format(enchant.getMaxLevel()));

            builder.setItem(item.getSlot(), item.getItem().parse(replacer));

            builder.addClickEvent(item.getSlot(), event -> {

                if (this.enchantUtils.getPickaxeFromUUID(player, pickaxeUUID) ==  null) {
                    this.plugin.getMessageCache().sendMessage(player, "messages.pickaxe-in-inventory");
                    return;
                }

                final int level = this.enchantUtils.getLevel(pickaxe, enchant);

                if (level >= enchant.getMaxLevel()) {
                    // setMaxLevel temp item
                    return;
                }

                // Open Menu here
            });
        }

        player.openInventory(builder.build());
    }
}
