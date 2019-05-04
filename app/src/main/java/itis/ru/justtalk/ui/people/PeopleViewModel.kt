package itis.ru.justtalk.ui.people

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.PeopleInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.ClickEvent
import javax.inject.Inject

class PeopleViewModel @Inject constructor(
    private val interactor: PeopleInteractor
) : BaseViewModel() {
    val usersLiveData = MutableLiveData<List<User>>()
    val navigateToChat = MutableLiveData<ClickEvent<User?>>()

    fun getUsers(limit: Long) {
        showLoadingLiveData.value = true
        disposables.add(
            interactor.getUsers(limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showLoadingLiveData.value = false
                    usersLiveData.value = it
                }, {
                    showLoadingLiveData.value = false
                })
        )
    }

    fun onMessageClick(index: Int){
        navigateToChat.value = ClickEvent(usersLiveData.value?.get(index))
    }
}
