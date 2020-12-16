package de.bensoft.bukkit.buku.persistence.config;

import javax.persistence.EntityManagerFactory;

/**
 * Created by CUSTDEV3 on 04/11/2020.
 */
public abstract class AbstractPersistenceConfiguration {

    public abstract EntityManagerFactory getFactory(final ClassLoader classLoader);

}
