package de.bensoft.bukkit.buku.cmd.test.simple;

import de.bensoft.bukkit.buku.cmd.api.AbstractBukuPlayerCommand;
import de.bensoft.bukkit.buku.cmd.api.BukuCommand;
import org.bukkit.entity.Player;

@BukuCommand(identifier = "sub", description = "test")
public class SimpleTestSubCommand extends AbstractBukuPlayerCommand {

    @Override
    protected boolean execute(Player player) {
        return true;
    }

}
