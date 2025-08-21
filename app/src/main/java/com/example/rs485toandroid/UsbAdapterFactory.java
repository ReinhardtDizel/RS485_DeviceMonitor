package com.example.rs485toandroid;

import android.hardware.usb.UsbDevice;
import java.util.ArrayList;
import java.util.List;

/**
 * Фабрика для создания и управления экземплярами адаптеров.
 * Автоматически регистрирует все доступные реализации адаптеров.
 * Предоставляет методы для проверки поддержки устройств и создания соответствующих адаптеров.
 */
public class UsbAdapterFactory {
    private static final List<UsbAdapter> registeredAdapters = new ArrayList<>();

    static {
        // Регистрируем все доступные адаптеры
        registeredAdapters.add(new Ch340Adapter());
        registeredAdapters.add(new Cp2102Adapter());
        registeredAdapters.add(new FtdiAdapter());
        // Добавьте другие адаптеры по мере необходимости
    }

    /**
     * Создает экземпляр адаптера для указанного устройства
     * @return Соответствующий адаптер или null если устройство не поддерживается
     */
    public static UsbAdapter createAdapter(UsbDevice device) {
        for (UsbAdapter adapter : registeredAdapters) {
            if (adapter.supportsDevice(device)) {
                return adapter;
            }
        }
        return null;
    }

    /**
     * Проверяет, поддерживается ли устройство каким-либо адаптером
     */
    public static boolean isDeviceSupported(UsbDevice device) {
        return createAdapter(device) != null;
    }

    /**
     * Возвращает список всех зарегистрированных адаптеров
     */
    public static List<UsbAdapter> getRegisteredAdapters() {
        return new ArrayList<>(registeredAdapters);
    }
}