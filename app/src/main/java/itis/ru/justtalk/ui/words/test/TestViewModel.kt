package itis.ru.justtalk.ui.words.test

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.interactor.WordsInteractor
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.models.utils.EndTestModel
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.words.groups.ARG_GROUP_ID
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class TestViewModel @Inject constructor(private val interactor: WordsInteractor) : BaseViewModel(),
    Subject {
    val wordListLiveData = MutableLiveData<Response<List<Word>>>()
    val endTestListLiveData = MutableLiveData<Response<EndTestModel>>()
    val testGoingOnLiveData = MutableLiveData<Response<Int>>()
    private var wordList: MutableList<Word> = mutableListOf()
    private var groupId: Long = -1
    private var count = 0
    private var correctCount = 0
    private var observers: MutableList<Observer> = mutableListOf()

    override fun register(observer: Observer) {
        if (!observers.contains(observer)) {
            observers.add(observer)
        }
    }

    override fun unregister(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObserversHintClicked(clicked: Boolean) {
        observers.forEach {
            it.update(clicked)
        }
    }

    fun startTest(bundle: Bundle?) {
        if (bundle == null) {
            disposables.add(
                interactor.getAllWords()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        wordList = it.toMutableList()
                        wordListLiveData.value = Response.success(wordList)
                    }, { error ->
                        wordListLiveData.value = Response.error(error)
                        error.printStackTrace()
                    })
            )
        } else {
            groupId = bundle[ARG_GROUP_ID] as Long
            disposables.add(
                interactor.getWordsInGroup(groupId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        wordList = it.toMutableList()
                        wordListLiveData.value = Response.success(wordList)
                    }, { error ->
                        wordListLiveData.value = Response.error(error)
                        error.printStackTrace()
                    })
            )
        }
    }

    fun wrong(word: Word) {
        if (word.progress > 0) {
            val index = wordList.indexOf(word)
            word.progress--
            wordList[index] = word
            testGoingOnLiveData.value = Response.success(++count)
        }
    }

    fun correct(word: Word) {
        if (word.progress < 10) {
            val index = wordList.indexOf(word)
            word.progress++
            wordList[index] = word
            testGoingOnLiveData.value = Response.success(++count)
        }
        correctCount++
    }

    fun endTest() {
        testGoingOnLiveData.value = Response.success(count++)
        if (groupId != -1L) {
            disposables.add(
                interactor.getGroupById(groupId)
                    .observeOn(Schedulers.io())
                    .subscribe({
                        interactor.addWords(wordList, it)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                val endTestModel = EndTestModel(wordList.size, correctCount)
                                endTestListLiveData.value = Response.success(endTestModel)
                            }, { error ->
                                endTestListLiveData.value = Response.error(error)
                                error.printStackTrace()
                            })
                    }, { error ->
                        endTestListLiveData.value = Response.error(error)
                        error.printStackTrace()
                    })
            )
        } else {
            disposables.add(
                interactor.addWordsWithourGroup(wordList)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val endTestModel = EndTestModel(wordList.size, correctCount)
                        endTestListLiveData.value = Response.success(endTestModel)
                    }, { error ->
                        endTestListLiveData.value = Response.error(error)
                        error.printStackTrace()
                    })
            )
        }
    }

    fun getHint() {
        notifyObserversHintClicked(true)
    }
}
