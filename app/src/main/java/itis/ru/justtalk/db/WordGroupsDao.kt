package itis.ru.justtalk.db

import android.arch.persistence.room.*
import io.reactivex.Single
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.models.db.WordGroupWithWord

@Dao
interface WordGroupsDao {
    @Transaction
    @Query("SELECT * FROM word_group")
    fun getAllWordGroups(): Single<List<WordGroup>>

    @Transaction
    @Query("SELECT * from word_group WHERE id =:groupId")
    fun getAllWordsInGroup(groupId: Long): Single<WordGroupWithWord>

    @Transaction
    fun insert(wordGroupWithWord: WordGroupWithWord) {
        wordGroupWithWord.wordGroup?.let {
            insert(it)
            wordGroupWithWord.list.forEach { word ->
                insert(word)
            }
        }

    }

    @Query("SELECT * FROM word_group WHERE id = :id")
    fun getById(id: Long): Single<WordGroup>

    @Query("DELETE FROM word_group")
    fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(group: WordGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word: Word)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(wordList: List<Word>)

    @Update
    fun update(group: WordGroup)

    @Delete
    fun delete(group: WordGroup)

    @Query("SELECT * FROM word")
    fun getAllWords(): Single<List<Word>>
}
