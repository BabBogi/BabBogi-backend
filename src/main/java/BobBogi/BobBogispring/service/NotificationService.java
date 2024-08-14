package BobBogi.BobBogispring.service;

import BobBogi.BobBogispring.domain.Consumption;
import BobBogi.BobBogispring.domain.FcmToken;
import BobBogi.BobBogispring.domain.RecommendationNutrition;
import BobBogi.BobBogispring.repository.ConsumptionRepository;
import BobBogi.BobBogispring.repository.FcmTokenRepository;
import BobBogi.BobBogispring.repository.RecommendationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private FcmTokenRepository fcmTokenRepository;

    @Autowired
    private FcmService fcmService;

    @Autowired
    private ConsumptionRepository ConsumptionIntakeRepository;

    @Autowired
    private RecommendationRepository recommendedIntakeRepository;

    public void sendNotification(String token) {
        FcmToken fcmToken = fcmTokenRepository.findByToken(token).get();
        Long userId = fcmToken.getUserId();

        List<Consumption> consumptions = ConsumptionIntakeRepository.findAllByUserIdOrderByIdAndDate(userId, LocalDate.now());
        Optional<RecommendationNutrition> recommendedIntake = recommendedIntakeRepository.findById(userId);

        if (!consumptions.isEmpty() && recommendedIntake.isPresent()) {
            RecommendationNutrition recommendedIntakeObj = recommendedIntake.get();

            double kcal = 0d;
            double carbohydrate = 0d;
            double sugar = 0d;
            double protein = 0d;
            double fat = 0d;
            double saturatedFat = 0d;
            double transFat = 0d;
            double natrium = 0d;
            double cholesterol = 0d;

            for(Consumption consumption:consumptions){
                kcal+=consumption.getKcal();
                carbohydrate+=consumption.getCarbohydrate();
                sugar+=consumption.getSugar();
                protein+=consumption.getProtein();
                fat+=consumption.getFat();
                saturatedFat+=consumption.getSaturatedfat();
                transFat+=consumption.getTransfat();
                natrium+=consumption.getNatrium();
                cholesterol+=consumption.getCholesterol();
            }

            double kcalPercentage = kcal/recommendedIntakeObj.getKcal();
            double carbohydratePercentage = carbohydrate/recommendedIntakeObj.getCarbohydrate();
            double sugarPercentage = sugar/recommendedIntakeObj.getSugar();
            double proteinPercentage = protein/recommendedIntakeObj.getProtein();
            double fatPercentage = fat/recommendedIntakeObj.getFat();
            double saturatedFatPercentage = saturatedFat/recommendedIntakeObj.getSaturatedfat();
            double transFatPercentage = transFat/recommendedIntakeObj.getTransfat();
            double natriumPercentage = natrium/recommendedIntakeObj.getNatrium();
            double cholesterolPercentage = cholesterol/recommendedIntakeObj.getCholesterol();

            checkAndSendNotification(token,"열량", kcalPercentage);
            checkAndSendNotification(token, "탄수화물", carbohydratePercentage);
            checkAndSendNotification(token, "당", sugarPercentage);
            checkAndSendNotification(token, "단백질", proteinPercentage);
            checkAndSendNotification(token, "지방", fatPercentage);
            checkAndSendNotification(token, "포화지방", saturatedFatPercentage);
            checkAndSendNotification(token, "트랜스지방", transFatPercentage);
            checkAndSendNotification(token, "나트륨", natriumPercentage);
            checkAndSendNotification(token, "콜레스테롤", cholesterolPercentage);
        } else {
            System.out.println("Remaining or recommended intake information not found for user: " + userId);
        }
    }

    private double calculateIntakePercentage(double remaining, double recommended) {
        return 1 - (remaining / recommended);
    }

    private void checkAndSendNotification(String token, String nutrition, double intakePercentage) {
        String title;
        String body;

        if (intakePercentage > 1.0) {
            title = "위험!";
            body = "현재 " + nutrition + "의 섭취량이 권장량의 100%를 넘었습니다!";
            fcmService.sendNotification(token, title, body);
        } else if (intakePercentage > 0.8) {
            title = "경고!";
            body = "현재 " + nutrition + "의 섭취량이 권장량의 80%를 넘었습니다!";
            fcmService.sendNotification(token, title, body);

        } else if (intakePercentage > 0.5) {
            title = "주의!";
            body = "현재 " + nutrition + "의 섭취량이 권장량의 50%를 넘었습니다!";
            fcmService.sendNotification(token, title, body);
        }
    }

    public void  saveUserToken(FcmToken Token){
        fcmTokenRepository.save(Token);
    }

}
