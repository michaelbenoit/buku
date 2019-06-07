package de.bensoft.bukkit.buku.cmd.test.simple;


import de.bensoft.bukkit.buku.cmd.AbstractBukuCommand;
import de.bensoft.bukkit.buku.cmd.BukuCommand;
import org.bukkit.command.CommandSender;

@BukuCommand(identifier = "test",
        isRootCommand = true,
        subcommands = {
                SimpleTestSubCommand.class
        })
public class SimpleTestRootCommand extends AbstractBukuCommand {
        @Override
        protected boolean execute(CommandSender sender) {
                return false;
        }
}
