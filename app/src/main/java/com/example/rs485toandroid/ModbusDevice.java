package com.example.rs485toandroid;

/**
 * Абстрактный базовый класс для устройств Modbus
 * Определяет общий интерфейс для работы с различными устройствами
 */
public abstract class ModbusDevice {
    protected int deviceAddress;
    protected String deviceName;

    /**
     * Создает новое устройство Modbus
     * @param address Адрес устройства в сети Modbus
     * @param name Название устройства
     */
    public ModbusDevice(int address, String name) {
        this.deviceAddress = address;
        this.deviceName = name;
    }

    /**
     * Создает запрос на чтение регистров
     * @param register Начальный адрес регистра
     * @param count Количество регистров для чтения
     * @return Массив байтов запроса
     */
    public abstract byte[] createReadRequest(int register, int count);

    /**
     * Создает запрос на запись регистра
     * @param register Адрес регистра для записи
     * @param value Значение для записи
     * @return Массив байтов запроса
     */
    public abstract byte[] createWriteRequest(int register, int value);

    /**
     * Обрабатывает ответ от устройства
     * @param data Полученные данные
     * @param display Интерфейс для обновления UI
     */
    public abstract void processResponse(byte[] data, DataDisplay display);

    /**
     * Возвращает регистры для периодического опроса
     * @return Массив адресов регистров
     */
    public abstract int[] getPollingRegisters();

    /**
     * Возвращает интервал опроса в миллисекундах
     * @return Интервал опроса
     */
    public abstract int getPollingInterval();

    /**
     * Возвращает адрес устройства
     * @return Адрес устройства Modbus
     */
    public int getDeviceAddress() {
        return deviceAddress;
    }

    /**
     * Возвращает название устройства
     * @return Название устройства
     */
    public String getDeviceName() {
        return deviceName;
    }
}