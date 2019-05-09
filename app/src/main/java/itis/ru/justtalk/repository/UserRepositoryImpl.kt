package itis.ru.justtalk.repository

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.RemoteUser
import itis.ru.justtalk.models.User
import javax.inject.Inject

const val UID = "name"
const val USER_NAME = "name"
const val USER_AGE = "age"
const val USER_GENDER = "gender"
const val USER_ABOUT_ME = "about_me"
const val USER_LOCATION = "location"
const val USER_AVATAR_URL = "avatar_url"
const val USER_LEARNING_LANGUAGE = "learning_language"
const val USER_LEARNING_LANGUAGE_LEVEL = "learning_language_level"
const val USER_SPEAKING_LANGUAGE = "speaking_language"
const val USER_SPEAKING_LANGUAGE_LEVEL = "speaking_language_level"
const val USERS = "users"

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {
    override fun login(account: GoogleSignInAccount): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithCredential(
                GoogleAuthProvider.getCredential(
                    account.idToken,
                    null
                )
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(task.exception ?: Exception(""))
                }
            }
        }
    }

    override fun addUserToDb(user: User): Completable {
        return Completable.create { emitter ->
            val userMap = HashMap<String, Any>()
            userMap[UID] = firebaseAuth.currentUser?.email as String
            userMap[USER_NAME] = user.name
            userMap[USER_AGE] = user.age
            userMap[USER_ABOUT_ME] = user.aboutMe
            userMap[USER_AVATAR_URL] = user.avatarUrl
            userMap[USER_GENDER] = user.gender
            userMap[USER_LOCATION] = user.location
            userMap[USER_LEARNING_LANGUAGE] = user.learningLanguage
            userMap[USER_LEARNING_LANGUAGE_LEVEL] = user.learningLanguageLevel
            userMap[USER_SPEAKING_LANGUAGE] = user.speakingLanguage
            userMap[USER_SPEAKING_LANGUAGE_LEVEL] = user.speakingLanguageLevel
            db.collection(USERS)
                .document(firebaseAuth.currentUser?.email ?: "")
                .set(userMap)
                .addOnSuccessListener {
                    emitter.onComplete()
                    //Log.d("MYLOG", it.toString())
                }.addOnFailureListener {
                    emitter.onError(it)
                    Log.d("MYLOG", it.message)
                }
        }
    }

    override fun getUserFromDb(firebaseUser: FirebaseUser): Single<RemoteUser> {
        return Single.create { emitter ->
            db.collection(USERS)
                .document(firebaseUser.email ?: "")
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        //val user = result?.toObject(RemoteUser::class.java)
                        emitter.onSuccess(
                            result?.toObject(RemoteUser::class.java) ?: RemoteUser(
                                "",
                                "",
                                0,
                                "",
                                "",
                                ArrayList(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                GeoPoint(0.0, 0.0)
                            )
                        )
                        Log.d("MYLOG", task.result?.data.toString())
                    } else {
                        emitter.onError(task.exception ?: Exception(""))
                    }
                }
        }
    }

    override fun getMyProfile(): Single<RemoteUser> {
        return firebaseAuth.currentUser?.let {
            getUserFromDb(it)
        } ?: Single.error(Exception("user not exists"))
    }

    override fun getEmptyUser(): Single<RemoteUser> {
        return Single.create { emitter ->
            firebaseAuth.currentUser?.let {
                val user =
                    RemoteUser(
                        "",
                        it.displayName ?: "",
                        0,
                        "",
                        it.photoUrl.toString(),
                        arrayListOf("", "", "", "", ""),
                        "",
                        "",
                        "",
                        "",
                        "",
                        GeoPoint(0.0, 0.0)
                    )
                emitter.onSuccess(user)
            }
        }
    }

    override fun getUsers(userLocation: GeoPoint, limit: Long): Single<List<RemoteUser>> {
        updateUserLocationInDb(userLocation)
        return Single.create { emitter ->
            db.collection(USERS)
/*
                .whereLessThanOrEqualTo("location", userLocation)
*/
                .limit(limit)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.toObjects(RemoteUser::class.java)
                            ?.let { emitter.onSuccess(it) }
                    } else {
                        emitter.onError(
                            task.exception ?: java.lang.Exception("error getting all users")
                        )
                    }
                }
        }
    }

    private fun updateUserLocationInDb(userLocation: GeoPoint) {
        val userMap = HashMap<String, Any>()
        userMap[USER_LOCATION] = userLocation
        db.collection(USERS)
            .document(firebaseAuth.currentUser?.email ?: "")
            .update(userMap)
    }

}
