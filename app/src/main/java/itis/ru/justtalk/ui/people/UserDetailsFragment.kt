package itis.ru.justtalk.ui.people

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.models.user.User
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.utils.ViewModelFactory
import itis.ru.justtalk.utils.getDistanceFromLocation
import kotlinx.android.synthetic.main.fragmet_user_details.*
import javax.inject.Inject

class UserDetailsFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: UserDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injectDependencies()
        return inflater.inflate(R.layout.fragmet_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setHasOptionsMenu(true)
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {
        setToolbarAndBottomNavVisibility(
            toolbarVisibility = View.VISIBLE,
            bottomNavVisibility = View.GONE
        )
        setArrowToolbarVisibility(true)

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(UserDetailsViewModel::class.java)

        viewModel.getUserInfo(arguments)
        observeUserLiveData()
    }

    private fun setUserInfo(user: User) {
        Glide.with(this)
            .load(user.avatarUrl)
            .placeholder(R.drawable.image_placeholder)
            .into(iv_user_avatar)
        tv_name_age.text = "${user.name}, ${user.age}"
        setToolbarTitle(user.name)
        tv_learning_lang.text = user.learningLanguage
        tv_speaking_lang.text = user.speakingLanguage
    }

    private fun setDistanceToUser(user: User, myProfile: User) {
        tv_distance.text = getDistanceFromLocation(user.location, myProfile.location)
    }

    private fun observeUserLiveData() =
        viewModel.userLiveData.observe(this, Observer {
            it?.let { user ->
                setUserInfo(user)
                viewModel.myProfileLiveData.observe(this, Observer { myProfileResponse ->
                    if (myProfileResponse?.data != null) {
                        setDistanceToUser(user, myProfileResponse.data)
                    }
                    if (myProfileResponse?.error != null) {
                        showSnackbar(getString(R.string.snackbar_error_message))
                    }
                })
            }
        })

    companion object {
        fun newInstance(arguments: Bundle?): UserDetailsFragment {
            val fragment = UserDetailsFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}
