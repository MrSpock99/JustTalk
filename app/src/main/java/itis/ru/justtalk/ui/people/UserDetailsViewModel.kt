package itis.ru.justtalk.ui.people

import androidx.lifecycle.MutableLiveData
import android.os.Bundle
import itis.ru.justtalk.interactor.MyProfileInteractor
import itis.ru.justtalk.models.user.User
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class UserDetailsViewModel @Inject constructor(
    private val myProfileInteractor: MyProfileInteractor
) : BaseViewModel() {
    val userLiveData = MutableLiveData<User>()
    val myProfileLiveData = MutableLiveData<Response<User>>()

    fun getUserInfo(bundle: Bundle?) {
        val user = (bundle?.getParcelable(MyProfileFragment.ARG_USER) as User)
        userLiveData.value = user
        setDistanceToUser()
    }

    private fun setDistanceToUser() {
        disposables.add(
            myProfileInteractor.getMyProfile()
                .subscribe({
                    myProfileLiveData.value = Response.success(it)
                }, {
                    myProfileLiveData.value = Response.error(it)
                    it.printStackTrace()
                })
        )
    }
}
