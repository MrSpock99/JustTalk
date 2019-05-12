package itis.ru.justtalk.models.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = "word", foreignKeys = [ForeignKey(
        entity = WordGroup::class, parentColumns = ["id"],
        childColumns = ["word_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Word(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "word_id")
    val wordId: Long,
    val word: String,
    val translation: String,
    val progress: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
)
