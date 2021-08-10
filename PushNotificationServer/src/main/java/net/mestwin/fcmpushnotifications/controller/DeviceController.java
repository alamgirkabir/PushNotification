/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mestwin.fcmpushnotifications.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.mestwin.fcmpushnotifications.model.DeviceRequest;
import net.mestwin.fcmpushnotifications.model.PushNotificationRequest;
import net.mestwin.fcmpushnotifications.repository.DeviceRepository;
import net.mestwin.fcmpushnotifications.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alamgir
 */
@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private PushNotificationService pushNotificationService;

    @PostMapping("/add")
    public boolean addDeviceId(@RequestBody String json) throws IOException {
        ObjectMapper om = new ObjectMapper();
        DeviceRequest deviceRequest = om.readValue(json, DeviceRequest.class);
        if (!deviceRepository.getDevices().containsKey(deviceRequest.getDeviceId())) {
            PushNotificationRequest request = new PushNotificationRequest("Received order", "An orrder arrived for serving.", null);
            request.setToken(deviceRequest.getDeviceToken());
            pushNotificationService.sendPushNotificationToToken(request);
        }
        deviceRepository.updateDeviceList(deviceRequest);

        return true;
    }
}
