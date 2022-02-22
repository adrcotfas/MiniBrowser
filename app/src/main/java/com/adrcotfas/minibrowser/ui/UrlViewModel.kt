package com.adrcotfas.minibrowser.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.adrcotfas.minibrowser.cache.UrlDatabase
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.adrcotfas.minibrowser.cache.UrlEntity
import androidx.lifecycle.LiveData
import com.adrcotfas.minibrowser.utils.Constants

@HiltViewModel
class UrlViewModel @Inject internal constructor(
    private val database: UrlDatabase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    fun insert(url: String?) {
        database.urlDao().insert(UrlEntity(0, url!!))
    }

    val urls: LiveData<List<UrlEntity>>
        get() = database.urlDao().get()

    fun clear() {
        database.urlDao().clear()
    }

    var loadImages: Boolean
        get() = sharedPreferences.getBoolean(Constants.PREF_LOAD_IMAGES, false)
        set(value) {
            sharedPreferences.edit().putBoolean(Constants.PREF_LOAD_IMAGES, value).apply()
        }
}