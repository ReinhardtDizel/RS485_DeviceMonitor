package com.example.rs485toandroid.connection.interfaces;

import com.example.rs485toandroid.connection.models.ConnectionConfig;

/**
 * Интерфейс слушателя событий изменения настроек соединения.
 * Обеспечивает callback-методы для обработки изменений конфигурации подключения.
 */
public interface SettingsListener {
    /**
     * Вызывается при изменении скорости передачи данных (бодрейта).
     *
     * @param baudRate новая скорость передачи данных в бодах
     */
    void onBaudRateChanged(int baudRate);

    /**
     * Вызывается при изменении конфигурации соединения.
     *
     * @param config новая конфигурация соединения
     */
    void onConnectionConfigChanged(ConnectionConfig config);
}