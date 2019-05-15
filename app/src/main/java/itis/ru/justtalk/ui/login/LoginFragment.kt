package itis.ru.justtalk.ui.login

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.utils.LoginState
import itis.ru.justtalk.utils.ScreenState
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.view.*
import javax.inject.Inject

class LoginFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: LoginViewModel
    @Inject
    lateinit var mGoogleApiClient: GoogleSignInClient

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
        setToolbarAndBottomNavVisibility(
            toolbarVisibility = View.GONE,
            bottomNavVisibility = View.GONE
        )

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(LoginViewModel::class.java)
        viewModel.loginState.observe(::getLifecycle, ::updateUI)

        view.btn_login.setOnClickListener {
            openGoogleActivity(mGoogleApiClient)
        }
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)

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
                EditProfileInfoFragment.toString(),
                null
            )
            LoginState.Error -> view?.let {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        }
    }

    private fun openGoogleActivity(googleSignInClient: GoogleSignInClient) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, LoginViewModel.REQUEST_AUTH)
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
