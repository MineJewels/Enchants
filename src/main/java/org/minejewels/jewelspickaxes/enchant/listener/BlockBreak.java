package org.minejewels.jewelspickaxes.enchant.listener;

import net.abyssdev.abysslib.listener.AbyssListener;
import net.abyssdev.slikey.effectlib.util.MathUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelscobblecubes.events.CobbleCubeBreakEvent;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;
import org.minejewels.jewelspickaxes.enchant.player.EnchantPlayer;
import org.minejewels.jewelspickaxes.enchant.player.cache.EnchantPlayerCache;
import org.minejewels.jewelspickaxes.enchant.utils.EnchantUtils;

import java.util.Map;
import java.util.UUID;

public class BlockBreak extends AbyssListener<JewelsPickaxes> {

    private final EnchantUtils enchantUtils;
    private final EnchantPlayerCache playerCache;

    public BlockBreak(final JewelsPickaxes plugin) {
        super(plugin);

        this.enchantUtils = plugin.getEnchantUtils();
        this.playerCache = plugin.getPlayerCache();
    }

    @EventHandler
    public void onBreak(final CobbleCubeBreakEvent event) {

        final Player player = event.getPlayer();
        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!this.enchantUtils.isPickaxe(item)) return;

        final UUID pickaxeUUID = this.enchantUtils.getPickaxeUUID(item);

        final EnchantPlayer enchantPlayer = this.playerCache.getEnchantPlayer(player);

        if (!enchantPlayer.hasRegisteredPickaxe(pickaxeUUID)) {
            enchantPlayer.registerPickaxe(item, this.enchantUtils);
        }

        if (!this.plugin.getUpdateRegistry().containsValue(pickaxeUUID)) {
            this.plugin.getUpdateRegistry().register(player, pickaxeUUID);
        }

        final int exp = MathUtils.random(1, 3);

        this.enchantUtils.addExperience(player, pickaxeUUID, item, exp);

        if (this.plugin.getEnchantUtils().getExperience(item) > this.plugin.getAscendUtil().getNeededExperience(this.plugin.getEnchantUtils().getLevel(item))) {
            this.plugin.getEnchantUtils().setExperience(player, pickaxeUUID, item, this.plugin.getAscendUtil().getNeededExperience(this.plugin.getEnchantUtils().getLevel(item)));
        }

        if (this.plugin.getEnchantUtils().getExperience(item) >= this.plugin.getAscendUtil().getNeededExperience(this.plugin.getEnchantUtils().getLevel(item)) && this.plugin.getEnchantUtils().getLevel(item) != this.plugin.getMaxAscendLevel()) {

            this.plugin.getMessageCache().sendMessage(player, "messages.ascension-needed");

            event.setCancelled(true);
            return;
        }

        for (final Map.Entry<Enchant, Integer> enchant : enchantPlayer.getEnchants(pickaxeUUID).entrySet()) {
            enchant.getKey().onBreak(event, player, enchant.getValue());
        }


    }

    @EventHandler
    public void onDamage(final PlayerItemDamageEvent event) {

        final ItemStack item = event.getItem();

        if (!this.enchantUtils.isPickaxe(item)) return;

        event.setCancelled(true);
    }
}
