package itis.ru.justtalk.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.ui.login.LoginFragment
import itis.ru.justtalk.ui.messages.ChatWithUserFragment
import itis.ru.justtalk.ui.messages.MessagesFragment
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import itis.ru.justtalk.ui.people.PeopleFragment
import itis.ru.justtalk.ui.people.UserDetailsFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    navigateTo(MyProfileFragment(), null)
                }
                R.id.nav_people -> {
                    navigateTo(PeopleFragment(), null)
                }
                R.id.nav_messages -> {
                    navigateTo(MessagesFragment(), null)
                }
                else -> {
                    return@OnNavigationItemSelectedListener false
                }
            }
            return@OnNavigationItemSelectedListener true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        injectDependencies()
        if (isLoggedIn()) {
            navigateTo(PeopleFragment(), null)
            bottom_navigation.selectedItemId = R.id.nav_people
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
            is MessagesFragment -> {
                transaction.replace(
                    R.id.main_container,
                    MessagesFragment.newInstance()
                )
            }
            is ChatWithUserFragment -> {
                transaction.replace(
                    R.id.main_container,
                    ChatWithUserFragment.newInstance(arguments)
                )
            }
            is UserDetailsFragment -> {
                transaction.replace(
                    R.id.main_container,
                    UserDetailsFragment.newInstance(arguments)
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
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            pb_main.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }
}
