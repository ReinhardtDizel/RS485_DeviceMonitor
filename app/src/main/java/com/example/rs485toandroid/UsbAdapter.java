package com.example.rs485toandroid;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import java.io.IOException;

/**
 * Интерфейс для работы с USB-RS485 адаптерами.
 * Определяет базовые методы инициализации, конфигурации порта и проверки поддержки устройств.
 * Реализации интерфейса обеспечивают совместимость с конкретными моделями адаптеров (CH340, CP2102, FTDI).
 */
public interface UsbAdapter {
    /**
     * Инициализирует адаптер с указанным устройством
     * @param device USB устройство для инициализации
     * @param connection соединение с USB устройством
     * @throws IOException если произошла ошибка инициализации
     */
    void initialize(UsbDevice device, UsbDeviceConnection connection) throws IOException;

    /**
     * Настраивает параметры последовательного порта
     * @param port последовательный порт для конфигурации
     * @throws IOException если произошла ошибка конфигурации
     */
    void configurePort(UsbSerialPort port) throws IOException;

    /**
     * Выполняет специальную инициализацию, если требуется
     * @param connection соединение с USB устройством
     * @throws IOException если произошла ошибка инициализации
     */
    void performSpecialInit(UsbDeviceConnection connection) throws IOException;

    /**
     * Возвращает имя адаптера
     * @return строковое представление имени адаптера
     */
    String getAdapterName();

    /**
     * Проверяет, поддерживается ли устройство этим адаптером
     * @param device USB устройство для проверки
     * @return true если устройство поддерживается, иначе false
     */
    boolean supportsDevice(UsbDevice device);

    /**
     * Устанавливает скорость соединения
     * @param baudRate скорость в бодах
     */
    public void setBaudRate(int baudRate);

    /**
     * Устанавливает конфигурацию соединения
     * @param config конфигурация соединения
     */
    public void setConnectionConfig(ConnectionConfig config);

    /**
     * Возвращает текущую конфигурацию соединения
     * @return текущая конфигурация соединения
     */
    public ConnectionConfig getConnectionConfig();
}