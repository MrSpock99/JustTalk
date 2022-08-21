package itis.ru.justtalk.models.db

import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithWord(
    @Embedded var group: Group? = null,
    @Relation(parentColumn = "id", entity = Word::class, entityColumn = "group_id")
    var list: List<Word> = emptyList()
)
