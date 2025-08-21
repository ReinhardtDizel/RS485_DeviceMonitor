package com.example.rs485toandroid;

import android.hardware.usb.UsbDevice;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import java.io.IOException;

/**
 * Реализация для адаптеров на чипе CH340.
 * Использует конфигурацию 9600/8-N-2 (2 стоп-бита) вместо стандартной.
 * VID/PID проверяются по официальным идентификаторам производителя.
 */
public class Ch340Adapter extends BaseUsbAdapter {
    /**
     * {@inheritDoc}
     * Для CH340 устанавливает 2 стоп-бита вместо стандартного 1
     */
    @Override
    public void configurePort(UsbSerialPort port) throws IOException {
        port.setParameters(getConnectionConfig().getBaudRate(), 8,
                UsbSerialPort.STOPBITS_2, UsbSerialPort.PARITY_NONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdapterName() {
        return "CH340 USB-RS485 Adapter";
    }

    /**
     * {@inheritDoc}
     * Проверяет VID=6790 и PID=29987 или 21795
     */
    @Override
    public boolean supportsDevice(UsbDevice device) {
        return device.getVendorId() == 6790 &&
                (device.getProductId() == 29987 || device.getProductId() == 21795);
    }

    @Override
    public void setBaudRate(int baudRate) {

    }
}