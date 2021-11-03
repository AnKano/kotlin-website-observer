package com.example.websiteobserver

import android.app.Application
import com.example.websiteobserver.database.WebsiteRoomDatabase
import com.example.websiteobserver.database.WebsitesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WebsiteObserverApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { WebsiteRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WebsitesRepository(database.dao()) }
}