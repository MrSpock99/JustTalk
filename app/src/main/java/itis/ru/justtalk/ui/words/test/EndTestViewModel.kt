package itis.ru.justtalk.ui.words.test

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import itis.ru.justtalk.models.utils.EndTestModel
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class EndTestViewModel @Inject constructor() : BaseViewModel() {
    val testResultLiveData = MutableLiveData<Response<EndTestModel>>()

    fun getTestResult(bundle: Bundle?) {
        if (bundle != null) {
            val result =
                EndTestModel(bundle.getInt(ARG_LIST_SIZE), bundle.getInt(ARG_CORRECT_COUNT))
            testResultLiveData.value = Response.success(result)
        }
    }
}
