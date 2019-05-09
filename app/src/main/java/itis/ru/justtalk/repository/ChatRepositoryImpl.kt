package itis.ru.justtalk.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.ChatUser
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
        return Completable.create{emitter ->

            db.collection("contacts")
                .document(userFromUid)
                .collection("user_contacts")
                .document(userTo.uid)
                .set(userTo, SetOptions.merge())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        emitter.onComplete()
                    }else{
                        emitter.onError(task.exception ?: Exception("error adding to contacts"))
                    }
                }
        }
    }

}