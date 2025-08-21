package com.example.rs485toandroid;

import android.hardware.usb.UsbDevice;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import java.io.IOException;

/**
 * Реализация для адаптеров на чипах FTDI (FT232R/FTX).
 * Использует стандартную конфигурацию порта (9600/8-N-1).
 * Поддерживает основные модели адаптеров FTDI с соответствующими VID/PID.
 */
public class FtdiAdapter extends BaseUsbAdapter {
    /**
     * {@inheritDoc}
     */
    @Override
    public void configurePort(UsbSerialPort port) throws IOException {
        port.setParameters(getConnectionConfig().getBaudRate(), 8,
                UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdapterName() {
        return "FTDI USB-RS485 Adapter";
    }

    /**
     * {@inheritDoc}
     * Проверяет VID=1027 и PID=24577 или 24592
     */
    @Override
    public boolean supportsDevice(UsbDevice device) {
        return device.getVendorId() == 1027 &&
                (device.getProductId() == 24577 || device.getProductId() == 24592);
    }
}