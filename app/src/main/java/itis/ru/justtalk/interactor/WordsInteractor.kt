package itis.ru.justtalk.interactor

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.api.UnsplashImageApi
import itis.ru.justtalk.models.db.Group
import itis.ru.justtalk.models.db.GroupWithWord
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.unsplash.Result
import itis.ru.justtalk.repository.WordsRepository
import javax.inject.Inject

class WordsInteractor @Inject constructor(
    private val repository: WordsRepository,
    private val unsplashImageApi: UnsplashImageApi
) {

    fun addWord(word: Word, group: Group, autoPhoto: Boolean?): Completable {
        return repository.addWord(word, group, autoPhoto)
            .subscribeOn(Schedulers.io())
    }

    fun addWords(wordList: List<Word>, group: Group): Completable {
        return repository.addWords(wordList, group)
            .subscribeOn(Schedulers.io())
    }

    fun addWordsWithourGroup(wordList: List<Word>): Completable {
        return repository.addWordsWithoutGroup(wordList)
            .subscribeOn(Schedulers.io())
    }

    fun addGroup(group: Group, autoPhoto: Boolean?): Completable {
        return repository.addGroup(group, autoPhoto)
            .subscribeOn(Schedulers.io())
    }

    fun deleteGroup(group: GroupWithWord): Completable {
        return repository.deleteGroup(group)
            .subscribeOn(Schedulers.io())
    }

    fun getGroups(): Single<List<Group>> {
        return repository.getAllGroups()
            .subscribeOn(Schedulers.io())
    }

    fun getWordsInGroup(groupId: Long): Single<List<Word>> {
        return repository.getGroupWords(groupId)
            .subscribeOn(Schedulers.io())
            .map {
                it.list
            }
    }

    fun getGroupById(groupId: Long): Single<Group> {
        return repository.geGroupById(groupId)
            .subscribeOn(Schedulers.io())
    }

    fun getAllWords(): Single<List<Word>> {
        return repository.getAllWords()
            .subscribeOn(Schedulers.io())
    }

    fun getGroupWithWords(groupId: Long): Single<GroupWithWord> {
        return repository.getGroupWords(groupId)
            .subscribeOn(Schedulers.io())
    }

    fun deleteWord(word: Word): Completable {
        return repository.deleteWord(word)
            .subscribeOn(Schedulers.io())
    }

    fun editWord(word: Word): Completable {
        return repository.editWord(word)
            .subscribeOn(Schedulers.io())
    }

    fun editGroup(group: Group): Completable {
        return repository.editGroup(group)
            .subscribeOn(Schedulers.io())
    }

    fun getPhotosByKeyword(keyword: String): Observable<List<String?>> {
        return unsplashImageApi.getPhotoByKeyword(keyword = keyword)
            .subscribeOn(Schedulers.io())
            .map { it.results }
            .map { it.map { it.urls?.small } }
    }
}
