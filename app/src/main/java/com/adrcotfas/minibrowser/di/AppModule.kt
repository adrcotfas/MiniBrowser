package com.adrcotfas.minibrowser.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;

import com.adrcotfas.minibrowser.cache.UrlDatabase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    static SharedPreferences provideSharedPreferences(@ApplicationContext Context appContext) {
        return PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    @Provides
    static UrlDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, UrlDatabase.class, UrlDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }
}