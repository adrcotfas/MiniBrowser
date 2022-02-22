package com.adrcotfas.minibrowser.di

import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.SharedPreferences
import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.adrcotfas.minibrowser.cache.UrlDatabase
import androidx.room.Room
import dagger.Module
import dagger.Provides

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    @Provides
    fun provideDatabase(application: Application): UrlDatabase {
        return Room.databaseBuilder(
            application,
            UrlDatabase::class.java,
            UrlDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}