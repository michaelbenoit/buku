package de.bensoft.bukkit.buku.cmd.api;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(value = RUNTIME)
@Target(value = FIELD)
public @interface Parameter {

    String description();

}
