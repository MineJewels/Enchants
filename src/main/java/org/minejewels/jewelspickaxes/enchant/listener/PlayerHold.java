package org.minejewels.jewelspickaxes.enchant.listener;

import net.abyssdev.abysslib.listener.AbyssListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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

        handleItemChange(player, previousItem, newItem);
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            final Player player = (Player) event.getWhoClicked();
            final ItemStack newItem = event.getNewItems().get(0);
            final ItemStack previousItem = event.getOldCursor();

            this.plugin.getLogger().info("Inventory Drag Event detected by " + player.getName());
            this.plugin.getLogger().info("New Item: " + newItem);
            this.plugin.getLogger().info("Previous Item: " + previousItem);

            handleItemChange(player, previousItem, newItem);
        }
    }

    private void handleItemChange(Player player, ItemStack previousItem, ItemStack newItem) {
        if (previousItem != null) {
            this.plugin.getEnchantUtils().getEnchantments(previousItem).forEach((enchant, level) -> enchant.onUnequip(player, previousItem, level));
        }

        if (this.plugin.getEnchantUtils().isPickaxe(newItem)) {
            this.plugin.getEnchantUtils().getEnchantments(newItem).forEach((enchant, level) -> enchant.onEquip(player, newItem, level));
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        handleItemDrop(player, droppedItem);
    }

    private void handleItemDrop(Player player, ItemStack droppedItem) {
        if (droppedItem != null && this.plugin.getEnchantUtils().isPickaxe(droppedItem)) {
            this.plugin.getEnchantUtils().getEnchantments(droppedItem).forEach((enchant, level) -> enchant.onUnequip(player, droppedItem, level));
        }
    }
}
