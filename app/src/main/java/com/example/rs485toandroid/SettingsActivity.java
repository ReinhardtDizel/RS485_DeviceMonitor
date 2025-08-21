package com.example.rs485toandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Активность для настройки параметров приложения
 */
public class SettingsActivity extends AppCompatActivity implements SettingsListener {
    private SettingsManager settingsManager;
    private Spinner baudRateSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsManager = new SettingsManager(this);

        baudRateSpinner = findViewById(R.id.baudRateSpinner);
        setupBaudRateSpinner();
    }

    private void setupBaudRateSpinner() {
        // Получаем доступные скорости
        int[] baudRates = settingsManager.getAvailableBaudRates();
        List<String> baudRateStrings = new ArrayList<>();

        for (int rate : baudRates) {
            baudRateStrings.add(String.valueOf(rate));
        }

        // Настраиваем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, baudRateStrings
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baudRateSpinner.setAdapter(adapter);

        // Устанавливаем текущее значение
        baudRateSpinner.setSelection(settingsManager.getBaudRateIndex(settingsManager.getBaudRate()));

        // Обработчик изменения скорости
        baudRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedBaudRate = Integer.parseInt(parent.getItemAtPosition(position).toString());
                onBaudRateChanged(selectedBaudRate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onBaudRateChanged(int baudRate) {
        settingsManager.setBaudRate(baudRate);
        // Здесь можно уведомить другие компоненты о изменении настроек
    }
}