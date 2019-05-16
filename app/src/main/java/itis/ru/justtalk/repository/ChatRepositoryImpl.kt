package itis.ru.justtalk.repository

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.models.Message
import itis.ru.justtalk.models.user.ChatUser
import itis.ru.justtalk.models.user.RemoteChatUser
import itis.ru.justtalk.ui.people.NO_CHAT_ID
import javax.inject.Inject

const val CONTACTS: String = "contacts"
const val USER_CONTACTS: String = "user_contacts"
const val MESSAGES: String = "messages"
const val CHATS: String = "chats"
const val USER_CHATS: String = "user_chats"
const val CHAT_MESSAGES: String = "chat_messages"
const val SENT_AT = "sentAt"
const val MESSAGE_TEXT = "messageText"

class ChatRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ChatRepository {

    override fun getUser(uid: String): Single<RemoteChatUser> {
        return Single.create { emitter ->
            db.collection(USERS).document(uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userRemote: RemoteChatUser =
                            task.result?.toObject(RemoteChatUser::class.java) as RemoteChatUser
                        userRemote.uid = uid
                        emitter.onSuccess(userRemote)
                    } else {
                        emitter.onError(task.exception ?: Exception("error getting RemoteChatUser"))
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
            var chatId: String
            if (immutableChatId == NO_CHAT_ID) {
                chatId = db.collection(MESSAGES).document().id
                for ((key, _) in userFrom.chats) {
                    if (userTo.chats.contains(key)) {
                        chatId = key
                    }
                }
            } else {
                chatId = immutableChatId
            }

            userFrom.chats[chatId] = true
            db.collection(USERS)
                .document(userFrom.uid)
                .set(userFrom, SetOptions.merge())

            db.collection(CONTACTS)
                .document(userTo.uid)
                .collection(USER_CONTACTS)
                .document(userFrom.uid)
                .set(userFrom, SetOptions.merge())

            db.collection(CHATS)
                .document(userTo.uid)
                .collection(USER_CHATS)
                .document(chatId)
                .set(userFrom, SetOptions.merge())

            userTo.chats[chatId] = true
            db.collection(USERS)
                .document(userTo.uid)
                .set(userTo, SetOptions.merge())

            db.collection(CONTACTS)
                .document(userFrom.uid)
                .collection(USER_CONTACTS)
                .document(userTo.uid)
                .set(userTo, SetOptions.merge())

            db.collection(CHATS)
                .document(userFrom.uid)
                .collection(USER_CHATS)
                .document(chatId)
                .set(userTo, SetOptions.merge())

            db.collection(MESSAGES)
                .document(chatId)
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
            query.addSnapshotListener { snp, e ->
                if (e != null) {
                    emitter.onError(e)
                } else {
                    emitter.onSuccess(options)
                }

            }
        }
    }

    override fun getContacts(userFromUid: String): Single<Pair<MutableList<RemoteChatUser>, MutableList<String>>> {
        return Single.create { emitter ->
            db.collection(CHATS).document(userFromUid).collection(USER_CHATS)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val contactsList = mutableListOf<RemoteChatUser>()
                        val chatList = mutableListOf<String>()

                        task.result?.forEach { it ->
                            getLastMessageInChat(it.id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe({ message ->
                                    val user = it.toObject(RemoteChatUser::class.java)
                                    user.lastMessage = message
                                    contactsList.add(user)
                                    chatList.add(it.id)
                                    if (contactsList.size == task.result?.size()) {
                                        emitter.onSuccess(Pair(contactsList, chatList))
                                    }
                                }, { error ->
                                    emitter.onError(
                                        error ?: Exception("error getting last message")
                                    )
                                })
                        }
                    } else {
                        emitter.onError(
                            task.exception ?: Exception("error getting contacts")
                        )
                    }
                }
        }
    }

    private fun getLastMessageInChat(chatId: String): Single<String> {
        return Single.create { emitter ->
            val query = db.collection(MESSAGES).document(chatId).collection(CHAT_MESSAGES)
                .orderBy(SENT_AT, Query.Direction.DESCENDING)
            query.get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(it.result?.documents?.get(0)?.data?.get(MESSAGE_TEXT).toString())
                    } else {
                        emitter.onError(it.exception ?: Exception("error getting last message"))
                    }
                }
        }
    }
}
