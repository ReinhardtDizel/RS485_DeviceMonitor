package com.example.rs485toandroid.core.interfaces;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import com.example.rs485toandroid.core.models.ConnectionConfig;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;

public interface IConnectionAdapter {
    void initialize(UsbDevice device, UsbDeviceConnection connection) throws IOException;
    void configurePort(UsbSerialPort port) throws IOException;
    void performSpecialInit(UsbDeviceConnection connection) throws IOException;
    String getAdapterName();
    boolean supportsDevice(UsbDevice device);
    void setConnectionConfig(ConnectionConfig config);
    ConnectionConfig getConnectionConfig();
}