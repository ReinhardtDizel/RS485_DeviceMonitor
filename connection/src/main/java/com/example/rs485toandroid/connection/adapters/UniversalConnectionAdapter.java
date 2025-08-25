package com.example.rs485toandroid.connection.adapters;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import com.example.rs485toandroid.connection.models.AdapterConfig;
import com.example.rs485toandroid.connection.models.ControlCommand;
import com.example.rs485toandroid.core.models.ConnectionConfig; // Исправлен импорт
import com.hoho.android.usbserial.driver.UsbSerialPort;
import java.io.IOException;
import java.util.List;

public class UniversalConnectionAdapter extends BaseConnectionAdapter {
    private final AdapterConfig adapterConfig;

    public UniversalConnectionAdapter(AdapterConfig adapterConfig) {
        this.adapterConfig = adapterConfig;
    }

    @Override
    public void configurePort(UsbSerialPort port) throws IOException {
        ConnectionConfig connectionConfig = getConnectionConfig();

        int baudRate = adapterConfig.getDefaultBaudRate() != null ?
                adapterConfig.getDefaultBaudRate() : connectionConfig.getBaudRate();

        int dataBits = adapterConfig.getDefaultDataBits() != null ?
                adapterConfig.getDefaultDataBits() : connectionConfig.getDataBits();

        int stopBits = adapterConfig.getDefaultStopBits() != null ?
                adapterConfig.getDefaultStopBits() : connectionConfig.getStopBits();

        int parity = adapterConfig.getDefaultParity() != null ?
                adapterConfig.getDefaultParity() : connectionConfig.getParity();

        // Преобразуем значение parity в константы UsbSerialPort
        int parityConstant;
        switch (parity) {
            case 1:
                parityConstant = UsbSerialPort.PARITY_ODD;
                break;
            case 2:
                parityConstant = UsbSerialPort.PARITY_EVEN;
                break;
            case 3:
                parityConstant = UsbSerialPort.PARITY_MARK;
                break;
            case 4:
                parityConstant = UsbSerialPort.PARITY_SPACE;
                break;
            case 0:
            default:
                parityConstant = UsbSerialPort.PARITY_NONE;
                break;
        }

        port.setParameters(baudRate, dataBits, stopBits, parityConstant);
    }

    @Override
    public void performSpecialInit(UsbDeviceConnection connection) throws IOException {
        List<ControlCommand> commands = adapterConfig.getInitCommands();
        if (commands != null) {
            for (ControlCommand command : commands) {
                try {
                    connection.controlTransfer(
                            command.getRequestType(),
                            command.getRequest(),
                            command.getValue(),
                            command.getIndex(),
                            command.getData(),
                            command.getData() != null ? command.getData().length : 0,
                            0
                    );
                } catch (Exception e) {
                    throw new IOException("Failed to execute control command: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public String getAdapterName() {
        return adapterConfig.getName();
    }

    @Override
    public boolean supportsDevice(UsbDevice device) {
        return device.getVendorId() == adapterConfig.getVendorId() &&
                adapterConfig.getProductIds().contains(device.getProductId());
    }
}