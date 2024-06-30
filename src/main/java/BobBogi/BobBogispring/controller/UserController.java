package BobBogi.BobBogispring.controller;

import BobBogi.BobBogispring.domain.Consumption;
import BobBogi.BobBogispring.domain.FcmToken;
import BobBogi.BobBogispring.domain.RecommendationNutrition;
import BobBogi.BobBogispring.domain.User;
import BobBogi.BobBogispring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.round;

@Controller
public class UserController {
    private final UserService userService;
    private final RecommendationService recommendationService;

    private final ConsumptionService consumptionService;

    private final NotificationService notificationService;

    @Autowired
    public UserController(UserService userService, RecommendationService recommendationService, ConsumptionService consumptionService, NotificationService notificationService){
        this.userService = userService;
        this.recommendationService = recommendationService;
        this.consumptionService = consumptionService;
        this.notificationService = notificationService;
    }

    @PostMapping("User")
    @ResponseBody
    public Long helloApi(@RequestParam String token, @RequestBody User user) {
        RecommendationNutrition nutrition = new RecommendationNutrition();
        FcmToken fcmToken = new FcmToken();
        Consumption initialvalue = new Consumption();
        Double BMR = 0D;
        if(user.getGender().equals("M")){
            BMR = (88.362+(13.397*user.getWeight())+(4.799*user.getHeight())-(user.getAge()*5.667))*1.35;
        }else{
            BMR = (447.593+(9.247*user.getWeight())+(3.098*user.getHeight())-(user.getAge()*4.339))*1.35;
        }
        BMR = (double)round(BMR);
        Double Temp_carbo = (double) round((BMR*0.55)/4);
        Double Temp_protein = (double) round(user.getWeight());
        Double Temp_fat = (double) round((BMR*0.275)/9);
        Double Temp_sugar = (double) round((BMR*0.1)/4);
        Double Temp_satfat = (double) round((BMR*0.1)/9);
        if(user.getDisease().equals("null")) {
            nutrition.setKcal(BMR);
            nutrition.setCarbohydrate(Temp_carbo);
            nutrition.setSugar(Temp_sugar);
            nutrition.setProtein(Temp_protein);
            nutrition.setFat(Temp_fat);
            nutrition.setTransfat(1D);
            nutrition.setSaturatedfat(Temp_satfat);
            nutrition.setCholesterol(300D);
            nutrition.setNatrium(2000D);
        }else if(user.getDisease().equals("diabetes")){
            nutrition.setKcal(BMR*0.7);
            nutrition.setCarbohydrate(Temp_carbo);
            nutrition.setSugar(Temp_sugar/4);
            nutrition.setProtein(Temp_protein);
            nutrition.setFat(Temp_fat);
            nutrition.setTransfat(0.5D);
            nutrition.setSaturatedfat(Temp_satfat);
            nutrition.setCholesterol(300D);
            nutrition.setNatrium(2000D);
        }else if(user.getDisease().equals("highbloodpressure")) {
            nutrition.setKcal(BMR*0.7);
            nutrition.setCarbohydrate(Temp_carbo);
            nutrition.setSugar(Temp_sugar);
            nutrition.setProtein(Temp_protein);
            nutrition.setFat(Temp_fat);
            nutrition.setTransfat(0.5D);
            nutrition.setSaturatedfat((double)round(BMR*0.005));
            nutrition.setCholesterol(200D);
            nutrition.setNatrium(1000D);
        }
        if(user.getId()!= null){
            userService.join(user);
            List<Consumption> consumptions = consumptionService.getAllConsumptionsByUserIdOrdered(user.getId());
            if(consumptions.size()==1){
                initialvalue.setUserId(user.getId());
                initialvalue.setRemainingkcal(nutrition.getKcal());
                initialvalue.setRemainingCarbohydrate(nutrition.getCarbohydrate());
                initialvalue.setRemainingSugar(nutrition.getSugar());
                initialvalue.setRemainingProtein(nutrition.getProtein());
                initialvalue.setRemainingFat(nutrition.getFat());
                initialvalue.setRemainingTransfat(nutrition.getTransfat());
                initialvalue.setRemainingSaturatedfat(nutrition.getSaturatedfat());
                initialvalue.setRemainingCholesterol(nutrition.getCholesterol());
                initialvalue.setRemainingNatrium(nutrition.getNatrium());
            }else{
                Optional<RecommendationNutrition> temp = recommendationService.findOne(user.getId());
                RecommendationNutrition first = temp.get();
                Consumption last = consumptions.get(consumptions.size()-1);
                initialvalue.setUserId(user.getId());
                initialvalue.setRemainingkcal(nutrition.getKcal()-(first.getKcal()-last.getRemainingkcal()));
                initialvalue.setRemainingCarbohydrate(nutrition.getCarbohydrate()-(first.getCarbohydrate()-last.getRemainingCarbohydrate()));
                initialvalue.setRemainingSugar(nutrition.getSugar()-(first.getSugar()-last.getRemainingSugar()));
                initialvalue.setRemainingProtein(nutrition.getProtein()-(first.getProtein()-last.getRemainingProtein()));
                initialvalue.setRemainingFat(nutrition.getFat()-(first.getFat()-last.getRemainingFat()));
                initialvalue.setRemainingTransfat(nutrition.getTransfat()-(first.getTransfat()-last.getRemainingTransfat()));
                initialvalue.setRemainingSaturatedfat(nutrition.getSaturatedfat()-(first.getSaturatedfat()-last.getRemainingSaturatedfat()));
                initialvalue.setRemainingCholesterol(nutrition.getCholesterol()-(first.getCholesterol()-last.getRemainingCholesterol()));
                initialvalue.setRemainingNatrium(nutrition.getNatrium()-(first.getNatrium()-last.getRemainingNatrium()));
            }
            recommendationService.updateOne(user.getId(), nutrition);
            consumptionService.Initialnutrition(initialvalue);
            return user.getId();
        }else {
            fcmToken.setToken(token);
            userService.join(user);
            fcmToken.setUserId(user.getKey());
            notificationService.saveUserToken(fcmToken);
            nutrition.setId(user.getKey());
            recommendationService.SaveNutritionInfo(nutrition);
            initialvalue.setUserId(user.getKey());
            initialvalue.setRemainingkcal(nutrition.getKcal());
            initialvalue.setRemainingCarbohydrate(nutrition.getCarbohydrate());
            initialvalue.setRemainingSugar(nutrition.getSugar());
            initialvalue.setRemainingProtein(nutrition.getProtein());
            initialvalue.setRemainingFat(nutrition.getFat());
            initialvalue.setRemainingTransfat(nutrition.getTransfat());
            initialvalue.setRemainingSaturatedfat(nutrition.getSaturatedfat());
            initialvalue.setRemainingCholesterol(nutrition.getCholesterol());
            initialvalue.setRemainingNatrium(nutrition.getNatrium());
            consumptionService.Initialnutrition(initialvalue);
            return user.getKey();
        }
    }

