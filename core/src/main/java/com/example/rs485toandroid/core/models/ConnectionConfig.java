package com.example.rs485toandroid.core.models;

public class ConnectionConfig {
    private int baudRate;
    private int dataBits;
    private int stopBits;
    private int parity;

    public ConnectionConfig() {
        this.baudRate = 9600;
        this.dataBits = 8;
        this.stopBits = 1;
        this.parity = 0;
    }

    // Геттеры и сеттеры
    public int getBaudRate() { return baudRate; }
    public void setBaudRate(int baudRate) { this.baudRate = baudRate; }

    public int getDataBits() { return dataBits; }
    public void setDataBits(int dataBits) { this.dataBits = dataBits; }

    public int getStopBits() { return stopBits; }
    public void setStopBits(int stopBits) { this.stopBits = stopBits; }

    public int getParity() { return parity; }
    public void setParity(int parity) { this.parity = parity; }
}
