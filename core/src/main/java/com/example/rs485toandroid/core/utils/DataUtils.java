package com.example.rs485toandroid.core.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DataUtils {

    public static float bytesToFloat(byte[] bytes, ByteOrder order) {
        return ByteBuffer.wrap(bytes)
                .order(order)
                .getFloat();
    }

    public static int bytesToInt(byte[] bytes, ByteOrder order) {
        return ByteBuffer.wrap(bytes)
                .order(order)
                .getInt();
    }

    public static String formatTemperature(float value) {
        return String.format("%.1f°C", value);
    }

    public static boolean isBitSet(byte b, int bit) {
        return (b & (1 << bit)) != 0;
    }
}