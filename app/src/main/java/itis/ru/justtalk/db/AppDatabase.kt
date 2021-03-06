package itis.ru.justtalk.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import itis.ru.justtalk.models.db.Group
import itis.ru.justtalk.models.db.Word

const val DB_NAME: String = "WORDS.db"

@Database(entities = [Group::class, Word::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordsDao(): WordGroupsDao
}
