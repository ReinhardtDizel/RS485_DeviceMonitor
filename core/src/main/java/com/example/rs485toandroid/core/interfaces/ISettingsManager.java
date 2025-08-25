package com.example.rs485toandroid.core.interfaces;

public interface ISettingsManager {
    void setBaudRate(int baudRate);
    int getBaudRate();
    int[] getAvailableBaudRates();
    int getBaudRateIndex(int baudRate);
}