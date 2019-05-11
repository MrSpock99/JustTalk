package itis.ru.justtalk.interactor

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.models.ChatUser
import itis.ru.justtalk.models.Message
import itis.ru.justtalk.repository.ChatRepository
import javax.inject.Inject

class ChatInteractor @Inject constructor(private val chatRepository: ChatRepository) {
    fun getUser(uid: String): Single<ChatUser> {
        return chatRepository.getUser(uid)
            .subscribeOn(Schedulers.io())
    }

    fun addToContacts(userFromUid: String, userTo: ChatUser): Completable {
        return chatRepository.addToContacts(userFromUid, userTo)
    }

    fun sendMessage(
        userFrom: ChatUser,
        userTo: ChatUser,
        chatId: String,
        message: Message
    ): Completable {
        return chatRepository.sendMessage(userFrom, userTo, chatId, message)
            .subscribeOn(Schedulers.io())
    }
}
