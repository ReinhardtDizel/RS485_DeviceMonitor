package com.example.rs485toandroid.connection.implementations;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.example.rs485toandroid.connection.adapters.UsbAdapter;
import com.example.rs485toandroid.connection.interfaces.AdapterManager;
import com.example.rs485toandroid.connection.interfaces.Connection;
import com.example.rs485toandroid.connection.interfaces.ConnectionListener;
import com.example.rs485toandroid.connection.interfaces.ConnectionManager;
import com.example.rs485toandroid.connection.interfaces.SettingsListener;
import com.example.rs485toandroid.connection.models.ConnectionConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация менеджера соединений.
 * Центральный компонент для управления USB-соединениями с RS485 адаптерами.
 * Обеспечивает установку, разрыв соединений, отправку данных и управление событиями.
 */
public class ConnectionManagerImpl implements ConnectionManager, SettingsListener {
    private final UsbManager usbManager;
    private final AdapterManager adapterManager;
    private final List<ConnectionListener> connectionListeners = new ArrayList<>();
    private final List<SettingsListener> settingsListeners = new ArrayList<>();

    private Connection currentConnection;
    private ConnectionConfig config = new ConnectionConfig();

    /**
     * Создает менеджер соединений с указанными зависимостями.
     *
     * @param usbManager системный менеджер USB
     * @param adapterManager менеджер адаптеров
     */
    public ConnectionManagerImpl(UsbManager usbManager, AdapterManager adapterManager) {
        this.usbManager = usbManager;
        this.adapterManager = adapterManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean connect(UsbDevice device) {
        try {
            UsbAdapter adapter = adapterManager.createAdapter(device);
            if (adapter == null) {
                notifyConnectionError("Device not supported");
                return false;
            }

            currentConnection = new UsbConnectionImpl(device, usbManager, adapter, config);
            currentConnection.addConnectionListener(new ConnectionListener() {
                @Override
                public void onConnectionSuccess() {
                    notifyConnectionSuccess();
                }

                @Override
                public void onConnectionError(String error) {
                    notifyConnectionError(error);
                }

                @Override
                public void onDataReceived(byte[] data) {
                    notifyDataReceived(data);
                }

                @Override
                public void onDisconnected() {
                    notifyDisconnected();
                }
            });

            return currentConnection.connect();
        } catch (Exception e) {
            notifyConnectionError(e.getMessage());
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        if (currentConnection != null) {
            currentConnection.disconnect();
            currentConnection = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendData(byte[] data) {
        if (currentConnection != null) {
            currentConnection.sendData(data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return currentConnection != null && currentConnection.isConnected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConnectionListener(ConnectionListener listener) {
        connectionListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConnectionListener(ConnectionListener listener) {
        connectionListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSettingsListener(SettingsListener listener) {
        settingsListeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSettingsListener(SettingsListener listener) {
        settingsListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBaudRateChanged(int baudRate) {
        config.setBaudRate(baudRate);
        if (currentConnection != null) {
            currentConnection.updateConfig(config);
        }

        for (SettingsListener listener : settingsListeners) {
            listener.onBaudRateChanged(baudRate);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConnectionConfigChanged(ConnectionConfig config) {
        this.config = config;
        if (currentConnection != null) {
            currentConnection.updateConfig(config);
        }

        for (SettingsListener listener : settingsListeners) {
            listener.onConnectionConfigChanged(config);
        }
    }

    /**
     * Уведомляет всех слушателей об успешном подключении.
     */
    private void notifyConnectionSuccess() {
        for (ConnectionListener listener : connectionListeners) {
            listener.onConnectionSuccess();
        }
    }

    /**
     * Уведомляет всех слушателей об ошибке подключения.
     *
     * @param error описание ошибки
     */
    private void notifyConnectionError(String error) {
        for (ConnectionListener listener : connectionListeners) {
            listener.onConnectionError(error);
        }
    }

    /**
     * Уведомляет всех слушателей о получении данных.
     *
     * @param data полученные данные
     */
    private void notifyDataReceived(byte[] data) {
        for (ConnectionListener listener : connectionListeners) {
            listener.onDataReceived(data);
        }
    }

    /**
     * Уведомляет всех слушателей о разрыве соединения.
     */
    private void notifyDisconnected() {
        for (ConnectionListener listener : connectionListeners) {
            listener.onDisconnected();
        }
    }
}