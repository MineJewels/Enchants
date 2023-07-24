package org.minejewels.jewelspickaxes.enchant.utils;

import net.abyssdev.abysslib.nbt.NBTUtils;
import net.abyssdev.abysslib.text.Color;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.eclipse.collections.api.factory.Maps;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.enchant.Enchant;
import org.minejewels.jewelspickaxes.pickaxe.Pickaxe;

import java.util.*;

public class EnchantUtils {

    private final JewelsPickaxes plugin;
    private final List<String> enchantOrder;

    public EnchantUtils(final JewelsPickaxes plugin) {
        this.plugin = plugin;

        this.enchantOrder = this.plugin.getSettingsConfig().getStringList("enchant-order");
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
            if (!this.getPickaxeUUID(itemStack).toString().equalsIgnoreCase(uuid.toString())) continue;

            return itemStack;
        }

        return null;
    }

    public Integer getSlotFromUUID(final Player player, final UUID uuid) {

        int count = -1;

        for (final ItemStack itemStack : player.getInventory().getContents()) {

            count++;

            if (!this.isPickaxe(itemStack)) continue;
            if (!this.getPickaxeUUID(itemStack).toString().equalsIgnoreCase(uuid.toString())) continue;

            return count;
        }

        return -1;
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
        return NBTUtils.get().contains(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase());
    }

    public int getLevel(final ItemStack pickaxe, final Enchant enchant) {
        if (!this.hasEnchantment(pickaxe, enchant)) return 0;

        return NBTUtils.get().getInt(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase());
    }

    public void setLevel(final Player player, final ItemStack pickaxe, final Enchant enchant, final int level) {

        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase(), level);

        player.getInventory().setItem(
                this.getSlotFromUUID(player, this.getPickaxeUUID(pickaxe)),
                updatedItem
        );

        this.plugin.getPlayerCache().getEnchantPlayer(player).updatePickaxe(pickaxe, this.plugin.getEnchantUtils());

        this.updatePickaxe(player, this.getPickaxeUUID(updatedItem));
    }

    public void removeLevel(final Player player, final ItemStack pickaxe, final Enchant enchant, final int level) {

        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase(), this.getLevel(pickaxe, enchant) - level);

        this.updatePickaxe(player, this.getPickaxeUUID(updatedItem));

        this.plugin.getPlayerCache().getEnchantPlayer(player).updatePickaxe(pickaxe, this.plugin.getEnchantUtils());
    }

    public void addLevel(final Player player, final ItemStack pickaxe, final Enchant enchant, final int level) {

        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-ENCHANT-" + enchant.getName().toUpperCase(), this.getLevel(pickaxe, enchant) + level);

        this.updatePickaxe(player, this.getPickaxeUUID(updatedItem));

        this.plugin.getPlayerCache().getEnchantPlayer(player).updatePickaxe(pickaxe, this.plugin.getEnchantUtils());
    }

    public int getExperience(final ItemStack pickaxe) {
        return  NBTUtils.get().getInt(pickaxe, "PICKAXE-EXPERIENCE");
    }

    public void addExperience(final Player player, final UUID pickaxeUUID, final ItemStack pickaxe, final int amount) {
        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-EXPERIENCE", this.getExperience(pickaxe) + amount);

        player.getInventory().setItem(this.getSlotFromUUID(player, pickaxeUUID), updatedItem);
    }

    public void setExperience(final Player player, final UUID pickaxeUUID, final ItemStack pickaxe, final int amount) {
        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-EXPERIENCE", amount);

        player.getInventory().setItem(this.getSlotFromUUID(player, pickaxeUUID), updatedItem);
    }

    public int getLevel(final ItemStack pickaxe) {
        return NBTUtils.get().getInt(pickaxe, "PICKAXE-LEVEL");
    }

    public void addLevel(final Player player, final UUID pickaxeUUID, final ItemStack pickaxe, final int amount) {
        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-LEVEL", this.getLevel(pickaxe) + amount);

        player.getInventory().setItem(this.getSlotFromUUID(player, pickaxeUUID), updatedItem);
    }

    public void setLevel(final Player player, final UUID pickaxeUUID, final ItemStack pickaxe, final int amount) {
        final ItemStack updatedItem = NBTUtils.get().setInt(pickaxe, "PICKAXE-LEVEL", amount);

        player.getInventory().setItem(this.getSlotFromUUID(player, pickaxeUUID), updatedItem);
    }

    public void updatePickaxe(final Player player, final UUID pickaxeUUID) {

        final ItemStack item = this.getPickaxeFromUUID(player, pickaxeUUID);

        if (!this.isPickaxe(item)) return;

        final Pickaxe pickaxe = this.getPickaxeType(item);

        final ItemMeta pickaxeMeta = item.getItemMeta();

        final List<String> newLore = new LinkedList<>();

        for (final String line : pickaxe.getItem().getLore()) {

            if (!line.contains("%enchants%")) {
                newLore.add(line
                        .replace("%owner%", player.getName())
                        .replace("%level%", Utils.format(this.getLevel(item)))
                        .replace("%exp%", Utils.format(this.getExperience(item)))
                        .replace("%max-exp%", Utils.format(this.plugin.getAscendUtil().getNeededExperience(this.getLevel(item))))
                        .replace("%progress-bar%", Utils.getProgressBar(
                                this.getExperience(item),
                                this.plugin.getAscendUtil().getNeededExperience(this.getLevel(item)),
                                33,
                                "|",
                                "&a",
                                "&c")
                        ));
                continue;
            }

            for (String key : this.enchantOrder) {

                final Optional<Enchant> optionalEnchant = this.plugin.getEnchantRegistry().get(key.toUpperCase());

                if (!optionalEnchant.isPresent()) {
                    continue;
                }

                final Enchant enchant = optionalEnchant.get();

                if (!this.hasEnchantment(item, enchant)) continue;

                final int level = this.getLevel(item, enchant);

                newLore.add(enchant.getLoreFormat().replace("%level%", Utils.format(level)));
            }
        }

        pickaxeMeta.setLore(Color.parse(newLore));

        item.setItemMeta(pickaxeMeta);

        if (this.getSlotFromUUID(player, pickaxeUUID) == -1) return;

        player.getInventory().setItem(this.getSlotFromUUID(player, pickaxeUUID), item);

        player.updateInventory();
    }

    public ItemStack getNewPickaxe(final Player player, final UUID pickaxeUUID, final int newLevel) {

        final ItemStack item = this.getPickaxeFromUUID(player, pickaxeUUID).clone();

        if (!this.isPickaxe(item)) return item;

        final Pickaxe pickaxe = this.getPickaxeType(item);

        final ItemMeta pickaxeMeta = item.getItemMeta();

        final List<String> newLore = new LinkedList<>();

        for (final String line : pickaxe.getItem().getLore()) {

            if (!line.contains("%enchants%")) {
                newLore.add(line
                        .replace("%owner%", player.getName())
                        .replace("%level%", Utils.format(newLevel))
                        .replace("%exp%", Utils.format(0))
                        .replace("%max-exp%", Utils.format(this.plugin.getAscendUtil().getNeededExperience(newLevel)))
                        .replace("%progress-bar%", Utils.getProgressBar(
                                0,
                                this.plugin.getAscendUtil().getNeededExperience(newLevel),
                                33,
                                "|",
                                "&a",
                                "&c")
                        ));
                continue;
            }

            for (String key : this.enchantOrder) {

                final Optional<Enchant> optionalEnchant = this.plugin.getEnchantRegistry().get(key.toUpperCase());

                if (!optionalEnchant.isPresent()) {
                    continue;
                }

                final Enchant enchant = optionalEnchant.get();

                if (!this.hasEnchantment(item, enchant)) continue;

                final int level = this.getLevel(item, enchant);

                newLore.add(enchant.getLoreFormat().replace("%level%", Utils.format(level)));
            }
        }

        pickaxeMeta.setLore(Color.parse(newLore));

        item.setType(this.plugin.getAscendRegistry().get(newLevel).get().getNewMaterial());
        item.setItemMeta(pickaxeMeta);

        return item;
    }
}
