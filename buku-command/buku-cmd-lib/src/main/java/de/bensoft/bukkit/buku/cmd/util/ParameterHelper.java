package de.bensoft.bukkit.buku.cmd.util;

import de.bensoft.bukkit.buku.cmd.api.AbstractCommand;
import de.bensoft.bukkit.buku.cmd.api.CommandFailureException;
import de.bensoft.bukkit.buku.cmd.api.Parameter;
import de.bensoft.bukkit.buku.cmd.api.qualifier.AbstractQualifier;
import de.bensoft.bukkit.buku.cmd.api.qualifier.ParameterQualifier;
import de.bensoft.bukkit.buku.cmd.api.qualifier.QualifierException;
import de.bensoft.bukkit.buku.cmd.registry.QualifierRegistry;
import de.bensoft.bukkit.buku.cmd.util.model.BukuCommandDescription;
import de.bensoft.bukkit.buku.cmd.util.model.CommandArguments;
import de.bensoft.bukkit.buku.cmd.util.model.ParameterDescription;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by CUSTDEV3 on 29/10/2020.
 */
public class ParameterHelper {


    public static List<ParameterDescription> buildParameterDescriptions(final BukuCommandDescription bukuCommandDescription) {
        final Class<?> commandClass = bukuCommandDescription.getCommandClass();
        final List<Field> fields = Arrays.asList(commandClass.getDeclaredFields());

        return fields.stream()
                .filter(field -> field.getAnnotation(Parameter.class) != null)
                .map(field -> {
                    final Parameter annotation = field.getAnnotation(Parameter.class);

                    final Annotation qualifier = Arrays.asList(field.getAnnotations())
                            .stream()
                            .filter(a -> a.annotationType().getAnnotation(ParameterQualifier.class) != null)
                            .findFirst()
                            .orElse(null);


                    return new ParameterDescription(annotation, field, qualifier);
                })
                .collect(Collectors.toList());
    }

    public static boolean validateParameters(final BukuCommandDescription commandDescription,
                                             final CommandSender sender,
                                             final CommandArguments arguments) {

        if (commandDescription.getParameters().isEmpty()) {
            return true;
        }

        if (commandDescription.getParameters().size() != arguments.getArguments().size()) {
            sender.sendMessage("Invalid Parameters!");
            commandDescription.printUsage(sender);
            return false;
        }


        return true;
    }

    public static void injectArgumentValues(final BukuCommandDescription commandDescription,
                                            final AbstractCommand commandInstance,
                                            final CommandArguments arguments) throws CommandFailureException {
        for (int i = 0; i < commandDescription.getParameters().size(); i++) {
            final ParameterDescription parameterDescription = commandDescription.getParameters().get(i);

            try {
                final String input = arguments.getArguments().get(i);
                final Annotation qualifier = parameterDescription.getQualifier();

                final Object value;
                if (qualifier != null) {
                    final AbstractQualifier abstractQualifierInstance = QualifierRegistry.getInstance().getAbstractQualifierInstance(qualifier);
                    value = abstractQualifierInstance.getValue(input);
                } else {
                    value = input;
                }

                parameterDescription.getField().setAccessible(true);
                parameterDescription.getField().set(commandInstance, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to set value of parameter!", e);
            } catch (QualifierException e) {
                throw new CommandFailureException(e.getMessage(), e);
            }
        }
    }
}
