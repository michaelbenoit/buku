package de.bensoft.bukkit.buku.persistence;

import javax.persistence.EntityManager;


/**
 * Created by CUSTDEV3 on 08/11/2020.
 */
public class PersistenceUtil {

    public interface PersistenceAction {
        void execute();
    }

    public static void executeInTransaction(final PersistenceAction callable) {
        final EntityManager em = PersistenceService.getInstance().getEntityManager();

        try {
            em.getTransaction().begin();
            callable.execute();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
        }

    }
}
