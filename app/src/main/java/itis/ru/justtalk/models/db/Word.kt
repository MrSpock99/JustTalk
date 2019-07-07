package itis.ru.justtalk.models.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "word")
data class Word(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "word_id")
    val wordId: Long = 0,
    val word: String,
    val translation: String,
    var progress: Int = 0,
    @ColumnInfo(name = "image_url")
    var imageUrl: String,
    @ColumnInfo(name = "group_id")
    val groupId: Long = 0
)
