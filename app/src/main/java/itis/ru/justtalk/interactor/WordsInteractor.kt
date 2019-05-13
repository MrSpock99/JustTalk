package itis.ru.justtalk.interactor

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.repository.WordsRepository
import javax.inject.Inject

class WordsInteractor @Inject constructor(private val repository: WordsRepository) {
    fun addWord(word: Word, wordGroup: WordGroup): Completable {
        return repository.addWord(word, wordGroup)
            .subscribeOn(Schedulers.io())
    }

    fun addGroup(wordGroup: WordGroup): Completable {
        return repository.addGroup(wordGroup)
            .subscribeOn(Schedulers.io())
    }

    fun getGroups(): Single<List<WordGroup>> {
        return repository.getAllGroups()
            .subscribeOn(Schedulers.io())
    }

    fun getWordsInGroup(groupId: Long): Single<List<Word>> {
        return repository.getGroupWords(groupId)
            .map {
                it.list
            }
    }
}
