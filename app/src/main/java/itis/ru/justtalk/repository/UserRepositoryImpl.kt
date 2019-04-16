package itis.ru.justtalk.repository

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import io.reactivex.Completable
import javax.inject.Inject

private const val USER_NAME = "name"
private const val USER_AGE = "age"
private const val USER_GENDER = "gender"
private const val USER_LOCATION = "location"
private const val USER_AVATAR_URL = "avatar_url"
private const val USERS = "users"

class UserRepositoryImpl @Inject constructor(
    private val mFirebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {

    override fun login(account: GoogleSignInAccount): Completable {
        return Completable.create { emitter ->
            mFirebaseAuth.signInWithCredential(
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

    override fun addUserToDb(age: Int, gender: String, location: HashMap<String, Double>) {
        val userMap = HashMap<String, Any>()
        userMap[USER_NAME] = mFirebaseAuth.currentUser?.displayName.toString()
        userMap[USER_AGE] = age
        userMap[USER_AVATAR_URL] = mFirebaseAuth.currentUser?.photoUrl.toString()
        userMap[USER_GENDER] = gender
        userMap[USER_LOCATION] = GeoPoint(location["lat"] ?: 0.0, location["lon"] ?: 0.0)
        db.collection(USERS)
            .add(userMap)
            .addOnSuccessListener {
                Log.d("MYLOG", it.toString())
            }.addOnFailureListener {
                Log.d("MYLOG", it.message)
            }
    }

    override fun getUserFromDb(firebaseUser: FirebaseUser) {

    }

}
