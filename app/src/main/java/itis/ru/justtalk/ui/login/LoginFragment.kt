package itis.ru.justtalk.ui.login

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import itis.ru.justtalk.R
import itis.ru.justtalk.di.component.DaggerMainComponent
import itis.ru.justtalk.di.module.AppModule
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.utils.LoginState
import itis.ru.justtalk.utils.ScreenState
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.view.*
import javax.inject.Inject

class LoginFragment : Fragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: LoginViewModel
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var rootActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        injectDependencies()
        init(rootView)
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onGoogleIntentResult(requestCode, data)
    }

    private fun init(view: View) {
        rootActivity = activity as MainActivity

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(LoginViewModel::class.java)
        viewModel.loginState.observe(::getLifecycle, ::updateUI)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_api_token))
            .requestEmail()
            .build()

        mGoogleApiClient = activity?.let {
            GoogleApiClient.Builder(it)
                .enableAutoManage(it, viewModel)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        }

        view.btn_login.setOnClickListener {
            mGoogleApiClient?.let { it1 -> openGoogleActivity(it1) }
        }
    }

    private fun injectDependencies() {
        val component = DaggerMainComponent.builder()
            .appModule(AppModule())
            .build()
        component.inject(this)
    }

    private fun updateUI(screenState: ScreenState<LoginState>?) {
        when (screenState) {
            ScreenState.Loading -> rootActivity.showLoading(true)
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: LoginState) {
        rootActivity.showLoading(false)
        when (renderState) {
            LoginState.Success -> (activity as MainActivity).navigateTo(
                EditProfileInfoFragment(),
                null
            )
            LoginState.Error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGoogleActivity(googleApiClient: GoogleApiClient) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, LoginViewModel.REQUEST_AUTH)
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
