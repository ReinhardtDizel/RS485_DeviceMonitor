package com.example.rs485toandroid.devices.config;

import java.util.List;
import java.util.Map;

public class DeviceConfig {
    public String deviceModel;
    public String version;
    public List<ParameterGroup> parameterGroups;
    public int pollingInterval = 1000;

    public static class ParameterGroup {
        public String groupCode;
        public String groupName;
        public List<DeviceParameter> parameters;
    }

    public static class DeviceParameter {
        public String code;
        public int modbusAddress;
        public String name;
        public String description;
        public String dataType;
        public String range;
        public String unit;
        public String defaultValue;
        public String accessLevel;
        public int decimals = 0;
        public float minValue;
        public float maxValue;
        public boolean readOnly = false;
        public List<Option> options;

        public static class Option {
            public int value;
            public String description;
        }
    }
}