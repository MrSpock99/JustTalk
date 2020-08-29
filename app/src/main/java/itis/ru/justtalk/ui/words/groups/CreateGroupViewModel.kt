package itis.ru.justtalk.ui.words.groups

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import itis.ru.justtalk.models.db.Group
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.base.REQUEST_CODE
import itis.ru.justtalk.ui.words.words.ARG_AUTO_PHOTO
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class CreateGroupViewModel @Inject constructor() : BaseViewModel() {
    val photoChooseSuccessLiveData = MutableLiveData<Response<String>>()
    val createGroupFinishLiveData = MutableLiveData<Response<Intent>>()
    val editGroupLiveData = MutableLiveData<Response<Group>>()
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

    fun createGroupFinish(groupName: String, autoPhoto: Boolean) {
        resultIntent.putExtra(ARG_GROUP_NAME, groupName)
        resultIntent.putExtra(ARG_AUTO_PHOTO, autoPhoto)
        createGroupFinishLiveData.value = Response.success(resultIntent)
    }

    fun getArguments(intent: Intent?) {
        val requestCode = intent?.extras?.getInt(REQUEST_CODE, 0)
        when (requestCode) {
            REQ_CODE_EDIT_GROUP -> {
                resultIntent.putExtra(ARG_GROUP_ID, intent.getLongExtra(ARG_GROUP_ID, 0))
                resultIntent.putExtra(ARG_IMAGE_URL, intent.getStringExtra(ARG_IMAGE_URL))
                intent.extras?.let { extras ->
                    editGroupLiveData.value = Response.success(
                        Group(
                            id = extras.getLong(ARG_GROUP_ID),
                            name = extras.getString(ARG_GROUP_NAME),
                            imageUrl = extras.getString(ARG_IMAGE_URL)
                        )
                    )
                }
            }
        }
    }
}
