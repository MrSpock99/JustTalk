package itis.ru.justtalk.ui.editinfo

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.interactor.myprofile.MyProfileInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import javax.inject.Inject

class EditProfileInfoViewModel @Inject constructor(
    private val interactor: MyProfileInteractor
) : BaseViewModel() {

    val myProfileLiveData = MutableLiveData<User>()
    val editProfileSuccessLiveData = MutableLiveData<Boolean>()

    fun getMyProfile(bundleArgs: Bundle?) {
        if (bundleArgs?.getParcelable<User>(MyProfileFragment.ARG_USER) != null) {
            myProfileLiveData.value = bundleArgs.getParcelable(MyProfileFragment.ARG_USER)
        } else {
            interactor.getEmptyUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    myProfileLiveData.value = it
                }
        }
    }

    fun editProfileInfo(user: User) {
        showLoadingLiveData.value = true
        disposables.add(
            interactor.editUserInfo(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showLoadingLiveData.value = false
                    editProfileSuccessLiveData.value = true
                },{
                    editProfileSuccessLiveData.value = false
                })
        )
    }
}
