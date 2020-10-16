package de.bensoft.bukkit.buku.cmd.api;

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

    Class<? extends AbstractBukuCommand>[] subCommands() default {};

    boolean isRootCommand() default false;

    String[] aliases() default {};

    String usage() default "";

    String description();

}
