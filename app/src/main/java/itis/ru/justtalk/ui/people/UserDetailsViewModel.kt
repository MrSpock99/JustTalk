package itis.ru.justtalk.ui.people

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import itis.ru.justtalk.interactor.myprofile.MyProfileInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(
    private val myProfileInteractor: MyProfileInteractor
) : BaseViewModel() {
    val userLiveData = MutableLiveData<User>()
    val myProfileLiveData = MutableLiveData<User>()

    fun getUserInfo(bundle: Bundle?) {
        val user = (bundle?.getParcelable(MyProfileFragment.ARG_USER) as User)
        userLiveData.value = user
        setDistanceToUser()
    }

    private fun setDistanceToUser() {
        disposables.add(
            myProfileInteractor.getMyProfile()
                .subscribe({
                    myProfileLiveData.value = it
                }, {})
        )
    }
}
