package org.minejewels.jewelspickaxes.enchant.utils;

import net.abyssdev.abysslib.nbt.NBTUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eclipse.collections.api.factory.Maps;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;
import org.minejewels.jewelspickaxes.pickaxe.Pickaxe;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class EnchantUtils {

    private final JewelsPickaxes plugin;

    public EnchantUtils(final JewelsPickaxes plugin) {
        this.plugin = plugin;
    }

    public boolean isPickaxe(final ItemStack pickaxe) {
        return NBTUtils.get().contains(pickaxe, "PICKAXE-UUID");
    }

    public UUID getPickaxeUUID(final ItemStack pickaxe) {
        if (!this.isPickaxe(pickaxe)) return null;

        return UUID.fromString(NBTUtils.get().getString(pickaxe, "PICKAXE-UUID"));
    }

    public ItemStack getPickaxeFromUUID(final Player player, final UUID uuid) {

        for (final ItemStack itemStack : player.getInventory().getContents()) {
            if (!this.isPickaxe(itemStack)) continue;
            if (!(this.getPickaxeUUID(itemStack) == uuid)) continue;

            return itemStack;
        }

        return null;
    }

    public Pickaxe getPickaxeType(final ItemStack pickaxe) {
        if (!this.isPickaxe(pickaxe)) return null;

        final Optional<Pickaxe> optionalPickaxe = this.plugin.getPickaxeRegistry().get(NBTUtils.get().getString(pickaxe, "PICKAXE-TYPE"));

        return optionalPickaxe.orElse(null);

    }

    public Map<Enchant, Integer> getEnchantments(final ItemStack pickaxe) {

        final Map<Enchant, Integer> enchantments = Maps.mutable.empty();

        if (!this.isPickaxe(pickaxe)) return enchantments;

        for (final Enchant enchant : this.plugin.getEnchantRegistry().values()) {
            if (!NBTUtils.get().contains(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase())) continue;
            enchantments.put(enchant, NBTUtils.get().getInt(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase()));
        }

        return enchantments;
    }

    public boolean hasEnchantment(final ItemStack pickaxe, final Enchant enchant) {
        if (!this.isPickaxe(pickaxe)) return false;
        return NBTUtils.get().contains(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase());
    }

    public int getLevel(final ItemStack pickaxe, final Enchant enchant) {
        if (!this.hasEnchantment(pickaxe, enchant)) return 0;

        return NBTUtils.get().getInt(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase());
    }

    public void setLevel(final Player player, final ItemStack pickaxe, final Enchant enchant, final int level) {

        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase(), level);

        player.getInventory().setItemInMainHand(updatedItem);

        this.plugin.getPlayerCache().getEnchantPlayer(player).updatePickaxe(pickaxe, this.plugin.getEnchantUtils());
    }

    public void removeLevel(final Player player, final ItemStack pickaxe, final Enchant enchant, final int level) {

        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase(), this.getLevel(pickaxe, enchant) - level);

        player.getInventory().setItemInMainHand(updatedItem);

        this.plugin.getPlayerCache().getEnchantPlayer(player).updatePickaxe(pickaxe, this.plugin.getEnchantUtils());
    }

    public void addLevel(final Player player, final ItemStack pickaxe, final Enchant enchant, final int level) {

        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase(), this.getLevel(pickaxe, enchant) + level);

        player.getInventory().setItemInMainHand(updatedItem);

        this.plugin.getPlayerCache().getEnchantPlayer(player).updatePickaxe(pickaxe, this.plugin.getEnchantUtils());
    }
}
