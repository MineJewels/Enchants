package org.minejewels.jewelspickaxes.ascend.commands;

import net.abyssdev.abysslib.command.AbyssCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Lists;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.menu.AscensionMenu;

import java.util.UUID;

public class AscensionCommand extends AbyssCommand<JewelsPickaxes, Player> {

    public AscensionCommand(JewelsPickaxes plugin) {
        super(plugin, "ascension", Player.class);

        this.setAliases(Lists.mutable.of("ascend", "ascensions", "ascends"));
        this.register();
    }

    @Override
    public void execute(CommandContext<Player> context) {

        final Player player = context.getSender();

        if (!this.plugin.getEnchantUtils().isPickaxe(player.getInventory().getItemInMainHand())) {
            this.plugin.getMessageCache().sendMessage(player, "messages.pickaxe-in-inventory");
            return;
        }

        final UUID pickaxeUUID = this.plugin.getEnchantUtils().getPickaxeUUID(player.getInventory().getItemInMainHand());

        new AscensionMenu(this.plugin).open(player, pickaxeUUID);
    }
}
