package com.example.rs485toandroid.core.events;

public class DeviceDataEvent {
    private final String parameterName;
    private final float value;
    private final String unit;

    public DeviceDataEvent(String parameterName, float value, String unit) {
        this.parameterName = parameterName;
        this.value = value;
        this.unit = unit;
    }

    // Геттеры
    public String getParameterName() { return parameterName; }
    public float getValue() { return value; }
    public String getUnit() { return unit; }
}