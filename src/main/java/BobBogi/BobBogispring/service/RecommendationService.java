package BobBogi.BobBogispring.service;

import BobBogi.BobBogispring.domain.RecommendationNutrition;
import BobBogi.BobBogispring.repository.RecommendationRepository;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Transactional
public class RecommendationService {
    private final RecommendationRepository Recommendationrepository;
    public RecommendationService(RecommendationRepository  RecommendationRepositroy){
        this.Recommendationrepository = RecommendationRepositroy;
    }

    public void SaveNutritionInfo(RecommendationNutrition nutrition) {
        Recommendationrepository.save(nutrition);
        return;
    }
    public Optional<RecommendationNutrition> findOne(Long id) {
        return Recommendationrepository.findById(id);
    }
    public Optional<RecommendationNutrition> updateOne(Long id, RecommendationNutrition UpdateNutrition) {
        return Recommendationrepository.update(id, UpdateNutrition);
    }
}
