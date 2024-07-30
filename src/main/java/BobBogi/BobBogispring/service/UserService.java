package BobBogi.BobBogispring.service;

import BobBogi.BobBogispring.domain.User;
import BobBogi.BobBogispring.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public class UserService {
    private final UserRepository repository;
    public UserService(UserRepository userRepositroy){
        this.repository = userRepositroy;
    }
    public void join(User user) {
        repository.save(user);
        return;
    }
    public List<User> findOne(Long id) {
        return repository.findById(id);
    }
    public void updateOne(Long id, User user) {
        return;
    }

    public void DeleteUserWeight(Long id){
        repository.delete(id);
        return;
    }

    public void UpdateUserWeight(Long id, Double weight){
        repository.update(id, weight);
        return;
    }
}
