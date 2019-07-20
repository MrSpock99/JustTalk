package itis.ru.justtalk.repository

import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.db.Group
import itis.ru.justtalk.models.db.GroupWithWord
import itis.ru.justtalk.models.db.Word

interface WordsRepository {
    fun addWord(word: Word, group: Group): Completable
    fun addWords(wordList: List<Word>, group: Group): Completable
    fun addWordsWithoutGroup(wordList: List<Word>): Completable
    fun addGroup(group: Group): Completable
    fun deleteGroup(group: GroupWithWord): Completable
    fun deleteWord(word: Word): Completable
    fun getGroupWords(groupId: Long): Single<GroupWithWord>
    fun getAllWords(): Single<List<Word>>
    fun getAllGroups(): Single<List<Group>>
    fun geGroupById(groupId: Long): Single<Group>
    fun editWord(word: Word): Completable
}
