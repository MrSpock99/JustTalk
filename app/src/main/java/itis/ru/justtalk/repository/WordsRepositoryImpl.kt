package itis.ru.justtalk.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.api.UnsplashImageApi
import itis.ru.justtalk.db.WordGroupsDao
import itis.ru.justtalk.models.db.Group
import itis.ru.justtalk.models.db.GroupWithWord
import itis.ru.justtalk.models.db.Word
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(
    private val dao: WordGroupsDao,
    private val unsplashImageApi: UnsplashImageApi
) : WordsRepository {

    override fun addWords(wordList: List<Word>, group: Group): Completable {
        return Completable.create { emitter ->
            val wordGroupWithWord = GroupWithWord(group = group, list = wordList)
            dao.insert(wordGroupWithWord)
            emitter.onComplete()
        }
    }

    override fun addWord(word: Word, group: Group, autoPhoto: Boolean?): Completable {
        return Completable.create { emitter ->
            val wordGroupWithWord =
                GroupWithWord(group = group, list = listOf(word))
            dao.insert(wordGroupWithWord)
            emitter.onComplete()
        }
    }

    override fun addGroup(group: Group, autoPhoto: Boolean?): Completable {
        return Completable.create { emitter ->
            if (autoPhoto != null && autoPhoto) {
                unsplashImageApi.getPhotoByKeyword(keyword = group.name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        if (it.results!!.isNotEmpty()) {
                            group.imageUrl = it.results?.get(0)?.urls?.small.toString()
                        }
                        dao.insert(group)
                        emitter.onComplete()
                    }, {
                        emitter.onError(it)
                    })
            } else {
                dao.insert(group)
                emitter.onComplete()
            }
        }
    }

    override fun getGroupWords(groupId: Long): Single<GroupWithWord> {
        return dao.getAllWordsInGroup(groupId)
    }

    override fun getAllWords(): Single<List<Word>> {
        return dao.getAllWords()
    }

    override fun getAllGroups(): Single<List<Group>> {
        return dao.getAllWordGroups()
    }

    override fun geGroupById(groupId: Long): Single<Group> {
        return dao.getById(groupId)
    }

    override fun addWordsWithoutGroup(wordList: List<Word>): Completable {
        return Completable.create { emitter ->
            dao.insert(wordList)
            emitter.onComplete()
        }
    }

    override fun deleteGroup(group: GroupWithWord): Completable {
        return Completable.create { emitter ->
            dao.deleteGroup(group)
            emitter.onComplete()
        }
    }

    override fun deleteWord(word: Word): Completable {
        return Completable.create { emitter ->
            dao.deleteWord(word)
            emitter.onComplete()
        }
    }

    override fun editWord(word: Word): Completable {
        return Completable.create { emitter ->
            dao.updateWord(word.wordId, word.word, word.translation)
            emitter.onComplete()
        }
    }

    override fun editGroup(group: Group): Completable {
        return Completable.create { emitter ->
            dao.updateGroup(group.id, group.name, group.imageUrl)
            emitter.onComplete()
        }
    }
}
