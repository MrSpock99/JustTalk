package itis.ru.justtalk.ui.words.words

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.WordsInteractor
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class WordsViewModel @Inject constructor(private val interactor: WordsInteractor) :
    BaseViewModel() {
    val addWordSuccessLiveData = MutableLiveData<Response<Boolean>>()
    val allWordsLiveData = MutableLiveData<Response<List<Word>>>()

    fun addWord(word: Word, wordGroup: WordGroup) {
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
}