package itis.ru.justtalk.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Completable

interface UserRepository {
    fun login(account: GoogleSignInAccount): Completable
    fun addUserToDb(age: Int, gender: String, location: HashMap<String, Double>)
}
