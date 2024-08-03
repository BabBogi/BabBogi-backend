package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    List<User> findById(Long id);
    public Long update(Long id, Double weight);
    public Long delete(Long id);
}
