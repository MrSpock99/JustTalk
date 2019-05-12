package itis.ru.justtalk.ui.messages

import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.ChatInteractor
import itis.ru.justtalk.interactor.MyProfileInteractor
import itis.ru.justtalk.models.ContactsAndChats
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val myProfileInteractor: MyProfileInteractor
) : BaseViewModel() {

    val contactsListLiveData = MutableLiveData<Response<ContactsAndChats>>()

    fun getContacts() {
        showLoadingLiveData.value = true
        disposables.add(
            myProfileInteractor.getMyUid()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ myUid ->
                    chatInteractor.getContacts(myUid)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally {
                            showLoadingLiveData.value = false
                        }
                        .subscribe({ contactsChats ->
                            contactsListLiveData.value = Response.success(contactsChats)
                        }, { error ->
                            contactsListLiveData.value = Response.error(error)
                            error.printStackTrace()
                        })
                }, { error ->
                    contactsListLiveData.value = Response.error(error)
                    error.printStackTrace()
                })
        )
    }
}
