package itis.ru.justtalk.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import itis.ru.justtalk.interactor.login.LoginInteractor
import itis.ru.justtalk.utils.LoginState
import itis.ru.justtalk.utils.ScreenState

class LoginViewModel(private val loginInteractor: LoginInteractor) : ViewModel(),
        GoogleApiClient.OnConnectionFailedListener, LoginInteractor.OnLoginFinishedListener {

    private val mLoginState: MutableLiveData<ScreenState<LoginState>> = MutableLiveData()
    val loginState: LiveData<ScreenState<LoginState>>
        get() = mLoginState

    fun onSignInClick(account: GoogleSignInAccount) {
        mLoginState.value = ScreenState.Loading
        loginInteractor.login(account, this)
    }

    override fun onSuccess() {
        mLoginState.value = ScreenState.Render(LoginState.Success)
    }

    override fun onError() {
        mLoginState.value = ScreenState.Render(LoginState.Error)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        mLoginState.value = ScreenState.Render(LoginState.Error)
    }
}

class LoginViewModelFactory(private val loginInteractor: LoginInteractor) :
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(loginInteractor) as T
    }
}
