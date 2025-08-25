package com.example.rs485toandroid.connection.adapters;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import com.example.rs485toandroid.core.interfaces.IConnectionAdapter;
import com.example.rs485toandroid.core.models.ConnectionConfig; // Исправлен импорт
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;

/**
 * Базовая реализация UsbAdapter, содержащая общую логику для всех адаптеров.
 * Предоставляет стандартную конфигурацию порта и базовую реализацию методов.
 * Наследуемые классы должны переопределять методы для специфичного поведения.
 */
public abstract class BaseConnectionAdapter implements IConnectionAdapter {
    protected UsbDevice device;
    protected UsbDeviceConnection connection;
    protected ConnectionConfig connectionConfig; // Теперь из core модуля

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
        // Преобразуем значение parity из ConnectionConfig в константы UsbSerialPort
        int parityConstant;
        switch (connectionConfig.getParity()) {
            case 1:
                parityConstant = UsbSerialPort.PARITY_ODD;
                break;
            case 2:
                parityConstant = UsbSerialPort.PARITY_EVEN;
                break;
            case 3:
                parityConstant = UsbSerialPort.PARITY_MARK;
                break;
            case 4:
                parityConstant = UsbSerialPort.PARITY_SPACE;
                break;
            case 0:
            default:
                parityConstant = UsbSerialPort.PARITY_NONE;
                break;
        }

        port.setParameters(
                connectionConfig.getBaudRate(),
                connectionConfig.getDataBits(),
                connectionConfig.getStopBits(),
                parityConstant // Используем преобразованное значение
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