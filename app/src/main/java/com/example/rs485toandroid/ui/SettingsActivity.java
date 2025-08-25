package com.example.rs485toandroid.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rs485toandroid.R;
import com.example.rs485toandroid.core.interfaces.ISettingsListener;
import com.example.rs485toandroid.core.interfaces.ISettingsManager;
import com.example.rs485toandroid.core.models.ConnectionConfig;

import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity implements ISettingsListener {

    @Inject
    ISettingsManager settingsManager;

    private Spinner baudRateSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        baudRateSpinner = findViewById(R.id.baudRateSpinner);
        setupBaudRateSpinner();
    }

    private void setupBaudRateSpinner() {
        int[] baudRates = settingsManager.getAvailableBaudRates();
        List<String> baudRateStrings = new ArrayList<>();

        for (int rate : baudRates) {
            baudRateStrings.add(String.valueOf(rate));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, baudRateStrings
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baudRateSpinner.setAdapter(adapter);

        baudRateSpinner.setSelection(settingsManager.getBaudRateIndex(settingsManager.getBaudRate()));

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
    }

    @Override
    public void onConnectionConfigChanged(ConnectionConfig config) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}