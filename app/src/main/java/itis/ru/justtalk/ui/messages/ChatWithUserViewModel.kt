package itis.ru.justtalk.ui.messages

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.ChatInteractor
import itis.ru.justtalk.interactor.MyProfileInteractor
import itis.ru.justtalk.models.ChatUser
import itis.ru.justtalk.models.Message
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.ui.people.ARG_USER_UID
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class ChatWithUserViewModel @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val myProfileInteractor: MyProfileInteractor
) : BaseViewModel() {

    val startChatSuccessLiveData = MutableLiveData<Response<Boolean>>()
    val sendMessageSuccessLiveData = MutableLiveData<Response<Boolean>>()

    private lateinit var userTo: ChatUser
    private lateinit var myUid: String

    fun startChat(arguments: Bundle?) {
        val userToUid = arguments?.getString(ARG_USER_UID).toString()
        disposables.add(
            myProfileInteractor
                .getMyUid()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ myUid ->
                    this.myUid = myUid
                    chatInteractor
                        .getUser(userToUid)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ chatUser ->
                            userTo = chatUser
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

    fun sendMessage(chatId: String, textMessage: String) {
        disposables.add(
            chatInteractor.getUser(myUid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userFrom ->
                    chatInteractor.sendMessage(
                        userFrom,
                        userTo,
                        chatId,
                        Message(textMessage, userFrom.uid)
                    ).observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            sendMessageSuccessLiveData.value = Response.success(true)
                        }, { error ->
                            sendMessageSuccessLiveData.value = Response.error(error)
                            error.printStackTrace()
                        })

                }, { error ->
                    sendMessageSuccessLiveData.value = Response.error(error)
                    error.printStackTrace()
                })
        )
    }
}
