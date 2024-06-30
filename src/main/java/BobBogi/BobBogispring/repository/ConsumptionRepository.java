package BobBogi.BobBogispring.repository;

import BobBogi.BobBogispring.domain.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    // 가장 최근의 소비 항목을 반환
    Optional<Consumption> findTopByUserIdOrderByIdDesc(Long userId);

    // 특정 userId의 모든 소비 항목을 id와 date 순으로 정렬하여 반환 get 일때
    // 변경 전 @Query("SELECT c FROM Consumption c WHERE c.userId = :userId AND c.date = :date ORDER BY c.userId, c.date")
    @Query("SELECT c FROM Consumption c WHERE c.userId = :userId AND DATE(c.date) = :date ORDER BY c.userId, c.date") //변경 후
    List<Consumption> findAllByUserIdOrderByIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("SELECT c FROM Consumption c WHERE c.userId = :userId ORDER BY c.userId, c.date")
    List<Consumption> findAllByUserIdOrderByIdAndDate(@Param("userId") Long userId);


    // 특정 userId의 모든 소비 항목을 반환
    List<Consumption> findAllByUserId(Long userId);
}
