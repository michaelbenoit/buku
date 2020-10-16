package de.bensoft.bukkit.buku.cmd;

import de.bensoft.bukkit.buku.cmd.util.CommandDiscoveryUtil;
import de.bensoft.bukkit.buku.cmd.util.CommandRegistrationUtil;
import de.bensoft.bukkit.buku.cmd.util.model.BukuCommandDescription;

import java.util.List;

/**
 * Created by CUSTDEV3 on 16/10/2020.
 */
public class BukuCommandInitializer {

    private static BukuCommandInitializer instance;

    public static BukuCommandInitializer instance() {
        if (instance == null) {
            instance = new BukuCommandInitializer();
        }
        return instance;
    }

    private BukuCommandInitializer() {

    }

    public void initializeBukuCommand(final String commandRootPackage) {
        final String path = BukuCommandInitializer.class.getClassLoader()
                .getResource("jul-log.properties")
                .getFile();
        System.setProperty("java.util.logging.config.file", path);

        final List<BukuCommandDescription> bukuCommandDescriptions = CommandDiscoveryUtil
                .instance()
                .collectRootCommands(commandRootPackage);

        CommandRegistrationUtil.instance().registerCommands(bukuCommandDescriptions);
    }
}
