package com.example.rs485toandroid.connection.models;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AdapterConfig {
    private String name;
    private int vendorId;
    private List<Integer> productIds;
    private Integer defaultBaudRate;
    private Integer defaultDataBits;
    private Integer defaultStopBits;
    private Integer defaultParity;
    private List<ControlCommand> initCommands;

    public static AdapterConfig fromJson(JSONObject json) throws Exception {
        AdapterConfig config = new AdapterConfig();
        config.name = json.getString("name");
        config.vendorId = json.getInt("vendorId");

        // Parse product IDs
        config.productIds = new ArrayList<>();
        JSONArray productIdsArray = json.getJSONArray("productIds");
        for (int i = 0; i < productIdsArray.length(); i++) {
            config.productIds.add(productIdsArray.getInt(i));
        }

        // Parse optional parameters
        if (json.has("defaultBaudRate")) {
            config.defaultBaudRate = json.getInt("defaultBaudRate");
        }
        if (json.has("defaultDataBits")) {
            config.defaultDataBits = json.getInt("defaultDataBits");
        }
        if (json.has("defaultStopBits")) {
            config.defaultStopBits = json.getInt("defaultStopBits");
        }
        if (json.has("defaultParity")) {
            config.defaultParity = json.getInt("defaultParity");
        }

        // Parse init commands
        config.initCommands = new ArrayList<>();
        if (json.has("initCommands")) {
            JSONArray commandsArray = json.getJSONArray("initCommands");
            for (int i = 0; i < commandsArray.length(); i++) {
                JSONObject commandJson = commandsArray.getJSONObject(i);
                ControlCommand command = new ControlCommand();
                command.setRequestType(commandJson.getInt("requestType"));
                command.setRequest(commandJson.getInt("request"));
                command.setValue(commandJson.getInt("value"));
                command.setIndex(commandJson.getInt("index"));

                if (commandJson.has("data")) {
                    JSONArray dataArray = commandJson.getJSONArray("data");
                    byte[] data = new byte[dataArray.length()];
                    for (int j = 0; j < dataArray.length(); j++) {
                        data[j] = (byte) dataArray.getInt(j);
                    }
                    command.setData(data);
                }

                config.initCommands.add(command);
            }
        }

        return config;
    }

    // Getters
    public String getName() { return name; }
    public int getVendorId() { return vendorId; }
    public List<Integer> getProductIds() { return productIds; }
    public Integer getDefaultBaudRate() { return defaultBaudRate; }
    public Integer getDefaultDataBits() { return defaultDataBits; }
    public Integer getDefaultStopBits() { return defaultStopBits; }
    public Integer getDefaultParity() { return defaultParity; }
    public List<ControlCommand> getInitCommands() { return initCommands; }
}