package com.learnathome.di

import android.app.Application
import android.util.Log
//import androidx.room.RoomDatabase
//import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class RoomDatabaseModule(application: Application) {

//    private var libraryApplication = application
//    private lateinit var libraryDatabase: LibraryDatabase
//
//    companion object {
//        private const val EDUCATIONAL_BOOKS_CATEGORY_ID = 1L
//        private const val NOVELS_CATEGORY_ID = 2L
//        private const val OTHER_BOOKS_CATEGORY_ID = 3L
//    }
//
//    private val databaseCallback = object : RoomDatabase.Callback() {
//        override fun onCreate(db: SupportSQLiteDatabase) {
//            super.onCreate(db)
//            Log.d("RoomDatabaseModule =>", "onCreate")
//
//            Coroutines.io {
//                addSampleBooksToDatabase()
//            }
//
//        }
//    }
//
//    private fun addSampleBooksToDatabase() {
//
//    }
//
//    @Singleton
//    @Provides
//    fun providesRoomDatabase(): LibraryDatabase {
//        libraryDatabase = Room.databaseBuilder(libraryApplication, LibraryDatabase::class.java, "library_database")
//            .fallbackToDestructiveMigration()
//            .addCallback(databaseCallback)
//            .build()
//        return libraryDatabase
//    }
//
//    @Singleton
//    @Provides
//    fun providesCategoryDAO(libraryDatabase: LibraryDatabase) = libraryDatabase.getCategoryDAO()
//
//    @Singleton
//    @Provides
//    fun providesBookDAO(libraryDatabase: LibraryDatabase) = libraryDatabase.getBookDAO()
}