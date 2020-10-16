package de.bensoft.bukkit.buku.cmd.util;

import de.bensoft.bukkit.buku.cmd.api.BukuCommand;
import de.bensoft.bukkit.buku.cmd.exception.BukuCommandException;
import de.bensoft.bukkit.buku.cmd.util.model.BukuCommandDescription;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDiscoveryUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CommandDiscoveryUtil.class);

    private static CommandDiscoveryUtil instance;

    public static CommandDiscoveryUtil instance() {
        if (instance == null) {
            instance = new CommandDiscoveryUtil();
        }
        return instance;
    }

    private CommandDiscoveryUtil() {

    }

    public List<BukuCommandDescription> collectRootCommands(String rootPackage) {
        final Reflections reflections = new Reflections(rootPackage);

        final List<BukuCommandDescription> rootCommands = reflections
                .getTypesAnnotatedWith(BukuCommand.class)
                .stream()
                .map(commandType -> {
                    final BukuCommand annotation = commandType.getAnnotation(BukuCommand.class);
                    return Pair.of(annotation, (Class) commandType);
                })
                .filter(pair -> pair.getLeft().isRootCommand())
                .map(pair -> processCommandStructure(pair, null))
                .collect(Collectors.toList());
        LOG.info("Discovered {} root commands", rootCommands.size());
        return rootCommands;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private BukuCommandDescription processCommandStructure(
            final Pair<BukuCommand, Class> element,
            final BukuCommandDescription parent) {
        final BukuCommand bukuCommand = element.getLeft();

        final StringBuilder sbFullIdentifier = new StringBuilder();
        if (parent != null) {
            sbFullIdentifier.append(parent.getFullIdentifier());
            sbFullIdentifier.append(" ");
        }
        sbFullIdentifier.append(bukuCommand.identifier());

        final BukuCommandDescription commandDescription = new BukuCommandDescription(
                sbFullIdentifier.toString(),
                parent,
                bukuCommand,
                element.getRight()
        );
        if (parent != null) {
            parent.getChildren().add(commandDescription);
        }

        Arrays.asList(bukuCommand.subCommands())
                .forEach(subCommandClass -> {
                    final BukuCommand subCommandAnnotation = subCommandClass.getAnnotation(BukuCommand.class);
                    if (subCommandAnnotation == null) {
                        throw new BukuCommandException(
                                MessageFormat.format(
                                        "Error during command disovery. The sub command {0} must be annotated with @BukuCommand!",
                                        subCommandClass.getCanonicalName()
                                )
                        );
                    }
                    processCommandStructure(Pair.of(subCommandAnnotation, subCommandClass), commandDescription);
                });

        return commandDescription;
    }


}
