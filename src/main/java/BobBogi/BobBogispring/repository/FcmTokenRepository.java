package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByToken(String token);
    Optional<FcmToken> findByUserId(Long userId);
}
