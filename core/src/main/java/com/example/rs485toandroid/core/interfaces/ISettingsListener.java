package com.example.rs485toandroid.core.interfaces;

import com.example.rs485toandroid.core.models.ConnectionConfig;

public interface ISettingsListener {
    void onBaudRateChanged(int baudRate);
    void onConnectionConfigChanged(ConnectionConfig config);
}
