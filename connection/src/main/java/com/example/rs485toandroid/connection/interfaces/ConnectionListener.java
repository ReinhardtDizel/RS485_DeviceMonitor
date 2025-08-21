package com.example.rs485toandroid.connection.interfaces;

/**
 * Интерфейс слушателя событий соединения.
 * Обеспечивает callback-методы для обработки различных событий подключения.
 */
public interface ConnectionListener {
    /**
     * Вызывается при успешном установлении соединения с устройством.
     */
    void onConnectionSuccess();

    /**
     * Вызывается при возникновении ошибки во время подключения или работы соединения.
     *
     * @param error описание ошибки
     */
    void onConnectionError(String error);

    /**
     * Вызывается при получении данных от подключенного устройства.
     *
     * @param data массив полученных байтов
     */
    void onDataReceived(byte[] data);

    /**
     * Вызывается при разрыве соединения с устройством.
     */
    void onDisconnected();
}