package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.User;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class JpaUserRepository implements UserRepository {
    private final EntityManager em;
    public JpaUserRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public void save(User user) {
        em.persist(user);
        em.flush();
        return;
    }

    @Override
    public List<User> findById(Long id) {
        List<User> result = em.createQuery("select u.id,u.name,u.height,u.weight,u.age,u.gender,u.disease,u.date from User u where u.id = :id", User.class).setParameter("id", id).getResultList();
        return result;
    }

    @Override
    public void delete(Long id) {
        User user = em.createQuery("select u from User u where u.key = :id", User.class).setParameter("id", id).getSingleResult();
        em.remove(user);
        return;
    }

    @Override
    public void update(Long id, Double weight) {
        User user = em.createQuery("select u from User u where u.key = :id", User.class).setParameter("id", id).getSingleResult();
        user.setWeight(weight);
        return;
    }
}
