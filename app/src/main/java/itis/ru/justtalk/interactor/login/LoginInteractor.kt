package itis.ru.justtalk.interactor.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.repository.UserRepository

class LoginInteractor /*@Inject*/ constructor(
    private val mUserRepository: UserRepository
) {

    fun login(account: GoogleSignInAccount): Completable {
        return mUserRepository.login(account)
            .subscribeOn(Schedulers.io())
    }

    fun addUserToDb(age: Int, gender: String, location: HashMap<String, Double>) {
        mUserRepository.addUserToDb(age, gender, location)
    }
}
