package BobBogi.BobBogispring.service;

import BobBogi.BobBogispring.domain.SavedFood;
import BobBogi.BobBogispring.repository.SavedFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedFoodService {

    @Autowired
    private SavedFoodRepository savedFoodRepository;

    public List<String> getFoodNamesByNameAndCategory(String name, List<String> categories) {
        return savedFoodRepository.findByFoodnameContainingAndFoodIn(name, categories)
                .stream()
                .map(food -> food.getFoodname().replaceAll("[\\r\\n]", "") + " - " + food.getCompanyname().replaceAll("[\\r\\n]", ""))
                .collect(Collectors.toList());
    }

    public SavedFood getFoodByName(String foodname) {
        return savedFoodRepository.findByFoodname(foodname);
    }
}
