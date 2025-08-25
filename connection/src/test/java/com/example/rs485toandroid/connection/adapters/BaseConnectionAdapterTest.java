// BaseConnectionAdapterTest.java
package com.example.rs485toandroid.connection.adapters;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import com.example.rs485toandroid.core.models.ConnectionConfig;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BaseConnectionAdapterTest {

    @Mock
    private UsbDevice mockDevice;
    @Mock
    private UsbDeviceConnection mockConnection;
    @Mock
    private UsbSerialPort mockPort;

    private TestConnectionAdapter adapter;

    static class TestConnectionAdapter extends BaseConnectionAdapter {
        @Override
        public String getAdapterName() {
            return "TestAdapter";
        }

        @Override
        public boolean supportsDevice(UsbDevice device) {
            return true;
        }
    }

    @Before
    public void setUp() {
        adapter = new TestConnectionAdapter();
    }

    @Test
    public void testInitialize() {
        adapter.initialize(mockDevice, mockConnection);
        assertNotNull(adapter.device);
        assertNotNull(adapter.connection);
        assertNotNull(adapter.connectionConfig);
    }

    @Test
    public void testConfigurePort() throws IOException {
        ConnectionConfig config = new ConnectionConfig();
        config.setBaudRate(9600);
        config.setDataBits(8);
        config.setStopBits(1);
        config.setParity(0);

        adapter.setConnectionConfig(config);
        adapter.configurePort(mockPort);

        verify(mockPort).setParameters(9600, 8, 1, UsbSerialPort.PARITY_NONE);
    }

    @Test
    public void testSetAndGetConnectionConfig() {
        ConnectionConfig config = new ConnectionConfig();
        config.setBaudRate(115200);

        adapter.setConnectionConfig(config);
        ConnectionConfig result = adapter.getConnectionConfig();

        assertEquals(115200, result.getBaudRate());
    }
}