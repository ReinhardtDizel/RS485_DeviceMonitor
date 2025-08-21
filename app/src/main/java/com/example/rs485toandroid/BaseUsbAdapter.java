package com.example.rs485toandroid;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import java.io.IOException;

/**
 * Базовая реализация UsbAdapter, содержащая общую логику для всех адаптеров.
 * Предоставляет стандартную конфигурацию порта и пустую реализацию специальной инициализации.
 * Наследуемые классы должны переопределять методы для специфичного поведения.
 */
public abstract class BaseUsbAdapter implements UsbAdapter {
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
        this.connectionConfig = new ConnectionConfig(); // Конфигурация по умолчанию
    }

    /**
     * Устанавливает конфигурацию соединения
     * @param config конфигурация соединения
     */
    public void setConnectionConfig(ConnectionConfig config) {
        this.connectionConfig = config;
    }

    /**
     * Возвращает текущую конфигурацию соединения
     * @return текущая конфигурация соединения
     */
    public ConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configurePort(UsbSerialPort port) throws IOException {
        port.setParameters(connectionConfig.getBaudRate(), connectionConfig.getDataBits(),
                connectionConfig.getStopBits(), connectionConfig.getParity());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performSpecialInit(UsbDeviceConnection connection) throws IOException {
        // Базовая реализация не требует специальной инициализации
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

    @Override
    public void setBaudRate(int baudRate) {
        connectionConfig.setBaudRate(baudRate);
    }
}