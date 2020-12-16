package de.bensoft.bukkit.buku.cmd.test.simple;

import de.bensoft.bukkit.buku.cmd.api.AbstractPlayerCommand;
import de.bensoft.bukkit.buku.cmd.api.Command;
import org.bukkit.entity.Player;

@Command(identifier = "sub", description = "test")
public class SimpleTestSubCommand extends AbstractPlayerCommand {

    @Override
    protected boolean execute(Player player) {
        return true;
    }

}
