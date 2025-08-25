package com.example.rs485toandroid.core.interfaces;

import android.hardware.usb.UsbDevice;

public interface IAdapterManager {
    IConnectionAdapter createAdapter(UsbDevice device);
    boolean isDeviceSupported(UsbDevice device);
}