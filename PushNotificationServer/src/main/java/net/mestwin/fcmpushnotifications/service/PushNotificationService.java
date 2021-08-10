package net.mestwin.fcmpushnotifications.service;

import net.mestwin.fcmpushnotifications.firebase.FCMService;
import net.mestwin.fcmpushnotifications.model.PushNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import net.mestwin.fcmpushnotifications.model.DeviceRequest;
import net.mestwin.fcmpushnotifications.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class PushNotificationService {

    @Value("#{${app.notifications.defaults}}")
    private Map<String, String> defaults;

    @Autowired
    private DeviceRepository deviceRepository;

    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    private FCMService fcmService;
    int count = 0;

    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }

//    @Scheduled(initialDelay = 0, fixedDelay = 1000 * 60 * 60)
//    @Scheduled(initialDelay = 0 , fixedDelay = 5000)
//    @Scheduled(fixedRate=60*60*1000, initialDelay=10*60*1000)
    public void sendSamplePushNotification() {
        //            fcmService.sendMessageWithoutData(getSamplePushNotificationRequest());
        if (deviceRepository.getDevices().size() > 0) {
            count++;
            

            for (Map.Entry<String, DeviceRequest> entry : deviceRepository.getDevices().entrySet()) {
                PushNotificationRequest request = new PushNotificationRequest("Received order " + count, "An orrder arrived for serving.", null);
                request.setToken(entry.getValue().getDeviceToken());
                sendPushNotificationToToken(request);
            }

        }
    }

    public void sendPushNotification(PushNotificationRequest request) {
        try {
            fcmService.sendMessage(getSamplePayloadData(), request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("messageId", defaults.get("payloadMessageId"));
        pushData.put("text", defaults.get("payloadData") + " " + LocalDateTime.now());
        return pushData;
    }

    private PushNotificationRequest getSamplePushNotificationRequest() {
        PushNotificationRequest request = new PushNotificationRequest(defaults.get("title"),
                defaults.get("message"),
                defaults.get("topic"));
        return request;
    }

}
