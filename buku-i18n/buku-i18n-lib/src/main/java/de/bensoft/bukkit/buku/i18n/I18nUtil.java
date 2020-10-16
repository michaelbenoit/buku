package de.bensoft.bukkit.buku.i18n;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class I18nUtil {

    private static final String DEFAULT_LANG = "en";

    private static final Map<String, ResourceBundle> bundleMap = new HashMap<>();

    private static ResourceBundle getBundle(final I18nMessage message, final String language) {
        if (bundleMap.containsKey(message.getClass().getCanonicalName() + language)) {
            return bundleMap.get(message.getClass().getCanonicalName() + language);
        } else {
            final Locale locale = Locale.forLanguageTag(language);
            final String path = message.getClass().getCanonicalName().replaceAll("\\.", "/");
            final ResourceBundle resourceBundle = ResourceBundle.getBundle(
                    path,
                    locale,
                    I18nUtil.class.getClassLoader());

            bundleMap.put(message.getClass().getCanonicalName() + language, resourceBundle);
            return resourceBundle;
        }
    }

    public static String translateMessage(Player player, I18nMessage message, Object... replacements) {
        final ResourceBundle resourceBundle = getBundle(message, getPlayerLanguage(player));

        final String msg;
        try {
            msg = resourceBundle.getString(message.name());
        } catch (MissingResourceException e) {
            return "???UNABLE_TO_GET_I18N_VAL???";
        }

        if (replacements != null) {
            return MessageFormat.format(msg, replacements);
        } else {
            return msg;
        }
    }

    public static String translateMessage(CommandSender commandSender, I18nMessage message, Object... replacements) {

        if (commandSender instanceof Player) {
            return translateMessage((Player) commandSender, message, replacements);
        } else {
            return translateMessage(message, replacements);
        }

    }

    public static String translateMessage(I18nMessage message, Object... replacements) {
        final String msg;
        try {
            msg = getBundle(message, DEFAULT_LANG).getString(message.name());
        } catch (MissingResourceException e) {
            return "???UNABLE_TO_GET_I18N_VAL???";
        }
        if (replacements != null && replacements.length > 0) {
            return MessageFormat.format(msg, replacements);
        } else {
            return msg;
        }
    }

    private static String getPlayerLanguage(Player p) {
        return p.getLocale().replaceAll("_", "-");
        /*try {
            final Method m = getMethod("getHandle", p.getClass());
            final Object handle = m.invoke(p, (Object[]) null);
            final Field fLocale = handle.getClass().getDeclaredField("locale");
            fLocale.setAccessible(true);
            return (String) fLocale.get(handle);
        } catch (Exception e) {
            return DEFAULT_LANG;
        }*/
    }

    private static Method getMethod(String name, Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name))
                return m;
        }
        return null;
    }
}
