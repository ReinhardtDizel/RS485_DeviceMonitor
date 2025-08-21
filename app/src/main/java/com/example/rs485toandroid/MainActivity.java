package com.example.rs485toandroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Основная активность приложения, обеспечивающая управление подключением,
 * отображение данных и взаимодействие с устройством
 * TODO: Реализовать обработку изменений настроек из SettingsActivity
 * TODO: Добавить сохранение состояния при повороте экрана
 * TODO: Реализовать обработку ошибок подключения более детально
 */
public class MainActivity extends AppCompatActivity implements
        UsbConnectionManager.ConnectionListener,
        DevicePoller.PollingListener,
        DataDisplay {

    private static final String ACTION_USB_PERMISSION = "com.example.rs485toandroid.USB_PERMISSION";
    private static final int REQUEST_PERMISSIONS = 1;

    private UsbManager usbManager;
    private UsbConnectionManager usbConnection;
    private DevicePoller devicePoller;
    private ModbusDevice currentDevice;
    private SettingsManager settingsManager;

    private TextView outputTextView;
    private EditText inputEditText;
    private Button connectButton, sendButton, clearButton, refreshButton, applyButton, initButton;
    private ProgressBar progressBar;
    private ScrollView scrollView;

    private boolean isConnected = false;
    private StringBuilder logBuilder = new StringBuilder();
    private DashboardFragment dashboardFragment;

    private final BroadcastReceiver usbPermissionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_USB_PERMISSION.equals(intent.getAction())) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false) && device != null) {
                    connectToDevice(device);
                } else {
                    logMessage("Разрешение на устройство отклонено");
                    updateConnectionState(false);
                }
            }
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем менеджер настроек
        settingsManager = new SettingsManager(this);

        String deviceTypeName = getIntent().getStringExtra("DEVICE_TYPE");
        DeviceSelectionActivity.DeviceType deviceType =
                DeviceSelectionActivity.DeviceType.valueOf(deviceTypeName);

        switch (deviceType) {
            case TRM201:
                currentDevice = new TRM201Device(1);
                break;
            case VESPER:
                currentDevice = new VesperDevice(2);
                break;
            default:
                throw new IllegalStateException("Неизвестный тип устройства");
        }

        usbManager = (UsbManager) getSystemService(USB_SERVICE);
        usbConnection = new UsbConnectionManager(usbManager, this, settingsManager);
        devicePoller = new DevicePoller();
        usbConnection.setCurrentDevice(currentDevice);

        registerReceiver(usbPermissionReceiver, new IntentFilter(ACTION_USB_PERMISSION));
        initializeUI();

        if (dashboardFragment != null) {
            dashboardFragment.setDeviceName(currentDevice.getDeviceName());
        }

        checkPermissions();
        logMessage("Приложение запущено. Готово к подключению.");
    }

    /**
     * Инициализирует пользовательский интерфейс активности
     * TODO: Добавить кнопку для открытия настроек
     * TODO: Реализовать обновление UI при изменении настроек
     */
    private void initializeUI() {
        outputTextView = findViewById(R.id.outputTextView);
        inputEditText = findViewById(R.id.inputEditText);
        connectButton = findViewById(R.id.connectButton);
        sendButton = findViewById(R.id.sendButton);
        clearButton = findViewById(R.id.clearButton);
        refreshButton = findViewById(R.id.refreshButton);
        applyButton = findViewById(R.id.applyButton);
        initButton = findViewById(R.id.initButton);
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollView);

        dashboardFragment = new DashboardFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, dashboardFragment);
        transaction.commit();

        connectButton.setOnClickListener(v -> toggleConnection());
        sendButton.setOnClickListener(v -> sendCustomCommand());
        clearButton.setOnClickListener(v -> clearOutput());

        refreshButton.setOnClickListener(v -> {
            if (isConnected) {
                readDeviceParameters();
            } else {
                logMessage("Устройство не подключено");
            }
        });

        applyButton.setOnClickListener(v -> sendCommand(0x010A, 1, "APLY"));
        initButton.setOnClickListener(v -> {
            sendCommand(0x010B, 1, "INIT");
            disconnectDevice();
        });

        inputEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendCustomCommand();
                return true;
            }
            return false;
        });

        // TODO: Добавить обработчик для кнопки настроек
        // ImageButton settingsButton = findViewById(R.id.settingsButton);
        // settingsButton.setOnClickListener(v -> openSettings());
    }

    /**
     * Открывает экран настроек
     */
    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(usbPermissionReceiver, new IntentFilter(ACTION_USB_PERMISSION));
        // TODO: Проверить, изменились ли настройки и применить их
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(usbPermissionReceiver);
        disconnectDevice();
        if (devicePoller != null) {
            devicePoller.stopPolling();
        }
    }

    /**
     * Проверяет и запрашивает необходимые разрешения
     */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsNeeded = new ArrayList<>();

            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };

            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsNeeded.add(permission);
                }
            }

            if (!permissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        permissionsNeeded.toArray(new String[0]),
                        REQUEST_PERMISSIONS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    logMessage("Разрешение получено: " + permissions[i]);
                } else {
                    logMessage("Разрешение отклонено: " + permissions[i]);
                }
            }
        }
    }

    /**
     * Отправляет команду записи на устройство
     * @param register Адрес регистра для записи
     * @param value Значение для записи
     * @param name Название команды для логирования
     */
    private void sendCommand(int register, int value, String name) {
        if (isConnected && currentDevice != null) {
            byte[] request = currentDevice.createWriteRequest(register, value);
            usbConnection.sendData(request);
            logMessage(">> Отправлена команда " + name);
        } else {
            logMessage("Устройство не подключено");
        }
    }

    /**
     * Перезапускает периодический опрос устройства
     */
    private void restartPolling() {
        devicePoller.stopPolling();
        if (isConnected && currentDevice != null) {
            devicePoller.startPolling(this, currentDevice.getPollingInterval());
        }
    }
    /**
     * Читает параметры устройства в зависимости от его типа
     */
    private void readDeviceParameters() {
        if (currentDevice instanceof TRM201Device) {
            readRegister(0x0400, 13);
            readRegister(0x0100, 12);
        }
    }

    /**
     * Отправляет запрос на чтение регистров
     * @param register Начальный адрес регистра
     * @param count Количество регистров для чтения
     */
    private void readRegister(int register, int count) {
        if (currentDevice != null) {
            byte[] request = currentDevice.createReadRequest(register, count);
            logMessage(">> Запрос: " + ModbusUtils.bytesToHex(request));
            usbConnection.sendData(request);
        }
    }

    @Override
    public void onConnectionSuccess() {
        runOnUiThread(() -> {
            isConnected = true;
            connectButton.setText("Отключиться");
            logMessage(">>> Устройство подключено");
            progressBar.setVisibility(View.GONE);
            connectButton.setEnabled(true);
            restartPolling();
        });
    }

    @Override
    public void onConnectionError(String error) {
        runOnUiThread(() -> {
            logMessage("Ошибка: " + error);
            updateConnectionState(false);
        });
    }

    @Override
    public void onDataReceived(byte[] data) {
        runOnUiThread(() -> {
            String hexResponse = ModbusUtils.bytesToHex(data);
            logMessage("<< Получено: " + hexResponse);

            if (currentDevice != null) {
                currentDevice.processResponse(data, this);
            }
        });
    }

    @Override
    public void onDisconnected() {
        runOnUiThread(() -> {
            logMessage(">>> Устройство отключено");
            updateConnectionState(false);
        });
    }

    @Override
    public void onPollingRequired() {
        if (!isConnected || currentDevice == null) return;

        for (int register : currentDevice.getPollingRegisters()) {
            readRegister(register, 1);
        }
    }

    @Override
    public void updateTemperature(float value) {
        runOnUiThread(() -> {
            if (dashboardFragment != null) {
                dashboardFragment.updateTemperature(value);
            }
        });
    }

    @Override
    public void updateSensorType(int type) {
        runOnUiThread(() -> {
            if (dashboardFragment != null) {
                dashboardFragment.updateSensorType(type);
            }
        });
    }

    @Override
    public void updateStatus(int status) {
        runOnUiThread(() -> {
            if (dashboardFragment != null) {
                dashboardFragment.updateStatus(status);
            }
        });
    }

    @Override
    public void updateHeatingParams(int[] params) {
        runOnUiThread(() -> {
            if (dashboardFragment != null) {
                dashboardFragment.updateHeatingParams(params);
            }
        });
    }

    @Override
    public void updateCommParams(int[] params) {
        runOnUiThread(() -> {
            if (dashboardFragment != null) {
                dashboardFragment.updateCommParams(params);
            }
        });
    }

    @Override
    public void onDeviceChanged(String deviceName) {
        runOnUiThread(() -> {
            if (dashboardFragment != null) {
                dashboardFragment.setDeviceName(deviceName);
            }
        });
    }

    /**
     * Переключает состояние подключения к устройству
     */
    private void toggleConnection() {
        if (isConnected) {
            disconnectDevice();
        } else {
            connectDevice();
        }
    }

    /**
     * Подключается к устройству
     */
    private void connectDevice() {
        progressBar.setVisibility(View.VISIBLE);
        connectButton.setEnabled(false);

        new Thread(() -> {
            Map<String, UsbDevice> devices = usbManager.getDeviceList();
            UsbDevice device = findTargetDevice(devices);

            if (device == null) {
                runOnUiThread(() -> {
                    logMessage("Устройство не найдено");
                    updateConnectionState(false);
                });
                return;
            }

            if (!usbManager.hasPermission(device)) {
                requestUsbPermission(device);
            } else {
                connectToDevice(device);
            }
        }).start();
    }

    /**
     * Подключается к конкретному USB-устройству
     * @param device USB-устройство для подключения
     */
    private void connectToDevice(UsbDevice device) {
        boolean success = usbConnection.connect(device);
        if (success) {
            logMessage("Подключено через: " + usbConnection.getAdapterName());
        } else {
            updateConnectionState(false);
        }
    }

    /**
     * Отключается от устройства
     */
    private void disconnectDevice() {
        devicePoller.stopPolling();
        usbConnection.disconnect();
        isConnected = false;
    }

    /**
     * Обновляет состояние подключения в UI
     * @param connected Флаг подключения
     */
    private void updateConnectionState(boolean connected) {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            connectButton.setText(connected ? "Отключиться" : "Подключиться");
            connectButton.setEnabled(true);
            isConnected = connected;
        });
    }

    /**
     * Находит целевое устройство среди доступных USB-устройств
     * @param devices Карта доступных USB-устройств
     * @return Найденное устройство или null
     */
    private UsbDevice findTargetDevice(Map<String, UsbDevice> devices) {
        for (UsbDevice device : devices.values()) {
            if (usbConnection.isDeviceSupported(device)) {
                return device;
            }
        }
        return null;
    }

    /**
     * Запрашивает разрешение на работу с USB-устройством
     * @param device USB-устройство для запроса разрешения
     */
    private void requestUsbPermission(UsbDevice device) {
        PendingIntent permissionIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(ACTION_USB_PERMISSION),
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ?
                        PendingIntent.FLAG_MUTABLE : PendingIntent.FLAG_IMMUTABLE
        );
        usbManager.requestPermission(device, permissionIntent);
    }

    /**
     * Отправляет пользовательскую команду в hex-формате
     */
    private void sendCustomCommand() {
        if (!isConnected) {
            logMessage("Ошибка: устройство не подключено");
            return;
        }

        String input = inputEditText.getText().toString().trim();
        if (input.isEmpty()) {
            logMessage("Введите команду для отправки");
            return;
        }

        try {
            byte[] data = ModbusUtils.hexStringToByteArray(input);
            usbConnection.sendData(data);
            logMessage(">> Отправлено: " + input);
            inputEditText.setText("");
        } catch (Exception e) {
            logMessage("Ошибка отправки: " + e.getMessage());
        }
    }

    /**
     * Добавляет сообщение в лог и обновляет отображение
     * @param message Сообщение для логирования
     */
    private void logMessage(String message) {
        runOnUiThread(() -> {
            logBuilder.append(message).append("\n");
            outputTextView.setText(logBuilder.toString());

            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        });
    }

    /**
     * Очищает вывод лога
     */
    private void clearOutput() {
        logBuilder.setLength(0);
        outputTextView.setText("");
    }
}