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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    public Long helloApi(@RequestParam String token, @RequestBody User user, @RequestParam boolean recommendation) {
        RecommendationNutrition nutrition = RecommendationNutritionCalculator(user);
        FcmToken fcmToken = new FcmToken();
        user.setDate(String.valueOf(LocalDateTime.now()));
        userService.join(user);
        if(user.getId()!= null){
            if(recommendation) {
                recommendationService.updateOne(user.getId(), nutrition);
            }
            return user.getId();
        }else {
            user.setId(user.getKey());
            fcmToken.setToken(token);
            fcmToken.setUserId(user.getKey());
            notificationService.saveUserToken(fcmToken);
            nutrition.setId(user.getKey());
            recommendationService.SaveNutritionInfo(nutrition);
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
        return recommendationService.updateOne(UpdateNutrition.getId(), UpdateNutrition).get();
    }

    @GetMapping("/dailyreport")
    @ResponseBody
    public String dailyreport(@RequestParam(name = "id")Long id, @RequestParam(name = "date")String datestr){
        LocalDate date = LocalDate.parse(datestr);
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("(E)", Locale.KOREAN);
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
                "나이: "+ user.getAge() +",\n"+
                "키: "+ user.getHeight() +",\n"+
                "몸무게: "+ user.getWeight() +",\n"+
                "보유 성인병: "+userDisease+",\n"+
                "섭취한 열량(단위: kcal): "+ kcal +",\n"+
                "섭취한 탄수화물의 양(단위: g): "+ carbohydrate +",\n"+
                "섭취한 당의 양(단위: g): "+ sugar +",\n"+
                "섭취한 단백질의 양(단위: g): "+ protein +",\n"+
                "섭취한 지방의 양(단위: g): "+ fat +",\n"+
                "섭취한 트랜스지방의 양(단위: g): "+ transfat +",\n"+
                "섭취한 포화지방의 양(단위: g): "+ saturatedfat +",\n"+
                "섭취한 콜레스테롤의 양(단위: mg): "+ cholesterol +",\n"+
                "섭취한 나트륨의 양(단위: mg): "+ natrium +"\n"+
                "앞의 내용은 나의 건강정보와 내가 하루 동안 섭취한 영양소의 양이야. 내가 하루동안 영양 섭취를 잘 했는지 평가해!";

        ChatGPTRequest request = new ChatGPTRequest(model, RequestMessage);
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
        return "설정 날짜: "+datestr+date.format(dayFormatter)+"\n\n"+chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    @GetMapping("/report")
    @ResponseBody
    public String report(@RequestParam(name = "id")Long id, @RequestParam(name = "startdate")String startdatestr, @RequestParam(name = "enddate")String enddatestr){
        LocalDate startdate = LocalDate.parse(startdatestr);
        LocalDate enddate = LocalDate.parse(enddatestr);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("(E)", Locale.KOREAN);
        Long range = ChronoUnit.DAYS.between(startdate, enddate) + 1;
        List<String> RequestMessages = new ArrayList<>();
        List<List<Consumption>> consumptionLists = new ArrayList<>();
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

        Double kcal=0d;
        Double carbohydrate=0d;
        Double sugar=0d;
        Double protein=0d;
        Double fat=0d;
        Double transfat=0d;
        Double saturatedfat=0d;
        Double cholesterol=0d;
        Double natrium=0d;
        List<Double> kcalList = new ArrayList<>();
        List<Double> carbohydrateList = new ArrayList<>();
        List<Double> sugarList = new ArrayList<>();
        List<Double> proteinList = new ArrayList<>();
        List<Double> fatList = new ArrayList<>();
        List<Double> transfatList = new ArrayList<>();
        List<Double> saturatedfatList = new ArrayList<>();
        List<Double> cholesterolList = new ArrayList<>();
        List<Double> natriumList = new ArrayList<>();

        for(int i=0; i<range; i++){
            consumptionLists.add(consumptionService.getAllConsumptionsByUserIdOrdered(id,startdate.plusDays(i)));
        }
        if(consumptionLists.size()!=range){
            return "";
        }else{
            for(int i=0; i<consumptionLists.size(); i++) {
                for (int j = 0; j < consumptionLists.get(i).size(); j++) {
                    kcal += consumptionLists.get(i).get(j).getKcal()*consumptionLists.get(i).get(j).getFoodCount();
                    carbohydrate += consumptionLists.get(i).get(j).getCarbohydrate()*consumptionLists.get(i).get(j).getFoodCount();
                    sugar += consumptionLists.get(i).get(j).getSugar()*consumptionLists.get(i).get(j).getFoodCount();
                    protein += consumptionLists.get(i).get(j).getProtein()*consumptionLists.get(i).get(j).getFoodCount();
                    fat += consumptionLists.get(i).get(j).getFat()*consumptionLists.get(i).get(j).getFoodCount();
                    transfat += consumptionLists.get(i).get(j).getTransfat()*consumptionLists.get(i).get(j).getFoodCount();
                    saturatedfat += consumptionLists.get(i).get(j).getSaturatedfat()*consumptionLists.get(i).get(j).getFoodCount();
                    cholesterol += consumptionLists.get(i).get(j).getCholesterol()*consumptionLists.get(i).get(j).getFoodCount();
                    natrium += consumptionLists.get(i).get(j).getNatrium()*consumptionLists.get(i).get(j).getFoodCount();
                }
                kcalList.add(kcal);
                carbohydrateList.add(carbohydrate);
                sugarList.add(sugar);
                proteinList.add(protein);
                fatList.add(fat);
                transfatList.add(transfat);
                saturatedfatList.add(saturatedfat);
                cholesterolList.add(cholesterol);
                natriumList.add(natrium);
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

        RequestMessages.add("성별: " + userGender + ",\n" +
                "나이: " + user.getAge() + ",\n" +
                "키: " + user.getHeight() + ",\n" +
                "몸무게: " + user.getWeight() + ",\n" +
                "보유 성인병: " + userDisease);
        for(int i=0; i<range; i++) {
            RequestMessages.add(startdate.plusDays(i).format(dateFormatter)+startdate.plusDays(i).format(dayFormatter)+"\n"+
                    "섭취한 열량(단위: kcal): " + kcalList.get(i) + ",\n" +
                    "섭취한 탄수화물의 양(단위: g): " + carbohydrateList.get(i) + ",\n" +
                    "섭취한 당의 양(단위: g): " + sugarList.get(i) + ",\n" +
                    "섭취한 단백질의 양(단위: g): " + proteinList.get(i) + ",\n" +
                    "섭취한 지방의 양(단위: g): " + fatList.get(i) + ",\n" +
                    "섭취한 트랜스지방의 양(단위: g): " + transfatList.get(i) + ",\n" +
                    "섭취한 포화지방의 양(단위: g): " + saturatedfatList.get(i) + ",\n" +
                    "섭취한 콜레스테롤의 양(단위: mg): " + cholesterolList.get(i) + ",\n" +
                    "섭취한 나트륨의 양(단위: mg): " + natriumList.get(i));
        }
        RequestMessages.add("앞의 내용은 나의 건강정보와 내가 " + range + "일 동안 섭취한 영양소의 양이야. " + "내가 " + range + "일 동안 영양 섭취를 잘 했는지 분석해! 섭취한 영양소의 양이 전부다 0인 날은 무시하고 분석에 포함하지 말아 명령이야. 오래걸려도 되니까 분석을 꼭 생성해 다음에 답변을 주겠다는 식의 답변은 하지마 명령이야.");

        ChatGPTRequest request = new ChatGPTRequest(model, RequestMessages, RequestMessages.size());
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
        return "설정 기간: "+startdatestr+startdate.format(dayFormatter)+" ~ "+enddatestr+enddate.format(dayFormatter)+"\n\n"+chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    @DeleteMapping("/deleteweight")
    @ResponseBody
    public void deleteweight(@RequestParam(name = "id")Long id, @RequestParam(name = "recommendation")boolean recommendation){
        User user = userService.DeleteUserWeight(id);
        List<User> userlist = userService.findOne(user.getId());
        int size = userlist.size();
        if(userlist.get(size-1).getKey()<id && recommendation){
            User temp = userlist.get(size-1);
            temp.setAge(user.getAge());
            temp.setDisease(user.getDisease());
            temp.setHeight(user.getHeight());
            temp.setGender(user.getGender());
            temp.setName(user.getName());
            userService.updateOne(temp.getKey(),temp);
            RecommendationNutrition nutrition = RecommendationNutritionCalculator(temp);
            recommendationService.updateOne(temp.getId(), nutrition);
        }
    }

    @PutMapping("/updateweight")
    @ResponseBody
    public void updateweight(@RequestParam(name = "id")Long id, @RequestParam(name = "weight")Double weight, @RequestParam(name = "recommendation")boolean recommendation){
        Long userId = userService.UpdateUserWeight(id, weight);
        List<User> user = userService.findOne(userId);
        int size = user.size();
        System.out.println(user.get(size-1).getKey());
        System.out.println(id);
        if(user.get(size - 1).getKey().equals(id) && recommendation){
            RecommendationNutrition nutrition = RecommendationNutritionCalculator(user.get(size-1));
            recommendationService.updateOne(userId, nutrition);
        }
    }

    private RecommendationNutrition RecommendationNutritionCalculator(User user){
        RecommendationNutrition nutrition = new RecommendationNutrition();
        Double BMR = 0D;
        if(user.getGender().equals("M")){
            BMR = (88.362+(13.397*user.getWeight())+(4.799*user.getHeight())-(user.getAge()*5.667))*1.35;
        }else{
            BMR = (447.593+(9.247*user.getWeight())+(3.098*user.getHeight())-(user.getAge()*4.339))*1.35;
        }
        BMR = (double) round(BMR*10.0)/10.0;
        Double Temp_carbo = (double) round(((BMR*0.55)/4)*10.0)/10.0;
        Double Temp_protein = user.getWeight();
        Double Temp_fat = (double) round(((BMR*0.275)/9)*10.0)/10.0;
        Double Temp_sugar = (double) round(((BMR*0.1)/4)*10.0)/10.0;
        Double Temp_satfat = (double) round(((BMR*0.1)/9)*10.0)/10.0;
        Double Temp_transfat = (double) round(((BMR*0.01)/9)*10.0)/10.0;
        if(user.getDisease().equals("null")) {
            nutrition.setKcal(BMR);
            nutrition.setCarbohydrate(Temp_carbo);
            nutrition.setSugar(Temp_sugar);
            nutrition.setProtein(Temp_protein);
            nutrition.setFat(Temp_fat);
            nutrition.setTransfat(Temp_transfat);
            nutrition.setSaturatedfat(Temp_satfat);
            nutrition.setCholesterol(300D);
            nutrition.setNatrium(2300D);
        }else if(user.getDisease().equals("diabetes")){
            nutrition.setKcal((double) round(BMR*0.85*10.0)/10.0);
            nutrition.setCarbohydrate((double) round(((BMR*0.85*0.45)/4)*10.0)/10.0);
            nutrition.setSugar((double) round(Temp_sugar*0.85*0.55*10.0)/10.0);
            nutrition.setProtein(Temp_protein);
            nutrition.setFat((double) round(Temp_fat*0.85*10.0)/10.0);
            nutrition.setTransfat((double) round(Temp_transfat*0.85*0.7*10.0)/10.0);
            nutrition.setSaturatedfat((double) round(Temp_satfat*0.85*0.7*10.0)/10.0);
            nutrition.setCholesterol(200D);
            nutrition.setNatrium(2300D);
        }else if(user.getDisease().equals("highbloodpressure")) {
            nutrition.setKcal((double) round(BMR*0.85*10.0)/10.0);
            nutrition.setCarbohydrate((double) round(Temp_carbo*0.85*10.0)/10.0);
            nutrition.setSugar((double) round(Temp_sugar*0.85*10.0)/10.0);
            nutrition.setProtein(Temp_protein);
            nutrition.setFat((double) round(Temp_fat*0.85*10.0)/10.0);
            nutrition.setTransfat((double) round(Temp_transfat*0.85*0.7*10.0)/10.0);
            nutrition.setSaturatedfat((double) round(Temp_satfat*0.85*0.7*10.0)/10.0);
            nutrition.setCholesterol(200D);
            nutrition.setNatrium(1500D);
        }
        return nutrition;
    }
}
