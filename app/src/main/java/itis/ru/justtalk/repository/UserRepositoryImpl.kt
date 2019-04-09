package itis.ru.justtalk.repository

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe

private const val USER_NAME = "name"
private const val USER_AGE = "age"
private const val USER_GENDER = "gender"
private const val USER_LOCATION = "location"
private const val USERS = "users"

class UserRepositoryImpl(private val mFirebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) : UserRepository {

    override fun login(account: GoogleSignInAccount): Maybe<AuthResult> {
        /*return Single.create { emitter: Compleatable ->
            mFirebaseAuth.signInWithCredential(
                GoogleAuthProvider.getCredential(
                    account.idToken,
                    null
                )
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onSuccess()
                } else {
                    emitter.onError()
                }
            }
        }
        */return RxFirebaseAuth.signInWithCredential(
                mFirebaseAuth,
                GoogleAuthProvider.getCredential(account.idToken, null)
        )
    }

    override fun addUserToDb(age: Int, gender: String, location: HashMap<String, Double>) {
        val userMap = HashMap<String, Any>()
        userMap[USER_NAME] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        userMap[USER_AGE] = age
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
}
