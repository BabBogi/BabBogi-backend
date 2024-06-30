package BobBogi.BobBogispring.controller;

import BobBogi.BobBogispring.domain.Consumption;
import BobBogi.BobBogispring.domain.FcmToken;
import BobBogi.BobBogispring.service.ConsumptionService;
import BobBogi.BobBogispring.service.NotificationService;
import BobBogi.BobBogispring.repository.FcmTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consumptions")
public class ConsumptionController {

    private final ConsumptionService consumptionService;
    private final NotificationService notificationService;
    private final FcmTokenRepository fcmTokenRepository;

    // 생성자 주입을 사용하여 ConsumptionService 의존성 주입
    @Autowired
    public ConsumptionController(ConsumptionService consumptionService, NotificationService notificationService, FcmTokenRepository fcmTokenRepository) {
        this.consumptionService = consumptionService;
        this.notificationService = notificationService;
        this.fcmTokenRepository = fcmTokenRepository;
    }

    // 소비 항목 리스트를 저장하는 POST 요청 처리
    @PostMapping
    @ResponseBody
    public ResponseEntity<List<Consumption>> saveConsumptions(@RequestParam("userId") Long userId, @RequestBody List<Consumption> consumptions) {
        // 각 소비 항목에 사용자 ID를 설정
        for (Consumption consumption : consumptions) {
            consumption.setUserId(userId);
        }
        // 소비 항목을 저장하고 저장된 항목 리스트를 반환
        List<Consumption> savedConsumptions = consumptionService.saveAllConsumptions(consumptions);
        
        //userId로 token 값 string으로 가져오기

        Optional<FcmToken> fcmTokenOptional = fcmTokenRepository.findByUserId(userId);
        if (fcmTokenOptional.isPresent()) {
            String token = fcmTokenOptional.get().getToken();
            notificationService.sendNotification(token);
        }
        
        return ResponseEntity.ok(savedConsumptions);
    }

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<List<Consumption>> getAllConsumptionsByUserIdOrdered(
            @RequestParam("userId") Long userId,
            @RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        List<Consumption> consumptions = consumptionService.getAllConsumptionsByUserIdOrdered(userId, date);
        return ResponseEntity.ok(consumptions);
    }

}
