package com.example.rs485toandroid.connection.di;

import android.content.Context;
import android.hardware.usb.UsbManager;
import com.example.rs485toandroid.connection.implementations.AdapterManager;
import com.example.rs485toandroid.connection.implementations.ConnectionManagerImpl;
import com.example.rs485toandroid.core.interfaces.IAdapterManager;
import com.example.rs485toandroid.connection.utils.CommandQueue;
import com.example.rs485toandroid.core.interfaces.IConnectionManager;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class ConnectionModule {

    @Provides
    @Singleton
    public IAdapterManager provideAdapterManager(@ApplicationContext Context context) {
        return new AdapterManager(context);
    }

    @Provides
    @Singleton
    public UsbManager provideUsbManager(@ApplicationContext Context context) {
        return (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    @Provides
    @Singleton
    public IConnectionManager provideConnectionManager(UsbManager usbManager, IAdapterManager adapterManager) {
        return new ConnectionManagerImpl(usbManager, adapterManager);
    }

    @Provides
    @Singleton
    public CommandQueue provideCommandQueue(IConnectionManager connectionManager) {
        return new CommandQueue(connectionManager);
    }
}