package org.minejewels.jewelspickaxes.pickaxe;

import lombok.Data;
import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.nbt.NBTUtils;
import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;

import java.util.Map;
import java.util.UUID;

@Data
public class Pickaxe {

    private final String name;
    private final boolean unbreakable;
    private final ItemBuilder item;
    private final Map<Enchant, Integer> defaultEnchantments;

    public void givePickaxe(final Player player, final JewelsPickaxes plugin) {

        ItemStack pickaxe = this.item.setHideItemFlags(true).parse(new PlaceholderReplacer().addPlaceholder("%owner%", player.getName()));

        pickaxe = NBTUtils.get().setString(pickaxe, "PICKAXE-TYPE", name.toUpperCase());
        pickaxe = NBTUtils.get().setString(pickaxe, "PICKAXE-UUID", UUID.randomUUID().toString());

        player.getInventory().addItem(pickaxe);

        for (Map.Entry<Enchant, Integer> enchant : this.defaultEnchantments.entrySet()) {
            plugin.getEnchantUtils().setLevel(player, pickaxe, enchant.getKey(), enchant.getValue());
        }

        plugin.getMessageCache().sendMessage(player, "messages.pickaxe-given", new PlaceholderReplacer().addPlaceholder("%type%", WordUtils.formatText(this.getName())));
    }

}
