package com.example.rs485toandroid;

public class DataParser {
    public static void processResponse(byte[] data, DashboardFragment dashboardFragment) {
        int[] parsedData;

        // Обработка одиночных регистров
        if (data.length == 7) {
            parsedData = ModbusManager.parseRegisterData(data, 1);
            if (parsedData != null) {
                int register = ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);
                int value = parsedData[0];

                switch (register) {
                    case 0x0000: dashboardFragment.updateStatus(value); break;
                    case 0x0001: dashboardFragment.updateTemperature(value * 0.1f); break;
                    case 0x0200: dashboardFragment.updateSensorType(value); break;
                }
            }
        }
        // Обработка блока регистров
        else if (data.length > 7) {
            int register = ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);
            int count = ((data[4] & 0xFF) << 8) | (data[5] & 0xFF);

            parsedData = ModbusManager.parseRegisterData(data, count);
            if (parsedData != null) {
                if (register == 0x0400) {
                    dashboardFragment.updateHeatingParams(parsedData);
                } else if (register == 0x0100) {
                    dashboardFragment.updateCommParams(parsedData);
                }
            }
        }
    }
}