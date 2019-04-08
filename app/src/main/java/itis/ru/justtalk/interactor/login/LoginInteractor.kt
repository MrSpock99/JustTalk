package itis.ru.justtalk.interactor.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.rxkotlin.subscribeBy
import itis.ru.justtalk.repository.UserRepository

class LoginInteractor(private val mUserRepository: UserRepository) {
    interface OnLoginFinishedListener {
        fun onSuccess()
        fun onError()
    }

    fun login(account: GoogleSignInAccount, listener: OnLoginFinishedListener) {
        mUserRepository.login(account)
                .subscribeBy(
                        onSuccess = { listener.onSuccess() },
                        onError = { listener.onError() })
        /* mFirebaseAuth = FirebaseAuth.getInstance()
         val credential = GoogleAuthProvider.getCredential(account.idToken, null)
         mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
             if (task.isSuccessful) {
                 listener.onSuccess()
             } else {
                 listener.onError()
             }
         }*/
    }

    fun addUserToDb(age: Int, gender: String, location: HashMap<String, Double>) {
        mUserRepository.addUserToDb(age, gender, location)
    }
}
