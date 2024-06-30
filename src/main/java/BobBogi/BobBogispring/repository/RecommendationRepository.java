package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.RecommendationNutrition;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository {
    void save(RecommendationNutrition nutrition);
    Optional<RecommendationNutrition> findById(Long id);
    Optional<RecommendationNutrition> update(Long id, RecommendationNutrition nutrition);

    List<RecommendationNutrition> findAll();  // 모든 권장 영양 정보를 가져오는 메서드 추가 (시간)
    void delete(Long id);
}
