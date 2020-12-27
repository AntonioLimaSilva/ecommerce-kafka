package br.com.luciano.ecommerce;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.io.Closeable;
import java.util.Optional;

public class UserService implements Closeable {

    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public UserService() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("Ecommerce-PU");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public Optional<UserEntity> findByUuid(String userId) {
        var jpql = "select us from UserEntity us where us.email = :userId";

        try {
            return Optional.of(entityManager.createQuery(jpql, UserEntity.class)
                    .setParameter("userId", userId).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void create(UserEntity userEntity) {
        entityManager.getTransaction().begin();
        entityManager.persist(userEntity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void close() {
        this.entityManagerFactory.close();
        this.entityManager.close();
    }

}
