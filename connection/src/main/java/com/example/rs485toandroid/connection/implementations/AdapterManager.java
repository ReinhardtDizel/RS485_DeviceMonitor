package com.example.rs485toandroid.connection.implementations;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import com.example.rs485toandroid.core.interfaces.IConnectionAdapter;
import com.example.rs485toandroid.connection.adapters.UsbAdapterFactory;
import com.example.rs485toandroid.core.interfaces.IAdapterManager;
import java.util.ArrayList;
import java.util.List;

public class AdapterManager implements IAdapterManager {
    private final List<IConnectionAdapter> registeredAdapters = new ArrayList<>();
    private final Context context;

    public AdapterManager(Context context) {
        this.context = context;
        loadAdaptersFromConfigs();
    }

    private void loadAdaptersFromConfigs() {
        try {
            String[] configFiles = context.getAssets().list("adapters");
            if (configFiles != null) {
                for (String configFile : configFiles) {
                    if (configFile.endsWith(".json")) {
                        String configPath = "adapters/" + configFile;
                        IConnectionAdapter adapter = UsbAdapterFactory.createAdapter(context, configPath);
                        registeredAdapters.add(adapter);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load adapter configurations", e);
        }
    }

    @Override
    public IConnectionAdapter createAdapter(UsbDevice device) {
        for (IConnectionAdapter adapter : registeredAdapters) {
            if (adapter.supportsDevice(device)) {
                return adapter;
            }
        }
        return null;
    }

    @Override
    public boolean isDeviceSupported(UsbDevice device) {
        return createAdapter(device) != null;
    }
}