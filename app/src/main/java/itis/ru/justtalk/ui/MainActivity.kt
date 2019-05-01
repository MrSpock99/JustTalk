package itis.ru.justtalk.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.ui.login.LoginFragment
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import itis.ru.justtalk.ui.people.PeopleFragment
import kotlinx.android.synthetic.main.activity_main.*
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
                toolbar.visibility = View.GONE
                bottom_navigation.visibility = View.GONE
            }
            is PeopleFragment -> {
                transaction.replace(
                    R.id.main_container,
                    PeopleFragment.newInstance()
                )
                toolbar.visibility = View.VISIBLE
                bottom_navigation.visibility = View.VISIBLE
            }
            is MyProfileFragment -> {
                transaction.replace(
                    R.id.main_container,
                    MyProfileFragment.newInstance()
                )
                toolbar.visibility = View.VISIBLE
                bottom_navigation.visibility = View.VISIBLE
            }
            is EditProfileInfoFragment -> {
                transaction.replace(
                    R.id.main_container,
                    EditProfileInfoFragment.newInstance(arguments)
                )
                toolbar.visibility = View.VISIBLE
                bottom_navigation.visibility = View.GONE
            }
        }
        transaction.commit()
    }

    fun showLoading(show: Boolean) {
        if (show) {
            pb_main.visibility = View.VISIBLE
        } else {
            pb_main.visibility = View.GONE
        }
    }
}
