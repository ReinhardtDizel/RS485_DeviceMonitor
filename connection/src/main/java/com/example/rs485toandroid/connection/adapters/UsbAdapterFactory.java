package com.example.rs485toandroid.connection.adapters;

import android.content.Context;

import com.example.rs485toandroid.core.interfaces.IConnectionAdapter;
import com.example.rs485toandroid.connection.models.AdapterConfig;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UsbAdapterFactory {
    public static IConnectionAdapter createAdapter(Context context, String configPath) {
        try {
            InputStream is = context.getAssets().open(configPath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(jsonString);

            AdapterConfig config = AdapterConfig.fromJson(json);
            return new UniversalConnectionAdapter(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create adapter from config: " + configPath, e);
        }
    }
}