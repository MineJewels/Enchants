package org.minejewels.jewelspickaxes.pickaxe.commands.sub;

import net.abyssdev.abysslib.command.AbyssSubCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.set.ImmutableSet;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.pickaxe.Pickaxe;

import java.util.Optional;

public class PickaxeGiveCommand extends AbyssSubCommand<JewelsPickaxes> {

    public PickaxeGiveCommand(JewelsPickaxes plugin) {
        super(plugin, false, 0, Sets.immutable.of("give", "givepickaxe"));
    }

    @Override
    public void execute(CommandContext<?> context) {
        final CommandSender sender = context.getSender();

        if (!sender.hasPermission("jewelspickaxes.admin")) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.no-permission");
            return;
        }

        final Player target = context.asPlayer(0);
        final String pickaxe = context.asString(1).toUpperCase();

        final Optional<Pickaxe> optionalPickaxe = this.plugin.getPickaxeRegistry().get(pickaxe);

        if (!optionalPickaxe.isPresent()) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.invalid-pickaxe");
            return;
        }

        if (target == null) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.invalid-player");
            return;
        }

        optionalPickaxe.get().givePickaxe(target, plugin);
    }
}
