package com.example.rs485toandroid.devices.utils;

/**
 * Утилитный класс для работы с протоколом Modbus
 * Содержит методы для расчета CRC, проверки данных и преобразования форматов
 */
public class ModbusUtils {

    private static final int[] CRC_TABLE = new int[256];

    // Статический блок инициализации таблицы CRC
    static {
        for (int i = 0; i < 256; i++) {
            int crc = i;
            for (int j = 0; j < 8; j++) {
                if ((crc & 1) == 1) {
                    crc = (crc >>> 1) ^ 0xA001;
                } else {
                    crc = crc >>> 1;
                }
            }
            CRC_TABLE[i] = crc;
        }
    }

    /**
     * Вычисляет CRC16 для массива данных по алгоритму Modbus RTU с использованием таблицы
     * @param data Данные для расчета CRC
     * @return Значение CRC16
     */
    public static int calculateCRC(byte[] data) {
        int crc = 0xFFFF;
        for (byte b : data) {
            int tableIndex = (crc ^ (b & 0xFF)) & 0xFF;
            crc = (crc >>> 8) ^ CRC_TABLE[tableIndex];
        }
        return crc;
    }
    /**
     * Проверяет корректность CRC в полученных данных для протокола Modbus RTU
     * @param data Данные с CRC (последние 2 байта - CRC)
     * @return true если CRC корректно, false в противном случае
     */
    public static boolean checkCRC(byte[] data) {
        if (data.length < 2) {
            return false;
        }

        // Вычисляем CRC для данных без последних двух байт
        byte[] dataWithoutCRC = new byte[data.length - 2];
        System.arraycopy(data, 0, dataWithoutCRC, 0, data.length - 2);

        int calculatedCRC = calculateCRC(dataWithoutCRC);

        // Извлекаем CRC из сообщения (последние два байта в порядке little-endian)
        int receivedCRC = (data[data.length - 2] & 0xFF) | ((data[data.length - 1] & 0xFF) << 8);

        return calculatedCRC == receivedCRC;
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
    public static byte[] createReadRequest(int deviceAddress, int register, int count) {
        byte[] data = {
                (byte) deviceAddress,
                0x03,
                (byte) (register >> 8),
                (byte) (register & 0xFF),
                (byte) (count >> 8),
                (byte) (count & 0xFF)
        };

        int crc = calculateCRC(data);
        return new byte[]{
                data[0], data[1], data[2], data[3], data[4], data[5],
                (byte) (crc & 0xFF),
                (byte) (crc >> 8)
        };
    }

    public static byte[] createWriteRequest(int deviceAddress, int register, int value) {
        byte[] data = {
                (byte) deviceAddress,
                0x06,
                (byte) (register >> 8),
                (byte) (register & 0xFF),
                (byte) (value >> 8),
                (byte) (value & 0xFF)
        };

        int crc = calculateCRC(data);
        return new byte[]{
                data[0], data[1], data[2], data[3], data[4], data[5],
                (byte) (crc & 0xFF),
                (byte) (crc >> 8)
        };
    }
}