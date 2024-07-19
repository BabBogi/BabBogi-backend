package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.SavedFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedFoodRepository extends JpaRepository<SavedFood, Integer> {
    List<SavedFood> findByFoodnameContaining(String name);
    SavedFood findByFoodname(String foodname);
}
