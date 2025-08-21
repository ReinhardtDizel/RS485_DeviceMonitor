package com.example.rs485toandroid.connection.interfaces;

import android.hardware.usb.UsbDevice;

import com.example.rs485toandroid.connection.adapters.UsbAdapter;

/**
 * Интерфейс менеджера адаптеров.
 * Предоставляет методы для создания и проверки поддержки USB-адаптеров.
 */
public interface AdapterManager {
    /**
     * Создает экземпляр адаптера для указанного USB-устройства.
     *
     * @param device USB-устройство
     * @return экземпляр адаптера или null, если устройство не поддерживается
     */
    UsbAdapter createAdapter(UsbDevice device);

    /**
     * Проверяет, поддерживается ли указанное USB-устройство.
     *
     * @param device USB-устройство для проверки
     * @return true если устройство поддерживается, иначе false
     */
    boolean isDeviceSupported(UsbDevice device);
}