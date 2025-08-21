package com.example.rs485toandroid.connection;

import android.content.Context;
import android.hardware.usb.UsbManager;

import com.example.rs485toandroid.connection.implementations.AdapterManagerImpl;
import com.example.rs485toandroid.connection.implementations.ConnectionManagerImpl;
import com.example.rs485toandroid.connection.interfaces.AdapterManager;
import com.example.rs485toandroid.connection.interfaces.ConnectionManager;
import com.example.rs485toandroid.connection.models.ConnectionConfig;

/**
 * API для создания и управления соединениями.
 * Предоставляет фабричные методы для создания менеджеров соединений.
 */
public class ConnectionApi {
    /**
     * Создает менеджер соединений с конфигурацией по умолчанию.
     *
     * @param context контекст приложения
     * @return менеджер соединений
     */
    public static ConnectionManager createConnectionManager(Context context) {
        return createConnectionManager(context, new ConnectionConfig());
    }

    /**
     * Создает менеджер соединений с указанной конфигурацией.
     *
     * @param context контекст приложения
     * @param config конфигурация соединения
     * @return менеджер соединений
     */
    public static ConnectionManager createConnectionManager(Context context, ConnectionConfig config) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        AdapterManager adapterManager = new AdapterManagerImpl();
        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl(usbManager, adapterManager);

        // Применяем начальную конфигурацию
        connectionManager.onConnectionConfigChanged(config);

        return connectionManager;
    }
}