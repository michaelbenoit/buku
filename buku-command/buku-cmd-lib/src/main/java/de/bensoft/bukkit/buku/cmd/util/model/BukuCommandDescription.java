package de.bensoft.bukkit.buku.cmd.util.model;

import de.bensoft.bukkit.buku.cmd.api.Command;
import de.bensoft.bukkit.buku.cmd.util.QualifiedName;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by CUSTDEV3 on 16/10/2020.
 */
public class BukuCommandDescription {

    private final String fullIdentifier;
    private final List<String> fullAliases = new ArrayList<>();
    private final List<ParameterDescription> parameters = new ArrayList<>();

    private final BukuCommandDescription parent;
    private final List<BukuCommandDescription> children = new ArrayList<>();

    private final Command bukuCommandAnnotation;
    private final Class<?> commandClass;

    public BukuCommandDescription(final String fullIdentifier,
                                  final BukuCommandDescription parent,
                                  final Command bukuCommandAnnotation,
                                  final Class<?> commandClass) {
        this.fullIdentifier = fullIdentifier;
        this.parent = parent;
        this.bukuCommandAnnotation = bukuCommandAnnotation;
        this.commandClass = commandClass;
    }

    public String getCommandMatch(final QualifiedName qualifiedName) {
        final String input = qualifiedName.toString();

        if (fullIdentifier.equals(input)) {
            return fullIdentifier;
        } else if (!fullAliases.isEmpty()) {

            if (parameters.isEmpty()) {
                return fullAliases.stream().filter(fa -> input.equals(fa)).findFirst().orElse(null);
            } else {
                return fullAliases.stream().filter(fa -> input.startsWith(fa)).findFirst().orElse(null);
            }
        }

        return null;
    }

    public void printUsage(final CommandSender sender) {
        final Command annotation = getBukuCommandAnnotation();

        final StringBuilder sbArguments = new StringBuilder();
        getParameters()
                .forEach(p -> {
                    sbArguments.append("<");
                    sbArguments.append(p.getAnnotation().description());
                    sbArguments.append("> ");
                });
        final String argumentString = sbArguments.toString();

        final List<String> lines = new ArrayList<>();
        lines.add("--------------------------------------------");
        lines.add("  /" + getFullIdentifier() + " " + argumentString);
        lines.add("    " + annotation.description());
        lines.add("--------------------------------------------");

        if (!getChildren().isEmpty()) {
            lines.add("Available sub commands:");
            getChildren().forEach(subCommand -> {
                lines.add(subCommand.getFullIdentifier());
            });
        }

        final String usageString = lines.stream().collect(Collectors.joining(System.lineSeparator()));

        sender.sendMessage(usageString);
    }


    public String getFullIdentifier() {
        return fullIdentifier;
    }

    public List<String> getFullAliases() {
        return fullAliases;
    }

    public BukuCommandDescription getParent() {
        return parent;
    }

    public List<BukuCommandDescription> getChildren() {
        return children;
    }

    public Command getBukuCommandAnnotation() {
        return bukuCommandAnnotation;
    }

    public Class<?> getCommandClass() {
        return commandClass;
    }

    public List<ParameterDescription> getParameters() {
        return parameters;
    }
}
