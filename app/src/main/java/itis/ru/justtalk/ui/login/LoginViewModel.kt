package itis.ru.justtalk.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.login.LoginInteractor
import itis.ru.justtalk.utils.LoginState
import itis.ru.justtalk.utils.ScreenState
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor
) : ViewModel(), GoogleApiClient.OnConnectionFailedListener {

    val loginState: LiveData<ScreenState<LoginState>>
        get() = mLoginState

    private val mLoginState: MutableLiveData<ScreenState<LoginState>> = MutableLiveData()

    fun onGoogleIntentResult(requestCode: Int, data: Intent?) {
        if (requestCode == REQUEST_AUTH) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            result?.let { onSignInClick(it) }
        }
    }

    private fun onSignInClick(result: GoogleSignInResult) {
        if (result.isSuccess) {
            mLoginState.value = ScreenState.Loading
            result.signInAccount?.let { account ->
                loginInteractor.login(account)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mLoginState.value = ScreenState.Render(LoginState.Success)
                    }, {
                        mLoginState.value = ScreenState.Render(LoginState.Error)
                        it.printStackTrace()
                    })
            }
        } else {
            mLoginState.value = ScreenState.Render(LoginState.Error)
        }
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        mLoginState.value = ScreenState.Render(LoginState.Error)
    }

    companion object {
        const val REQUEST_AUTH = 9001
    }
}

/*
class LoginViewModelFactory */
/*@Inject*//*
 constructor(private val loginInteractor: LoginInteractor) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginInteractor) as T
    }
}
*/
