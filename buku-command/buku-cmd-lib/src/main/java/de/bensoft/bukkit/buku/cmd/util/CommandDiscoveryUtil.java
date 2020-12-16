package de.bensoft.bukkit.buku.cmd.util;

import de.bensoft.bukkit.buku.cmd.api.Command;
import de.bensoft.bukkit.buku.cmd.exception.BukuCommandException;
import de.bensoft.bukkit.buku.cmd.util.model.BukuCommandDescription;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static de.bensoft.bukkit.buku.cmd.util.ParameterHelper.buildParameterDescriptions;

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
                .getTypesAnnotatedWith(Command.class)
                .stream()
                .map(commandType -> {
                    final Command annotation = commandType.getAnnotation(Command.class);
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
            final Pair<Command, Class> element,
            final BukuCommandDescription parent) {
        final Command bukuCommand = element.getLeft();

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
        commandDescription.getParameters().addAll(buildParameterDescriptions(commandDescription));

        if (parent != null && !parent.getFullAliases().isEmpty()) {
            // Register all local presentations, prefixed by parents aliases
            final List<String> localCommandRepresentations = new ArrayList<>();
            localCommandRepresentations.add(bukuCommand.identifier());
            localCommandRepresentations.addAll(Arrays.asList(bukuCommand.aliases()));

            final List<String> aliases = localCommandRepresentations
                    .stream()
                    .map(localCommand -> parent.getFullAliases()
                            .stream()
                            .map(parentAlias -> String.join(" ", parentAlias, localCommand))
                            .collect(Collectors.toList())
                    )
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            commandDescription.getFullAliases().addAll(aliases);

        } else if (bukuCommand.aliases().length > 0) {
            commandDescription.getFullAliases().addAll(Arrays.asList(bukuCommand.aliases()));
        }

        if (parent != null) {
            parent.getChildren().add(commandDescription);
        }

        Arrays.asList(bukuCommand.subCommands())
                .forEach(subCommandClass -> {
                    final Command subCommandAnnotation = subCommandClass.getAnnotation(Command.class);
                    if (subCommandAnnotation == null) {
                        throw new BukuCommandException(
                                MessageFormat.format(
                                        "Error during command discovery. The sub command {0} must be annotated with @BukuCommand!",
                                        subCommandClass.getCanonicalName()
                                )
                        );
                    }
                    processCommandStructure(Pair.of(subCommandAnnotation, subCommandClass), commandDescription);
                });

        return commandDescription;
    }


}
