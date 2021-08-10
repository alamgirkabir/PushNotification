/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mestwin.fcmpushnotifications.repository;

import java.util.HashMap;
import net.mestwin.fcmpushnotifications.model.DeviceRequest;
import org.springframework.stereotype.Component;

/**
 *
 * @author alamgir
 */
@Component
public class DeviceRepository {
    private HashMap<String, DeviceRequest> devices = new HashMap<>();
    public void updateDeviceList(DeviceRequest deviceRequest){
        devices.put(deviceRequest.getDeviceId(), deviceRequest);
    }
    public HashMap<String, DeviceRequest> getDevices(){
        return devices;
    }
}
