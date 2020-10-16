package de.bensoft.bukkit.buku.cmd.test.simple;


import de.bensoft.bukkit.buku.cmd.api.AbstractBukuCommand;
import de.bensoft.bukkit.buku.cmd.api.BukuCommand;
import org.bukkit.command.CommandSender;

@BukuCommand(identifier = "test",
        isRootCommand = true,
        description = "test",
        subCommands = {
                SimpleTestSubCommand.class
        })
public class SimpleTestRootCommand extends AbstractBukuCommand {
        @Override
        protected boolean execute(CommandSender sender) {
                return false;
        }
}
