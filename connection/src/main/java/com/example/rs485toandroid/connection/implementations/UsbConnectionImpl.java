package com.example.rs485toandroid.connection.implementations;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.example.rs485toandroid.connection.interfaces.Connection;
import com.example.rs485toandroid.core.interfaces.IConnectionAdapter;
import com.example.rs485toandroid.core.interfaces.IConnectionListener;
import com.example.rs485toandroid.core.models.ConnectionConfig;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UsbConnectionImpl implements Connection, SerialInputOutputManager.Listener {
    private final UsbDevice device;
    private final UsbManager usbManager;
    private final IConnectionAdapter adapter;
    private ConnectionConfig config;

    private UsbSerialPort serialPort;
    private SerialInputOutputManager serialIoManager;
    private CopyOnWriteArrayList<IConnectionListener> listeners = new CopyOnWriteArrayList<>();
    private boolean isConnected = false;

    public UsbConnectionImpl(UsbDevice device, UsbManager usbManager, IConnectionAdapter adapter, ConnectionConfig config) {
        this.device = device;
        this.usbManager = usbManager;
        this.adapter = adapter;
        this.config = config;
    }

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

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void addConnectionListener(IConnectionListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeConnectionListener(IConnectionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void updateConfig(ConnectionConfig config) {
        this.config = config;
        if (adapter != null) {
            adapter.setConnectionConfig(config);
            if (isConnected && serialPort != null) {
                try {
                    adapter.configurePort(serialPort);
                } catch (IOException e) {
                    notifyError("Failed to apply new configuration: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onNewData(byte[] data) {
        notifyDataReceived(data);
    }

    @Override
    public void onRunError(Exception e) {
        notifyError("I/O error: " + e.getMessage());
        disconnect();
    }

    private void notifyConnected() {
        for (IConnectionListener listener : listeners) {
            listener.onConnectionSuccess();
        }
    }

    private void notifyError(String error) {
        for (IConnectionListener listener : listeners) {
            listener.onConnectionError(error);
        }
    }

    private void notifyDataReceived(byte[] data) {
        for (IConnectionListener listener : listeners) {
            listener.onDataReceived(data);
        }
    }

    private void notifyDisconnected() {
        for (IConnectionListener listener : listeners) {
            listener.onDisconnected();
        }
    }
}