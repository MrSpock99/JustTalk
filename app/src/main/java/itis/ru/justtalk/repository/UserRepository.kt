package itis.ru.justtalk.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import io.reactivex.Completable
import io.reactivex.Single
import itis.ru.justtalk.models.RemoteUser
import itis.ru.justtalk.models.User

interface UserRepository {
    fun login(account: GoogleSignInAccount): Completable
    fun addUserToDb(user: User): Completable
    fun getUserFromDb(firebaseUser: FirebaseUser): Single<RemoteUser>
    fun getMyProfile(): Single<RemoteUser>
    fun getEmptyUser(): Single<RemoteUser>
    fun getUsers(userLocation: GeoPoint, limit: Long): Single<List<RemoteUser>>
}
