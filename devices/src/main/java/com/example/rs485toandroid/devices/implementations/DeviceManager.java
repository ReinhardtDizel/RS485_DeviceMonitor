package com.example.rs485toandroid.devices.implementations;

import android.content.Context;

import com.example.rs485toandroid.core.interfaces.IDevice;
import com.example.rs485toandroid.core.interfaces.IDeviceManager;
import com.example.rs485toandroid.core.interfaces.IEventPublisher;
import com.example.rs485toandroid.core.models.DeviceInfo;
import com.example.rs485toandroid.devices.config.JsonConfigManager;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceManager implements IDeviceManager {
    private final JsonConfigManager configManager;
    private final IEventPublisher eventPublisher;

    public DeviceManager(Context context, IEventPublisher eventPublisher) {
        this.configManager = new JsonConfigManager(context);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<DeviceInfo> getAvailableDevices() {
        List<JsonConfigManager.DeviceInfo> deviceInfos = configManager.getAvailableDevices();

        return deviceInfos.stream().map(deviceInfo -> {
            DeviceInfo coreDeviceInfo = new DeviceInfo();
            coreDeviceInfo.configName = deviceInfo.configName;
            coreDeviceInfo.deviceModel = deviceInfo.deviceModel;
            coreDeviceInfo.version = deviceInfo.version;
            coreDeviceInfo.description = deviceInfo.description;
            return coreDeviceInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public IDevice createDevice(String configName, int address) {
        if (!configExists(configName)) {
            throw new IllegalArgumentException("Device configuration not found: " + configName);
        }

        try {
            InputStream jsonConfig = configManager.getConfigInputStream(configName);
            return new UniversalModbusDevice(address, jsonConfig, eventPublisher);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create device from config: " + configName, e);
        }
    }

    @Override
    public boolean configExists(String configName) {
        return configManager.configExists(configName);
    }

    @Override
    public DeviceInfo getDeviceInfo(String configName) {
        JsonConfigManager.DeviceInfo deviceInfo = configManager.parseDeviceInfo(configName);

        DeviceInfo coreDeviceInfo = new DeviceInfo();
        coreDeviceInfo.configName = deviceInfo.configName;
        coreDeviceInfo.deviceModel = deviceInfo.deviceModel;
        coreDeviceInfo.version = deviceInfo.version;
        coreDeviceInfo.description = deviceInfo.description;

        return coreDeviceInfo;
    }
}