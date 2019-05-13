package itis.ru.justtalk.ui.words

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class CreateGroupViewModel @Inject constructor() : BaseViewModel() {
    val photoChooseSuccessLiveData = MutableLiveData<Response<String>>()
    val createGroupFinishLiveData = MutableLiveData<Response<Intent>>()
    private val resultIntent: Intent = Intent()

    fun onPhotoChooseResult(requestCode: Int, data: Intent?) {
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                val selectedImage = data?.data.toString()
                resultIntent.putExtra(ARG_IMAGE_URL, selectedImage)
                photoChooseSuccessLiveData.value = Response.success(selectedImage)
            }
        }
    }

    fun createGroupFinish(groupName: String) {
        resultIntent.putExtra(ARG_GROUP_NAME, groupName)
        createGroupFinishLiveData.value = Response.success(resultIntent)
    }
}
