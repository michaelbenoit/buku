package de.bensoft.bukkit.buku.cmd;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(value = RUNTIME)
@Target(value = TYPE)
public @interface BukuCommand {

    String identifier();

    Class<? extends AbstractBukuCommand>[] subcommands() default {};
    
    boolean isRootCommand() default false;
}
