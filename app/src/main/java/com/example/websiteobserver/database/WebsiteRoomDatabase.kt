package com.example.websiteobserver.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.websiteobserver.database.entities.Website
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Website::class], version = 1, exportSchema = false)
abstract class WebsiteRoomDatabase : RoomDatabase() {
    abstract fun dao(): WebsitesDao

    private class WebsiteDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val wordDao = database.dao()
                    wordDao.insertEntry(Website("Google Main Page", "https://google.com"))
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WebsiteRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WebsiteRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WebsiteRoomDatabase::class.java,
                    "websites"
                )
                    .addCallback(WebsiteDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}