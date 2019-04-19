package itis.ru.justtalk.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import itis.ru.justtalk.R
import itis.ru.justtalk.di.component.DaggerMainComponent
import itis.ru.justtalk.di.module.AppModule
import itis.ru.justtalk.ui.login.LoginFragment
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import itis.ru.justtalk.ui.people.PeopleFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()
        if (isLoggedIn()) {
            navigateTo(PeopleFragment())
        } else {
            navigateTo(LoginFragment())
        }
    }

    private fun injectDependencies() {
        val component = DaggerMainComponent.builder()
            .appModule(AppModule())
            .build()
        component.inject(this)
    }

    private fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    fun navigateTo(fragment: Fragment) {
        val transaction =
            supportFragmentManager.beginTransaction()
        when (fragment) {
            is LoginFragment -> transaction.replace(R.id.main_container, LoginFragment.newInstance())
            is PeopleFragment -> transaction.replace(R.id.main_container, PeopleFragment.newInstance())
            is MyProfileFragment -> transaction.replace(R.id.main_container, MyProfileFragment.newInstance())
        }
        transaction.commit()
    }
}
