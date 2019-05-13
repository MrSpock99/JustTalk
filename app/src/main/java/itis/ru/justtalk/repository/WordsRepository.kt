package itis.ru.justtalk.repository

import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.models.db.WordGroupWithWord

interface WordsRepository {
    fun addWord(word: Word, wordGroup: WordGroup): Completable
    fun addGroup(group: WordGroup): Completable
    fun getGroupWords(groupId: Long): Single<WordGroupWithWord>
    fun getAllWords(): Single<List<Word>>
    fun getAllGroups(): Single<List<WordGroup>>
}
