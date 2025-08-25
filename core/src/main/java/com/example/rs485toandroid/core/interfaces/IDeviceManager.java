package com.example.rs485toandroid.core.interfaces;

import com.example.rs485toandroid.core.models.DeviceInfo;

import java.util.List;

public interface IDeviceManager {
    List<DeviceInfo> getAvailableDevices();
    IDevice createDevice(String configName, int address);
    boolean configExists(String configName);
    DeviceInfo getDeviceInfo(String configName);
}