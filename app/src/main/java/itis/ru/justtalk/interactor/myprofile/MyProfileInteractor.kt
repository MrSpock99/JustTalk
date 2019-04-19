package itis.ru.justtalk.interactor.myprofile

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.models.AppUser
import itis.ru.justtalk.repository.UserRepository
import javax.inject.Inject

class MyProfileInteractor @Inject constructor(
    private val mUserRepository: UserRepository
) {
    fun getUserInfo(firebaseUser: FirebaseUser): Single<AppUser> {
        return mUserRepository.getUserFromDb(firebaseUser)
            .subscribeOn(Schedulers.io())
    }

    fun getMyProfile(): Single<AppUser> {
        return mUserRepository.getMyProfile()
            .subscribeOn(Schedulers.io())
    }
}
