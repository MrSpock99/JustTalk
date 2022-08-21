package itis.ru.justtalk.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_group")
data class Group(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
    var progress: Int = 0,
    @ColumnInfo(name = "image_url")
    var imageUrl: String = ""
)

