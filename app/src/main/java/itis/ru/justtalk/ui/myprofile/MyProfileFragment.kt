package itis.ru.justtalk.ui.myprofile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.ui.settings.SettingsFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import javax.inject.Inject

class MyProfileFragment : Fragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: MyProfileViewModel
    private lateinit var rootActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injectDependencies()
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        rootActivity = activity as MainActivity
        setToolbarAndBottomNavVisibility()

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(MyProfileViewModel::class.java)

        viewModel.getMyProfile()
        observeProfileLiveData()
        observeShowLoadingLiveData()

        btn_settings.setOnClickListener {
            rootActivity.navigateTo(SettingsFragment(), null)
        }
        btn_add_photo.setOnClickListener {
            //(activity as MainActivity).navigateTo(SettingsFragment())
        }
        btn_edit.setOnClickListener {
            viewModel.editProfileClick()
        }
    }

    private fun setToolbarAndBottomNavVisibility(){
        rootActivity.toolbar.visibility = View.VISIBLE
        rootActivity.bottom_navigation.visibility = View.VISIBLE
    }

    private fun observeProfileLiveData() {
        viewModel.myProfileLiveData.observe(this, Observer {
            it?.let { user ->
                setUserNameAndAvatar(user.name, user.avatarUrl)
            }
        })
    }

    private fun observeShowLoadingLiveData() {
        viewModel.showLoadingLiveData.observe(this, Observer {
            it?.let { show ->
                rootActivity.showLoading(show)
            }
        })

        viewModel.navigateToEdit.observe(this, Observer {event ->
            event?.getContentIfNotHandled()?.let { user ->
                val profileBundle = Bundle()
                profileBundle.putParcelable(ARG_USER, user)
                rootActivity.navigateTo(EditProfileInfoFragment(), profileBundle)
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

    private fun injectDependencies() {
        /*val component = DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
        component.inject(this)*/
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    companion object {
        fun newInstance() = MyProfileFragment()
        const val ARG_USER = "user"
    }
}
