package itis.ru.justtalk.repository

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Maybe

class UserRepositoryImpl(private val mFirebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) : UserRepository {

    override fun login(account: GoogleSignInAccount): Maybe<FirebaseUser> {
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
        ).map {
            //addUserToDb(account)
            it.user
            //добавить в бд
        }
    }

    override fun addUserToDb(age: Int, gender: String, location: HashMap<String, Double>) {
        val userMap = HashMap<String, Any>()
        userMap["name"] = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        userMap["age"] = age
        userMap["gender"] = gender
        userMap["location"] = GeoPoint(location["lat"] ?: 0.0, location["lon"] ?: 0.0)
        db.collection("users")
                .add(userMap)
                .addOnSuccessListener {
                    Log.d("MYLOG", it.toString())
                }.addOnFailureListener {
                    Log.d("MYLOG", it.message)
                }
    }

    private fun getUserAgeAndGender() {

    }

}
