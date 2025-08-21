package com.example.rs485toandroid.connection.implementations;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.example.rs485toandroid.connection.adapters.UsbAdapter;
import com.example.rs485toandroid.connection.interfaces.Connection;
import com.example.rs485toandroid.connection.interfaces.ConnectionListener;
import com.example.rs485toandroid.connection.models.ConnectionConfig;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация соединения с USB-устройством.
 * Инкапсулирует логику работы с физическим USB-соединением через библиотеку usb-serial-for-android.
 * Обеспечивает установку соединения, отправку/прием данных и управление событиями.
 */
public class UsbConnectionImpl implements Connection, SerialInputOutputManager.Listener {
    private final UsbDevice device;
    private final UsbManager usbManager;
    private final UsbAdapter adapter;
    private ConnectionConfig config;

    private UsbSerialPort serialPort;
    private SerialInputOutputManager serialIoManager;
    private List<ConnectionListener> listeners = new ArrayList<>();
    private boolean isConnected = false;

    /**
     * Создает соединение с указанными параметрами.
     *
     * @param device USB-устройство
     * @param usbManager системный менеджер USB
     * @param adapter адаптер для работы с устройством
     * @param config конфигурация соединения
     */
    public UsbConnectionImpl(UsbDevice device, UsbManager usbManager, UsbAdapter adapter, ConnectionConfig config) {
        this.device = device;
        this.usbManager = usbManager;
        this.adapter = adapter;
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean connect() {
        try {
            UsbDeviceConnection connection = usbManager.openDevice(device);
            if (connection == null) {
                notifyError("Failed to open device connection");
                return false;
            }

            adapter.initialize(device, connection);
            adapter.setConnectionConfig(config);

            List<UsbSerialPort> ports = UsbSerialProber.getDefaultProber().probeDevice(device).getPorts();
            if (ports.isEmpty()) {
                notifyError("No serial ports found");
                return false;
            }

            serialPort = ports.get(0);
            serialPort.open(connection);

            adapter.performSpecialInit(connection);
            adapter.configurePort(serialPort);

            serialIoManager = new SerialInputOutputManager(serialPort, this);
            serialIoManager.start();

            isConnected = true;
            notifyConnected();
            return true;

        } catch (Exception e) {
            notifyError("Connection error: " + e.getMessage());
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
        try {
            if (serialIoManager != null) {
                serialIoManager.stop();
                serialIoManager = null;
            }
            if (serialPort != null) {
                serialPort.close();
                serialPort = null;
            }
            isConnected = false;
            notifyDisconnected();
        } catch (IOException e) {
            notifyError("Disconnection error: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendData(byte[] data) {
        if (serialPort != null && isConnected) {
            try {
                serialPort.write(data, 100);
            } catch (IOException e) {
                notifyError("Send error: " + e.getMessage());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConnectionListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConnectionListener(ConnectionListener listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateConfig(ConnectionConfig config) {
        this.config = config;
        if (adapter != null) {
            adapter.setConnectionConfig(config);
        }
        // TODO: Применить новую конфигурацию к активному соединению
    }

    /**
     * Вызывается при получении новых данных от устройства.
     *
     * @param data полученные данные
     */
    @Override
    public void onNewData(byte[] data) {
        notifyDataReceived(data);
    }

    /**
     * Вызывается при возникновении ошибки ввода-вывода.
     *
     * @param e исключение, содержащее информацию об ошибке
     */
    @Override
    public void onRunError(Exception e) {
        notifyError("I/O error: " + e.getMessage());
        disconnect();
    }

    /**
     * Уведомляет всех слушателей об успешном подключении.
     */
    private void notifyConnected() {
        for (ConnectionListener listener : listeners) {
            listener.onConnectionSuccess();
        }
    }

    /**
     * Уведомляет всех слушателей об ошибке.
     *
     * @param error описание ошибки
     */
    private void notifyError(String error) {
        for (ConnectionListener listener : listeners) {
            listener.onConnectionError(error);
        }
    }

    /**
     * Уведомляет всех слушателей о получении данных.
     *
     * @param data полученные данные
     */
    private void notifyDataReceived(byte[] data) {
        for (ConnectionListener listener : listeners) {
            listener.onDataReceived(data);
        }
    }

    /**
     * Уведомляет всех слушателей о разрыве соединения.
     */
    private void notifyDisconnected() {
        for (ConnectionListener listener : listeners) {
            listener.onDisconnected();
        }
    }
}