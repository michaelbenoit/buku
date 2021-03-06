package de.bensoft.bukkit.buku.cmd.util;

import de.bensoft.bukkit.buku.cmd.api.AbstractCommand;
import de.bensoft.bukkit.buku.cmd.api.AbstractPlayerCommand;
import de.bensoft.bukkit.buku.cmd.api.Command;
import de.bensoft.bukkit.buku.cmd.api.CommandFailureException;
import de.bensoft.bukkit.buku.cmd.exception.BukuCommandException;
import de.bensoft.bukkit.buku.cmd.i18n.BukuCommandMessages;
import de.bensoft.bukkit.buku.cmd.util.model.BukuCommandDescription;
import de.bensoft.bukkit.buku.cmd.util.model.CommandArguments;
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
import java.util.Arrays;
import java.util.List;

import static de.bensoft.bukkit.buku.cmd.util.ParameterHelper.validateParameters;

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
        final Command annotation = commandDescription.getBukuCommandAnnotation();

        final BukkitCommand bukkitCommand = new BukkitCommand(
                commandDescription.getFullIdentifier(),
                annotation.description(),
                annotation.usage(),
                commandDescription.getFullAliases()
        ) {

            @Override
            public boolean execute(final CommandSender commandSender,
                                   final String s,
                                   final String[] arguments) {
                return onExecuteBukkitCommand(commandSender, s, arguments);
            }

            @Override
            public List<String> tabComplete(CommandSender sender,
                                            String alias,
                                            String[] args) throws IllegalArgumentException {
                return onTabComplete(sender, alias, args);
            }
        };

        return bukkitCommand;
    }

    private List<String> onTabComplete(CommandSender sender,
                                       String alias,
                                       String[] args) {

        final QualifiedName fqn = new QualifiedName(alias);
        fqn.addAll(Arrays.asList(args));

        final BukuCommandDescription command = findCommand(fqn);
        if (command == null) {
            return Arrays.asList();
        }

        /*if (!command.getChildren().isEmpty()) {
            return command.getChildren().stream()
                    .map(c -> new QualifiedName(c.getFullIdentifier()).skip(1).toString())
                    .collect(Collectors.toList());
        }*/

        return Arrays.asList();
    }

    private boolean onExecuteBukkitCommand(CommandSender commandSender, String s, String[] arguments) {
        final QualifiedName fqn = new QualifiedName(s);
        fqn.addAll(Arrays.asList(arguments));

        final BukuCommandDescription command = findCommand(fqn);
        if (command == null) {
            commandSender.sendMessage(I18nUtil.translateMessage(BukuCommandMessages.COMMAND_NOT_FOUND));
            return false;
        }

        final CommandArguments cargs = new CommandArguments(fqn.toString().replaceAll(command.getCommandMatch(fqn), "").trim());
        final Object commandInstance = getCommandInstance(command.getCommandClass());

        if (!(commandInstance instanceof AbstractCommand)) {
            command.printUsage(commandSender);
            return false;
        } else {
            return invokeCommand(command, (AbstractCommand) commandInstance, commandSender, cargs);
        }

    }

    private BukuCommandDescription findCommand(final QualifiedName qualifiedName) {
        return flatCommandDescriptions
                .stream()
                .filter(d -> d.getCommandMatch(qualifiedName) != null)
                .findFirst()
                .orElse(null);
    }

    private Boolean invokeCommand(final BukuCommandDescription commandDescription,
                                  final AbstractCommand commandInstance,
                                  final CommandSender sender,
                                  final CommandArguments arguments) {

        if (commandInstance instanceof AbstractPlayerCommand && ensureAndGetPlayer(sender) == null) {
            return false;
        }

        if (validateParameters(commandDescription, sender, arguments)) {
            try {
                ParameterHelper.injectArgumentValues(commandDescription, commandInstance, arguments);
                return commandInstance.onCommand(sender, arguments);
            } catch (CommandFailureException e) {
                sender.sendMessage("ERROR: " + e.getMessage());
            }
        }
        return false;
    }

    private static <T> T getCommandInstance(final Class<T> clazz) {
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
