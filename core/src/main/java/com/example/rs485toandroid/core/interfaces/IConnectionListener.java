package com.example.rs485toandroid.core.interfaces;

public interface IConnectionListener {
    void onConnectionSuccess();
    void onConnectionError(String error);
    void onDataReceived(byte[] data);
    void onDisconnected();
}
