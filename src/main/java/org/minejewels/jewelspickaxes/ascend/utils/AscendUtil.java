package org.minejewels.jewelspickaxes.ascend.utils;

import net.abyssdev.abysslib.placeholder.PlaceholderReplacer;
import net.abyssdev.abysslib.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.ascend.Ascend;

import java.util.UUID;

public class AscendUtil {

    private final JewelsPickaxes plugin;

    public AscendUtil(final JewelsPickaxes plugin) {
        this.plugin = plugin;
    }

    public AscensionResult canAscend(final int level, final int experience) {

        if (level == plugin.getMaxAscendLevel()) return AscensionResult.MAX_LEVEL;

        final Ascend ascend = this.plugin.getAscendRegistry().get(level + 1).get();

        if (experience < ascend.getRequiredExperience()) return AscensionResult.NOT_ENOUGH_EXP;

        return AscensionResult.SUCCESSFUL;
    }

    public int getNeededExperience(final int level) {

        if (level == plugin.getMaxAscendLevel()) return 0;

        final Ascend ascend = this.plugin.getAscendRegistry().get(level + 1).orElse(this.plugin.getAscendRegistry().get(plugin.getMaxAscendLevel()).get());

        return ascend.getRequiredExperience();
    }

    public void ascend(final Player player, final UUID pickaxeUUID, final int currentLevel, final int experience) {

        switch (this.canAscend(currentLevel, experience)) {
            case NOT_ENOUGH_EXP:
                this.plugin.getMessageCache().sendMessage(player, "messages.cannot-ascend");
                return;
            case MAX_LEVEL:
                this.plugin.getMessageCache().sendMessage(player, "messages.max-ascension");
                return;
            case SUCCESSFUL:

                ItemStack pickaxe = this.plugin.getEnchantUtils().getPickaxeFromUUID(player, pickaxeUUID);

                final Ascend ascend = this.plugin.getAscendRegistry().get(currentLevel + 1).get();

                pickaxe.setType(ascend.getNewMaterial());

                this.plugin.getEnchantUtils().setExperience(player, pickaxeUUID, pickaxe, 0);
                this.plugin.getEnchantUtils().addLevel(player, pickaxeUUID, pickaxe, 1);

                this.plugin.getEnchantUtils().updatePickaxe(player, pickaxeUUID);

                this.plugin.getMessageCache().sendMessage(player, "messages.ascended", new PlaceholderReplacer()
                                .addPlaceholder("%old-level%", Utils.format(currentLevel))
                                .addPlaceholder("%level%", Utils.format(currentLevel + 1)));
        }
    }

    enum AscensionResult {

        SUCCESSFUL,
        NOT_ENOUGH_EXP,
        MAX_LEVEL
    }
}
