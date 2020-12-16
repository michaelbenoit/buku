package de.bensoft.bukkit.buku.config.api;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by CUSTDEV3 on 03/11/2020.
 */
@Documented
@Retention(value = RUNTIME)
@Target(value = TYPE)
public @interface ConfigurationClass {
}
