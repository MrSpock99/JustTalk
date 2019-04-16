package itis.ru.justtalk.ui.myprofile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import itis.ru.justtalk.interactor.myprofile.MyProfileInteractor
import itis.ru.justtalk.utils.MyProfileState
import itis.ru.justtalk.utils.ScreenState
import java.net.URL
import javax.inject.Inject

class MyProfileViewModel @Inject constructor(
    private val mMyProfileInteractor: MyProfileInteractor,
    private val mFirebaseAuth: FirebaseAuth
) : ViewModel() {

    val mMyProfileState: LiveData<ScreenState<MyProfileState>>
        get() {
            if (!::myProfileState.isInitialized) {
                myProfileState = MutableLiveData()
                myProfileState.value = ScreenState.Loading
                mFirebaseAuth.currentUser?.let { mMyProfileInteractor.getUserInfo(it) }
            }
            return myProfileState
        }

    private lateinit var myProfileState: MutableLiveData<ScreenState<MyProfileState>>


    private fun onUserInfoLoaded(userName: String, userAvatar: URL) {
        myProfileState.value = ScreenState.Render(MyProfileState.ShowUserInfo(userName, userAvatar))
    }

}
