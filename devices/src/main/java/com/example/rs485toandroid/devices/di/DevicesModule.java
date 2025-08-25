package com.example.rs485toandroid.devices.di;

import android.content.Context;

import com.example.rs485toandroid.core.adapters.EventBusAdapter;
import com.example.rs485toandroid.core.interfaces.IDeviceManager;
import com.example.rs485toandroid.core.interfaces.IEventPublisher;
import com.example.rs485toandroid.devices.implementations.DeviceManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DevicesModule {

    @Provides
    public IEventPublisher provideEventPublisher() {
        return new EventBusAdapter();
    }

    @Provides
    public IDeviceManager provideDeviceFactory(@ApplicationContext Context context, IEventPublisher eventPublisher) {
        return new DeviceManager(context, eventPublisher);
    }
}