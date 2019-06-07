package de.bensoft.bukkit.buku.descgen.lib;

import de.bensoft.bukkit.buku.descgen.lib.exception.DescGeneratorException;
import de.bensoft.bukkit.buku.descgen.lib.extension.DescgenExtension;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DescGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DescGenerator.class);

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
            String str = content.stream().collect(Collectors.joining(System.lineSeparator()));
            str += applyPlugins();

            Files.write(pluginYmlPath, str.getBytes());
            LOG.info("Wrote plugin.yml to " + pluginYmlPath.toAbsolutePath().toString());

        } catch (IOException e) {
            throw new DescGeneratorException(
                    "Unable to save plugin.yml",
                    e
            );
        }
    }

    public String applyPlugins() {
        final Reflections reflections = new Reflections();
        final Set<Class<? extends DescgenExtension>> plugins = reflections.getSubTypesOf(DescgenExtension.class);

        if (!plugins.isEmpty()) {
            LOG.info("Found " + plugins.size() + " plugins to consider");

            String res = System.lineSeparator();
            res += plugins.stream()
                    .map(pluginClass -> {
                        try {
                            return (DescgenExtension) pluginClass.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new DescGeneratorException("Unable to instantiate plugin " + pluginClass.getName(), e);
                        }
                    }).map(plugin -> {
                        LOG.info("Executing plugin " + plugin.getClass().getSimpleName());
                        return plugin.execute(this.specification);
                    }).collect(Collectors.joining(System.lineSeparator()));
            return res;
        }

        return "";
    }
}
