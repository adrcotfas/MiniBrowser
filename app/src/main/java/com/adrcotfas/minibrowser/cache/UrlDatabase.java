package com.adrcotfas.minibrowser.cache;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UrlEntity.class}, version = 1, exportSchema = false)
public abstract class UrlDatabase extends RoomDatabase {

    public abstract UrlDao urlDao();
    public static final String DATABASE_NAME = "history_db";
}