package de.bensoft.bukkit.buku.cmd.util.model;

import de.bensoft.bukkit.buku.cmd.api.Parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by CUSTDEV3 on 29/10/2020.
 */
public class ParameterDescription {

    private final Parameter annotation;
    private final Field field;
    private final Annotation qualifier;

    public ParameterDescription(final Parameter annotation,
                                final Field field,
                                final Annotation qualifier) {
        this.annotation = annotation;
        this.field = field;
        this.qualifier = qualifier;
    }

    public Parameter getAnnotation() {
        return annotation;
    }

    public Field getField() {
        return field;
    }

    public Annotation getQualifier() {
        return qualifier;
    }
}
