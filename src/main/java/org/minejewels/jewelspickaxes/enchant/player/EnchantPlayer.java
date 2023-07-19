package org.minejewels.jewelspickaxes.enchant.player;

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eclipse.collections.api.factory.Maps;
import org.minejewels.jewelspickaxes.enchant.Enchant;
import org.minejewels.jewelspickaxes.enchant.utils.EnchantUtils;

import java.util.Map;
import java.util.UUID;

@Data
public class EnchantPlayer {

    private final Player player;
    private final Map<UUID, Map<Enchant, Integer>> enchants;

    public EnchantPlayer(final Player player) {
        this.player = player;
        this.enchants = Maps.mutable.empty();
    }

    public boolean hasRegisteredPickaxe(final UUID pickaxe) {
        return this.enchants.containsKey(pickaxe);
    }

    public void registerPickaxe(final ItemStack pickaxe, final EnchantUtils utils) {
        if (!utils.isPickaxe(pickaxe)) return;

        final UUID uuid = utils.getPickaxeUUID(pickaxe);

        if (hasRegisteredPickaxe(uuid)) return;

        this.enchants.put(uuid, utils.getEnchantments(pickaxe));
    }

    public Map<Enchant, Integer> getEnchants(final UUID pickaxe) {
        if (!hasRegisteredPickaxe(pickaxe)) return null;

        return this.enchants.get(pickaxe);
    }

    public void updatePickaxe(final ItemStack pickaxe, final EnchantUtils utils) {

        final UUID uuid = utils.getPickaxeUUID(pickaxe);

        if (!hasRegisteredPickaxe(uuid)) return;

        this.enchants.put(uuid, utils.getEnchantments(pickaxe));
    }
}
