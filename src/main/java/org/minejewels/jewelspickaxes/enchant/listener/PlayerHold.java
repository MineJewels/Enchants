package org.minejewels.jewelspickaxes.enchant.listener;

import net.abyssdev.abysslib.listener.AbyssListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelspickaxes.JewelsPickaxes;

public class PlayerHold extends AbyssListener<JewelsPickaxes> {

    public PlayerHold(final JewelsPickaxes plugin) {
        super(plugin);
    }

    @EventHandler
    public void onHoldEvent(final PlayerItemHeldEvent event) {

        final Player player = event.getPlayer();

        final ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        final ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

        if (previousItem != null) {
            this.plugin.getEnchantUtils().getEnchantments(previousItem).forEach((enchant, level) -> enchant.onUnequip(player, previousItem, level));
        }

        if (newItem != null && this.plugin.getEnchantUtils().isPickaxe(newItem)) {
            this.plugin.getEnchantUtils().getEnchantments(newItem).forEach((enchant, level) -> enchant.onEquip(player, newItem, level));
        }
    }
}
