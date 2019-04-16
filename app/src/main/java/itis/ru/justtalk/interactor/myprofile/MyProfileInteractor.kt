package itis.ru.justtalk.interactor.myprofile

import com.google.firebase.auth.FirebaseUser
import itis.ru.justtalk.repository.UserRepository
import javax.inject.Inject
import kotlin.reflect.KFunction0

class MyProfileInteractor @Inject constructor(
    private val mUserRepository: UserRepository
){
    fun getUserInfo(firebaseUser: FirebaseUser) {
        mUserRepository.getUserFromDb(firebaseUser)
    }
}