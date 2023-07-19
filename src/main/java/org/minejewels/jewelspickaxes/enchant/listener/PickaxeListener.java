package org.minejewels.jewelspickaxes.enchant.listener;

import net.abyssdev.abysslib.listener.AbyssListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelspickaxes.JewelsPickaxes;

public class PickaxeListener extends AbyssListener<JewelsPickaxes> {

    public PickaxeListener(final JewelsPickaxes plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        if (!this.plugin.getEnchantUtils().isPickaxe(item)) {
            return;
        }

        this.plugin.getPickaxeMenu().open(player, this.plugin.getEnchantUtils().getPickaxeUUID(item));
    }
}
