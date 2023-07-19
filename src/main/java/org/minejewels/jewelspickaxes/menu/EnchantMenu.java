package org.minejewels.jewelspickaxes.menu;

import net.abyssdev.abysslib.menu.MenuBuilder;
import net.abyssdev.abysslib.menu.templates.AbyssMenu;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;
import org.minejewels.jewelspickaxes.enchant.utils.EnchantUtils;

import java.util.UUID;

public class EnchantMenu extends AbyssMenu {

    private final JewelsPickaxes plugin;
    private final EnchantUtils enchantUtils;

    public EnchantMenu(final JewelsPickaxes plugin) {
        super(plugin.getMenusConfig(), "enchant-menu.");

        this.plugin = plugin;
        this.enchantUtils = plugin.getEnchantUtils();
    }

    public void open(final Player player, final Enchant enchant, final UUID pickaxeUUID) {

        final MenuBuilder builder = this.createBase();

        final PlaceholderReplacer replacer = new PlaceholderReplacer()
                .addPlaceholder("%enchant%", WordUtils.formatText(enchant.getName()));

        if (this.enchantUtils.getPickaxeFromUUID(player, pickaxeUUID) ==  null) {
            this.plugin.getMessageCache().sendMessage(player, "messages.pickaxe-in-inventory");
            return;
        }
        final ItemStack pickaxe = this.enchantUtils.getPickaxeFromUUID(player, pickaxeUUID);

        if (this.plugin.getMenusConfig().getBoolean("enchant-menu.display-pickaxe.enabled")) {
            builder.setItem(this.plugin.getMenusConfig().getInt("enchant-menu.display-pickaxe.slot"), pickaxe);
        }

        player.openInventory(builder.build(replacer));
    }
}
