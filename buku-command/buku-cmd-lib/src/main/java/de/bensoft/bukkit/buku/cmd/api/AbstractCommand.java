package de.bensoft.bukkit.buku.cmd.api;

import de.bensoft.bukkit.buku.cmd.util.model.CommandArguments;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {

    public boolean onCommand(final CommandSender sender,
                             final CommandArguments arguments) {

        return execute(sender);
    }

    protected abstract boolean execute(CommandSender sender);

}
