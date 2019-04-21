package itis.ru.justtalk.interactor.myprofile

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.models.User
import itis.ru.justtalk.repository.UserRepository
import javax.inject.Inject

class MyProfileInteractor @Inject constructor(
    private val mUserRepository: UserRepository
) {
    fun getUserInfo(firebaseUser: FirebaseUser): Single<User> {
        return mUserRepository.getUserFromDb(firebaseUser)
            .subscribeOn(Schedulers.io())
    }

    fun getMyProfile(): Single<User> {
        return mUserRepository.getMyProfile()
            .subscribeOn(Schedulers.io())
    }
}
