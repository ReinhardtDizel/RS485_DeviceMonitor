package com.example.rs485toandroid.core.utils;

/**
 * Класс для хранения констант скоростей соединения
 * Избавляет от магических чисел в коде
 */
public final class BaudRates {
    private BaudRates() {
        // Запрещаем создание экземпляров
    }

    public static final int BAUD_9600 = 9600;
    public static final int BAUD_19200 = 19200;
    public static final int BAUD_38400 = 38400;
    public static final int BAUD_57600 = 57600;
    public static final int BAUD_115200 = 115200;

    public static final int DEFAULT = BAUD_9600;

    public static int[] getAll() {
        return new int[]{BAUD_9600, BAUD_19200, BAUD_38400, BAUD_57600, BAUD_115200};
    }
}