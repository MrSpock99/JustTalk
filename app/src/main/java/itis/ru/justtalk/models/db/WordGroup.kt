package itis.ru.justtalk.models.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "word_group")
data class WordGroup(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
/*
    val list: List<Word>,
*/
    var progress: Int = 0,
    @ColumnInfo(name = "image_url")
    var imageUrl: String = ""
)

