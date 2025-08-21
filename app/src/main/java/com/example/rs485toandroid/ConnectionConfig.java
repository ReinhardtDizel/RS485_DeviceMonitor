package com.example.rs485toandroid;

public class ConnectionConfig {
    private int baudRate;
    private int dataBits;
    private int stopBits;
    private int parity;
    private int flowControl;

    public ConnectionConfig() {
        this.baudRate = 9600;
        this.dataBits = 8;
        this.stopBits = 1;
        this.parity = 0;
        this.flowControl = 0;
    }

    public ConnectionConfig(SettingsManager settingsManager) {
        // Загружаем настройки из менеджера
        this.baudRate = settingsManager.getBaudRate();
        this.dataBits = 8;
        this.stopBits = 1;
        this.parity = 0; // NONE
        this.flowControl = 0; // NONE
    }

    public ConnectionConfig(int baudRate, int dataBits, int stopBits, int parity, int flowControl) {
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.flowControl = flowControl;
    }

    public int getBaudRate() { return baudRate; }
    public void setBaudRate(int baudRate) { this.baudRate = baudRate; }
    public int getDataBits() { return dataBits; }
    public void setDataBits(int dataBits) { this.dataBits = dataBits; }
    public int getStopBits() { return stopBits; }
    public void setStopBits(int stopBits) { this.stopBits = stopBits; }
    public int getParity() { return parity; }
    public void setParity(int parity) { this.parity = parity; }
    public int getFlowControl() { return flowControl; }
    public void setFlowControl(int flowControl) { this.flowControl = flowControl; }
}