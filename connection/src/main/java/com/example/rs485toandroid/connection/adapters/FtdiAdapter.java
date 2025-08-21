package com.example.rs485toandroid.connection.adapters;

import android.hardware.usb.UsbDevice;

/**
 * Реализация адаптера для чипов FTDI (FT232R/FTX).
 * Использует стандартную конфигурацию порта (9600/8-N-1).
 * Поддерживает основные модели адаптеров FTDI с соответствующими VID/PID.
 */
public class FtdiAdapter extends BaseUsbAdapter {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdapterName() {
        return "FTDI USB-RS485 Adapter";
    }

    /**
     * {@inheritDoc}
     * Проверяет VID=1027 и PID=24577 или 24592.
     */
    @Override
    public boolean supportsDevice(UsbDevice device) {
        return device.getVendorId() == 1027 &&
                (device.getProductId() == 24577 || device.getProductId() == 24592);
    }
}