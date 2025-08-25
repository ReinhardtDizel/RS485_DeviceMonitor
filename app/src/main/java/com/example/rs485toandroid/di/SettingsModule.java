package com.example.rs485toandroid.di;

import android.content.Context;
import com.example.rs485toandroid.settings.AndroidSettingsManager;
import com.example.rs485toandroid.core.interfaces.ISettingsManager;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class SettingsModule {

    @Provides
    @Singleton
    public ISettingsManager provideSettingsManager(@ApplicationContext Context context) {
        return new AndroidSettingsManager(context);
    }
}