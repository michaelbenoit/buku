package de.bensoft.bukkit.buku.persistence;

import de.bensoft.bukkit.buku.persistence.config.AbstractPersistenceConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created by CUSTDEV3 on 04/11/2020.
 */
public class PersistenceService {

    private static PersistenceService instance;

    public static PersistenceService getInstance() {
        if (instance == null) {
            instance = new PersistenceService();
        }
        return instance;
    }

    private EntityManager entityManager;
    private EntityManagerFactory factory;

    private PersistenceService() {
    }

    public void init(final AbstractPersistenceConfiguration configuration,
                     final ClassLoader pluginClassLoader) {

        final ClassLoader originalContextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(pluginClassLoader);
            factory = configuration.getFactory(pluginClassLoader);
        } finally {
            Thread.currentThread().setContextClassLoader(originalContextClassLoader);
        }

        entityManager = factory.createEntityManager();
    }

    public void release() {
        entityManager.close();
        factory.close();
    }

    public EntityManager getEntityManager() {
        if (entityManager == null) {
            throw new RuntimeException("BukuPersistence isn't configured!");
        }

        return entityManager;
    }


}
