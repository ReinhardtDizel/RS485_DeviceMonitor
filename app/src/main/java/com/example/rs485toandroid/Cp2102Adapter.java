package com.example.rs485toandroid;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import java.io.IOException;

/**
 * Реализация для адаптеров на чипе CP2102.
 * Требует отправки контрольных запросов для активации RS485 режима.
 * Использует стандартную конфигурацию порта (9600/8-N-1).
 */
public class Cp2102Adapter extends BaseUsbAdapter {
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
     * Отправляет специальные контрольные запросы для настройки RS485 режима
     */
    @Override
    public void performSpecialInit(UsbDeviceConnection connection) throws IOException {
        try {
            connection.controlTransfer(0x40, 0x00, 0x0000, 0, null, 0, 0);
            connection.controlTransfer(0x40, 0x03, 0x4138, 0, null, 0, 0);
            connection.controlTransfer(0x40, 0x01, 0x0101, 0, null, 0, 0);
        } catch (Exception e) {
            throw new IOException("CP2102 initialization failed: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdapterName() {
        return "CP2102 USB-RS485 Adapter";
    }

    /**
     * {@inheritDoc}
     * Проверяет VID=4292 и PID=60000
     */
    @Override
    public boolean supportsDevice(UsbDevice device) {
        return device.getVendorId() == 4292 && device.getProductId() == 60000;
    }
}