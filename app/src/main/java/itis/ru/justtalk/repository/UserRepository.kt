package itis.ru.justtalk.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.User

interface UserRepository {
    fun login(account: GoogleSignInAccount): Completable
    fun addUserToDb(user: User): Completable
    fun getUserFromDb(firebaseUser: FirebaseUser): Single<User>
    fun getMyProfile(): Single<User>
    fun getEmptyUser(): Single<User>
    fun getUsers(limit: Long): Single<List<User>>
}
