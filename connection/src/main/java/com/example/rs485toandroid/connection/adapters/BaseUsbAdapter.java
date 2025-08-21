package com.example.rs485toandroid.connection.adapters;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import com.example.rs485toandroid.connection.models.ConnectionConfig;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;

/**
 * Базовая реализация UsbAdapter, содержащая общую логику для всех адаптеров.
 * Предоставляет стандартную конфигурацию порта и базовую реализацию методов.
 * Наследуемые классы должны переопределять методы для специфичного поведения.
 */
public abstract class BaseUsbAdapter implements com.example.rs485toandroid.connection.adapters.UsbAdapter {
    protected UsbDevice device;
    protected UsbDeviceConnection connection;
    protected ConnectionConfig connectionConfig;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(UsbDevice device, UsbDeviceConnection connection) {
        this.device = device;
        this.connection = connection;
        this.connectionConfig = new ConnectionConfig();
    }

    /**
     * {@inheritDoc}
     * Базовая реализация настраивает стандартные параметры порта.
     */
    @Override
    public void configurePort(UsbSerialPort port) throws IOException {
        port.setParameters(
                connectionConfig.getBaudRate(),
                connectionConfig.getDataBits(),
                connectionConfig.getStopBits(),
                UsbSerialPort.PARITY_NONE
        );
    }

    /**
     * {@inheritDoc}
     * Базовая реализация не требует специальной инициализации.
     */
    @Override
    public void performSpecialInit(UsbDeviceConnection connection) throws IOException {
        // Базовая реализация не требует специальной инициализации
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnectionConfig(ConnectionConfig config) {
        this.connectionConfig = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String getAdapterName();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean supportsDevice(UsbDevice device);
}