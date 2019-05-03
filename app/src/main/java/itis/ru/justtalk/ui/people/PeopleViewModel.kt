package itis.ru.justtalk.ui.people

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.PeopleInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.ClickEvent
import javax.inject.Inject

class PeopleViewModel @Inject constructor(
    private val interactor: PeopleInteractor
) : BaseViewModel() {
    val usersLiveData = MutableLiveData<User>()
    val showLoadingLiveData = MutableLiveData<Boolean>()
    val navigateToMessages = MutableLiveData<ClickEvent<User>>()

    fun getUsers(limit: Long) {
        disposables.add(
            interactor.getUsers(limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("MYLOG", it.toString())
                }, {
                    Log.d("MYLOG", it.toString())
                })
        )
    }
}