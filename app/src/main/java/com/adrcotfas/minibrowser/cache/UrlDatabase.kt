package com.adrcotfas.minibrowser.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UrlEntity::class], version = 1, exportSchema = false)
abstract class UrlDatabase : RoomDatabase() {
    abstract fun urlDao(): UrlDao

    companion object {
        const val DATABASE_NAME = "history_db"
    }
}