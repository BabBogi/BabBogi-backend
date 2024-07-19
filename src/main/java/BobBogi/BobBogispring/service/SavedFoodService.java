package BobBogi.BobBogispring.service;

import BobBogi.BobBogispring.domain.SavedFood;
import BobBogi.BobBogispring.repository.SavedFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SavedFoodService {

    @Autowired
    private SavedFoodRepository savedFoodRepository;

    public List<Map<String, String>> getFoodNamesByNameAndCategory(String name) {
        return savedFoodRepository.findByFoodnameContaining(name)
                .stream()
                .map(food -> Map.of(
                        "foodgroup", food.getFoodgroup(),
                        "food", food.getFood(),
                        "foodname", food.getFoodname(),
                        "company", food.getCompanyname()
                ))
                .collect(Collectors.toList());
    }

    public SavedFood getFoodByName(String foodname) {
        return savedFoodRepository.findByFoodname(foodname);
    }
}
