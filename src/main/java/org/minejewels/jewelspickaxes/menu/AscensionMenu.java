package org.minejewels.jewelspickaxes.menu;

import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.menu.item.MenuItemBuilder;
import net.abyssdev.abysslib.menu.templates.AbyssMenu;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.ascend.utils.AscendUtil;

import java.util.List;
import java.util.UUID;

public class AscensionMenu extends AbyssMenu {

    private final JewelsPickaxes plugin;

    private final List<Integer> leftSideSlots, rightSideSlots;
    private final int currentPickaxeSlot, newPickaxeSlot;

    private final MenuItemBuilder confirmPurchasable, confirmNotPurchasable, confirmMaxLevel;
    private final MenuItemBuilder cancelPurchasable, cancelNotPurchasable, cancelMaxLevel;
    private final MenuItemBuilder infoPurchasable, infoNotPurchasable, infoMaxLevel;
    private final ItemBuilder leftSidePurchasable, leftSideNotPurchasable, leftSideMaxLevel;
    private final ItemBuilder rightSidePurchasable, rightSideNotPurchasable, rightSideMaxLevel;

    public AscensionMenu(final JewelsPickaxes plugin) {
        super(plugin.getMenusConfig(), "ascend-menu.");

        this.plugin = plugin;
        this.leftSideSlots = plugin.getMenusConfig().getIntegerList("ascend-menu.left-side");
        this.rightSideSlots = plugin.getMenusConfig().getIntegerList("ascend-menu.right-side");

        this.currentPickaxeSlot = plugin.getMenusConfig().getInt("ascend-menu.constant-items.current-pickaxe");
        this.newPickaxeSlot = plugin.getMenusConfig().getInt("ascend-menu.constant-items.new-pickaxe");

        this.confirmPurchasable = new MenuItemBuilder(plugin.getMenusConfig().getItemBuilder("ascend-menu.items.confirm.purchasable.item"), plugin.getMenusConfig().getInt("ascend-menu.items.confirm.purchasable.slot"));
        this.confirmNotPurchasable = new MenuItemBuilder(plugin.getMenusConfig().getItemBuilder("ascend-menu.items.confirm.not-purchasable.item"), plugin.getMenusConfig().getInt("ascend-menu.items.confirm.not-purchasable.slot"));
        this.confirmMaxLevel = new MenuItemBuilder(plugin.getMenusConfig().getItemBuilder("ascend-menu.items.confirm.max-level.item"), plugin.getMenusConfig().getInt("ascend-menu.items.confirm.max-level.slot"));

        this.cancelPurchasable = new MenuItemBuilder(plugin.getMenusConfig().getItemBuilder("ascend-menu.items.cancel.purchasable.item"), plugin.getMenusConfig().getInt("ascend-menu.items.cancel.purchasable.slot"));
        this.cancelNotPurchasable = new MenuItemBuilder(plugin.getMenusConfig().getItemBuilder("ascend-menu.items.cancel.not-purchasable.item"), plugin.getMenusConfig().getInt("ascend-menu.items.cancel.not-purchasable.slot"));
        this.cancelMaxLevel = new MenuItemBuilder(plugin.getMenusConfig().getItemBuilder("ascend-menu.items.cancel.max-level.item"), plugin.getMenusConfig().getInt("ascend-menu.items.cancel.max-level.slot"));

        this.infoPurchasable = new MenuItemBuilder(plugin.getMenusConfig().getItemBuilder("ascend-menu.items.upgrade-info.purchasable.item"), plugin.getMenusConfig().getInt("ascend-menu.items.upgrade-info.purchasable.slot"));
        this.infoNotPurchasable = new MenuItemBuilder(plugin.getMenusConfig().getItemBuilder("ascend-menu.items.upgrade-info.not-purchasable.item"), plugin.getMenusConfig().getInt("ascend-menu.items.upgrade-info.not-purchasable.slot"));
        this.infoMaxLevel = new MenuItemBuilder(plugin.getMenusConfig().getItemBuilder("ascend-menu.items.upgrade-info.max-level.item"), plugin.getMenusConfig().getInt("ascend-menu.items.upgrade-info.max-level.slot"));

        this.leftSidePurchasable = this.plugin.getMenusConfig().getItemBuilder("ascend-menu.items.left-side.purchasable.item");
        this.leftSideNotPurchasable = this.plugin.getMenusConfig().getItemBuilder("ascend-menu.items.left-side.not-purchasable.item");
        this.leftSideMaxLevel = this.plugin.getMenusConfig().getItemBuilder("ascend-menu.items.left-side.max-level.item");

        this.rightSidePurchasable = this.plugin.getMenusConfig().getItemBuilder("ascend-menu.items.right-side.purchasable.item");
        this.rightSideNotPurchasable = this.plugin.getMenusConfig().getItemBuilder("ascend-menu.items.right-side.not-purchasable.item");
        this.rightSideMaxLevel = this.plugin.getMenusConfig().getItemBuilder("ascend-menu.items.right-side.max-level.item");
    }

