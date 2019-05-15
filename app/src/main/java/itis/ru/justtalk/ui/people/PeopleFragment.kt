package itis.ru.justtalk.ui.people

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.CardPagerAdapter
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.ui.messages.ChatWithUserFragment
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_people.*
import javax.inject.Inject

const val ACCESS_FINE_LOCATION_REQUEST_CODE: Int = 1001
const val ARG_USER_UID: String = "ARG_USER_UID"
const val ARG_CHAT_ID: String = "ARG_CHAT_ID"
const val NO_CHAT_ID: String = "no_chat_id"

class PeopleFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: PeopleViewModel
    private var cardAdapter: CardPagerAdapter =
        CardPagerAdapter()
    private var viewPagerPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(PeopleViewModel::class.java)

        observeUsersLiveData()
        observeMyLocationLiveData()
        observeShowLoadingLiveData()
        observeNavigateToChat()
        observeNavigateToUserDetails()

        if (ContextCompat.checkSelfPermission(
                rootActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.getUsersNearby()
        } else {
            ActivityCompat.requestPermissions(
                rootActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_FINE_LOCATION_REQUEST_CODE
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        restoreState()
    }

    override fun onPause() {
        super.onPause()
        viewPagerPosition = viewPager.currentItem
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            ACCESS_FINE_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //viewModel.getUsersNearby()
                } else {
                    showSnackbar(getString(R.string.snackbar_permission_message))
                }
            }
        }
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {
        setToolbarAndBottomNavVisibility(
            toolbarVisibility = View.VISIBLE,
            bottomNavVisibility = View.VISIBLE
        )
        setArrowToolbarVisibility(false)
        setToolbarTitle(getString(R.string.fragment_people_toolbar_title))

        btn_message.setOnClickListener {
            viewModel.onMessageClick(viewPager.currentItem)
        }

        cardAdapter.setOnClickListener {
            viewModel.onUserClicked(it)
        }
    }

    private fun restoreState() {
        if (viewPagerPosition != -1) {
            viewPager.adapter = cardAdapter
            viewPager.currentItem = viewPagerPosition
        }
    }

    private fun observeMyLocationLiveData() =
        viewModel.myLocationLiveData.observe(this, Observer {
            if (it != null)
                cardAdapter.setMyLocation(it)
        })

    private fun observeUsersLiveData() =
        viewModel.usersLiveData.observe(this, Observer { list ->
            list?.let {
                if (it.data != null) {
                    cardAdapter.addCardItemS(it.data)
                    viewPager.adapter = cardAdapter
                    viewPager.offscreenPageLimit = 3
                }
                if (it.error != null) {
                    showSnackbar(getString(R.string.snackbar_error_message))
                }
            }
        })

    private fun observeShowLoadingLiveData() =
        viewModel.showLoadingLiveData.observe(this, Observer {
            it?.let { show ->
                rootActivity.showLoading(show)
            }
        })

    private fun observeNavigateToChat() =
        viewModel.navigateToChat.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let { user ->
                val chatBundle = Bundle()
                chatBundle.putString(ARG_USER_UID, user.uid)
                chatBundle.putString(ARG_CHAT_ID, NO_CHAT_ID)
                rootActivity.navigateTo(ChatWithUserFragment.toString(), chatBundle)
            }
        })

    private fun observeNavigateToUserDetails() =
        viewModel.navigateToUserDetails.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let { user ->
                val profileBundle = Bundle()
                profileBundle.putParcelable(MyProfileFragment.ARG_USER, user)
                rootActivity.navigateTo(UserDetailsFragment.toString(), profileBundle)
            }
        })

    companion object {
        fun newInstance() = PeopleFragment()
    }
}
