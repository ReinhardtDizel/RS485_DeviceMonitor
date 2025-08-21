package com.example.rs485toandroid.connection.interfaces;

import com.example.rs485toandroid.connection.models.ConnectionConfig;

/**
 * Интерфейс соединения с конкретным устройством.
 * Инкапсулирует логику работы с физическим подключением.
 */
public interface Connection {
    /**
     * Устанавливает соединение с устройством.
     *
     * @return true если подключение успешно установлено, иначе false
     */
    boolean connect();

    /**
     * Разрывает соединение с устройством.
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
     * Обновляет конфигурацию соединения.
     * Применяет новые параметры к активному соединению.
     *
     * @param config новая конфигурация соединения
     */
    void updateConfig(ConnectionConfig config);
}