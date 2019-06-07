package de.bensoft.bukkit.buku.cmd.util;

import de.bensoft.bukkit.buku.cmd.AbstractBukuCommand;
import de.bensoft.bukkit.buku.cmd.BukuCommand;
import de.bensoft.bukkit.buku.cmd.exception.BukuCommandException;
import de.bensoft.bukkit.buku.cmd.i18n.BukuCommandMessages;
import de.bensoft.bukkit.buku.i18n.I18nUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.List;
import java.util.stream.Collectors;

public class CommandUtil {


    public static void registerRootCommands(String rootPackage, JavaPlugin javaPlugin) {
        getRootCommands(rootPackage).forEach(cmd -> {
            javaPlugin.getCommand(cmd.getIdentifier()).setExecutor(cmd);
        });
    }

    public static List<AbstractBukuCommand> getRootCommands() {
        final Reflections reflections = new Reflections();
        return reflections.getTypesAnnotatedWith(BukuCommand.class).stream().filter(t -> {
            final BukuCommand annotation = t.getAnnotation(BukuCommand.class);
            return annotation.isRootCommand();
        }).map(t -> getCommandInstance((Class<AbstractBukuCommand>) t))
                .collect(Collectors.toList());
    }

    public static List<AbstractBukuCommand> getRootCommands(String rootPackage) {
        final Reflections reflections = new Reflections(rootPackage);
        return reflections.getTypesAnnotatedWith(BukuCommand.class).stream().filter(t -> {
            final BukuCommand annotation = t.getAnnotation(BukuCommand.class);
            return annotation.isRootCommand();
        }).map(t -> getCommandInstance((Class<AbstractBukuCommand>) t))
                .collect(Collectors.toList());
    }

    public static <T> T getCommandInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BukuCommandException(e);
        }
    }

    public static Player ensureAndGetPlayer(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(I18nUtil.translateMessage(BukuCommandMessages.ONLY_PLAYER_CAN_EXECUTE));
            return null;
        }
        return (Player) commandSender;
    }
}
