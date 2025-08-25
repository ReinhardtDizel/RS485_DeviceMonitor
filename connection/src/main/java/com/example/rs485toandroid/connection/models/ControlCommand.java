package com.example.rs485toandroid.connection.models;

public class ControlCommand {
    private int requestType;
    private int request;
    private int value;
    private int index;
    private byte[] data;

    // Getters and Setters
    public int getRequestType() { return requestType; }
    public void setRequestType(int requestType) { this.requestType = requestType; }

    public int getRequest() { return request; }
    public void setRequest(int request) { this.request = request; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
}