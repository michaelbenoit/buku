package de.bensoft.bukkit.buku.cmd.test.simple;

import de.bensoft.bukkit.buku.cmd.AbstractBukuPlayerCommand;
import de.bensoft.bukkit.buku.cmd.BukuCommand;
import org.bukkit.entity.Player;

@BukuCommand(identifier = "sub")
public class SimpleTestSubCommand extends AbstractBukuPlayerCommand {
    @Override
    protected boolean execute(Player player) {
        return true;
    }
}
