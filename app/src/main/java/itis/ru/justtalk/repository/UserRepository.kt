package itis.ru.justtalk.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable

interface UserRepository {
    fun login(account: GoogleSignInAccount): Completable
    fun addUserToDb(age: Int, gender: String, location: HashMap<String, Double>)
    fun getUserFromDb(firebaseUser: FirebaseUser)
}
