package com.example.rs485toandroid.core.interfaces;

import android.hardware.usb.UsbDevice;

import com.example.rs485toandroid.core.models.ConnectionConfig;

public interface IConnectionManager {
    boolean connect(UsbDevice device);
    void disconnect();
    void sendData(byte[] data);
    boolean isConnected();
    void addConnectionListener(IConnectionListener listener);
    void removeConnectionListener(IConnectionListener listener);
    void addSettingsListener(ISettingsListener listener);
    void removeSettingsListener(ISettingsListener listener);
    void updateConnectionConfig(ConnectionConfig config);
}