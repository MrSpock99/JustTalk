package itis.ru.justtalk.interactor

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.models.ChatUser
import itis.ru.justtalk.repository.ChatRepository
import javax.inject.Inject

class ChatInteractor @Inject constructor(private val chatRepository: ChatRepository) {
    fun getUser(uid: String): Single<ChatUser> {
        return chatRepository.getUser(uid)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addToContacts(userFromUid: String, userTo: ChatUser): Completable {
        return chatRepository.addToContacts(userFromUid, userTo)
    }
}
