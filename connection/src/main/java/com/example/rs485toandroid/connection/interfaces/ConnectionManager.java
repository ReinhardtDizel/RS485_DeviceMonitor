package com.example.rs485toandroid.connection.interfaces;

import android.hardware.usb.UsbDevice;

/**
 * Основной интерфейс для управления подключениями к USB-RS485 адаптерам.
 * Предоставляет методы для установки, разрыва соединения, отправки данных
 * и управления слушателями событий подключения.
 */
public interface ConnectionManager {
    /**
     * Устанавливает соединение с указанным USB-устройством.
     *
     * @param device USB-устройство для подключения
     * @return true если подключение успешно установлено, иначе false
     */
    boolean connect(UsbDevice device);

    /**
     * Разрывает текущее соединение с устройством.
     * Освобождает все связанные ресурсы.
     */
    void disconnect();

    /**
     * Отправляет данные через установленное соединение.
     *
     * @param data массив байтов для отправки
     */
    void sendData(byte[] data);

    /**
     * Проверяет, установлено ли соединение с устройством.
     *
     * @return true если соединение активно, иначе false
     */
    boolean isConnected();

    /**
     * Добавляет слушателя событий подключения.
     *
     * @param listener слушатель для добавления
     */
    void addConnectionListener(ConnectionListener listener);

    /**
     * Удаляет слушателя событий подключения.
     *
     * @param listener слушатель для удаления
     */
    void removeConnectionListener(ConnectionListener listener);

    /**
     * Добавляет слушателя событий изменения настроек.
     *
     * @param listener слушатель для добавления
     */
    void addSettingsListener(SettingsListener listener);

    /**
     * Удаляет слушателя событий изменения настроек.
     *
     * @param listener слушатель для удаления
     */
    void removeSettingsListener(SettingsListener listener);
}