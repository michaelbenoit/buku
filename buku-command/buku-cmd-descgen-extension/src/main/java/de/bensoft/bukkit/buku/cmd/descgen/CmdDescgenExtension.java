package de.bensoft.bukkit.buku.cmd.descgen;

import de.bensoft.bukkit.buku.cmd.AbstractBukuCommand;
import de.bensoft.bukkit.buku.cmd.util.CommandUtil;
import de.bensoft.bukkit.buku.descgen.lib.PluginSpecification;
import de.bensoft.bukkit.buku.descgen.lib.extension.DescgenExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CmdDescgenExtension implements DescgenExtension {

    private static final Logger LOG = LoggerFactory.getLogger(CmdDescgenExtension.class);

    @Override
    public String execute(PluginSpecification pluginSpecification) {

        final List<AbstractBukuCommand> rootCommands = CommandUtil.getRootCommands();

        if (!rootCommands.isEmpty()) {
            return rootCommands.stream()
                    .map(this::transformCommand).collect(Collectors.joining(System.lineSeparator()));
        }

        return "";
    }

    private String transformCommand(AbstractBukuCommand cmd) {
        LOG.info("Found root command " + cmd.getClass().getSimpleName());
        return "";
    }

}
