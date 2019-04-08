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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import itis.ru.justtalk.R
import itis.ru.justtalk.interactor.login.LoginInteractor
import itis.ru.justtalk.repository.UserRepositoryImpl
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.people.PeopleFragment
import itis.ru.justtalk.utils.LoginState
import itis.ru.justtalk.utils.ScreenState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel
    private var mGoogleApiClient: GoogleApiClient? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false);
        init(rootView)
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                result.signInAccount?.let { viewModel.onSignInClick(it) }
            }
        }
    }

    private fun init(view: View) {
        viewModel = ViewModelProviders.of(
                this,
                LoginViewModelFactory(LoginInteractor(UserRepositoryImpl(FirebaseAuth.getInstance(),
                        FirebaseFirestore.getInstance())))
        )[LoginViewModel::class.java]
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

    private fun updateUI(screenState: ScreenState<LoginState>?) {
        when (screenState) {
            ScreenState.Loading -> showLoading(true)
            is ScreenState.Render -> processLoginState(screenState.renderState)
        }
    }

    private fun processLoginState(renderState: LoginState) {
        showLoading(false)
        when (renderState) {
            LoginState.Success -> (activity as MainActivity).navigateTo(PeopleFragment())
            LoginState.Error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            activity?.pb_main?.visibility = View.VISIBLE
        } else {
            activity?.pb_main?.visibility = View.GONE
        }
    }

    private fun openGoogleActivity(googleApiClient: GoogleApiClient) {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        fun newInstance() = LoginFragment()
    }
}
