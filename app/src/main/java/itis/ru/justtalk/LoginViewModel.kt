package itis.ru.justtalk

import android.arch.lifecycle.ViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

class LoginViewModel(private val mGoogleApiClient: GoogleApiClient) : ViewModel(),
    GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun firebaseGoogleAuth() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        /* val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
             .requestEmail()
             .build()
         mGoogleApiClient = GoogleApiClient.Builder(this)
             .enableAutoManage(this *//* FragmentActivity *//*, this *//* OnConnectionFailedListener *//*)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()*/
    }
}
