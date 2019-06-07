package de.bensoft.bukkit.buku.descgen.lib;

import java.util.Map;

public class PluginSpecification {

    private final String name;
    private final String version;
    private final String main;
    private final Map<String, String> other;

    public PluginSpecification(String name, String version, String main, Map<String, String> other) {
        this.name = name;
        this.version = version;
        this.main = main;
        this.other = other;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getOther() {
        return other;
    }

    public String getMain() {
        return main;
    }
}
