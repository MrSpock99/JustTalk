package itis.ru.justtalk.repository

import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.db.WordGroupsDao
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.models.db.WordGroupWithWord
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(private val dao: WordGroupsDao) : WordsRepository {
    override fun addWord(word: Word, wordGroup: WordGroup): Completable {
        return Completable.create { emitter ->
            val wordGroupWithWord = WordGroupWithWord(wordGroup = wordGroup, list = listOf(word))
            dao.insert(wordGroupWithWord)
            emitter.onComplete()
        }
    }

    override fun addGroup(group: WordGroup): Completable {
        return Completable.create {
            dao.insert(group)
            it.onComplete()
        }
    }

    override fun getGroupWords(groupId: Long): Single<WordGroupWithWord> {
        return dao.getAllWordsInGroup(groupId)
    }

    override fun getAllWords(): Single<List<Word>> {
        return dao.getAllWords()
    }

    override fun getAllGroups(): Single<List<WordGroup>> {
        return dao.getAllWordGroups()
    }
}
