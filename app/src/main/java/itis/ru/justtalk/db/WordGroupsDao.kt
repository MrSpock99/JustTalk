package itis.ru.justtalk.db

import android.arch.persistence.room.*
import io.reactivex.Single
import itis.ru.justtalk.models.db.Group
import itis.ru.justtalk.models.db.GroupWithWord
import itis.ru.justtalk.models.db.Word

@Dao
interface WordGroupsDao {
    @Transaction
    @Query("SELECT * FROM word_group")
    fun getAllWordGroups(): Single<List<Group>>

    @Transaction
    @Query("SELECT * from word_group WHERE id =:groupId")
    fun getAllWordsInGroup(groupId: Long): Single<GroupWithWord>

    @Transaction
    fun insert(groupWithWord: GroupWithWord) {
        groupWithWord.group?.let {
            insert(it)
            groupWithWord.list.forEach { word ->
                insert(word)
            }
        }
    }

    @Query("SELECT * FROM word_group WHERE id = :id")
    fun getById(id: Long): Single<Group>

    @Query("DELETE FROM word_group")
    fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word: Word)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(wordList: List<Word>)

    @Update
    fun update(group: Group)

    @Transaction
    fun deleteGroup(group: GroupWithWord) {
        group.list.forEach { word ->
            deleteWord(word)
        }
        group.group?.let { deleteGroup(it) }
    }

    @Delete
    fun deleteGroup(group: Group)

    @Delete
    fun deleteWord(word: Word)

    @Query("SELECT * FROM word")
    fun getAllWords(): Single<List<Word>>

    @Query("UPDATE word SET word = :word, translation = :translation WHERE word_id = :wordId")
    fun updateWord(wordId: Long?, word: String, translation: String)
}
