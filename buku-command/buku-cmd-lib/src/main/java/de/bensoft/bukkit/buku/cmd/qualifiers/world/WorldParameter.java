package de.bensoft.bukkit.buku.cmd.qualifiers.world;

import de.bensoft.bukkit.buku.cmd.api.qualifier.ParameterQualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by CUSTDEV3 on 01/11/2020.
 */
@ParameterQualifier
@Documented
@Retention(value = RUNTIME)
@Target(value = {FIELD, TYPE})
public @interface WorldParameter {
}
