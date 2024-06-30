package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.RecommendationNutrition;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaRecommendationRepository implements RecommendationRepository {
    private final EntityManager em;
    public JpaRecommendationRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public void save(RecommendationNutrition nutrition) {
        em.persist(nutrition);
        return;
    }

    @Override
    public Optional<RecommendationNutrition> findById(Long id) {
        RecommendationNutrition nutrition = em.find(RecommendationNutrition.class, id);
        return Optional.ofNullable(nutrition);
    }

    @Override
    public Optional<RecommendationNutrition> update(Long id, RecommendationNutrition nutrition) {
        Optional<RecommendationNutrition> BeforeNutrition = Optional.ofNullable(em.find(RecommendationNutrition.class, id));
        if(BeforeNutrition.isPresent()) {
            BeforeNutrition.get().setKcal(nutrition.getKcal());
            BeforeNutrition.get().setCarbohydrate(nutrition.getCarbohydrate());
            BeforeNutrition.get().setSugar(nutrition.getSugar());
            BeforeNutrition.get().setProtein(nutrition.getProtein());
            BeforeNutrition.get().setFat(nutrition.getFat());
            BeforeNutrition.get().setSaturatedfat(nutrition.getSaturatedfat());
            BeforeNutrition.get().setTransfat(nutrition.getTransfat());
            BeforeNutrition.get().setNatrium(nutrition.getNatrium());
            BeforeNutrition.get().setCholesterol(nutrition.getCholesterol());
            return BeforeNutrition;
        }else{
            return Optional.empty();
        }
    }

    @Override
    public List<RecommendationNutrition> findAll() {
        return em.createQuery("SELECT r FROM RecommendationNutrition r", RecommendationNutrition.class).getResultList();
    }

    @Override
    public void delete(Long id) {
        return;
    }
}
