package de.bensoft.bukkit.buku.cmd.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractPlayerCommand extends AbstractCommand {

    protected abstract boolean execute(Player player);

    @Override
    protected boolean execute(CommandSender sender) {
        return execute((Player)sender);
    }
}
