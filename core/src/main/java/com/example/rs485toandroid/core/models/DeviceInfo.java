package com.example.rs485toandroid.core.models;

public class DeviceInfo {
    public String configName;
    public String deviceModel;
    public String version;
    public String description;

    @Override
    public String toString() {
        return deviceModel + (version.isEmpty() ? "" : " (" + version + ")");
    }
}