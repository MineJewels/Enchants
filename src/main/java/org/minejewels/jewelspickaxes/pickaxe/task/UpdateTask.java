package org.minejewels.jewelspickaxes.pickaxe.task;

import net.abyssdev.abysslib.runnable.AbyssTask;
import org.bukkit.entity.Player;
import org.minejewels.jewelspickaxes.JewelsPickaxes;

import java.util.Map;
import java.util.UUID;

public class UpdateTask extends AbyssTask<JewelsPickaxes> {

    public UpdateTask(final JewelsPickaxes plugin) {
        super(plugin);

        this.runTaskTimerAsynchronously(plugin, 0L, plugin.getSettingsConfig().getInt("update-delay") * 20L);
    }

    @Override
    public void run() {

        if (this.plugin.getUpdateRegistry().values().isEmpty()) return;

        for (final Map.Entry<Player, UUID> pickaxe : this.plugin.getUpdateRegistry().entrySet()) {

            if (!(plugin.getEnchantUtils().getPickaxeFromUUID(pickaxe.getKey(), pickaxe.getValue()) == null)) continue;

            // update code
        }

        this.plugin.getUpdateRegistry().getRegistry().clear();
    }
}
