package com.adrcotfas.minibrowser.cache;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UrlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UrlEntity image);

    @Query("SELECT * FROM UrlEntity ORDER BY id DESC LIMIT 5")
    LiveData<List<UrlEntity>> get();

    @Query("DELETE FROM UrlEntity")
    void clear();
}