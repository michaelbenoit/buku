package de.bensoft.bukkit.buku.cmd.api.qualifier;

/**
 * Created by CUSTDEV3 on 01/11/2020.
 */
public abstract class AbstractQualifier<T> {

    public abstract T getValue(final String input) throws QualifierException;
}
