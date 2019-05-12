package itis.ru.justtalk.models.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "word_group")
data class WordGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
/*
    val list: List<Word>,
*/
    val progress: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
)
