package itis.ru.justtalk.ui.login

import android.arch.lifecycle.ViewModel
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import itis.ru.justtalk.R

class LoginViewModel : ViewModel(),
        GoogleApiClient.OnConnectionFailedListener {
    private lateinit var mView: LoginFragment
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onConnectionFailed(p0: ConnectionResult) {}

    fun firebaseGoogleAuth(account: GoogleSignInAccount) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(mView.activity, "Auth OK", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mView.activity, "Auth Error ${task.exception}", Toast.LENGTH_SHORT).show()
                Log.d("MYLOG", "Auth Error ${task.exception}")
            }
        }
    }

    fun onSignInClick() {
        mGoogleApiClient?.let { mView.openGoogleActivity(it) }
    }

    fun init(view: LoginFragment) {
        mView = view

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(view.activity?.getString(R.string.google_api_token))
                .requestEmail()
                .build()

        mGoogleApiClient = view.activity?.let {
            GoogleApiClient.Builder(it)
                    .enableAutoManage(it, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build()
        }
    }
}
