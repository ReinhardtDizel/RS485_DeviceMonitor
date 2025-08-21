package com.example.rs485toandroid;

/**
 * Заглушка реализации для частотного преобразователя Веспер
 * Требует дальнейшей реализации для полноценной работы
 */
public class VesperDevice extends ModbusDevice {

    /**
     * Создает новый экземпляр устройства Веспер
     * @param address Адрес устройства в сети Modbus
     */
    public VesperDevice(int address) {
        super(address, "Веспер ЧП");
    }

    @Override
    public byte[] createReadRequest(int register, int count) {
        // TODO: Реализовать для Веспер
        return new byte[0];
    }

    @Override
    public byte[] createWriteRequest(int register, int value) {
        // TODO: Реализовать для Веспер
        return new byte[0];
    }

    @Override
    public void processResponse(byte[] data, DataDisplay display) {
        // TODO: Реализовать для Веспер
    }

    @Override
    public int[] getPollingRegisters() {
        // TODO: Реализовать для Веспер
        return new int[0];
    }

    @Override
    public int getPollingInterval() {
        return 1000;
    }
}