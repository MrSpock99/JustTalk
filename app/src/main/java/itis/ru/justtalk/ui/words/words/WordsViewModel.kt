package itis.ru.justtalk.ui.words.words

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.WordsInteractor
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.words.groups.ARG_GROUP_ID
import itis.ru.justtalk.ui.words.groups.ARG_IMAGE_URL
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class WordsViewModel @Inject constructor(private val interactor: WordsInteractor) :
    BaseViewModel() {
    val wordOperationsLiveData = MutableLiveData<Response<Boolean>>()
    val allWordsLiveData = MutableLiveData<Response<List<Word>>>()

    fun addWord(arguments: Bundle?, data: Intent?) {
        val word = Word(
            groupId = arguments?.get(ARG_GROUP_ID) as Long,
            word = data?.getStringExtra(ARG_WORD).toString(),
            translation = data?.getStringExtra(ARG_TRANSLATION).toString(),
            imageUrl = data?.getStringExtra(ARG_IMAGE_URL).toString()
        )
        disposables.add(
            interactor.getGroupById(arguments.get(ARG_GROUP_ID) as Long)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ wordGroup ->
                    interactor.addWord(
                        word,
                        wordGroup,
                        data?.getBooleanExtra(ARG_AUTO_PHOTO, false)
                    )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            getWords(wordGroup.id)
                            wordOperationsLiveData.value = Response.success(true)
                        }, { error ->
                            wordOperationsLiveData.value = Response.error(error)
                            error.printStackTrace()
                        })
                }, { error ->
                    wordOperationsLiveData.value = Response.error(error)
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

    fun deleteWord(word: Word) {
        disposables.add(
            interactor.deleteWord(word)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    wordOperationsLiveData.value = Response.success(true)
                }, { error ->
                    wordOperationsLiveData.value = Response.error(error)
                    error.printStackTrace()
                })
        )
    }

    fun editWord(arguments: Bundle?, data: Intent?) {
        data?.extras?.let {
            val word = Word(
                groupId = arguments?.get(ARG_GROUP_ID) as Long,
                wordId = it.getLong(ARG_WORD_ID),
                word = it.getString(ARG_WORD),
                translation = it.getString(ARG_TRANSLATION),
                imageUrl = it.getString(ARG_IMAGE_URL) ?: ""
            )

            disposables.add(
                interactor.getGroupById(arguments.get(ARG_GROUP_ID) as Long)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { group ->
                        interactor.editWord(word)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                getWords(group.id)
                                wordOperationsLiveData.value = Response.success(true)
                            }, { error ->
                                wordOperationsLiveData.value = Response.error(error)
                                error.printStackTrace()
                            })
                    }
                    .subscribe({}, { error ->
                        wordOperationsLiveData.value = Response.error(error)
                        error.printStackTrace()
                    })
            )
        }

    }
}
