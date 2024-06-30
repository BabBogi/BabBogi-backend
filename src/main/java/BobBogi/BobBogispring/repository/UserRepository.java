package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    List<User> findById(Long id);
    void update(Long id, User user);
    void delete(Long id);
}
