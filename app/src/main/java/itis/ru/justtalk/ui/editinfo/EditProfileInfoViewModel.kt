package itis.ru.justtalk.ui.editinfo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import itis.ru.justtalk.interactor.myprofile.MyProfileInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import javax.inject.Inject

class EditProfileInfoViewModel @Inject constructor(
    private val interactor: MyProfileInteractor
) : ViewModel() {

    val myProfileLiveData = MutableLiveData<User>()
    val showLoadingLiveData = MutableLiveData<Boolean>()

    fun getMyProfile(bundleArgs: Bundle?) {
        if (bundleArgs?.getSerializable(MyProfileFragment.ARG_USER) != null) {
            myProfileLiveData.value = bundleArgs.getSerializable(MyProfileFragment.ARG_USER) as User
        } else {
            interactor.getEmptyUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    myProfileLiveData.value = it
                }
        }
    }
}
