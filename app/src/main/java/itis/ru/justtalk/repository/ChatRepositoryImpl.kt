package itis.ru.justtalk.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.ChatUser
import itis.ru.justtalk.models.Message
import javax.inject.Inject

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
            db.collection("contacts")
                .document(userFromUid)
                .collection("user_contacts")
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
            if (immutableChatId == "no_room_id") {
                chatId = db.collection("messages").document().id
                for ((key, _) in userFrom.chats) {
                    if (userTo.chats.contains(key)) {
                        chatId = key
                    }
                }
            }

            userFrom.chats[chatId] = true
            db.collection(USERS).document(userFrom.uid).set(userFrom, SetOptions.merge())
            db.collection("contacts").document(userTo.uid)
                .collection("user_contacts").document(userFrom.uid)
                .set(userFrom, SetOptions.merge())
            db.collection("chats").document(userTo.uid)
                .collection("user_chats")
                .document(chatId).set(userFrom, SetOptions.merge())

            userTo.chats[chatId] = true
            db.collection(USERS).document(userTo.uid).set(userTo, SetOptions.merge())
            db.collection("contacts").document(userFrom.uid)
                .collection("user_contacts").document(userTo.uid)
                .set(userTo, SetOptions.merge())
            db.collection("chats").document(userFrom.uid)
                .collection("user_chats")
                .document(chatId).set(userTo, SetOptions.merge())

            db.collection("messages").document(chatId)
                .collection("room_messages")
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
}
