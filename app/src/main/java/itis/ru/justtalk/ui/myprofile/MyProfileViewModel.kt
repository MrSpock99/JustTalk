package itis.ru.justtalk.ui.myprofile

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.MyProfileInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.ClickEvent
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class MyProfileViewModel @Inject constructor(
    private val interactor: MyProfileInteractor
) : BaseViewModel() {

    val myProfileLiveData = MutableLiveData<Response<User>>()
    val navigateToEdit = MutableLiveData<ClickEvent<User>>()

    fun getMyProfile() {
        showLoadingLiveData.value = true
        disposables.add(
            interactor.getMyProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    showLoadingLiveData.value = false
                }
                .subscribe({
                    myProfileLiveData.value = Response.success(it)
                }, {
                    myProfileLiveData.value = Response.error(it)
                    it.printStackTrace()
                })
        )
    }

    fun editProfileClick() {
        myProfileLiveData.value?.let {
            if (it.data != null)
                navigateToEdit.value = ClickEvent(it.data)
        }
    }
}
