package itis.ru.justtalk.ui.myprofile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.justtalk.interactor.myprofile.MyProfileInteractor
import itis.ru.justtalk.models.User
import javax.inject.Inject

class MyProfileViewModel @Inject constructor(
    private val interactor: MyProfileInteractor
) : ViewModel() {

    val myProfileLiveData = MutableLiveData<User>()
    val showLoadingLiveData = MutableLiveData<Boolean>()
    val navigateToEdit = MutableLiveData<User>()

    private val disposables = CompositeDisposable()

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
            navigateToEdit.value = it
        }
    }
}
