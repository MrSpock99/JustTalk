package itis.ru.justtalk.interactor.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.repository.UserRepository
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val userRepository: UserRepository
) {

    fun login(account: GoogleSignInAccount): Completable {
        return userRepository.login(account)
            .subscribeOn(Schedulers.io())
    }

    fun addUserToDb(age: Int, gender: String, location: HashMap<String, Double>) {
        //userRepository.addUserToDb(age, gender, location)
    }
}
