package de.bensoft.bukkit.buku.descgen.lib;

import de.bensoft.bukkit.buku.descgen.lib.exception.DescGeneratorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DescGenerator {

    private final PluginSpecification specification;

    public DescGenerator(PluginSpecification pluginSpecification) {
        this.specification = pluginSpecification;
    }

    public void generate(String path) {
        final List<String> content = new ArrayList<>();

        content.add("name: " + specification.getName());
        content.add("main: " + specification.getMain());
        content.add("version: " + specification.getVersion());


        final Path pluginYmlPath = Paths.get(path, "plugin.yml");
        try {
            final String str = content.stream().collect(Collectors.joining(System.lineSeparator()));
            Files.write(pluginYmlPath, str.getBytes());
        } catch (IOException e) {
            throw new DescGeneratorException(
                    "Unable to save plugin.yml",
                    e
            );
        }
    }
}
