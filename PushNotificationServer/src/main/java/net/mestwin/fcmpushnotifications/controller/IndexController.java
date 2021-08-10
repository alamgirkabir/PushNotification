/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mestwin.fcmpushnotifications.controller;

import java.util.HashMap;
import net.mestwin.fcmpushnotifications.model.DeviceRequest;
import net.mestwin.fcmpushnotifications.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alamgir
 */
@RestController
public class IndexController {
    @Autowired
    private DeviceRepository deviceRepository;
    
    @GetMapping
    public HashMap<String, DeviceRequest> index(){
        return deviceRepository.getDevices();
    }
}
