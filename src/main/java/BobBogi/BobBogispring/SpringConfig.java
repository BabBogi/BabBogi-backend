package BobBogi.BobBogispring;

import BobBogi.BobBogispring.repository.JpaRecommendationRepository;
import BobBogi.BobBogispring.repository.JpaUserRepository;
import BobBogi.BobBogispring.service.RecommendationService;
import BobBogi.BobBogispring.service.UserService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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

    @Value("${openai.api.key}")
    private String openAiKey;
    @Bean
    public RestTemplate template(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openAiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
