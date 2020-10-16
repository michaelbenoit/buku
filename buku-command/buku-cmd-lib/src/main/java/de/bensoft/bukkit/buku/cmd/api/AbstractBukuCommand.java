package de.bensoft.bukkit.buku.cmd.api;

import org.bukkit.command.CommandSender;

import java.util.Arrays;

public abstract class AbstractBukuCommand {

    public boolean onCommand(final CommandSender sender,
                             final String[] arguments) {

        return execute(sender);

       /* final QualifiedName qualifiedName = new QualifiedName(args);
        final BukuCommand cmdAnnotation = this.getClass().getAnnotation(BukuCommand.class);

        if (cmdAnnotation.subCommands().length == 0 || qualifiedName.isEmpty()) {
            return execute(sender);
        } else {
            final Class<? extends AbstractBukuCommand> subCmd = findSubcommand(qualifiedName.get(0), cmdAnnotation);
            if (subCmd != null) {
                try {
                    final AbstractBukuCommand abstractCommand = subCmd.newInstance();
                    return abstractCommand.onCommand(sender, qualifiedName.toArray(new String[qualifiedName.size()]));
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new BukuCommandException(e);
                }
            }
        }
        return false;*/
    }

    public Class<? extends AbstractBukuCommand> findSubcommand(String keyword, BukuCommand cmd) {
        return Arrays.asList(cmd.subCommands())
                .stream()
                .filter(subCommand -> {
                    final BukuCommand subCmdAnnotation = subCommand.getAnnotation(BukuCommand.class);
                    return subCmdAnnotation.identifier().equals(keyword);
                })
                .findFirst()
                .orElse(null);
    }

    protected abstract boolean execute(CommandSender sender);

}
