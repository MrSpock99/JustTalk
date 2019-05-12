package itis.ru.justtalk.models.db

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class WordGroupWithWord(
    @Embedded val wordGroup: WordGroup,
    @Relation(parentColumn = "id", entity = Word::class, entityColumn = "wordId")
    val list: List<Word>
)
