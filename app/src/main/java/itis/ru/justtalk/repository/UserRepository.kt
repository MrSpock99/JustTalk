package itis.ru.justtalk.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import io.reactivex.Maybe

interface UserRepository {
    fun login(account: GoogleSignInAccount): Maybe<AuthResult>
    fun addUserToDb(age: Int, gender: String, location: HashMap<String, Double>)
}
