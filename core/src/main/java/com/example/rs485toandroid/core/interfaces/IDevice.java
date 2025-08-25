package com.example.rs485toandroid.core.interfaces;

public interface IDevice {
    String getDeviceName();
    int getDeviceAddress();
    byte[] createReadRequest(int register, int count);
    byte[] createWriteRequest(int register, int value);
    void processResponse(int startRegister, int count, byte[] data);
    int[] getPollingRegisters();
    int getPollingInterval();
}