package itis.ru.justtalk.repository

import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.db.WordGroupsDao
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.db.WordGroup
import javax.inject.Inject

class WordsRepositoryImpl @Inject constructor(private val dao: WordGroupsDao) : WordsRepository {
    override fun addWord(word: Word, wordGroup: WordGroup): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addGroup(group: WordGroup): Completable {
        return Completable.create {
            dao.insert(group)
            it.onComplete()
        }
    }

    override fun getGroupWords(group: WordGroup): Single<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllWords(): Single<List<Word>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllGroups(): Single<List<WordGroup>> {
        return dao.getAllWordGroups()
    }
}
