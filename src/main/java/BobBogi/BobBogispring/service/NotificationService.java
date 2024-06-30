package BobBogi.BobBogispring.service;

import BobBogi.BobBogispring.domain.Consumption;
import BobBogi.BobBogispring.domain.FcmToken;
import BobBogi.BobBogispring.domain.RecommendationNutrition;
import BobBogi.BobBogispring.repository.ConsumptionRepository;
import BobBogi.BobBogispring.repository.FcmTokenRepository;
import BobBogi.BobBogispring.repository.RecommendationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private FcmTokenRepository fcmTokenRepository;

    @Autowired
    private FcmService fcmService;

    @Autowired
    private ConsumptionRepository remainingIntakeRepository;

    @Autowired
    private RecommendationRepository recommendedIntakeRepository;

    public void sendNotification(String token) {
        FcmToken fcmToken = fcmTokenRepository.findByToken(token).get();
        Long userId = fcmToken.getUserId();

        // get the latest remaining intake and recommended intake information
        Optional<Consumption> remainingIntake = remainingIntakeRepository.findTopByUserIdOrderByIdDesc(userId);
        Optional<RecommendationNutrition> recommendedIntake = recommendedIntakeRepository.findById(userId);

        if (remainingIntake.isPresent() && recommendedIntake.isPresent()) {
            Consumption remainingIntakeObj = remainingIntake.get();
            RecommendationNutrition recommendedIntakeObj = recommendedIntake.get();

            double kcalPercentage = calculateIntakePercentage(remainingIntakeObj.getRemainingkcal(), recommendedIntakeObj.getKcal());
            double carbohydratePercentage = calculateIntakePercentage(remainingIntakeObj.getRemainingCarbohydrate(), recommendedIntakeObj.getCarbohydrate());
            double sugarPercentage = calculateIntakePercentage(remainingIntakeObj.getRemainingSugar(), recommendedIntakeObj.getSugar());
            double proteinPercentage = calculateIntakePercentage(remainingIntakeObj.getRemainingProtein(), recommendedIntakeObj.getProtein());
            double fatPercentage = calculateIntakePercentage(remainingIntakeObj.getRemainingFat(), recommendedIntakeObj.getFat());
            double saturatedFatPercentage = calculateIntakePercentage(remainingIntakeObj.getRemainingSaturatedfat(), recommendedIntakeObj.getSaturatedfat());
            double transFatPercentage = calculateIntakePercentage(remainingIntakeObj.getRemainingTransfat(), recommendedIntakeObj.getTransfat());
            double natriumPercentage = calculateIntakePercentage(remainingIntakeObj.getRemainingNatrium(), recommendedIntakeObj.getNatrium());
            double cholesterolPercentage = calculateIntakePercentage(remainingIntakeObj.getRemainingCholesterol(), recommendedIntakeObj.getCholesterol());

            checkAndSendNotification(token,"Kcal", kcalPercentage);
            checkAndSendNotification(token, "carbohydrate", carbohydratePercentage);
            checkAndSendNotification(token, "sugar", sugarPercentage);
            checkAndSendNotification(token, "protein", proteinPercentage);
            checkAndSendNotification(token, "fat", fatPercentage);
            checkAndSendNotification(token, "saturated fat", saturatedFatPercentage);
            checkAndSendNotification(token, "trans fat", transFatPercentage);
            checkAndSendNotification(token, "natrium", natriumPercentage);
            checkAndSendNotification(token, "cholesterol", cholesterolPercentage);
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
            title = "위험";
            body = "지금 " + nutrition + "의 섭취량이 100%를 넘었습니다";
            fcmService.sendNotification(token, title, body);
        } else if (intakePercentage > 0.8) {
            title = "경고!";
            body = "지금 " + nutrition + "의 섭취량이 80%를 넘었습니다";
            fcmService.sendNotification(token, title, body);

        } else if (intakePercentage > 0.5) {
            title = "주의!";
            body = "지금 " + nutrition + "의 섭취량이 50%를 넘었습니다";
            fcmService.sendNotification(token, title, body);
        }
    }

    public void  saveUserToken(FcmToken Token){
        fcmTokenRepository.save(Token);
    }

}
