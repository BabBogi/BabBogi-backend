package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    List<User> findById(Long id);
    public void update(Long id, Double weight);
    public void delete(Long id);
}
