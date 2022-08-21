package itis.ru.justtalk.ui.main

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel() {

    val isLoginedLiveData = MutableLiveData<Response<Boolean>>()

    fun checkIsLogined() {
        if (firebaseAuth.currentUser != null) {
            isLoginedLiveData.value = Response.success(true)
        } else {
            isLoginedLiveData.value = Response.success(false)
        }
    }
}
