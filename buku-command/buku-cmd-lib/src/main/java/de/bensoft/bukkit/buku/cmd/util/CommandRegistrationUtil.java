package de.bensoft.bukkit.buku.cmd.util;

import de.bensoft.bukkit.buku.cmd.api.AbstractBukuCommand;
import de.bensoft.bukkit.buku.cmd.api.AbstractBukuPlayerCommand;
import de.bensoft.bukkit.buku.cmd.api.BukuCommand;
import de.bensoft.bukkit.buku.cmd.exception.BukuCommandException;
import de.bensoft.bukkit.buku.cmd.i18n.BukuCommandMessages;
import de.bensoft.bukkit.buku.cmd.util.model.BukuCommandDescription;
import de.bensoft.bukkit.buku.cmd.util.model.CommandDescriptionUtil;
import de.bensoft.bukkit.buku.i18n.I18nUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by CUSTDEV3 on 16/10/2020.
 */
public class CommandRegistrationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(CommandRegistrationUtil.class);
    private static CommandRegistrationUtil instance;

    public static CommandRegistrationUtil instance() {
        if (instance == null) {
            instance = new CommandRegistrationUtil();
        }
        return instance;
    }

    private List<BukuCommandDescription> commandDescriptions;
    private List<BukuCommandDescription> flatCommandDescriptions;

    private CommandRegistrationUtil() {

    }

    public void registerCommands(List<BukuCommandDescription> commandDescriptions) {
        this.commandDescriptions = commandDescriptions;
        this.flatCommandDescriptions = CommandDescriptionUtil.flatten(commandDescriptions);

        doRegister(commandDescriptions);
    }

    public void doRegister(List<BukuCommandDescription> commandDescriptions) {
        commandDescriptions.forEach(commandDescription -> {
            final BukkitCommand bukkitCommand = getBukkitCommand(commandDescription);

            final CommandMap commandMap = getCommandMap();
            commandMap.register(commandDescription.getFullIdentifier(), bukkitCommand);
            LOG.info("Successfully registered command {}", commandDescription.getFullIdentifier());

            doRegister(commandDescription.getChildren());
        });
    }

    private BukkitCommand getBukkitCommand(final BukuCommandDescription commandDescription) {
        final BukuCommand annotation = commandDescription.getBukuCommandAnnotation();

        final BukkitCommand bukkitCommand = new BukkitCommand(
                commandDescription.getFullIdentifier(),
                annotation.description(),
                annotation.usage(),
                Arrays.asList(annotation.aliases())
        ) {
            @Override
            public boolean execute(final CommandSender commandSender,
                                   final String s,
                                   final String[] arguments) {
                final BukuCommandDescription command = findCommand(s, arguments);
                if (command == null) {
                    commandSender.sendMessage(I18nUtil.translateMessage(BukuCommandMessages.COMMAND_NOT_FOUND));
                    return false;
                }
                final Object commandInstance = getCommandInstance(command.getCommandClass());
                if (!(commandInstance instanceof AbstractBukuCommand)) {
                    printUsage(command, commandSender, arguments);
                    return false;
                } else {
                    return invokeCommand(command, commandSender, arguments);
                }
            }
        };

        return bukkitCommand;
    }

    private BukuCommandDescription findCommand(String root, String[] arguments) {
        final QualifiedName fqn = new QualifiedName(root);
        fqn.addAll(Arrays.asList(arguments));
        return flatCommandDescriptions
                .stream()
                .filter(d -> d.isHandling(fqn))
                .findFirst()
                .orElse(null);
    }

    private void printUsage(final BukuCommandDescription commandDescription,
                            final CommandSender sender,
                            final String[] arguments) {

        final BukuCommand annotation = commandDescription.getBukuCommandAnnotation();
        final List<String> lines = new ArrayList<>();

        lines.add("--------------------------------------------");
        lines.add("  /" + commandDescription.getFullIdentifier());
        lines.add("    " + annotation.description());
        lines.add("--------------------------------------------");
        lines.add("Available sub commands:");

        commandDescription.getChildren().forEach(subCommand -> {
            lines.add(subCommand.getFullIdentifier());
        });

        final String usageString = lines.stream().collect(Collectors.joining(System.lineSeparator()));

        sender.sendMessage(usageString);
    }

    private Boolean invokeCommand(final BukuCommandDescription commandDescription,
                                  final CommandSender sender,
                                  final String[] arguments) {
        final AbstractBukuCommand commandInstance = (AbstractBukuCommand) getCommandInstance(
                commandDescription.getCommandClass());

        if (commandInstance instanceof AbstractBukuPlayerCommand && ensureAndGetPlayer(sender) == null) {
            return false;
        }

        return commandInstance.onCommand(sender, arguments);
    }

    private static <T> T getCommandInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BukuCommandException(e);
        }
    }

    private static CommandMap getCommandMap() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            return (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to retrieve CommandMap of BukkitServer!");
        }
    }

    private Player ensureAndGetPlayer(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(I18nUtil.translateMessage(BukuCommandMessages.ONLY_PLAYER_CAN_EXECUTE));
            return null;
        }
        return (Player) commandSender;
    }

}
