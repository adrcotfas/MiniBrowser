package com.adrcotfas.minibrowser.cache

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UrlDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: UrlEntity)

    @Query("SELECT * FROM UrlEntity ORDER BY id DESC LIMIT 5")
    fun get(): LiveData<List<UrlEntity>>

    @Query("DELETE FROM UrlEntity")
    fun clear()
}