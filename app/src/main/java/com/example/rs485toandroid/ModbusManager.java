package com.example.rs485toandroid;

import java.util.Arrays;

public class ModbusManager {
    private static final int DEFAULT_ADDRESS = 1;

    public static byte[] createReadRequest(int register, int count) {
        return createRequest(register, count, (byte) 0x03);
    }

    public static byte[] createWriteRequest(int register, int value) {
        return createRequest(register, value, (byte) 0x06);
    }

    private static byte[] createRequest(int register, int value, byte function) {
        byte[] data = {
                (byte) DEFAULT_ADDRESS,
                function,
                (byte) (register >> 8),
                (byte) (register & 0xFF),
                (byte) (value >> 8),
                (byte) (value & 0xFF)
        };

        int crc = calculateCRC(data);
        return new byte[] {
                data[0], data[1], data[2], data[3], data[4], data[5],
                (byte) (crc & 0xFF),
                (byte) (crc >> 8)
        };
    }

    public static boolean checkCRC(byte[] data) {
        if (data.length < 3) return false;

        int length = data.length - 2;
        byte[] packet = Arrays.copyOf(data, length);
        int calculated = calculateCRC(packet);
        int received = (data[length] & 0xFF) | ((data[length + 1] & 0xFF) << 8);

        return calculated == received;
    }

    private static int calculateCRC(byte[] data) {
        int crc = 0xFFFF;
        for (byte b : data) {
            crc ^= b & 0xFF;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc;
    }

    public static int[] parseRegisterData(byte[] data, int expectedCount) {
        if (!checkCRC(data) || data.length < 5 || data[1] != 0x03) {
            return null;
        }

        int byteCount = data[2] & 0xFF;
        if (byteCount != expectedCount * 2) {
            return null;
        }

        int[] result = new int[expectedCount];
        int index = 3;
        for (int i = 0; i < expectedCount; i++) {
            result[i] = ((data[index] & 0xFF) << 8) | (data[index+1] & 0xFF);
            index += 2;
        }
        return result;
    }
}
