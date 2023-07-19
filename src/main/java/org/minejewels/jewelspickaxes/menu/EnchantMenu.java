package org.minejewels.jewelspickaxes.menu;

import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.menu.item.MenuItemBuilder;
import net.abyssdev.abysslib.menu.item.MenuItemStack;
import net.abyssdev.abysslib.menu.templates.AbyssMenu;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import net.abyssdev.abysslib.utils.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelsminerals.JewelsMinerals;
import org.minejewels.jewelsminerals.player.MineralPlayer;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;
import org.minejewels.jewelspickaxes.enchant.utils.EnchantUtils;

import java.util.UUID;

public class EnchantMenu extends AbyssMenu {

    private final JewelsPickaxes plugin;
    private final EnchantUtils enchantUtils;

    private final ItemBuilder purchasableItem, lockedItem, maxLevelItem;

    private final MenuItemStack backItem;

    public EnchantMenu(final JewelsPickaxes plugin) {
        super(plugin.getMenusConfig(), "enchant-menu.");

        this.plugin = plugin;
        this.enchantUtils = plugin.getEnchantUtils();

        this.purchasableItem = plugin.getMenusConfig().getItemBuilder("enchant-menu.items.purchasable");
        this.lockedItem = plugin.getMenusConfig().getItemBuilder("enchant-menu.items.not-enough");
        this.maxLevelItem = plugin.getMenusConfig().getItemBuilder("enchant-menu.items.max-level");

        this.backItem = new MenuItemStack(
                plugin.getMenusConfig().getItemStack("enchant-menu.special-items.back.item"),
                plugin.getMenusConfig().getInt("enchant-menu.special-items.back.slot")
        );
    }

    public void open(final Player player, final Enchant enchant, final UUID pickaxeUUID) {

        final MenuBuilder builder = this.createBase();

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%enchant%", WordUtils.formatText(enchant.getName().replace("_", " ")));

        if (this.enchantUtils.getPickaxeFromUUID(player, pickaxeUUID) ==  null) {
            this.plugin.getMessageCache().sendMessage(player, "messages.pickaxe-in-inventory");
            return;
        }
        final ItemStack pickaxe = this.enchantUtils.getPickaxeFromUUID(player, pickaxeUUID);

        if (this.plugin.getMenusConfig().getBoolean("enchant-menu.display-pickaxe.enabled")) {
            builder.setItem(this.plugin.getMenusConfig().getInt("enchant-menu.display-pickaxe.slot"), pickaxe);
        }

        builder.setItem(this.backItem.getSlot(), this.backItem.getItem());

        builder.addClickEvent(this.backItem.getSlot(), event -> this.plugin.getPickaxeMenu().open(player, pickaxeUUID));

        final int enchantLevel = this.enchantUtils.getLevel(pickaxe, enchant);

        for (String key : this.plugin.getMenusConfig().getSectionKeys("enchant-menu.levels")) {

            final int level = Integer.parseInt(key);
            final int slot = this.plugin.getMenusConfig().getInt("enchant-menu.levels." + key);

            if (level + enchantLevel > enchant.getMaxLevel()) {
                builder.setItem(slot, this.maxLevelItem.setHideItemFlags(true).parse(replacer.addPlaceholder("%level%", Utils.format(level))));
                continue;
            }

            final MineralPlayer mineralPlayer =  JewelsMinerals.get().getPlayerStorage().get(player.getUniqueId());

            if (enchant.getCost(level + enchantLevel) > mineralPlayer.getTokens()) {
                builder.setItem(slot, this.lockedItem.setHideItemFlags(true).parse(replacer
                        .addPlaceholder("%level%", Utils.format(level))
                        .addPlaceholder("%new-level%", Utils.format(level + enchantLevel))
                        .addPlaceholder("%cost%", Utils.format(enchant.getCost(level + enchantLevel)))
                        .addPlaceholder("%amount%", Utils.format(mineralPlayer.getTokensNeeded(Math.round(enchant.getCost(level + enchantLevel)))))));
                continue;
            }

            builder.setItem(slot, this.purchasableItem.setHideItemFlags(true).parse(replacer
                    .addPlaceholder("%level%", Utils.format(level))
                    .addPlaceholder("%new-level%", Utils.format(level + enchantLevel))
                    .addPlaceholder("%cost%", Utils.format(enchant.getCost(level + enchantLevel)))));

            builder.addClickEvent(slot, event -> {
                mineralPlayer.removeTokens(Math.round(enchant.getCost(level + enchantLevel)));
                this.plugin.getEnchantUtils().addLevel(player, pickaxe, enchant, level);

                this.open(player, enchant, pickaxeUUID);
            });
        }

        player.openInventory(builder.build(replacer));
    }
}
