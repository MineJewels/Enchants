package org.minejewels.jewelspickaxes.pickaxe.commands;

import net.abyssdev.abysslib.command.AbyssCommand;
import net.abyssdev.abysslib.command.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.eclipse.collections.api.factory.Lists;
import org.minejewels.jewelspickaxes.JewelsPickaxes;
import org.minejewels.jewelspickaxes.pickaxe.commands.sub.PickaxeGiveCommand;

public class PickaxeCommand extends AbyssCommand<JewelsPickaxes, CommandSender> {

    public PickaxeCommand(final JewelsPickaxes plugin) {
        super(plugin, "pickaxe", CommandSender.class);

        this.setAliases(Lists.mutable.of("pickaxes", "pick"));

        this.register(new PickaxeGiveCommand(plugin));

        this.register();
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        final CommandSender sender = context.getSender();

        if (sender.hasPermission("jewelspickaxes.admin")) {
            this.plugin.getMessageCache().sendMessage(sender, "messages.help");
            return;
        }

        this.plugin.getMessageCache().sendMessage(sender, "messages.no-permission");
    }
}
