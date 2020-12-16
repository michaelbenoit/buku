package de.bensoft.bukkit.buku.cmd.registry;

import de.bensoft.bukkit.buku.cmd.api.qualifier.AbstractQualifier;
import de.bensoft.bukkit.buku.cmd.api.qualifier.ParameterQualifier;
import de.bensoft.bukkit.buku.cmd.util.Pair;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by CUSTDEV3 on 01/11/2020.
 */
public class QualifierRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(QualifierRegistry.class);
    private static QualifierRegistry instance;

    private Map<Annotation, AbstractQualifier> qualifierCache;

    public static QualifierRegistry getInstance() {
        if (instance == null) {
            instance = new QualifierRegistry();
        }
        return instance;
    }

    private QualifierRegistry() {

    }

    public AbstractQualifier getAbstractQualifierInstance(final Annotation annotation) {
        return qualifierCache.get(annotation);
    }

    public void init(final String rootPackage) {
        final Configuration config = new ConfigurationBuilder()
                .addUrls(
                        ClasspathHelper.forPackage(rootPackage)
                )
                .addUrls(
                        ClasspathHelper.forPackage("de.bensoft.bukkit.buku.cmd.qualifiers.world")
                )
                .filterInputsBy(new FilterBuilder().includePackage(
                        rootPackage,
                        "de.bensoft.bukkit.buku.cmd.qualifiers.world"
                ));


        final Reflections reflections = new Reflections(config);

        this.qualifierCache = reflections
                .getSubTypesOf(AbstractQualifier.class)
                .stream()
                .map(qualifier -> {
                    final Annotation qualifierAnnotation = Arrays.asList(qualifier.getAnnotations())
                            .stream()
                            .filter(a -> a.annotationType().getAnnotation(ParameterQualifier.class) != null)
                            .findFirst()
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            MessageFormat.format(
                                                    "Qualifier \"{0}\"class has no Qualifier Annotation!",
                                                    qualifier.getCanonicalName()
                                            )

                                    )
                            );

                    return Pair.of(qualifierAnnotation, qualifier);
                })
                .map(p -> {
                    final AbstractQualifier instance;
                    try {
                        instance = p.getRight().getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                    return Pair.of(p.getLeft(), instance);
                })
                .collect(Collectors.toMap(
                        p -> p.getLeft(),
                        v -> v.getRight()
                ));

        LOG.info("Discovered {} qualifiers", qualifierCache.size());
    }

}