    @GetMapping("UserNutrition")
    @ResponseBody
    public RecommendationNutrition helloApi2(@RequestParam("id") Long id) {
        Optional<RecommendationNutrition> RN = recommendationService.findOne(id);
        if(RN.isPresent()){
            return RN.get();
        }else{
            return null;
        }
    }

    @GetMapping("User")
    @ResponseBody
    public List<User> helloApi10(@RequestParam("id") Long id) {
        List<User> result = userService.findOne(id);
        return result;
    }

    @PutMapping("User")
    @ResponseBody
    public RecommendationNutrition helloApi3(@RequestBody RecommendationNutrition UpdateNutrition) {
        List<Consumption> consumptions = consumptionService.getAllConsumptionsByUserIdOrdered(UpdateNutrition.getId());
        Consumption initialvalue = new Consumption();
        if(consumptions.size()==1){
            initialvalue.setUserId(UpdateNutrition.getId());
            initialvalue.setRemainingkcal(UpdateNutrition.getKcal());
            initialvalue.setRemainingCarbohydrate(UpdateNutrition.getCarbohydrate());
            initialvalue.setRemainingSugar(UpdateNutrition.getSugar());
            initialvalue.setRemainingProtein(UpdateNutrition.getProtein());
            initialvalue.setRemainingFat(UpdateNutrition.getFat());
            initialvalue.setRemainingTransfat(UpdateNutrition.getTransfat());
            initialvalue.setRemainingSaturatedfat(UpdateNutrition.getSaturatedfat());
            initialvalue.setRemainingCholesterol(UpdateNutrition.getCholesterol());
            initialvalue.setRemainingNatrium(UpdateNutrition.getNatrium());
        }else{
            Optional<RecommendationNutrition> temp = recommendationService.findOne(UpdateNutrition.getId());
            RecommendationNutrition first = temp.get();
            Consumption last = consumptions.get(consumptions.size()-1);
            initialvalue.setUserId(UpdateNutrition.getId());
            initialvalue.setRemainingkcal(UpdateNutrition.getKcal()-(first.getKcal()-last.getRemainingkcal()));
            initialvalue.setRemainingCarbohydrate(UpdateNutrition.getCarbohydrate()-(first.getCarbohydrate()-last.getRemainingCarbohydrate()));
            initialvalue.setRemainingSugar(UpdateNutrition.getSugar()-(first.getSugar()-last.getRemainingSugar()));
            initialvalue.setRemainingProtein(UpdateNutrition.getProtein()-(first.getProtein()-last.getRemainingProtein()));
            initialvalue.setRemainingFat(UpdateNutrition.getFat()-(first.getFat()-last.getRemainingFat()));
            initialvalue.setRemainingTransfat(UpdateNutrition.getTransfat()-(first.getTransfat()-last.getRemainingTransfat()));
            initialvalue.setRemainingSaturatedfat(UpdateNutrition.getSaturatedfat()-(first.getSaturatedfat()-last.getRemainingSaturatedfat()));
            initialvalue.setRemainingCholesterol(UpdateNutrition.getCholesterol()-(first.getCholesterol()-last.getRemainingCholesterol()));
            initialvalue.setRemainingNatrium(UpdateNutrition.getNatrium()-(first.getNatrium()-last.getRemainingNatrium()));
        }
        if(recommendationService.updateOne(UpdateNutrition.getId(), UpdateNutrition).isPresent()){
            consumptionService.Initialnutrition(initialvalue);
            return recommendationService.updateOne(UpdateNutrition.getId(), UpdateNutrition).get();
        }else{
            return null;
        }
    }

}