    public void open(final Player player, final UUID uuid) {

        final MenuBuilder builder = this.createBase();

        final ItemStack pickaxe = this.plugin.getEnchantUtils().getPickaxeFromUUID(player, uuid).clone();
        final int currentLevel = this.plugin.getEnchantUtils().getLevel(pickaxe);
        final int currentExp = this.plugin.getEnchantUtils().getExperience(pickaxe);

        final AscendUtil.AscensionResult result = this.plugin.getAscendUtil().canAscend(currentLevel, currentExp);

        builder.setItem(this.currentPickaxeSlot, pickaxe);

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%current-level%", Utils.format(currentLevel));

        if (result == AscendUtil.AscensionResult.SUCCESSFUL) {

            replacer.addPlaceholder("%new-level%", Utils.format(currentLevel + 1));

            for (final int leftSideSlot : this.leftSideSlots) {
                builder.setItem(leftSideSlot, this.leftSidePurchasable);
            }

            for (final int rightSideSlot : this.rightSideSlots) {
                builder.setItem(rightSideSlot, this.rightSidePurchasable);
            }

            builder.setItem(this.confirmPurchasable.getSlot(), this.confirmPurchasable.getItem().parse(replacer));
            builder.setItem(this.cancelPurchasable.getSlot(), this.cancelPurchasable.getItem().parse(replacer));
            builder.setItem(this.infoPurchasable.getSlot(), this.infoPurchasable.getItem().parse(replacer));

            builder.setItem(this.newPickaxeSlot, this.plugin.getEnchantUtils().getNewPickaxe(player, uuid, currentLevel + 1));

            builder.addClickEvent(this.confirmPurchasable.getSlot(), event -> {

                this.plugin.getAscendUtil().ascend(
                        player,
                        uuid,
                        currentLevel,
                        currentExp
                );

                this.open(player, uuid);
            });
        }

        if (result == AscendUtil.AscensionResult.NOT_ENOUGH_EXP) {

            replacer.addPlaceholder("%new-level%", Utils.format(currentLevel + 1));

            for (final int leftSideSlot : this.leftSideSlots) {
                builder.setItem(leftSideSlot, this.leftSideNotPurchasable);
            }

            for (final int rightSideSlot : this.rightSideSlots) {
                builder.setItem(rightSideSlot, this.rightSideNotPurchasable);
            }

            builder.setItem(this.confirmNotPurchasable.getSlot(), this.confirmNotPurchasable.getItem().parse(replacer));
            builder.setItem(this.cancelNotPurchasable.getSlot(), this.cancelNotPurchasable.getItem().parse(replacer));
            builder.setItem(this.infoNotPurchasable.getSlot(), this.infoNotPurchasable.getItem().parse(replacer));

            builder.setItem(this.newPickaxeSlot, this.plugin.getEnchantUtils().getNewPickaxe(player, uuid, currentLevel + 1));
        }

        if (result == AscendUtil.AscensionResult.MAX_LEVEL) {

            replacer.addPlaceholder("%new-level%", "N/A");

            for (final int leftSideSlot : this.leftSideSlots) {
                builder.setItem(leftSideSlot, this.leftSideMaxLevel);
            }

            for (final int rightSideSlot : this.rightSideSlots) {
                builder.setItem(rightSideSlot, this.rightSideMaxLevel);
            }

            builder.setItem(this.confirmMaxLevel.getSlot(), this.confirmMaxLevel.getItem().parse(replacer));
            builder.setItem(this.cancelMaxLevel.getSlot(), this.cancelMaxLevel.getItem().parse(replacer));
            builder.setItem(this.infoMaxLevel.getSlot(), this.infoMaxLevel.getItem().parse(replacer));

            builder.setItem(this.newPickaxeSlot, this.infoMaxLevel.getItem());
        }

        player.openInventory(builder.build());
    }
}
