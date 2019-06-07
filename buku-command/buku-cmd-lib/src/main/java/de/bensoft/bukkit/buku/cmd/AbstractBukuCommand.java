package de.bensoft.bukkit.buku.cmd;

import de.bensoft.bukkit.buku.cmd.exception.BukuCommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public abstract class AbstractBukuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        final QualifiedName qualifiedName = new QualifiedName(args).skip(1);
        final BukuCommand cmdAnnotation = this.getClass().getAnnotation(BukuCommand.class);

        if (cmdAnnotation.subcommands().length == 0 || qualifiedName.isEmpty()) {
            return execute(sender);
        } else {
            final Class<? extends AbstractBukuCommand> subCmd = findSubcommand(qualifiedName.get(0), cmdAnnotation);
            if(subCmd != null) {
                try {
                    final AbstractBukuCommand abstractCommand = subCmd.newInstance();
                    return abstractCommand.onCommand(sender, command, label, qualifiedName.toArray(new String[qualifiedName.size()]));
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new BukuCommandException(e);
                }
            }
        }


        return false;
    }


    public Class<? extends AbstractBukuCommand> findSubcommand(String keyword, BukuCommand cmd) {
        return Arrays.asList(cmd.subcommands())
                .stream()
                .filter(subCommand -> {
                    final BukuCommand subCmdAnnotation = subCommand.getAnnotation(BukuCommand.class);
                    return subCmdAnnotation.identifier().equals(keyword);
                })
                .findFirst()
                .orElse(null);
    }

    public String getIdentifier() {
        final BukuCommand annotation = getClass().getAnnotation(BukuCommand.class);
        return annotation.identifier();
    }

    protected abstract boolean execute(CommandSender sender);

}
