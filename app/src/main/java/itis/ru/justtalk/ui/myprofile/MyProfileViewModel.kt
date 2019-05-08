package itis.ru.justtalk.ui.myprofile

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.myprofile.MyProfileInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.ClickEvent
import javax.inject.Inject

class MyProfileViewModel @Inject constructor(
    private val interactor: MyProfileInteractor
) : BaseViewModel() {

    val myProfileLiveData = MutableLiveData<User>()
    val navigateToEdit = MutableLiveData<ClickEvent<User>>()

    fun getMyProfile() {
        showLoadingLiveData.value = true
        disposables.add(
            interactor.getMyProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    myProfileLiveData.value = it
                    showLoadingLiveData.value = false
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun editProfileClick() {
        myProfileLiveData.value?.let {
            navigateToEdit.value = ClickEvent(it)
        }
    }
}
