package com.example.websiteobserver.database

import androidx.room.*

import com.example.websiteobserver.database.entities.Website
import kotlinx.coroutines.flow.Flow

@Dao
interface WebsitesDao {
    @Query("SELECT * FROM websites")
    fun getEverythingAsFlow(): Flow<List<Website>>

    @Query("SELECT * FROM websites")
    fun getEverythingAsList(): List<Website>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(website: Website)

    @Delete
    suspend fun deleteEntry(website: Website)

    @Query("DELETE FROM websites")
    suspend fun deleteEverything()
}