package itis.ru.justtalk.repository

import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.ChatUser

interface ChatRepository {
    fun getUser(uid: String): Single<ChatUser>
    fun addToContacts(userFromUid: String, userTo: ChatUser): Completable
}