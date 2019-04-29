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
import itis.ru.justtalk.models.User
import javax.inject.Inject

private const val USER_NAME = "name"
private const val USER_AGE = "age"
private const val USER_GENDER = "gender"
private const val USER_ABOUT_ME = "about_me"
private const val USER_LOCATION = "location"
private const val USER_AVATAR_URL = "avatar_url"
private const val USERS = "users"

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
        /* return RxFirebaseAuth.signInWithCredential(
                 mFirebaseAuth,
                 GoogleAuthProvider.getCredential(account.idToken, null)
         )*/
    }

    override fun addUserToDb(user: User): Completable {
        return Completable.create {emitter ->
            val userMap = HashMap<String, Any>()
            userMap[USER_NAME] = user.name
            userMap[USER_AGE] = user.age
            userMap[USER_ABOUT_ME] = user.aboutMe
            userMap[USER_AVATAR_URL] = user.avatarUrl
            userMap[USER_GENDER] = user.gender
            userMap[USER_LOCATION] = user.location
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

    override fun getUserFromDb(firebaseUser: FirebaseUser): Single<User> {
        return Single.create { emitter ->
            db.collection(USERS)
                .document(firebaseUser.email ?: "")
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        //val user = result?.toObject(User::class.java)
                        emitter.onSuccess(
                            result?.toObject(User::class.java) ?: User(
                                "",
                                0,
                                "",
                                "",
                                ArrayList(),
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

    override fun getMyProfile(): Single<User> {
        return firebaseAuth.currentUser?.let {
            getUserFromDb(it)
        } ?: Single.error(Exception("user not exists"))
    }

    override fun getEmptyUser(): Single<User> {
        return Single.create { emitter ->
            firebaseAuth.currentUser?.let {
                val user =
                    User(
                        it.displayName ?: "",
                        0,
                        "",
                        it.photoUrl.toString(),
                        arrayListOf("", "", "", "", ""),
                        "",
                        GeoPoint(0.0, 0.0)
                    )
                emitter.onSuccess(user)
            }
        }
    }
}
