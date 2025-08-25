package com.example.rs485toandroid.devices.implementations;

import android.util.Log;

import com.example.rs485toandroid.core.events.DeviceDataEvent;
import com.example.rs485toandroid.core.events.EventBus;
import com.example.rs485toandroid.core.interfaces.IDevice;
import com.example.rs485toandroid.core.interfaces.IEventPublisher;
import com.example.rs485toandroid.devices.config.DeviceConfig;
import com.example.rs485toandroid.devices.utils.ModbusUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UniversalModbusDevice implements IDevice {
    private static final String TAG = "UniversalModbusDevice";
    private final IEventPublisher eventPublisher;
    private final int deviceAddress;
    private String deviceName;
    private DeviceConfig deviceConfig;
    private Map<Integer, DeviceConfig.DeviceParameter> parameterMap = new HashMap<>();
    private Map<String, DeviceConfig.DeviceParameter> parameterNameMap = new HashMap<>();
    public UniversalModbusDevice(int address, InputStream jsonConfig, IEventPublisher eventPublisher) {
        this.deviceAddress = address;
        this.deviceName = "Universal Device";
        this.eventPublisher = eventPublisher;

        try {
            loadJsonConfiguration(jsonConfig);
            this.deviceName = deviceConfig.deviceModel;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load device configuration", e);
        }
    }


    private void loadJsonConfiguration(InputStream jsonConfig) throws IOException, JSONException {
        // Чтение JSON из потока
        int size = jsonConfig.available();
        byte[] buffer = new byte[size];
        jsonConfig.read(buffer);
        jsonConfig.close();
        String jsonString = new String(buffer, StandardCharsets.UTF_8);

        // Парсинг JSON
        JSONObject json = new JSONObject(jsonString);
        deviceConfig = new DeviceConfig();
        deviceConfig.deviceModel = json.getString("deviceModel");
        deviceConfig.version = json.optString("version", "");

        if (json.has("pollingInterval")) {
            deviceConfig.pollingInterval = json.getInt("pollingInterval");
        }

        // Парсинг групп параметров
        JSONArray groupsArray = json.getJSONArray("parameterGroups");
        deviceConfig.parameterGroups = new ArrayList<>();

        for (int i = 0; i < groupsArray.length(); i++) {
            JSONObject groupJson = groupsArray.getJSONObject(i);
            DeviceConfig.ParameterGroup group = new DeviceConfig.ParameterGroup();
            group.groupCode = groupJson.getString("groupCode");
            group.groupName = groupJson.getString("groupName");

            // Парсинг параметров
            JSONArray paramsArray = groupJson.getJSONArray("parameters");
            group.parameters = new ArrayList<>();

            for (int j = 0; j < paramsArray.length(); j++) {
                JSONObject paramJson = paramsArray.getJSONObject(j);
                DeviceConfig.DeviceParameter param = parseParameter(paramJson);
                group.parameters.add(param);

                // Добавляем в карты для быстрого доступа
                parameterMap.put(param.modbusAddress, param);
                parameterNameMap.put(param.name, param);
            }

            deviceConfig.parameterGroups.add(group);
        }
    }

    private DeviceConfig.DeviceParameter parseParameter(JSONObject paramJson) throws JSONException {
        DeviceConfig.DeviceParameter param = new DeviceConfig.DeviceParameter();

        param.code = paramJson.getString("code");
        param.modbusAddress = Integer.decode(paramJson.getString("modbusAddress"));
        param.name = paramJson.getString("name");
        param.description = paramJson.optString("description", "");
        param.dataType = paramJson.getString("dataType");
        param.range = paramJson.optString("range", "");
        param.unit = paramJson.optString("unit", "");
        param.defaultValue = paramJson.optString("defaultValue", "");
        param.accessLevel = paramJson.optString("accessLevel", "RW");

        // Обработка числовых атрибутов
        if (paramJson.has("decimals")) {
            param.decimals = paramJson.getInt("decimals");
        }

        if (paramJson.has("minValue")) {
            param.minValue = (float) paramJson.getDouble("minValue");
        }

        if (paramJson.has("maxValue")) {
            param.maxValue = (float) paramJson.getDouble("maxValue");
        }

        // Определяем, только ли для чтения параметр
        param.readOnly = "R".equals(param.accessLevel);

        // Обработка опций для enum-параметров
        if (paramJson.has("options")) {
            JSONArray optionsArray = paramJson.getJSONArray("options");
            param.options = new ArrayList<>();

            for (int k = 0; k < optionsArray.length(); k++) {
                JSONObject optionJson = optionsArray.getJSONObject(k);
                DeviceConfig.DeviceParameter.Option option = new DeviceConfig.DeviceParameter.Option();
                option.value = optionJson.getInt("value");
                option.description = optionJson.getString("description");
                param.options.add(option);
            }
        }

        return param;
    }

    @Override
    public byte[] createReadRequest(int register, int count) {
        return ModbusUtils.createReadRequest(getDeviceAddress(), register, count);
    }

    @Override
    public byte[] createWriteRequest(int register, int value) {
        DeviceConfig.DeviceParameter param = parameterMap.get(register);
        if (param != null && param.readOnly) {
            throw new IllegalArgumentException("Parameter " + param.name + " is read-only");
        }
        return ModbusUtils.createWriteRequest(getDeviceAddress(), register, value);
    }

    @Override
    public void processResponse(int startRegister, int count, byte[] data) {
        int[] parsedData = ModbusUtils.parseRegisterData(data);
        if (parsedData == null) return;

        int function = data[1] & 0xFF;

        if (function == 0x03) {
            for (int i = 0; i < parsedData.length; i++) {
                int register = startRegister + i;
                DeviceConfig.DeviceParameter param = parameterMap.get(register);
                if (param != null) {
                    float scaledValue = parsedData[i] * (float) Math.pow(10, -param.decimals);

                    DeviceDataEvent event = new DeviceDataEvent(
                            param.name, scaledValue, param.unit
                    );
                    eventPublisher.publish(event);
                }
            }
        }
    }

    @Override
    public int[] getPollingRegisters() {
        return parameterMap.keySet().stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    @Override
    public int getPollingInterval() {
        return deviceConfig != null ? deviceConfig.pollingInterval : 1000;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public int getDeviceAddress() {
        return deviceAddress;
    }

    public DeviceConfig.DeviceParameter getParameterByName(String name) {
        return parameterNameMap.get(name);
    }

    public DeviceConfig.DeviceParameter getParameterByAddress(int address) {
        return parameterMap.get(address);
    }

    public DeviceConfig getDeviceConfig() {
        return deviceConfig;
    }
}