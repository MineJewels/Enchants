package org.minejewels.jewelspickaxes;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import net.abyssdev.abysslib.builders.ItemBuilder;
import net.abyssdev.abysslib.config.AbyssConfig;
import net.abyssdev.abysslib.patterns.registry.Registry;
import net.abyssdev.abysslib.plugin.AbyssPlugin;
import net.abyssdev.abysslib.text.MessageCache;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.minejewels.jewelspickaxes.ascend.Ascend;
import org.minejewels.jewelspickaxes.ascend.commands.AscensionCommand;
import org.minejewels.jewelspickaxes.ascend.registry.AscendRegistry;
import org.minejewels.jewelspickaxes.ascend.utils.AscendUtil;
import org.minejewels.jewelspickaxes.enchant.Enchant;
import org.minejewels.jewelspickaxes.enchant.impl.*;
import org.minejewels.jewelspickaxes.enchant.listener.BlockBreak;
import org.minejewels.jewelspickaxes.enchant.listener.PickaxeListener;
import org.minejewels.jewelspickaxes.enchant.listener.PlayerHold;
import org.minejewels.jewelspickaxes.enchant.player.cache.EnchantPlayerCache;
import org.minejewels.jewelspickaxes.enchant.registry.EnchantRegistry;
import org.minejewels.jewelspickaxes.enchant.utils.EnchantUtils;
import org.minejewels.jewelspickaxes.menu.PickaxeMenu;
import org.minejewels.jewelspickaxes.pickaxe.Pickaxe;
import org.minejewels.jewelspickaxes.pickaxe.commands.PickaxeCommand;
import org.minejewels.jewelspickaxes.pickaxe.registry.PickaxeRegistry;
import org.minejewels.jewelspickaxes.pickaxe.registry.UpdateRegistry;
import org.minejewels.jewelspickaxes.pickaxe.task.UpdateTask;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public final class JewelsPickaxes extends AbyssPlugin {

    public static JewelsPickaxes api;

    private final AbyssConfig enchantsConfig = this.getAbyssConfig("enchants");
    private final AbyssConfig pickaxesConfig = this.getAbyssConfig("pickaxes");
    private final AbyssConfig menusConfig = this.getAbyssConfig("menus");
    private final AbyssConfig langConfig = this.getAbyssConfig("lang");
    private final AbyssConfig settingsConfig = this.getAbyssConfig("settings");
    private final AbyssConfig ascendConfig = this.getAbyssConfig("ascend");

    private final MessageCache messageCache = new MessageCache(this.langConfig);

    private final Registry<String, Enchant> enchantRegistry = new EnchantRegistry();
    private final Registry<String, Pickaxe> pickaxeRegistry = new PickaxeRegistry();
    private final Registry<Player, UUID> updateRegistry = new UpdateRegistry();
    private final Registry<Integer, Ascend> ascendRegistry = new AscendRegistry();

    private final EnchantUtils enchantUtils = new EnchantUtils(this);
    private final AscendUtil ascendUtil = new AscendUtil(this);
    private final EnchantPlayerCache playerCache = new EnchantPlayerCache();

    private final PickaxeMenu pickaxeMenu = new PickaxeMenu(this);

    private int maxAscendLevel = 0;

    @Override
    public void onEnable() {

        JewelsPickaxes.api = this;

        this.loadMessages(this.messageCache, this.langConfig);

        this.loadEnchants();
        this.loadPickaxes();
        this.loadAscend();

        new BlockBreak(this);
        new PickaxeListener(this);
        new PlayerHold(this);

        new UpdateTask(this);

        new PickaxeCommand(this);
        new AscensionCommand(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadEnchants() {
        Lists.immutable.of(
                new EfficiencyEnchant(this),
                new JumpEnchant(this),
                new HasteEnchant(this),
                new SpeedEnchant(this),
                new MineralGreedEnchant(this),
                new TsnuamiEnchant(this),
                new JackhammerEnchant(this),
                new KeyFinderEnchant(this)
        ).forEach(enchant -> this.enchantRegistry.register(enchant.getName().toUpperCase(), enchant));
    }

    private void loadPickaxes() {
        for (final String key : this.pickaxesConfig.getSectionKeys("pickaxes")) {

            final Map<Enchant, Integer> enchants = Maps.mutable.empty();

            for (String type : this.pickaxesConfig.getStringList("pickaxes." + key + ".default-enchantments")) {
                final String[] entry = type.split(";");

                final Optional<Enchant> enchant = this.getEnchantRegistry().get(entry[0].toUpperCase());

                if (!enchant.isPresent()) continue;

                enchants.put(enchant.get(), Integer.parseInt(entry[1]));
            }

            final Pickaxe pickaxe = new Pickaxe(
                    key.toUpperCase(),
                    this.pickaxesConfig.getBoolean("pickaxes." + key + ".unbreakable"),
                    this.pickaxesConfig.getItemBuilder("pickaxes." + key + ".item"),
                    enchants
            );

            this.pickaxeRegistry.register(key.toUpperCase(), pickaxe);
        }
    }

    private void loadAscend() {

        for (final String level : this.ascendConfig.getSectionKeys("levels")) {

            final int pickaxeLevel = Integer.parseInt(level);
            final Ascend ascend = new Ascend(
                    pickaxeLevel,
                    XMaterial.matchXMaterial(this.ascendConfig.getString("levels." + level + ".material")).get().parseMaterial(),
                    this.ascendConfig.getInt("levels." + level + ".experience-required")
            );

            this.ascendRegistry.register(pickaxeLevel, ascend);
        }

        this.maxAscendLevel = this.ascendRegistry.getRegistry().size();
    }

    public static JewelsPickaxes getAPI() {
        return JewelsPickaxes.api;
    }
}
