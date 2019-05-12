package itis.ru.justtalk.db

import android.arch.persistence.room.*
import io.reactivex.Single
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.models.db.WordGroupWithWord

@Dao
interface WordGroupsDao {
    @Transaction @Query("SELECT * FROM word_group")
    fun getAllWordGroups(): Single<List<WordGroup>>

    @Transaction
    fun insert(wordGroupWithWord: WordGroupWithWord){
        insert(wordGroupWithWord.wordGroup)
        wordGroupWithWord.list.forEach {
            insert(it)
        }
    }

    @Query("SELECT * FROM word_group WHERE id = :id")
    fun getById(id: Int): Single<WordGroup>

    @Query("DELETE FROM word_group")
    fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(group: WordGroup)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word)

    @Update
    fun update(group: WordGroup)

    @Delete
    fun delete(group: WordGroup)
}
