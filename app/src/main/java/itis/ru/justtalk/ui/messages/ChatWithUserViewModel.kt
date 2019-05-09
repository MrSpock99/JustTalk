package itis.ru.justtalk.ui.messages

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.ChatInteractor
import itis.ru.justtalk.interactor.MyProfileInteractor
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.people.ARG_USER_UID
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class ChatWithUserViewModel @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val myProfileInteractor: MyProfileInteractor
) : BaseViewModel() {

    val startChatSuccessLiveData = MutableLiveData<Response<Boolean>>()

    fun startChat(arguments: Bundle?) {
        val userToUid = arguments?.getString(ARG_USER_UID).toString()
        disposables.add(
            myProfileInteractor
                .getMyUid()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ myUid ->
                    chatInteractor
                        .getUser(userToUid)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ chatUser ->
                            chatInteractor.addToContacts(myUid, chatUser)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    startChatSuccessLiveData.value = Response.success(true)
                                }, { error ->
                                    startChatSuccessLiveData.value = Response.error(error)
                                    error.printStackTrace()
                                })
                        }, { error ->
                            startChatSuccessLiveData.value = Response.error(error)
                            error.printStackTrace()
                        })
                }, { error ->
                    startChatSuccessLiveData.value = Response.error(error)
                    error.printStackTrace()
                })
        )
    }
}
