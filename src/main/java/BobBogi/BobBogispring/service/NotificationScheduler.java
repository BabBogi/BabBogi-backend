package BobBogi.BobBogispring.service;

import BobBogi.BobBogispring.domain.FcmToken;
import BobBogi.BobBogispring.repository.FcmTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private FcmService fcmService;

    @Autowired
    private FcmTokenRepository fcmTokenRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void sendDailyNotifications() {
        List<FcmToken> tokens = fcmTokenRepository.findAll();
        for (FcmToken token : tokens) {
            fcmService.sendNotification(token.getToken(), "일일 초기화 알림",  "오늘도 건강한 하루 되세요!");
        }
    }
}
