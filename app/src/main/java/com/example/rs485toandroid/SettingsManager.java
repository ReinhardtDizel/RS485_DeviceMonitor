package com.example.rs485toandroid;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Менеджер настроек приложения
 * Инкапсулирует всю логику работы с настройками
 */
public class SettingsManager {
    private static final String PREFS_NAME = "RS485Settings";
    private static final String KEY_BAUD_RATE = "baud_rate";

    private final SharedPreferences prefs;

    public SettingsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Сохраняет выбранную скорость соединения
     * @param baudRate скорость в бодах
     */
    public void setBaudRate(int baudRate) {
        prefs.edit().putInt(KEY_BAUD_RATE, baudRate).apply();
    }

    /**
     * Возвращает сохраненную скорость соединения
     * @return скорость в бодах
     */
    public int getBaudRate() {
        return prefs.getInt(KEY_BAUD_RATE, BaudRates.DEFAULT);
    }

    /**
     * Возвращает массив доступных скоростей соединения
     * @return массив скоростей
     */
    public int[] getAvailableBaudRates() {
        return BaudRates.getAll();
    }

    /**
     * Возвращает индекс скорости в массиве доступных скоростей
     * @param baudRate скорость для поиска
     * @return индекс или 0 (по умолчанию) если не найдено
     */
    public int getBaudRateIndex(int baudRate) {
        int[] rates = getAvailableBaudRates();
        for (int i = 0; i < rates.length; i++) {
            if (rates[i] == baudRate) {
                return i;
            }
        }
        return 0; // Возвращаем индекс по умолчанию
    }
}