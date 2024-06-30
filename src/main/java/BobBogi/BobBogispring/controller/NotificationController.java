package BobBogi.BobBogispring.controller;

import BobBogi.BobBogispring.domain.FcmToken;
import BobBogi.BobBogispring.repository.FcmTokenRepository;
import BobBogi.BobBogispring.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    
    @Autowired
    private FcmTokenRepository fcmTokenRepository;

    @PostMapping("/register-token")
    public String registerToken(@RequestParam String token, @RequestParam long userId) {
        FcmToken fcmToken = new FcmToken();
        fcmToken.setToken(token);
        fcmToken.setUserId(userId);
        fcmTokenRepository.save(fcmToken);
        return "Token registered!";
    }
}

