package itis.ru.justtalk.repository

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.Message
import itis.ru.justtalk.models.user.ChatUser
import itis.ru.justtalk.models.utils.ContactsAndChats

interface ChatRepository {
    fun getUser(uid: String): Single<ChatUser>

    fun addToContacts(userFromUid: String, userTo: ChatUser): Completable

    fun sendMessage(
        userFrom: ChatUser,
        userTo: ChatUser,
        immutableChatId: String,
        message: Message
    ): Completable

    fun getMessages(chatId: String): Single<FirestoreRecyclerOptions<Message>>

    fun getContacts(userFromUid: String): Single<ContactsAndChats>
}
