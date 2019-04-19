package itis.ru.justtalk.ui.myprofile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import itis.ru.justtalk.R
import itis.ru.justtalk.di.component.DaggerMainComponent
import itis.ru.justtalk.di.module.AppModule
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.editinfo.EditInfoFragment
import itis.ru.justtalk.ui.settings.SettingsFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import javax.inject.Inject

class MyProfileFragment : Fragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: MyProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        injectDependencies()
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = ViewModelProviders.of(this, this.viewModeFactory).get(MyProfileViewModel::class.java)

        viewModel.getMyProfile()
        observeProfileLiveData()
        observeShowLoadingLiveData()

        btn_settings.setOnClickListener {
            (activity as MainActivity).navigateTo(SettingsFragment())
        }
        btn_add_photo.setOnClickListener {
            //(activity as MainActivity).navigateTo(SettingsFragment())
        }
        btn_edit.setOnClickListener {
            (activity as MainActivity).navigateTo(EditInfoFragment())
        }
    }

    private fun observeProfileLiveData() {
        viewModel.myProfileLiveData.observe(this, Observer {
            it?.let { user ->
                setUserNameAndAvatar(user.name, user.avatar_url)
            }
        })
    }

    private fun observeShowLoadingLiveData() {
        viewModel.showLoadingLiveData.observe(this, Observer {
            it?.let { show ->
                showLoading(show)
            }
        })
    }

    private fun setUserNameAndAvatar(userName: String, userAvatarUrl: String) {
        tv_user_name.text = userName
        Glide.with(this)
                .load(userAvatarUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(iv_user_avatar)
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            activity?.pb_main?.visibility = View.VISIBLE
        } else {
            activity?.pb_main?.visibility = View.GONE
        }
    }

    private fun injectDependencies() {
        val component = DaggerMainComponent.builder()
                .appModule(AppModule())
                .build()
        component.inject(this)
    }

    companion object {
        fun newInstance() = MyProfileFragment()
    }
}

