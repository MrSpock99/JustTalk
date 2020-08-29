package itis.ru.justtalk.repository

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.Message
import itis.ru.justtalk.models.user.ChatUser
import itis.ru.justtalk.models.user.RemoteChatUser

interface ChatRepository {
    fun getUser(uid: String): Single<RemoteChatUser>

    fun addToContacts(
        userFromUid: ChatUser,
        userTo: ChatUser,
        immutableChatId: String
    ): Single<String>

    fun sendMessage(
        userFrom: ChatUser,
        userTo: ChatUser,
        immutableChatId: String,
        message: Message
    ): Completable

    fun getMessages(chatIdImmutable: String): Single<FirestoreRecyclerOptions<Message>>

    fun getContacts(userFromUid: String): Single<Pair<MutableList<RemoteChatUser>, MutableList<String>>>
}
