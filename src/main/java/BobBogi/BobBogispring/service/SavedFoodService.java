package BobBogi.BobBogispring.service;

import BobBogi.BobBogispring.domain.SavedFood;
import BobBogi.BobBogispring.repository.SavedFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SavedFoodService {

    @Autowired
    private SavedFoodRepository savedFoodRepository;

    public List<Map<String, String>> getFoodNamesByNameAndCategory(String name) {
        try {
            List<SavedFood> foods = savedFoodRepository.findByFoodnameContaining(name);
            return foods.stream()
                    .map(food -> Map.of(
                            "foodcode", food.getFoodcode(),
                            "foodgroup", food.getFoodgroup(),
                            "food", food.getFood(),
                            "foodname", food.getFoodname(),
                            "company", cleanCompanyName(food.getCompanyname())
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // 로깅 추가
            System.err.println("Error occurred while fetching food names: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // 빈 리스트 반환
        }
    }

    public SavedFood getFoodByCode(String foodcode) {
        try {
            SavedFood savedFood = savedFoodRepository.findByFoodcode(foodcode);
            if (savedFood != null) {
                double weight = savedFood.getFoodweight() != null ? savedFood.getFoodweight() : 100.0;
                double factor = weight / 100.0;

                savedFood.setKcal(savedFood.getKcal() != null ? round(savedFood.getKcal() * factor, 1) : 0.0);
                savedFood.setProtein(savedFood.getProtein() != null ? round(savedFood.getProtein() * factor, 1) : 0.0);
                savedFood.setFat(savedFood.getFat() != null ? round(savedFood.getFat() * factor, 1) : 0.0);
                savedFood.setCarbohydrate(savedFood.getCarbohydrate() != null ? round(savedFood.getCarbohydrate() * factor, 1) : 0.0);
                savedFood.setSugar(savedFood.getSugar() != null ? round(savedFood.getSugar() * factor, 1) : 0.0);
                savedFood.setNatrium(savedFood.getNatrium() != null ? round(savedFood.getNatrium() * factor, 1) : 0.0);
                savedFood.setCholesterol(savedFood.getCholesterol() != null ? round(savedFood.getCholesterol() * factor, 1) : 0.0);
                savedFood.setSaturatedfat(savedFood.getSaturatedfat() != null ? round(savedFood.getSaturatedfat() * factor, 1) : 0.0);
                savedFood.setTransfat(savedFood.getTransfat() != null ? round(savedFood.getTransfat() * factor, 1) : 0.0);

                // companyname 특수 문자 제거 및 기본값 설정
                savedFood.setCompanyname(cleanCompanyName(savedFood.getCompanyname()));
            }
            return savedFood;
        } catch (Exception e) {
            // 로깅 추가
            System.err.println("Error occurred while fetching food details: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String cleanCompanyName(String companyname) {
        if (companyname == null || companyname.trim().isEmpty() || companyname.equals("\r")) {
            return "해당 없음";
        } else {
            // 문자열 끝에 \r이 있으면 제거
            return companyname.trim().replace("\r", "");
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
