package itis.ru.justtalk.ui.words.words

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.words.groups.ARG_IMAGE_URL
import itis.ru.justtalk.ui.words.groups.GALLERY_REQUEST_CODE
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class AddWordViewModel @Inject constructor() : BaseViewModel() {
    val addWordFinishLiveData = MutableLiveData<Response<Intent>>()
    val photoChooseSuccessLiveData = MutableLiveData<Response<String>>()
    private val resultIntent: Intent = Intent()

    fun addWordFinish(word: String, translation: String) {
        resultIntent.putExtra(ARG_WORD, word)
        resultIntent.putExtra(ARG_TRANSLATION, translation)
        addWordFinishLiveData.value = Response.success(resultIntent)
    }

    fun onPhotoChooseResult(requestCode: Int, data: Intent?) {
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                val selectedImage = data?.data.toString()
                resultIntent.putExtra(ARG_IMAGE_URL, selectedImage)
                photoChooseSuccessLiveData.value = Response.success(selectedImage)
            }
        }
    }
}