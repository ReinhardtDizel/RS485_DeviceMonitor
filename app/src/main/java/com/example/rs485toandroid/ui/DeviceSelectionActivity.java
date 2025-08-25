package com.example.rs485toandroid.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Активность для выбора типа устройства перед началом работы
 * Предоставляет интерфейс для выбора между различными поддерживаемыми устройствами
 */
@AndroidEntryPoint
public class DeviceSelectionActivity extends AppCompatActivity {

    /**
     * Перечисление поддерживаемых типов устройств
     */
    public enum DeviceType {
        TRM201, VESPER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.BLACK);
        layout.setPadding(32, 32, 32, 32);

        Button btnTRM201 = createDeviceButton("ТРМ201");
        btnTRM201.setOnClickListener(v -> launchMainActivity(DeviceType.TRM201));

        Button btnVesper = createDeviceButton("Веспер ЧП");
        btnVesper.setOnClickListener(v -> launchMainActivity(DeviceType.VESPER));

        layout.addView(btnTRM201);
        layout.addView(btnVesper);

        setContentView(layout);
    }

    /**
     * Создает стилизованную кнопку для выбора устройства
     * @param text Текст для отображения на кнопке
     * @return Настроенный объект Button
     */
    private Button createDeviceButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(24);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(Color.parseColor("#3F51B5"));
        button.setPadding(100, 50, 100, 50);
        return button;
    }

    /**
     * Запускает основную активность с выбранным типом устройства
     * @param deviceType Выбранный тип устройства
     */
    private void launchMainActivity(DeviceType deviceType) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("DEVICE_TYPE", deviceType.name());
        startActivity(intent);
        finish();
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}