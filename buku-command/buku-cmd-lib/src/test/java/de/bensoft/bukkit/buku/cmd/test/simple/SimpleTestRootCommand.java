package de.bensoft.bukkit.buku.cmd.test.simple;


import de.bensoft.bukkit.buku.cmd.api.AbstractCommand;
import de.bensoft.bukkit.buku.cmd.api.Command;
import org.bukkit.command.CommandSender;

@Command(identifier = "test",
        isRootCommand = true,
        description = "test",
        subCommands = {
                SimpleTestSubCommand.class
        })
public class SimpleTestRootCommand extends AbstractCommand {
        @Override
        protected boolean execute(CommandSender sender) {
                return false;
        }
}
