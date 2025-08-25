package com.example.rs485toandroid.connection.interfaces;

import com.example.rs485toandroid.core.interfaces.IConnectionListener;
import com.example.rs485toandroid.core.models.ConnectionConfig;

public interface Connection {
    boolean connect();
    void disconnect();
    void sendData(byte[] data);
    boolean isConnected();
    void addConnectionListener(IConnectionListener listener);
    void removeConnectionListener(IConnectionListener listener);
    void updateConfig(ConnectionConfig config);
}