package com.example.rs485toandroid;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import java.io.IOException;
import java.util.List;

/**
 * Центральный менеджер для управления USB-соединениями с RS485 адаптерами.
 * TODO: Реализовать автоматическое переподключение при изменении настроек
 * TODO: Добавить поддержку нескольких одновременных подключений
 * TODO: Реализовать очередь команд для избежания коллизий
 */
public class UsbConnectionManager implements SerialInputOutputManager.Listener, SettingsListener {
    public interface ConnectionListener {
        void onConnectionSuccess();
        void onConnectionError(String error);
        void onDataReceived(byte[] data);
        void onDisconnected();
    }

    private final UsbManager usbManager;
    private final ConnectionListener listener;
    private final SettingsManager settingsManager;

    private ModbusDevice currentDevice;
    private UsbSerialPort serialPort;
    private SerialInputOutputManager serialIoManager;
    private UsbAdapter currentAdapter;
    private ConnectionConfig connectionConfig;

    public UsbConnectionManager(UsbManager usbManager, ConnectionListener listener, SettingsManager settingsManager) {
        this.usbManager = usbManager;
        this.listener = listener;
        this.settingsManager = settingsManager;
        this.connectionConfig = new ConnectionConfig(settingsManager);
    }

    /**
     * Обрабатывает изменение скорости соединения
     * @param baudRate новая скорость в бодах
     */
    @Override
    public void onBaudRateChanged(int baudRate) {
        settingsManager.setBaudRate(baudRate);
        connectionConfig.setBaudRate(baudRate);

        if (currentAdapter != null) {
            currentAdapter.setConnectionConfig(connectionConfig);
        }

        // TODO: Реализовать переподключение с новой скоростью без потери состояния
        // TODO: Сохранить текущее устройство и автоматически переподключиться
    }

    public void setCurrentDevice(ModbusDevice device) {
        this.currentDevice = device;
    }

    public ModbusDevice getCurrentDevice() {
        return currentDevice;
    }

    public boolean isDeviceSupported(UsbDevice device) {
        return UsbAdapterFactory.isDeviceSupported(device);
    }

    public boolean isConnected() {
        return serialPort != null;
    }

    public boolean connect(UsbDevice device) {
        try {
            // Находим подходящий адаптер
            currentAdapter = UsbAdapterFactory.createAdapter(device);
            if (currentAdapter == null) {
                listener.onConnectionError("Unsupported device: " + device.getDeviceName());
                return false;
            }

            // Открываем соединение
            UsbDeviceConnection connection = usbManager.openDevice(device);
            if (connection == null) {
                listener.onConnectionError("Failed to open device connection");
                return false;
            }

            // Инициализируем адаптер
            currentAdapter.initialize(device, connection);

            // Устанавливаем конфигурацию соединения
            currentAdapter.setConnectionConfig(connectionConfig);

            // Получаем последовательный порт
            List<UsbSerialPort> ports = UsbSerialProber.getDefaultProber().probeDevice(device).getPorts();
            if (ports.isEmpty()) {
                listener.onConnectionError("No serial ports found");
                return false;
            }

            serialPort = ports.get(0);
            serialPort.open(connection);

            // Выполняем специальную инициализацию, если требуется
            currentAdapter.performSpecialInit(connection);

            // Настраиваем порт через адаптер
            currentAdapter.configurePort(serialPort);

            // Запускаем менеджер ввода-вывода
            serialIoManager = new SerialInputOutputManager(serialPort, this);
            serialIoManager.start();

            listener.onConnectionSuccess();
            return true;

        } catch (Exception e) {
            listener.onConnectionError("Connection error: " + e.getMessage());
            return false;
        }
    }

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
            currentAdapter = null;
            listener.onDisconnected();
        } catch (IOException e) {
            listener.onConnectionError("Disconnection error: " + e.getMessage());
        }
    }

    public void sendData(byte[] data) {
        if (serialPort != null) {
            try {
                serialPort.write(data, 100);
            } catch (IOException e) {
                listener.onConnectionError("Send error: " + e.getMessage());
            }
        }
    }

    @Override
    public void onNewData(byte[] data) {
        listener.onDataReceived(data);
    }

    @Override
    public void onRunError(Exception e) {
        listener.onConnectionError("I/O error: " + e.getMessage());
        disconnect();
    }

    public String getAdapterName() {
        return currentAdapter != null ? currentAdapter.getAdapterName() : "Unknown";
    }
}