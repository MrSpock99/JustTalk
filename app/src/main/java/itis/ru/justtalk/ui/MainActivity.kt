package itis.ru.justtalk.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.login.LoginFragment
import itis.ru.justtalk.ui.people.PeopleFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateTo(LoginFragment())
    }

    fun navigateTo(fragment: Fragment) {
        val transaction =
                supportFragmentManager.beginTransaction()
        when (fragment) {
            is LoginFragment -> transaction.replace(R.id.main_container, LoginFragment.newInstance())
            is PeopleFragment -> transaction.replace(R.id.main_container, PeopleFragment.newInstance())
        }
        transaction.commit()
    }
}
