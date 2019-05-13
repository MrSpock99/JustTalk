package itis.ru.justtalk.ui.words.words

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.WordsInteractor
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.words.groups.ARG_GROUP_ID
import itis.ru.justtalk.ui.words.groups.ARG_IMAGE_URL
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class WordsViewModel @Inject constructor(private val interactor: WordsInteractor) :
    BaseViewModel() {
    val addWordSuccessLiveData = MutableLiveData<Response<Boolean>>()
    val allWordsLiveData = MutableLiveData<Response<List<Word>>>()

    fun addWord(arguments: Bundle?, data: Intent?) {
        val word = Word(
            groupId = arguments?.get(ARG_GROUP_ID) as Long,
            word = data?.getStringExtra(ARG_WORD).toString(),
            translation = data?.getStringExtra(ARG_TRANSLATION).toString(),
            imageUrl = data?.getStringExtra(ARG_IMAGE_URL).toString()
        )
        val wordGroup = WordGroup(id = arguments.get(ARG_GROUP_ID) as Long)
        disposables.add(
            interactor.addWord(word, wordGroup)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    addWordSuccessLiveData.value = Response.success(true)
                }, { error ->
                    addWordSuccessLiveData.value = Response.error(error)
                    error.printStackTrace()
                })
        )
    }

    fun getWords(groupId: Long) {
        disposables.add(
            interactor.getWordsInGroup(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    allWordsLiveData.value = Response.success(it)
                }, { error ->
                    allWordsLiveData.value = Response.error(error)
                    error.printStackTrace()
                })
        )
    }
}