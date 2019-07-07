package itis.ru.justtalk.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.api.UnsplashImageApi
import itis.ru.justtalk.db.WordGroupsDao
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.models.db.WordGroupWithWord
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(
    private val dao: WordGroupsDao,
    private val unsplashImageApi: UnsplashImageApi
) : WordsRepository {

    override fun addWords(wordList: List<Word>, wordGroup: WordGroup): Completable {
        return Completable.create { emitter ->
            val wordGroupWithWord = WordGroupWithWord(wordGroup = wordGroup, list = wordList)
            dao.insert(wordGroupWithWord)
            emitter.onComplete()
        }
    }

    override fun addWord(word: Word, wordGroup: WordGroup): Completable {
        return Completable.create { emitter ->
            unsplashImageApi.getPhotoByKeyword(keyword = word.word)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    word.imageUrl = it.results?.get(0)?.urls?.small.toString()
                    val wordGroupWithWord =
                        WordGroupWithWord(wordGroup = wordGroup, list = listOf(word))
                    dao.insert(wordGroupWithWord)
                    emitter.onComplete()
                }, {
                    emitter.onError(it)
                })
        }
    }

    override fun addGroup(group: WordGroup): Completable {
        return Completable.create { emitter ->
            unsplashImageApi.getPhotoByKeyword(keyword = group.name)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    group.imageUrl = it.results?.get(0)?.urls?.small.toString()
                    dao.insert(group)
                    emitter.onComplete()
                }, {
                    emitter.onError(it)
                })
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

    override fun geGroupById(groupId: Long): Single<WordGroup> {
        return dao.getById(groupId)
    }

    override fun addWordsWithoutGroup(wordList: List<Word>): Completable {
        return Completable.create { emitter ->
            dao.insert(wordList)
            emitter.onComplete()
        }
    }
}
