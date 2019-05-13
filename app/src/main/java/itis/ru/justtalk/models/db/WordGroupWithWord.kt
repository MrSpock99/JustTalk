package itis.ru.justtalk.models.db

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class WordGroupWithWord(
    @Embedded var wordGroup: WordGroup? = null,
    @Relation(parentColumn = "id", entity = Word::class, entityColumn = "word_id")
    var list: List<Word> = emptyList()
)
