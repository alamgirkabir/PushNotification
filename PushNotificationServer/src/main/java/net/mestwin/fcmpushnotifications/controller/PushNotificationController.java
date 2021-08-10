package net.mestwin.fcmpushnotifications.controller;

import java.util.Map;
import net.mestwin.fcmpushnotifications.model.DeviceRequest;
import net.mestwin.fcmpushnotifications.model.PushNotificationRequest;
import net.mestwin.fcmpushnotifications.repository.DeviceRepository;
import net.mestwin.fcmpushnotifications.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/push")
public class PushNotificationController {

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private DeviceRepository deviceRepository;

//    public PushNotificationController(PushNotificationService pushNotificationService) {
//        this.pushNotificationService = pushNotificationService;
//    }
//    @PostMapping("/notification/topic")
//    public ResponseEntity sendNotification(@RequestBody PushNotificationRequest request) {
//        pushNotificationService.sendPushNotificationWithoutData(request);
//        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
//    }
//
//    @PostMapping("/notification/token")
//    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationRequest request) {
//        pushNotificationService.sendPushNotificationToToken(request);
//        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
//    }
//
//    @PostMapping("/notification/data")
//    public ResponseEntity sendDataNotification(@RequestBody PushNotificationRequest request) {
//        pushNotificationService.sendPushNotification(request);
//        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
//    }
//
//    @GetMapping("/notification")
//    public ResponseEntity sendSampleNotification() {
//        pushNotificationService.sendSamplePushNotification();
//        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
//    }
    @GetMapping("/send_all")
    public void sendPushToAllDevices() {
        for (Map.Entry<String, DeviceRequest> entry : deviceRepository.getDevices().entrySet()) {
            PushNotificationRequest request = new PushNotificationRequest("Received order", "An orrder arrived for serving.", null);
            request.setToken(entry.getValue().getDeviceToken());
            pushNotificationService.sendPushNotificationToToken(request);
        }

    }

    @GetMapping("/send/{deviceToken}")
    public void sendOneDevices(String deviceToken) {

        for (Map.Entry<String, DeviceRequest> entry : deviceRepository.getDevices().entrySet()) {
            if (entry.getValue().getDeviceToken() == null ? deviceToken == null : entry.getValue().getDeviceToken().equals(deviceToken)) {
                PushNotificationRequest request = new PushNotificationRequest("Received order", "An orrder arrived for serving.", null);
                request.setToken(entry.getValue().getDeviceToken());
                pushNotificationService.sendPushNotificationToToken(request);
            }
        }

    }
}
