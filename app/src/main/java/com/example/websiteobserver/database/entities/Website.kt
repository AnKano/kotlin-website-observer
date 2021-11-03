package com.example.websiteobserver.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "websites")
data class Website(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "lastPing") var lastKnownPing: Long? = null,
    @ColumnInfo(name = "status") var status: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}