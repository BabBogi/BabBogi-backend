package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.SavedFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedFoodRepository extends JpaRepository<SavedFood, Integer> {

    List<SavedFood> findByFoodnameContainingAndFoodIn(String foodname, List<String> food);

    SavedFood findByFoodname(String foodname);
}
