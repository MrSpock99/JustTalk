package itis.ru.justtalk.interactor.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginInteractor {
    private lateinit var mFirebaseAuth: FirebaseAuth

    interface OnLoginFinishedListener {
        fun onSuccess()
        fun onError()
    }

    fun login(account: GoogleSignInAccount, listener: OnLoginFinishedListener) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                listener.onSuccess()
            } else {
                listener.onError()
            }
        }
    }
}
