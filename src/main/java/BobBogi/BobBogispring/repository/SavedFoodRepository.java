package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.SavedFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedFoodRepository extends JpaRepository<SavedFood, Integer> {

    @Query("SELECT f FROM SavedFood f WHERE f.foodname LIKE %:name%")
    List<SavedFood> findByNameContaining(@Param("name") String name);

    SavedFood findByFoodname(String foodname);
}
