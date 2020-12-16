package de.bensoft.bukkit.buku.persistence.config;

import de.bensoft.bukkit.buku.persistence.BukuPersistenceConstants;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.hibernate.cfg.Configuration;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;

/**
 * Created by CUSTDEV3 on 04/11/2020.
 */
public class H2PersistenceConfiguration extends AbstractPersistenceConfiguration {
    final File databaseFile;

    public H2PersistenceConfiguration(File databaseFile) {
        this.databaseFile = databaseFile;
    }

    @Override
    public EntityManagerFactory getFactory(final ClassLoader classLoader) {

        final Configuration configuration = new Configuration();
        configuration.setProperty("javax.persistence.jdbc.driver", "org.h2.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("show_sql", "true");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:file:" + databaseFile.getAbsolutePath());

        try (ScanResult scanResult =
                     new ClassGraph()
                             .addClassLoader(classLoader)
                             .enableAnnotationInfo()
                             .scan()) {
            final ClassInfoList entities = scanResult.getClassesWithAnnotation(Entity.class.getCanonicalName());
            entities.forEach(
                    e -> {
                        try {
                            configuration.addAnnotatedClass(Class.forName(e.getName()));
                        } catch (ClassNotFoundException classNotFoundException) {
                            throw new RuntimeException(classNotFoundException);
                        }
                    }
            );


        }
        EntityManagerFactory programmaticEmf =
                Persistence.createEntityManagerFactory(BukuPersistenceConstants.PU_NAME, configuration.getProperties());
        return programmaticEmf;
    }
}
