package itis.ru.justtalk.ui.words.words

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.base.REQUEST_CODE
import itis.ru.justtalk.ui.words.groups.ARG_GROUP_ID
import itis.ru.justtalk.ui.words.groups.ARG_IMAGE_URL
import itis.ru.justtalk.ui.words.groups.GALLERY_REQUEST_CODE
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class AddWordViewModel @Inject constructor() : BaseViewModel() {
    val addWordFinishLiveData = MutableLiveData<Response<Intent>>()
    val photoChooseSuccessLiveData = MutableLiveData<Response<String>>()
    val editWordLiveData = MutableLiveData<Response<Word>>()
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

    fun getArguments(intent: Intent?) {
        val requestCode = intent?.extras?.getInt(REQUEST_CODE, 0)
        when (requestCode) {
            REQ_CODE_EDIT_WORD -> {
                intent.extras?.let { extras ->
                    resultIntent.putExtra(ARG_WORD_ID, extras.getLong(ARG_WORD_ID))
                    resultIntent.putExtra(ARG_GROUP_ID, extras.getLong(ARG_GROUP_ID))
                    resultIntent.putExtra(ARG_IMAGE_URL, extras.getLong(ARG_IMAGE_URL))
                    editWordLiveData.value = Response.success(
                        Word(
                            word = extras.getString(ARG_WORD, ""),
                            translation = extras.getString(ARG_TRANSLATION, ""),
                            groupId = extras.getLong(
                                ARG_GROUP_ID
                            ),
                            wordId = extras.getLong(ARG_WORD_ID),
                            imageUrl = extras.getString(ARG_IMAGE_URL, "")
                        )
                    )
                }
            }
            REQ_CODE_ADD_WORD -> {

            }
        }
    }
}