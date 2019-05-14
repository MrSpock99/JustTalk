package itis.ru.justtalk.ui.words.test

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.WordsInteractor
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.words.groups.ARG_GROUP_ID
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class TestViewModel @Inject constructor(private val interactor: WordsInteractor) : BaseViewModel() {
    val wordListLiveData = MutableLiveData<Response<List<Word>>>()
    private var wordList: List<Word> = emptyList()

    fun startTest(bundle: Bundle?) {
        if (bundle == null) {
            disposables.add(
                interactor.getAllWords()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        wordList = it
                        wordListLiveData.value = Response.success(wordList)
                    }, { error ->
                        wordListLiveData.value = Response.error(error)
                        error.printStackTrace()
                    })
            )
        } else {
            val groupId = bundle[ARG_GROUP_ID] as Long
            disposables.add(
                interactor.getWordsInGroup(groupId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        wordList = it
                        wordListLiveData.value = Response.success(wordList)
                    }, { error ->
                        wordListLiveData.value = Response.error(error)
                        error.printStackTrace()
                    })
            )
        }
    }

}