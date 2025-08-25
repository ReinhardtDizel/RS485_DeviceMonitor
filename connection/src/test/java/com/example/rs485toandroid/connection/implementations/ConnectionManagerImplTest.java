package com.example.rs485toandroid.connection.implementations;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.example.rs485toandroid.core.interfaces.IAdapterManager;
import com.example.rs485toandroid.core.interfaces.IConnectionListener;
import com.example.rs485toandroid.core.interfaces.ISettingsListener;
import com.example.rs485toandroid.core.models.ConnectionConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionManagerImplTest {

    @Mock
    private UsbManager mockUsbManager;
    @Mock
    private IAdapterManager mockAdapterManager;
    @Mock
    private UsbDevice mockDevice;
    @Mock
    private IConnectionListener mockConnectionListener;
    @Mock
    private ISettingsListener mockSettingsListener;

    private ConnectionManagerImpl connectionManager;

    @Before
    public void setUp() {
        connectionManager = new ConnectionManagerImpl(mockUsbManager, mockAdapterManager);
    }

    @Test
    public void testConnectWithUnsupportedDevice() {
        when(mockAdapterManager.createAdapter(any(UsbDevice.class))).thenReturn(null);

        boolean result = connectionManager.connect(mockDevice);

        assertFalse(result);
    }

    @Test
    public void testDisconnectWhenNotConnected() {
        connectionManager.disconnect();

        // Should not throw any exceptions
        assertFalse(connectionManager.isConnected());
    }

    @Test
    public void testAddAndRemoveConnectionListeners() {
        connectionManager.addConnectionListener(mockConnectionListener);
        connectionManager.removeConnectionListener(mockConnectionListener);

        // Should not throw any exceptions
        assertTrue(true);
    }

    @Test
    public void testAddAndRemoveSettingsListeners() {
        connectionManager.addSettingsListener(mockSettingsListener);
        connectionManager.removeSettingsListener(mockSettingsListener);

        // Should not throw any exceptions
        assertTrue(true);
    }

    @Test
    public void testUpdateConnectionConfig() {
        ConnectionConfig config = new ConnectionConfig();
        config.setBaudRate(19200);

        connectionManager.updateConnectionConfig(config);

        // Should not throw any exceptions
        assertTrue(true);
    }

    @Test
    public void testSendDataWhenNotConnected() {
        byte[] testData = new byte[]{0x01, 0x02};
        connectionManager.sendData(testData);

        // Should not throw any exceptions
        assertTrue(true);
    }

    @Test
    public void testIsConnectedWhenNotConnected() {
        assertFalse(connectionManager.isConnected());
    }
}