package de.bensoft.bukkit.buku.cmd.util.model;

import de.bensoft.bukkit.buku.cmd.api.BukuCommand;
import de.bensoft.bukkit.buku.cmd.util.QualifiedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CUSTDEV3 on 16/10/2020.
 */
public class BukuCommandDescription {

    private final String fullIdentifier;
    private final BukuCommandDescription parent;
    private final List<BukuCommandDescription> children = new ArrayList<>();

    private final BukuCommand bukuCommandAnnotation;
    private final Class<?> commandClass;

    public BukuCommandDescription(String fullIdentifier,
                                  BukuCommandDescription parent,
                                  BukuCommand bukuCommandAnnotation,
                                  Class<?> commandClass) {
        this.fullIdentifier = fullIdentifier;
        this.parent = parent;
        this.bukuCommandAnnotation = bukuCommandAnnotation;
        this.commandClass = commandClass;
    }

    public boolean isHandling(final QualifiedName qualifiedName) {
        return fullIdentifier.equals(qualifiedName.toString());
    }

    public String getFullIdentifier() {
        return fullIdentifier;
    }

    public BukuCommandDescription getParent() {
        return parent;
    }

    public List<BukuCommandDescription> getChildren() {
        return children;
    }

    public BukuCommand getBukuCommandAnnotation() {
        return bukuCommandAnnotation;
    }

    public Class<?> getCommandClass() {
        return commandClass;
    }
}
