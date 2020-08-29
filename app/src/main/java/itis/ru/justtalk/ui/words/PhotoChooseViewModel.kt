package itis.ru.justtalk.ui.words

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.WordsInteractor
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class PhotoChooseViewModel @Inject constructor(private val wordsInteractor: WordsInteractor) :
    BaseViewModel() {
    val photosLiveData = MutableLiveData<Response<List<String?>>>()

    fun getPhotos(keyword: String) {
        disposables.add(
            wordsInteractor.getPhotosByKeyword(keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    photosLiveData.value = Response.success(it)
                }, { error ->
                    photosLiveData.value = Response.error(error)
                    error.printStackTrace()
                })
        )
    }
}