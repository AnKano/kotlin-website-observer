package com.example.websiteobserver.database

import androidx.annotation.WorkerThread
import com.example.websiteobserver.database.entities.Website
import kotlinx.coroutines.flow.Flow

class WebsitesRepository(private val websitesDao: WebsitesDao) {
    val entries: Flow<List<Website>> = websitesDao.getEverythingAsFlow()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(website: Website) {
        websitesDao.insertEntry(website)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(website: Website) {
        websitesDao.deleteEntry(website)
    }
}