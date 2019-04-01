package itis.ru.justtalk.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.main_container, LoginFragment.newInstance()).commit()
    }
}
