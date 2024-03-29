package itis.ru.justtalk.di.module

import androidx.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import itis.ru.justtalk.db.AppDatabase
import itis.ru.justtalk.db.DB_NAME
import itis.ru.justtalk.db.WordGroupsDao
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDb(context: Context): AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java, DB_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideJokeDao(appDatabase: AppDatabase): WordGroupsDao = appDatabase.wordsDao()
}
