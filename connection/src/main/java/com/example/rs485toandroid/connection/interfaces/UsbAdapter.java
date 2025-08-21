package com.example.rs485toandroid.connection.adapters;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.example.rs485toandroid.connection.models.ConnectionConfig;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;

/**
 * Интерфейс адаптера для работы с конкретными типами USB-RS485 преобразователей.
 * Определяет методы для инициализации, конфигурации и управления адаптерами.
 */
public interface UsbAdapter {
    /**
     * Инициализирует адаптер с указанным устройством и соединением.
     *
     * @param device USB-устройство для инициализации
     * @param connection соединение с USB-устройством
     * @throws IOException если произошла ошибка инициализации
     */
    void initialize(UsbDevice device, UsbDeviceConnection connection) throws IOException;

    /**
     * Настраивает параметры последовательного порта.
     *
     * @param port последовательный порт для конфигурации
     * @throws IOException если произошла ошибка конфигурации
     */
    void configurePort(UsbSerialPort port) throws IOException;

    /**
     * Выполняет специальную инициализацию, если требуется для конкретного адаптера.
     *
     * @param connection соединение с USB-устройством
     * @throws IOException если произошла ошибка инициализации
     */
    void performSpecialInit(UsbDeviceConnection connection) throws IOException;

    /**
     * Возвращает имя адаптера.
     *
     * @return строковое представление имени адаптера
     */
    String getAdapterName();

    /**
     * Проверяет, поддерживается ли устройство этим адаптером.
     *
     * @param device USB-устройство для проверки
     * @return true если устройство поддерживается, иначе false
     */
    boolean supportsDevice(UsbDevice device);

    /**
     * Устанавливает конфигурацию соединения.
     *
     * @param config конфигурация соединения
     */
    void setConnectionConfig(ConnectionConfig config);

    /**
     * Возвращает текущую конфигурацию соединения.
     *
     * @return текущая конфигурация соединения
     */
    ConnectionConfig getConnectionConfig();
}