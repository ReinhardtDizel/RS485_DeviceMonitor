package com.example.rs485toandroid.connection.implementations;

import android.hardware.usb.UsbDevice;

import com.example.rs485toandroid.connection.adapters.Ch340Adapter;
import com.example.rs485toandroid.connection.adapters.Cp2102Adapter;
import com.example.rs485toandroid.connection.adapters.FtdiAdapter;
import com.example.rs485toandroid.connection.adapters.UsbAdapter;
import com.example.rs485toandroid.connection.interfaces.AdapterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация менеджера адаптеров.
 * Автоматически регистрирует все доступные реализации адаптеров.
 * Предоставляет методы для проверки поддержки устройств и создания соответствующих адаптеров.
 */
public class AdapterManagerImpl implements AdapterManager {
    private final List<UsbAdapter> registeredAdapters = new ArrayList<>();

    /**
     * Создает менеджер адаптеров и регистрирует все доступные реализации.
     */
    public AdapterManagerImpl() {
        // Регистрируем все доступные адаптеры
        registeredAdapters.add(new Ch340Adapter());
        registeredAdapters.add(new Cp2102Adapter());
        registeredAdapters.add(new FtdiAdapter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UsbAdapter createAdapter(UsbDevice device) {
        for (UsbAdapter adapter : registeredAdapters) {
            if (adapter.supportsDevice(device)) {
                return adapter;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDeviceSupported(UsbDevice device) {
        return createAdapter(device) != null;
    }
}