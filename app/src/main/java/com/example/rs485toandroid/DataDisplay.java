package com.example.rs485toandroid;

/**
 * Интерфейс для обновления UI данными с устройства
 */
public interface DataDisplay {
    /**
     * Обновляет значение температуры
     * @param value Значение температуры
     */
    void updateTemperature(float value);

    /**
     * Обновляет тип датчика
     * @param type Тип датчика
     */
    void updateSensorType(int type);

    /**
     * Обновляет статус устройства
     * @param status Значение статуса
     */
    void updateStatus(int status);

    /**
     * Обновляет параметры нагрева
     * @param params Массив параметров нагрева
     */
    void updateHeatingParams(int[] params);

    /**
     * Обновляет параметры связи
     * @param params Массив параметров связи
     */
    void updateCommParams(int[] params);

    /**
     * Уведомляет об изменении устройства
     * @param deviceName Название устройства
     */
    void onDeviceChanged(String deviceName);
}