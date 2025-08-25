package com.example.rs485toandroid.devices.exceptions;

public class DeviceException extends Exception {
    public DeviceException(String message) {
        super(message);
    }

    public DeviceException(String message, Throwable cause) {
        super(message, cause);
    }
}