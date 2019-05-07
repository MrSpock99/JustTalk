package itis.ru.justtalk.ui.people

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.utils.ViewModelFactory
import itis.ru.justtalk.utils.getDistanceFromLocation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragmet_user_details.*
import javax.inject.Inject

class UserDetailsFragment : Fragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: UserDetailsViewModel
    private lateinit var rootActivity: MainActivity

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
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                rootActivity.onBackPressed()
                return true
            }
        }
        return false
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {
        rootActivity = activity as MainActivity
        setToolbarAndBottomNavVisibility()
        rootActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(UserDetailsViewModel::class.java)

        viewModel.getUserInfo(arguments)
        observeUserLiveData()
    }

    private fun setToolbarAndBottomNavVisibility() {
        rootActivity.toolbar.visibility = View.VISIBLE
        rootActivity.bottom_navigation.visibility = View.GONE
    }

    private fun setUserInfo(user: User) {
        Glide.with(this)
            .load(user.avatarUrl)
            .placeholder(R.drawable.image_placeholder)
            .into(iv_user_avatar)
        tv_name_age.text = "${user.name}, ${user.age}"
        rootActivity.toolbar.tv_toolbar_title.text = user.name
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
                viewModel.myProfileLiveData.observe(this, Observer { myProfile ->
                    myProfile?.let { it1 -> setDistanceToUser(user, it1) }
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
