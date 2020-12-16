package de.bensoft.bukkit.buku.config.api;

import de.bensoft.bukkit.buku.config.impl.ConfigProxy;

import java.io.File;

/**
 * Created by CUSTDEV3 on 03/11/2020.
 */
public class BukuConfigService {

    public static <T> T getConfig(final File file,
                                  final Class<T> clazz) {
        return ConfigProxy.create(file, clazz).getRootProxy();
    }

}
