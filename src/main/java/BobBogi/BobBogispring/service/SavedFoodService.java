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

    public List<String> getFoodNamesByName(String name) {
        List<SavedFood> foods = savedFoodRepository.findByNameContaining(name);
        if (foods.isEmpty()) {
            return List.of("없음");
        }
        return foods.stream()
                .map(SavedFood::getFoodname)
                .collect(Collectors.toList());
    }

    public SavedFood getFoodByName(String name) {
        return savedFoodRepository.findByFoodname(name);
    }
}
