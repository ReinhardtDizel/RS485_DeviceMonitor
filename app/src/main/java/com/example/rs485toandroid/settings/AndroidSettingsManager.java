package com.example.rs485toandroid.settings;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.rs485toandroid.core.interfaces.ISettingsManager;
import com.example.rs485toandroid.core.utils.BaudRates;

public class AndroidSettingsManager implements ISettingsManager {
    private static final String PREFS_NAME = "RS485Settings";
    private static final String KEY_BAUD_RATE = "baud_rate";

    private final SharedPreferences prefs;

    public AndroidSettingsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void setBaudRate(int baudRate) {
        prefs.edit().putInt(KEY_BAUD_RATE, baudRate).apply();
    }

    @Override
    public int getBaudRate() {
        return prefs.getInt(KEY_BAUD_RATE, BaudRates.DEFAULT);
    }

    @Override
    public int[] getAvailableBaudRates() {
        return BaudRates.getAll();
    }

    @Override
    public int getBaudRateIndex(int baudRate) {
        int[] rates = getAvailableBaudRates();
        for (int i = 0; i < rates.length; i++) {
            if (rates[i] == baudRate) {
                return i;
            }
        }
        return 0;
    }
}