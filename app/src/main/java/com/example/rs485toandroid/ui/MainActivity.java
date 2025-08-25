package com.example.rs485toandroid.ui;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rs485toandroid.R;
import com.example.rs485toandroid.core.interfaces.IConnectionManager;
import com.example.rs485toandroid.core.models.ConnectionConfig;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    IConnectionManager connectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Обработка USB устройств, если активность была запущена через USB intent
        handleUsbIntent(getIntent());

        // Настройка конфигурации соединения
        ConnectionConfig config = new ConnectionConfig();
        config.setBaudRate(9600);
        config.setDataBits(8);
        config.setStopBits(1);
        config.setParity(0);

        connectionManager.updateConnectionConfig(config);
    }

    private void handleUsbIntent(Intent intent) {
        if (intent != null && UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) {
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device != null) {
                // Автоматическое подключение к устройству
                connectionManager.connect(device);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleUsbIntent(intent);
    }

    // Метод для перехода к активности выбора устройства
    private void openDeviceSelection() {
        Intent intent = new Intent(this, DeviceSelectionActivity.class);
        startActivity(intent);
    }
}