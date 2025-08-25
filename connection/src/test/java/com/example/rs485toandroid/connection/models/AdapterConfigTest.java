// AdapterConfigTest.java
package com.example.rs485toandroid.connection.models;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AdapterConfigTest {

    @Test
    public void testFromJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", "TestAdapter");
        json.put("vendorId", 1234);

        JSONArray productIds = new JSONArray();
        productIds.put(5678);
        productIds.put(9012);
        json.put("productIds", productIds);

        json.put("defaultBaudRate", 9600);
        json.put("defaultDataBits", 8);
        json.put("defaultStopBits", 1);
        json.put("defaultParity", 0);

        JSONArray commands = new JSONArray();
        JSONObject command = new JSONObject();
        command.put("requestType", 1);
        command.put("request", 2);
        command.put("value", 3);
        command.put("index", 4);

        JSONArray data = new JSONArray();
        data.put(5);
        data.put(6);
        data.put(7);
        command.put("data", data);

        commands.put(command);
        json.put("initCommands", commands);

        AdapterConfig config = AdapterConfig.fromJson(json);

        assertEquals("TestAdapter", config.getName());
        assertEquals(1234, config.getVendorId());
        assertEquals(2, config.getProductIds().size());
        assertEquals(Integer.valueOf(9600), config.getDefaultBaudRate());

        List<ControlCommand> initCommands = config.getInitCommands();
        assertNotNull(initCommands);
        assertEquals(1, initCommands.size());

        ControlCommand firstCommand = initCommands.get(0);
        assertEquals(1, firstCommand.getRequestType());
        assertEquals(2, firstCommand.getRequest());
        assertEquals(3, firstCommand.getValue());
        assertEquals(4, firstCommand.getIndex());
    }
}