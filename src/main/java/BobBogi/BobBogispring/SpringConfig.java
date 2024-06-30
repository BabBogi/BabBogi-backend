package BobBogi.BobBogispring;

import BobBogi.BobBogispring.repository.JpaRecommendationRepository;
import BobBogi.BobBogispring.repository.JpaUserRepository;
import BobBogi.BobBogispring.service.RecommendationService;
import BobBogi.BobBogispring.service.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    private final EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em){
        this.em = em;
    }

    @Bean
    public UserService userService(){
        return new UserService(jpauserRepository());
    }

    @Bean
    public JpaUserRepository jpauserRepository(){
        return new JpaUserRepository(em);
    }

    @Bean
    public RecommendationService recommendationService(){
        return new RecommendationService(jparecommendationRepository());
    }
    @Bean
    public JpaRecommendationRepository jparecommendationRepository(){
        return  new JpaRecommendationRepository(em);
    }
}
