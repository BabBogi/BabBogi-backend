package BobBogi.BobBogispring.service;

import BobBogi.BobBogispring.domain.Consumption;
import BobBogi.BobBogispring.domain.RecommendationNutrition;
import BobBogi.BobBogispring.repository.ConsumptionRepository;
import BobBogi.BobBogispring.repository.RecommendationRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ConsumptionService {

    private static final Logger logger = LoggerFactory.getLogger(ConsumptionService.class); //로그 메시지를 기록 가능 예를 들어, 메서드가 호출될 때 로그를 남기거나, 예외가 발생했을 때 로그를 남기는 것 가능

    //private: 클래스 외부에서 접근할 수 없다.
    //final: 필드가 한 번 초기화되면 더 이상 변경될 수 없다. 즉, 해당 필드는 생성자를 통해서만 초기화된다.
    private final ConsumptionRepository consumptionRepository;
    private final RecommendationRepository recommendationRepository;

    @Autowired
    public ConsumptionService(ConsumptionRepository consumptionRepository, RecommendationRepository recommendationRepository) {
        this.consumptionRepository = consumptionRepository;
        this.recommendationRepository = recommendationRepository;
    }

    public void Initialnutrition(Consumption initialvalue){
        consumptionRepository.save(initialvalue);
    }
    public List<Consumption> saveAllConsumptions(List<Consumption> consumptions) {
        Optional<Consumption> lastConsumptionOpt = consumptionRepository.findTopByUserIdOrderByIdDesc(consumptions.get(0).getUserId());

        if (!lastConsumptionOpt.isPresent()) {
            throw new IllegalArgumentException("No previous consumption found for user id: " + consumptions.get(0).getUserId());
        }

        Consumption lastConsumption = lastConsumptionOpt.get();

        Double remainingKcal = lastConsumption.getRemainingkcal();
        Double remainingCarbohydrate = lastConsumption.getRemainingCarbohydrate();
        Double remainingSugar = lastConsumption.getRemainingSugar();
        Double remainingProtein = lastConsumption.getRemainingProtein();
        Double remainingFat = lastConsumption.getRemainingFat();
        Double remainingTransfat = lastConsumption.getRemainingTransfat();
        Double remainingSaturatedfat = lastConsumption.getRemainingSaturatedfat();
        Double remainingCholesterol = lastConsumption.getRemainingCholesterol();
        Double remainingNatrium = lastConsumption.getRemainingNatrium();

        for (Consumption consumption : consumptions) {
            if (consumption.getFoodCount() == null) {
                consumption.setFoodCount(1L);
            }

            // 남은 영양소 계산
            remainingKcal -= consumption.getKcal() * consumption.getFoodCount();
            remainingCarbohydrate -= consumption.getCarbohydrate() * consumption.getFoodCount();
            remainingSugar -= consumption.getSugar() * consumption.getFoodCount();
            remainingProtein -= consumption.getProtein() * consumption.getFoodCount();
            remainingFat -= consumption.getFat() * consumption.getFoodCount();
            remainingTransfat -= consumption.getTransfat() * consumption.getFoodCount();
            remainingSaturatedfat -= consumption.getSaturatedfat() * consumption.getFoodCount();
            remainingCholesterol -= consumption.getCholesterol() * consumption.getFoodCount();
            remainingNatrium -= consumption.getNatrium() * consumption.getFoodCount();

            consumption.setRemainingkcal(remainingKcal);
            consumption.setRemainingCarbohydrate(remainingCarbohydrate);
            consumption.setRemainingSugar(remainingSugar);
            consumption.setRemainingProtein(remainingProtein);
            consumption.setRemainingFat(remainingFat);
            consumption.setRemainingTransfat(remainingTransfat);
            consumption.setRemainingSaturatedfat(remainingSaturatedfat);
            consumption.setRemainingCholesterol(remainingCholesterol);
            consumption.setRemainingNatrium(remainingNatrium);

            consumption.setDate(LocalDateTime.now());
        }

        return consumptionRepository.saveAll(consumptions);
    }


    @Scheduled(cron = "0 0 0 * * *") // 초(0~59), 분(0~59), 시(0~23), 일(1~31), 월(1~12), 요일(0~6)
    public void addConsumptionEntry() {
        logger.info("addConsumptionEntry() 메서드 실행"); // logger 중에 단순하게 정보를  기록하기 위해서 사용

        // 모든 사용자에 대해 권장 영양 정보를 가져옴
        List<RecommendationNutrition> allRecommendations = recommendationRepository.findAll();

        for (RecommendationNutrition recommendedNutrition : allRecommendations) {
            Long userId = recommendedNutrition.getId();  // 권장 영양 정보의 id를 userId로 사용

            Consumption consumption = new Consumption();
            consumption.setUserId(userId);
            consumption.setRemainingkcal(recommendedNutrition.getKcal());
            consumption.setRemainingCarbohydrate(recommendedNutrition.getCarbohydrate());
            consumption.setRemainingSugar(recommendedNutrition.getSugar());
            consumption.setRemainingProtein(recommendedNutrition.getProtein());
            consumption.setRemainingFat(recommendedNutrition.getFat());
            consumption.setRemainingTransfat(recommendedNutrition.getTransfat());
            consumption.setRemainingSaturatedfat(recommendedNutrition.getSaturatedfat());
            consumption.setRemainingCholesterol(recommendedNutrition.getCholesterol());
            consumption.setRemainingNatrium(recommendedNutrition.getNatrium());

            consumption.setDate(LocalDateTime.now());
            consumptionRepository.save(consumption);
            logger.info("새로운 Consumption 엔트리 저장 완료 for userId: " + userId);
        }
    }

    public List<Consumption> getAllConsumptionsByUserIdOrdered(Long userId, LocalDate date) {
        return consumptionRepository.findAllByUserIdOrderByIdAndDate(userId, date);
    }

    public List<Consumption> getAllConsumptionsByUserIdOrdered(Long userId) {
        return consumptionRepository.findAllByUserIdOrderByIdAndDate(userId);
    }

}
