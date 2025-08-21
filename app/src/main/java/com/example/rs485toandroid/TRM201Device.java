package com.example.rs485toandroid;

/**
 * Реализация устройства Modbus для терморегулятора ТРМ201
 * Обеспечивает специфичную для ТРМ201 логику работы
 */
public class TRM201Device extends ModbusDevice {
    private float temperature;
    private int sensorType;
    private int status;
    private int[] heatingParams = new int[13];
    private int[] commParams = new int[12];

    /**
     * Создает новый экземпляр устройства ТРМ201
     * @param address Адрес устройства в сети Modbus
     */
    public TRM201Device(int address) {
        super(address, "ТРМ201");
    }

    @Override
    public byte[] createReadRequest(int register, int count) {
        byte[] data = {
                (byte) deviceAddress,
                0x03,
                (byte) (register >> 8),
                (byte) (register & 0xFF),
                (byte) (count >> 8),
                (byte) (count & 0xFF)
        };

        int crc = ModbusUtils.calculateCRC(data);
        return new byte[] {
                data[0], data[1], data[2], data[3], data[4], data[5],
                (byte) (crc & 0xFF),
                (byte) (crc >> 8)
        };
    }

    @Override
    public byte[] createWriteRequest(int register, int value) {
        byte[] data = {
                (byte) deviceAddress,
                0x06,
                (byte) (register >> 8),
                (byte) (register & 0xFF),
                (byte) (value >> 8),
                (byte) (value & 0xFF)
        };

        int crc = ModbusUtils.calculateCRC(data);
        return new byte[] {
                data[0], data[1], data[2], data[3], data[4], data[5],
                (byte) (crc & 0xFF),
                (byte) (crc >> 8)
        };
    }

    @Override
    public void processResponse(byte[] data, DataDisplay display) {
        int[] parsedData = ModbusUtils.parseRegisterData(data);
        if (parsedData == null) return;

        int function = data[1] & 0xFF;
        int register = ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);

        if (function == 0x03) {
            if (data.length == 7) {
                int value = parsedData[0];
                switch (register) {
                    case 0x0000: status = value; break;
                    case 0x0001: temperature = value * 0.1f; break;
                    case 0x0200: sensorType = value; break;
                }
            }
            else if (register == 0x0400) {
                System.arraycopy(parsedData, 0, heatingParams, 0, Math.min(parsedData.length, 13));
            }
            else if (register == 0x0100) {
                System.arraycopy(parsedData, 0, commParams, 0, Math.min(parsedData.length, 12));
            }

            if (display != null) {
                display.updateTemperature(temperature);
                display.updateSensorType(sensorType);
                display.updateStatus(status);
                display.updateHeatingParams(heatingParams);
                display.updateCommParams(commParams);
            }
        }
    }

    @Override
    public int[] getPollingRegisters() {
        return new int[]{0x0000, 0x0001, 0x0200};
    }

    @Override
    public int getPollingInterval() {
        return 2500;
    }
}