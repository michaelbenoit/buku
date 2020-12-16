package de.bensoft.bukkit.buku.config.impl;

import de.bensoft.bukkit.buku.config.api.ConfigurationClass;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by CUSTDEV3 on 03/11/2020.
 */
public class ConfigProxy<T> {

    private boolean writingDisabled;

    private T rootProxy;
    private Class<T> rootClass;
    private File configFile;

    private Set<Class> proxiedClasses = new HashSet<>();

    private ConfigProxy(final File configFile,
                        final Class<T> clazz) {
        this.rootClass = clazz;
        this.configFile = configFile;

        final T subject = readFromYaml(configFile, clazz);
        writingDisabled = true;
        rootProxy = proxyObject(subject);
        proxyAllFieldsAndLists(rootProxy, clazz);

        writingDisabled = false;
    }

    public T getRootProxy() {
        return rootProxy;
    }

    public static <T> ConfigProxy<T> create(final File configFile,
                                            final Class<T> clazz) {

        return new ConfigProxy<>(configFile, clazz);
    }

    private Object intercept(final Object instance,
                             final Method method,
                             final Object[] args,
                             final MethodProxy proxy) throws Throwable {

        final boolean isSetter = method.getName().startsWith("set");
        if (isSetter) {
            if (args != null &&
                    args.length == 1 &&
                    args[0] != null &&
                    args[0].getClass().isAnnotationPresent(ConfigurationClass.class) &&
                    !isProxy(args[0])) {
                final Object newProxy = proxyObject(args[0]);
                args[0] = newProxy;
            }
        }
        final Object result = proxy.invokeSuper(instance, args);

        if (isSetter) {
            writeToYaml();
        }

        return result;
    }

    private static boolean isProxy(Object object) {
        return Arrays.asList(object.getClass().getDeclaredFields())
                .stream()
                .filter(f -> f.getName().equals("CGLIB$BOUND"))
                .findFirst()
                .isPresent();
    }

    public void writeToYaml() {
        if (writingDisabled) {
            return;
        }
        if (rootProxy == null) {
            return;
        }

        final Representer representer = new Representer() {
            @Override
            protected NodeTuple representJavaBeanProperty(Object javaBean,
                                                          Property property,
                                                          Object propertyValue,
                                                          Tag customTag) {
                final String propertyName = property.getName();
                if (propertyName.startsWith("CGLIB") || "callbacks".equals(propertyName)) {
                    return null;
                }
                return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        };

        proxiedClasses.forEach(c -> representer.addClassTag(c, Tag.MAP));

        final DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setIndent(2);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        final Yaml yaml = new Yaml(representer, dumperOptions);
        yaml.setBeanAccess(BeanAccess.PROPERTY);
        try {
            yaml.dump(rootProxy, new FileWriter(configFile));
        } catch (IOException e) {
            throw new RuntimeException("Unable to write yaml file!", e);
        }
    }

    private static <T> T readFromYaml(final File file, final Class<T> clazz) {
        final Yaml yaml = new Yaml(new CustomClassLoaderConstructor(clazz.getClassLoader()));

        final T subject;
        if (!file.exists()) {
            // Create a default config
            final Constructor<?> constructor = clazz.getConstructors()[0];
            try {
                subject = (T) constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Unable to create empty config", e);
            }
        } else {
            try {
                subject = yaml.loadAs(new FileInputStream(file), clazz);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Unable to read config", e);
            }
        }

        return subject;
    }


    public <P> P proxyObject(final P currentObj) {
        if (currentObj == null) {
            return null;
        }

        if (isProxy(currentObj) || !currentObj.getClass().isAnnotationPresent(ConfigurationClass.class)) {
            return currentObj;
        }

        final Class clazz = currentObj.getClass();
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(currentObj.getClass());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) ->
                intercept(obj, method, args, proxy));
        final Object proxy = enhancer.create();

        writingDisabled = true;
        if (currentObj != null) {
            final BeanCopier beanCopier = BeanCopier.create(clazz, clazz, false);
            beanCopier.copy(currentObj, proxy, null);
        }
        writingDisabled = false;

        proxiedClasses.add(proxy.getClass());

        return (P) proxy;
    }

    private void proxyAllFieldsAndLists(final Object obj,
                                        final Class originalClass) {

        try {
            for (final Field declaredField : originalClass.getDeclaredFields()) {
                declaredField.setAccessible(true);

                if (declaredField.getType().equals(List.class)) {
                    final Object value = declaredField.get(obj);

                    if (value != null) {
                        final ParameterizedType parameterizedType = (ParameterizedType) declaredField.getGenericType();
                        final Class genericType = (Class) parameterizedType.getActualTypeArguments()[0];
                        final List list = (List) value;

                        if (genericType.isAnnotationPresent(ConfigurationClass.class)) {
                            list.forEach(e -> proxyAllFieldsAndLists(e, e.getClass()));
                            final List newItems = (List) list.stream().map(e -> proxyObject(e)).collect(Collectors.toList());
                            list.clear();
                            list.addAll(newItems);
                        }

                        final BukuConfigList replacement = new BukuConfigList(list, this);
                        declaredField.set(obj, replacement);
                    }

                } else if (declaredField.getType().isAnnotationPresent(ConfigurationClass.class)) {
                    final Object value = declaredField.get(obj);
                    if (value != null) {
                        final Object proxy = proxyObject(value);
                        declaredField.set(obj, proxy);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to replace lists", e);
        }

    }
}
