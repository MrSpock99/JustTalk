package itis.ru.justtalk.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.ui.login.LoginFragment
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import itis.ru.justtalk.ui.people.PeopleFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.indeterminateProgressDialog
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        injectDependencies()
        if (isLoggedIn()) {
            navigateTo(PeopleFragment(), null)
        } else {
            navigateTo(LoginFragment(), null)
        }
    }

    private fun injectDependencies() {
        /* val component = DaggerAppComponent.builder()
             .appModule(AppModule())
             .build()
         component.inject(this)*/
        (application as BaseApplication).appComponent.inject(this)
    }

    private fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    fun navigateTo(fragment: Fragment, arguments: Bundle?) {
        val transaction =
            supportFragmentManager.beginTransaction()
        when (fragment) {
            is LoginFragment -> {
                transaction.replace(
                    R.id.main_container,
                    LoginFragment.newInstance()
                )
            }
            is PeopleFragment -> {
                transaction.replace(
                    R.id.main_container,
                    PeopleFragment.newInstance()
                )
            }
            is MyProfileFragment -> {
                transaction.replace(
                    R.id.main_container,
                    MyProfileFragment.newInstance()
                )
            }
            is EditProfileInfoFragment -> {
                transaction.replace(
                    R.id.main_container,
                    EditProfileInfoFragment.newInstance(arguments)
                )
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun showLoading(show: Boolean) {
        if (show) {
            pb_main.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            pb_main.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
