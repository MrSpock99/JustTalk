package itis.ru.justtalk.di.module

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import itis.ru.justtalk.db.AppDatabase
import itis.ru.justtalk.db.DB_NAME
import itis.ru.justtalk.db.WordGroupsDao
import javax.inject.Singleton

@Module
class DatabaseModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideDb(): AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java, DB_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideJokeDao(appDatabase: AppDatabase): WordGroupsDao = appDatabase.wordsDao()
}
