package itis.ru.justtalk.ui.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.base.BaseActivity
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.ui.login.LoginFragment
import itis.ru.justtalk.ui.messages.ChatWithUserFragment
import itis.ru.justtalk.ui.messages.ContactsFragment
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import itis.ru.justtalk.ui.people.PeopleFragment
import itis.ru.justtalk.ui.people.UserDetailsFragment
import itis.ru.justtalk.ui.words.groups.GroupsFragment
import itis.ru.justtalk.ui.words.test.EndTestFragment
import itis.ru.justtalk.ui.words.test.TestFragment
import itis.ru.justtalk.ui.words.words.WordsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    navigateTo(MyProfileFragment.toString(), null)
                }
                R.id.nav_people -> {
                    navigateTo(PeopleFragment.toString(), null)
                }
                R.id.nav_messages -> {
                    navigateTo(ContactsFragment.toString(), null)
                }
                R.id.nav_words -> {
                    navigateTo(GroupsFragment.toString(), null)
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
        injectDependencies()
        init()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            if (getTopFragment() is EndTestFragment) {
                navigateTo(GroupsFragment.toString(), null)
            }
            supportFragmentManager.popBackStackImmediate()
            getTopFragment()?.let {
                supportFragmentManager.beginTransaction()
                    .remove(it)
                    .commit()
            }
            setBottomNavSelectedItem(getTopFragment())
        } else {
            super.onBackPressed()
        }
    }

    private fun getTopFragment(): Fragment? {
        val fragmentList = supportFragmentManager.fragments
        var top: Fragment? = null
        for (i in fragmentList.indices.reversed()) {
            top = fragmentList[i] as Fragment
            return top
        }
        return top
    }

    private fun setBottomNavSelectedItem(fragment: Fragment?) {
        when (fragment) {
            is MyProfileFragment -> {
                bottom_navigation.selectedItemId = R.id.nav_profile
            }
            is PeopleFragment -> {
                bottom_navigation.selectedItemId = R.id.nav_people
            }
            is ContactsFragment -> {
                bottom_navigation.selectedItemId = R.id.nav_messages
            }
            is GroupsFragment -> {
                bottom_navigation.selectedItemId = R.id.nav_words
            }
        }
    }

    private fun init() {
        setSupportActionBar(toolbar)
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        viewModel = ViewModelProviders.of(this, this.viewModeFactory)
            .get(MainViewModel::class.java)
        viewModel.checkIsLogined()
        observeIsLoginedLiveData()
    }

    private fun injectDependencies() {
        (application as BaseApplication).appComponent.inject(this)
    }

    fun navigateTo(fragment: String, arguments: Bundle?) {
        val transaction =
            supportFragmentManager.beginTransaction()
        when (fragment) {
            LoginFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    LoginFragment.newInstance()
                )
            }
            PeopleFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    PeopleFragment.newInstance()
                )
            }
            MyProfileFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    MyProfileFragment.newInstance()
                )
            }
            EditProfileInfoFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    EditProfileInfoFragment.newInstance(arguments)
                )
            }
            ContactsFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    ContactsFragment.newInstance()
                )
            }
            ChatWithUserFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    ChatWithUserFragment.newInstance(arguments)
                )
            }
            UserDetailsFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    UserDetailsFragment.newInstance(arguments)
                )
            }
            GroupsFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    GroupsFragment.newInstance()
                )
            }
            WordsFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    WordsFragment.newInstance(arguments)
                )
            }
            TestFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    TestFragment.newInstance(arguments)
                )
            }
            EndTestFragment.toString() -> {
                transaction.replace(
                    R.id.main_container,
                    EndTestFragment.newInstance(arguments)
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

    fun showLoadingWithoutFreezing(show: Boolean) {
        if (show) {
            pb_main.visibility = View.VISIBLE
        } else {
            pb_main.visibility = View.GONE
        }
    }

    private fun observeIsLoginedLiveData() =
        viewModel.isLoginedLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                if (response.data) {
                    bottom_navigation.selectedItemId = R.id.nav_people
                    navigateTo(PeopleFragment.toString(), null)
                } else {
                    navigateTo(LoginFragment.toString(), null)
                }
            }
        })
}
