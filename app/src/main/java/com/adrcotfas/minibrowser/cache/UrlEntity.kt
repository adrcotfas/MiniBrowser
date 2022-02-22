package com.adrcotfas.minibrowser.cache

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["url"], unique = true)])
class UrlEntity(@field:PrimaryKey(autoGenerate = true) var id: Int, var url: String)