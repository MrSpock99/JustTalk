package itis.ru.justtalk.repository

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.ChatUser
import itis.ru.justtalk.models.Message
import itis.ru.justtalk.ui.people.NO_CHAT_ID
import javax.inject.Inject

const val CONTACTS: String = "contacts"
const val USER_CONTACTS: String = "user_contacts"
const val MESSAGES: String = "messages"
const val CHATS: String = "chats"
const val USER_CHATS: String = "user_chats"
const val CHAT_MESSAGES: String = "chat_messages"
const val SENT_AT = "sentAt"

class ChatRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ChatRepository {

    override fun getUser(uid: String): Single<ChatUser> {
        return Single.create { emitter ->
            db.collection(USERS).document(uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user: ChatUser = task.result?.toObject(ChatUser::class.java) as ChatUser
                        user.uid = uid
                        emitter.onSuccess(user)
                    } else {
                        emitter.onError(task.exception ?: Exception("error getting ChatUser"))
                    }
                }
        }
    }

    override fun addToContacts(userFromUid: String, userTo: ChatUser): Completable {
        return Completable.create { emitter ->
            db.collection(CONTACTS)
                .document(userFromUid)
                .collection(USER_CONTACTS)
                .document(userTo.uid)
                .set(userTo, SetOptions.merge())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(task.exception ?: Exception("error adding to contacts"))
                    }
                }
        }
    }

    override fun sendMessage(
        userFrom: ChatUser,
        userTo: ChatUser,
        immutableChatId: String,
        message: Message
    ): Completable {
        return Completable.create { emitter ->
            var chatId = ""
            if (immutableChatId == NO_CHAT_ID) {
                chatId = db.collection(MESSAGES).document().id
                for ((key, _) in userFrom.chats) {
                    if (userTo.chats.contains(key)) {
                        chatId = key
                    }
                }
            }

            userFrom.chats[chatId] = true
            db.collection(USERS).document(userFrom.uid).set(userFrom, SetOptions.merge())
            db.collection(CONTACTS).document(userTo.uid)
                .collection(USER_CONTACTS).document(userFrom.uid)
                .set(userFrom, SetOptions.merge())
            db.collection(CHATS).document(userTo.uid)
                .collection(USER_CHATS)
                .document(chatId).set(userFrom, SetOptions.merge())

            userTo.chats[chatId] = true
            db.collection(USERS).document(userTo.uid).set(userTo, SetOptions.merge())
            db.collection(CONTACTS).document(userFrom.uid)
                .collection(USER_CONTACTS).document(userTo.uid)
                .set(userTo, SetOptions.merge())
            db.collection(CHATS).document(userFrom.uid)
                .collection(USER_CHATS)
                .document(chatId).set(userTo, SetOptions.merge())

            db.collection(MESSAGES).document(chatId)
                .collection(CHAT_MESSAGES)
                .add(message)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(task.exception ?: Exception("error sending message"))
                    }
                }
        }
    }

    override fun getMessages(chatId: String): Single<FirestoreRecyclerOptions<Message>> {
        return Single.create { emitter ->
            val query = db.collection(MESSAGES).document(chatId).collection(CHAT_MESSAGES)
                .orderBy(SENT_AT, Query.Direction.ASCENDING)
            val options =
                FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message::class.java)
                    .build()
            query.addSnapshotListener { _, e ->
                if (e != null) {
                    emitter.onError(e)
                }
                emitter.onSuccess(options)
            }
        }
    }
}
