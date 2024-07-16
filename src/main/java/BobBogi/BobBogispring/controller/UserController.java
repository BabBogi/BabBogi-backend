package BobBogi.BobBogispring.controller;

import BobBogi.BobBogispring.domain.*;
import BobBogi.BobBogispring.service.*;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.round;

@Controller
public class UserController {
    private final UserService userService;
    private final RecommendationService recommendationService;

    private final ConsumptionService consumptionService;

    private final NotificationService notificationService;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

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

    @GetMapping("/dailyreport")
    @ResponseBody
    public String dailyreport(@RequestParam(name = "id")Long id, @RequestParam(name = "date")String datestr){
        LocalDate date = LocalDate.parse(datestr);
        String RequestMessage;
        List<Consumption> consumptionList;
        List<User> result = userService.findOne(id);
        User user = result.get(result.size()-1);
        String userDisease="";
        String userGender;

        if(user.getGender().equals("M")){
            userGender = "남";
        }else{
            userGender = "여";
        }
        if(user.getDisease().equals("null")) {
            userDisease = "없음";
        }else if(user.getDisease().equals("diabetes")) {
            userDisease = "당뇨병";
        }else if(user.getDisease().equals("highbloodpressure")) {
            userDisease = "고혈압";
        }
        String.valueOf(user.getAge());
        String.valueOf(user.getHeight());
        String.valueOf(user.getWeight());

        Double kcal=0d;
        Double carbohydrate=0d;
        Double sugar=0d;
        Double protein=0d;
        Double fat=0d;
        Double transfat=0d;
        Double saturatedfat=0d;
        Double cholesterol=0d;
        Double natrium=0d;

        consumptionList = consumptionService.getAllConsumptionsByUserIdOrdered(id,date);
        if(consumptionList.isEmpty()){
            return null;
        }else{
            for(int i=0; i<consumptionList.size(); i++){
                kcal += consumptionList.get(i).getKcal()*consumptionList.get(i).getFoodCount();
                carbohydrate += consumptionList.get(i).getCarbohydrate()*consumptionList.get(i).getFoodCount();
                sugar += consumptionList.get(i).getSugar()*consumptionList.get(i).getFoodCount();
                protein += consumptionList.get(i).getProtein()*consumptionList.get(i).getFoodCount();
                fat += consumptionList.get(i).getFat()*consumptionList.get(i).getFoodCount();
                transfat += consumptionList.get(i).getTransfat()*consumptionList.get(i).getFoodCount();
                saturatedfat += consumptionList.get(i).getSaturatedfat()*consumptionList.get(i).getFoodCount();
                cholesterol += consumptionList.get(i).getCholesterol()*consumptionList.get(i).getFoodCount();
                natrium += consumptionList.get(i).getNatrium()*consumptionList.get(i).getFoodCount();
            }
        }
        RequestMessage = "성별: "+userGender+",\n"+
                "나이: "+String.valueOf(user.getAge())+",\n"+
                "키: "+String.valueOf(user.getHeight())+",\n"+
                "몸무게: "+String.valueOf(user.getWeight())+",\n"+
                "보유 성인병: "+userDisease+",\n"+
                "섭취한 열량(단위: kcal): "+String.valueOf(kcal)+",\n"+
                "섭취한 탄수화물의 양(단위: g): "+String.valueOf(carbohydrate)+",\n"+
                "섭취한 당의 양(단위: g): "+String.valueOf(sugar)+",\n"+
                "섭취한 단백질의 양(단위: g): "+String.valueOf(protein)+",\n"+
                "섭취한 지방의 양(단위: g): "+String.valueOf(fat)+",\n"+
                "섭취한 트랜스지방의 양(단위: g): "+String.valueOf(transfat)+",\n"+
                "섭취한 포화지방의 양(단위: g): "+String.valueOf(saturatedfat)+",\n"+
                "섭취한 콜레스테롤의 양(단위: mg): "+String.valueOf(cholesterol)+",\n"+
                "섭취한 나트륨의 양(단위: mg): "+String.valueOf(natrium)+"\n"+
                "앞의 내용은 나의 건강정보와 내가 하루 동안 섭취한 영양소의 양이야. 내가 하루동안 영양 섭취를 잘 했는지 평가해!";
        System.out.println(RequestMessage);
        ChatGPTRequest request = new ChatGPTRequest(model, RequestMessage);
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    @GetMapping("/report")
    @ResponseBody
    public String report(@RequestParam(name = "id")Long id, @RequestParam(name = "enddate")String startdatestr, @RequestParam(name = "enddate")String enddatestr){
        LocalDate startdate = LocalDate.parse(startdatestr);
        LocalDate enddate = LocalDate.parse(enddatestr);
        Long range = ChronoUnit.DAYS.between(startdate, enddate) + 1;
        String RequestMessage;
        List<List<Consumption>> consumptionLists = null;
        Double kcal=0d;
        Double carbohydrate=0d;
        Double sugar=0d;
        Double protein=0d;
        Double fat=0d;
        Double transfat=0d;
        Double saturatedfat=0d;
        Double cholesterol=0d;
        Double natrium=0d;
        List<Double> kcalList = null;
        List<Double> carbohydrateList = null;
        List<Double> sugarList = null;
        List<Double> proteinList = null;
        List<Double> fatList = null;
        List<Double> transfatList = null;
        List<Double> saturatedfatList = null;
        List<Double> cholesterolList = null;
        List<Double> natriumListList = null;
        for(int i=0; i<range; i++){
            consumptionLists.add(consumptionService.getAllConsumptionsByUserIdOrdered(id,startdate.plusDays(i)));
        }
        if(consumptionLists.size()==range){
            return null;
        }else{
            for(int i=0; i<consumptionLists.size(); i++) {
                for (int j = 0; j < consumptionLists.get(i).size(); j++) {
                    kcal += consumptionLists.get(i).get(j).getKcal();
                    carbohydrate += consumptionLists.get(i).get(j).getCarbohydrate();
                    sugar += consumptionLists.get(i).get(j).getSugar();
                    protein += consumptionLists.get(i).get(j).getProtein();
                    fat += consumptionLists.get(i).get(j).getFat();
                    transfat += consumptionLists.get(i).get(j).getTransfat();
                    saturatedfat += consumptionLists.get(i).get(j).getSaturatedfat();
                    cholesterol += consumptionLists.get(i).get(j).getCholesterol();
                    natrium += consumptionLists.get(i).get(j).getNatrium();
                }
                kcalList.add(kcal);
                carbohydrateList.add(carbohydrate);
                sugarList.add(sugar);
                proteinList.add(protein);
                fatList.add(fat);
                transfatList.add(transfat);
                saturatedfatList.add(saturatedfat);
                cholesterolList.add(cholesterol);
                natriumListList.add(natrium);
                kcal=0d;
                carbohydrate=0d;
                sugar=0d;
                protein=0d;
                fat=0d;
                transfat=0d;
                saturatedfat=0d;
                cholesterol=0d;
                natrium=0d;
            }
        }
        RequestMessage = "이 만큼의 영양소를 "+ "" + "에 섭취했어 분석해줘";
        ChatGPTRequest request = new ChatGPTRequest(model, RequestMessage);
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
        //return chatGPTResponse.getChoices().get(0).getMessage().getContent();
        return "구현중입니다";
    }
}
