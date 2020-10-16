package de.bensoft.bukkit.buku.cmd.util.logging;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

/**
 * Created by CUSTDEV3 on 16/10/2020.
 */
public class LoggingUtil {

    public static void info() {
        final Logger logger = Bukkit.getServer().getLogger();
    }
}
