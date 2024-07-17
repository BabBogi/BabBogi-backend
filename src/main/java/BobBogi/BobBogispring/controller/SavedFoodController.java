package BobBogi.BobBogispring.controller;

import BobBogi.BobBogispring.domain.SavedFood;
import BobBogi.BobBogispring.service.SavedFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SavedFoodController {

    @Autowired
    private SavedFoodService savedFoodService;

    // 음식 이름과 카테고리로 검색하여 관련된 음식 목록과 회사 이름을 반환하는 API
    @GetMapping("/search")
    public List<String> searchFoodNames(@RequestParam String name, @RequestParam List<String> categories) {
        return savedFoodService.getFoodNamesByNameAndCategory(name, categories);
    }

    // 특정 음식의 영양소 정보를 반환하는 API
    @GetMapping("/food")
    public SavedFood getFoodDetails(@RequestParam String foodname) {
        return savedFoodService.getFoodByName(foodname);
    }
}
