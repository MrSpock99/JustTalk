package itis.ru.justtalk.ui.myprofile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_my_profile.*
import javax.inject.Inject

class MyProfileFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: MyProfileViewModel

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
        setToolbarAndBottomNavVisibility(
            toolbarVisibility = View.VISIBLE,
            bottomNavVisibility = View.VISIBLE
        )
        setToolbarTitle(getString(R.string.fragment_my_profile_toolbar_title))
        setArrowToolbarVisibility(false)

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(MyProfileViewModel::class.java)

        viewModel.getMyProfile()
        observeProfileLiveData()
        observeShowLoadingLiveData()

        btn_settings.setOnClickListener {
            //rootActivity.navigateTo(SettingsFragment, null)
        }
        btn_add_photo.setOnClickListener {
            //(activity as MainActivity).navigateTo(SettingsFragment())
        }
        btn_edit.setOnClickListener {
            viewModel.editProfileClick()
        }
    }

    private fun observeProfileLiveData() {
        viewModel.myProfileLiveData.observe(this, Observer {
            it?.let { userResponse ->
                if (userResponse.data != null)
                    setUserNameAndAvatar(userResponse.data.name, userResponse.data.avatarUrl)
                if (userResponse.error != null)
                    showSnackbar(getString(R.string.snackbar_error_message))
            }
        })
    }

    private fun observeShowLoadingLiveData() {
        viewModel.showLoadingLiveData.observe(this, Observer {
            it?.let { show ->
                rootActivity.showLoading(show)
            }
        })

        viewModel.navigateToEdit.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let { user ->
                val profileBundle = Bundle()
                profileBundle.putParcelable(ARG_USER, user)
                rootActivity.navigateTo(EditProfileInfoFragment.toString(), profileBundle)
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
