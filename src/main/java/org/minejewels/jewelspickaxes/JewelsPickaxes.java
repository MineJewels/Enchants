package org.minejewels.jewelspickaxes;

import lombok.Getter;
import net.abyssdev.abysslib.config.AbyssConfig;
import net.abyssdev.abysslib.plugin.AbyssPlugin;

@Getter
public final class JewelsPickaxes extends AbyssPlugin {

    private final AbyssConfig enchantsConfig = this.getAbyssConfig("enchants");

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
