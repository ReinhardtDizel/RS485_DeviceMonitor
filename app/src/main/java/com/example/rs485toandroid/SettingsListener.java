package com.example.rs485toandroid;

/**
 * Интерфейс для обработки событий изменения настроек
 */
public interface SettingsListener {
    /**
     * Вызывается при изменении скорости соединения
     * @param baudRate новая скорость в бодах
     */
    void onBaudRateChanged(int baudRate);
}