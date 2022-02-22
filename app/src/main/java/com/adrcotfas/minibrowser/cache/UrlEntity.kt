package com.adrcotfas.minibrowser.cache;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"url"}, unique = true)})
public class UrlEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public @NonNull String url;

    public UrlEntity(int id, @NonNull String url) {
        this.id = id;
        this.url = url;
    }
}