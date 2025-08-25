package com.example.rs485toandroid.devices.config;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonConfigManager {
    private static final String TAG = "JsonDeviceConfigManager";
    private static final String CONFIG_PATH = "devices_json/";

    private final Context context;
    private final Map<String, DeviceInfo> deviceInfoMap = new HashMap<>();

    public JsonConfigManager(Context context) {
        this.context = context;
        loadAvailableConfigs();
    }

    private void loadAvailableConfigs() {
        try {
            AssetManager assetManager = context.getAssets();
            String[] files = assetManager.list(CONFIG_PATH);
            if (files != null) {
                for (String file : files) {
                    if (file.endsWith(".json")) {
                        String configName = file.replace(".json", "");
                        DeviceInfo info = parseDeviceInfo(configName);
                        deviceInfoMap.put(configName, info);
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading device configurations", e);
        }
    }

    public DeviceInfo parseDeviceInfo(String configName) {
        try {
            InputStream configStream = getConfigInputStream(configName);
            String jsonString = readStream(configStream);
            JSONObject json = new JSONObject(jsonString);

            DeviceInfo info = new DeviceInfo();
            info.configName = configName;
            info.deviceModel = json.getString("deviceModel");
            info.version = json.optString("version", "");
            info.description = json.optString("description", info.deviceModel);

            return info;
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse device info for: " + configName, e);
            // Возвращаем базовую информацию даже при ошибке
            DeviceInfo info = new DeviceInfo();
            info.configName = configName;
            info.deviceModel = configName;
            info.description = configName;
            return info;
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    public List<DeviceInfo> getAvailableDevices() {
        return new ArrayList<>(deviceInfoMap.values());
    }

    public InputStream getConfigInputStream(String configName) throws IOException {
        String configPath = CONFIG_PATH + configName + ".json";
        return context.getAssets().open(configPath);
    }

    public boolean configExists(String configName) {
        return deviceInfoMap.containsKey(configName);
    }

    public static class DeviceInfo {
        public String configName;
        public String deviceModel;
        public String version;
        public String description;

        @Override
        public String toString() {
            return deviceModel + (version.isEmpty() ? "" : " (" + version + ")");
        }
    }
}