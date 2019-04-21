package itis.ru.justtalk.ui.editinfo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import itis.ru.justtalk.interactor.myprofile.MyProfileInteractor
import itis.ru.justtalk.models.User
import javax.inject.Inject

class EditProfileInfoViewModel @Inject constructor(
    private val interactor: MyProfileInteractor
) : ViewModel() {

    val myProfileLiveData = MutableLiveData<User>()
    val showLoadingLiveData = MutableLiveData<Boolean>()

    fun getMyProfile(bundleArgs: Bundle?) {
        myProfileLiveData.value = bundleArgs?.getSerializable("user") as User
    }
}