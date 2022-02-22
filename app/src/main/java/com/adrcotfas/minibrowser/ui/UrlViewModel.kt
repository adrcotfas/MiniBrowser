package com.adrcotfas.minibrowser.ui;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.adrcotfas.minibrowser.cache.UrlDatabase;
import com.adrcotfas.minibrowser.cache.UrlEntity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

import static com.adrcotfas.minibrowser.utils.Constants.PREF_LOAD_IMAGES;

@HiltViewModel
public class UrlViewModel extends ViewModel {

    private final UrlDatabase database;
    private final SharedPreferences sharedPreferences;

    @Inject
    UrlViewModel(UrlDatabase database, SharedPreferences sharedPreferences) {
        this.database = database;
        this.sharedPreferences = sharedPreferences;
    }

    public void insert(String url) {
        database.urlDao().insert(new UrlEntity(0, url));
    }
    public LiveData<List<UrlEntity>> getUrls() {
        return database.urlDao().get();
    }
    public void clear() {
        database.urlDao().clear();
    }

    public void setLoadImages(boolean value) {
        sharedPreferences.edit().putBoolean(PREF_LOAD_IMAGES, value).apply();
    }

    public boolean getLoadImages() {
        return sharedPreferences.getBoolean(PREF_LOAD_IMAGES, false);
    }
}
