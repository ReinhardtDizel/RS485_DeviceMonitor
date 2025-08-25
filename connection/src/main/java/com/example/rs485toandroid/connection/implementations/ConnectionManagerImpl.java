package com.example.rs485toandroid.connection.implementations;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import com.example.rs485toandroid.core.interfaces.IConnectionListener;
import com.example.rs485toandroid.core.interfaces.IConnectionManager;
import com.example.rs485toandroid.core.interfaces.ISettingsListener;
import com.example.rs485toandroid.core.models.ConnectionConfig;
import com.example.rs485toandroid.core.interfaces.IConnectionAdapter;
import com.example.rs485toandroid.core.interfaces.IAdapterManager;
import com.example.rs485toandroid.connection.interfaces.Connection;
import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
public class ConnectionManagerImpl implements IConnectionManager, ISettingsListener {
    private final UsbManager usbManager;
    private final IAdapterManager adapterManager;
    private final CopyOnWriteArrayList<IConnectionListener> connectionListeners = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<ISettingsListener> settingsListeners = new CopyOnWriteArrayList<>();

    private Connection currentConnection;
    private ConnectionConfig config = new ConnectionConfig();

    @Inject
    public ConnectionManagerImpl(UsbManager usbManager, IAdapterManager adapterManager) {
        this.usbManager = usbManager;
        this.adapterManager = adapterManager;
    }

    @Override
    public boolean connect(UsbDevice device) {
        try {
            IConnectionAdapter adapter = adapterManager.createAdapter(device);
            if (adapter == null) {
                notifyConnectionError("Device not supported");
                return false;
            }

            currentConnection = new UsbConnectionImpl(device, usbManager, adapter, config);
            currentConnection.addConnectionListener(new IConnectionListener() {
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

    @Override
    public void disconnect() {
        if (currentConnection != null) {
            currentConnection.disconnect();
            currentConnection = null;
        }
    }

    @Override
    public void sendData(byte[] data) {
        if (currentConnection != null) {
            currentConnection.sendData(data);
        }
    }

    @Override
    public boolean isConnected() {
        return currentConnection != null && currentConnection.isConnected();
    }

    @Override
    public void addConnectionListener(IConnectionListener listener) {
        connectionListeners.add(listener);
    }

    @Override
    public void removeConnectionListener(IConnectionListener listener) {
        connectionListeners.remove(listener);
    }

    @Override
    public void addSettingsListener(ISettingsListener listener) {
        settingsListeners.add(listener);
    }

    @Override
    public void removeSettingsListener(ISettingsListener listener) {
        settingsListeners.remove(listener);
    }

    @Override
    public void updateConnectionConfig(ConnectionConfig config) {
        this.config = config;
        if (currentConnection != null) {
            currentConnection.updateConfig(config);
        }

        // Уведомляем слушателей настроек
        for (ISettingsListener listener : settingsListeners) {
            listener.onConnectionConfigChanged(config);
        }
    }

    // Эти методы теперь часть ISettingsListener, а не IConnectionManager
    @Override
    public void onBaudRateChanged(int baudRate) {
        config.setBaudRate(baudRate);
        if (currentConnection != null) {
            currentConnection.updateConfig(config);
        }

        for (ISettingsListener listener : settingsListeners) {
            listener.onBaudRateChanged(baudRate);
        }
    }

    @Override
    public void onConnectionConfigChanged(ConnectionConfig config) {
        this.config = config;
        if (currentConnection != null) {
            currentConnection.updateConfig(config);
        }

        for (ISettingsListener listener : settingsListeners) {
            listener.onConnectionConfigChanged(config);
        }
    }

    private void notifyConnectionSuccess() {
        for (IConnectionListener listener : connectionListeners) {
            listener.onConnectionSuccess();
        }
    }

    private void notifyConnectionError(String error) {
        for (IConnectionListener listener : connectionListeners) {
            listener.onConnectionError(error);
        }
    }

    private void notifyDataReceived(byte[] data) {
        for (IConnectionListener listener : connectionListeners) {
            listener.onDataReceived(data);
        }
    }

    private void notifyDisconnected() {
        for (IConnectionListener listener : connectionListeners) {
            listener.onDisconnected();
        }
    }
}