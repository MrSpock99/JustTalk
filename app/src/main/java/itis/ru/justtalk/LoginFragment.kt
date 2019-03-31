package itis.ru.justtalk

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    override fun onConnected(p0: Bundle?) {
        Log.d("MYLOG", p0.toString())
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d("MYLOG", p0.toString())
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("MYLOG", p0.toString())
    }

    private val RC_SIGN_IN = 9001
    private var mProgressDialog: ProgressDialog? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mFirebaseAuth: FirebaseAuth? = null

    companion object {
        fun newInstance() = LoginFragment(

        )
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false);
        init(rootView)
        return rootView
    }

    override fun onPause() {
        super.onPause()
        activity?.let { mGoogleApiClient?.stopAutoManage(it) }
        mGoogleApiClient?.disconnect()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun init(view: View) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("499949272101-vmf6l1ib6td62v1g6s1qdpr1q9rqq5dl.apps.googleusercontent.com")
            .requestEmail()
            .build()
        if (mGoogleApiClient == null || mGoogleApiClient?.isConnected == false) {
            mGoogleApiClient = activity?.let {
                GoogleApiClient.Builder(it)
                    .enableAutoManage(it, this)
                    .addConnectionCallbacks(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build()
            }
        }

        view.btn_login.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(activity)
            mProgressDialog?.setMessage("Loading")
            mProgressDialog?.isIndeterminate = true
        }

        mProgressDialog?.show()
    }

    private fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog?.isShowing == true) {
            mProgressDialog?.hide()
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d("MYLOG", "handleSignInResult:" + result.status)
        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount
            acct?.let { authWithGoogle(it) }
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private fun authWithGoogle(account: GoogleSignInAccount) {
        mFirebaseAuth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mFirebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Auth OK", Toast.LENGTH_SHORT).show()
                //startActivity(Intent(activity, MainActivity::class.java))
                //finish()
            } else {
                Toast.makeText(activity, "Auth Error ${task.exception}", Toast.LENGTH_SHORT).show()
                Log.d("MYLOG", "Auth Error ${task.exception}")

            }
        }
    }
}
