package com.example.rs485toandroid;

import java.util.Arrays;

/**
 * Утилитный класс для работы с протоколом Modbus
 * Содержит методы для расчета CRC, проверки данных и преобразования форматов
 */
public class ModbusUtils {

    /**
     * Вычисляет CRC16 для массива данных
     * @param data Данные для расчета CRC
     * @return Значение CRC16
     */
    public static int calculateCRC(byte[] data) {
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

    /**
     * Проверяет корректность CRC в полученных данных
     * @param data Данные с CRC
     * @return true если CRC корректно
     */
    public static boolean checkCRC(byte[] data) {
        if (data.length < 3) return false;

        int length = data.length - 2;
        byte[] packet = Arrays.copyOf(data, length);
        int calculated = calculateCRC(packet);
        int received = (data[length] & 0xFF) | ((data[length + 1] & 0xFF) << 8);

        return calculated == received;
    }

    /**
     * Парсит данные регистров из ответа устройства
     * @param data Ответ устройства
     * @return Массив значений регистров или null при ошибке
     */
    public static int[] parseRegisterData(byte[] data) {
        if (!checkCRC(data) || data.length < 5 || data[1] != 0x03) {
            return null;
        }

        int byteCount = data[2] & 0xFF;
        int expectedCount = byteCount / 2;

        int[] result = new int[expectedCount];
        int index = 3;
        for (int i = 0; i < expectedCount; i++) {
            result[i] = ((data[index] & 0xFF) << 8) | (data[index+1] & 0xFF);
            index += 2;
        }
        return result;
    }

    /**
     * Преобразует массив байтов в HEX-строку
     * @param bytes Массив байтов
     * @return HEX-строка
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

    /**
     * Преобразует HEX-строку в массив байтов
     * @param hex HEX-строка
     * @return Массив байтов
     * @throws IllegalArgumentException при некорректной строке
     */
    public static byte[] hexStringToByteArray(String hex) {
        String cleanedHex = hex.replaceAll("\\s+", "").toUpperCase();
        if (cleanedHex.length() % 2 != 0) {
            throw new IllegalArgumentException("Некорректная HEX-строка");
        }

        byte[] data = new byte[cleanedHex.length() / 2];
        for (int i = 0; i < cleanedHex.length(); i += 2) {
            String byteStr = cleanedHex.substring(i, i + 2);
            data[i/2] = (byte) Integer.parseInt(byteStr, 16);
        }
        return data;
    }
}