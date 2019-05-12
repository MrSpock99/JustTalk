package itis.ru.justtalk.ui.messages

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.ContactsAdapter
import itis.ru.justtalk.models.ChatUser
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.ui.people.ARG_CHAT_ID
import itis.ru.justtalk.ui.people.ARG_USER_UID
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_contacts.*
import javax.inject.Inject

class ContactsFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: ContactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()

        viewModel = ViewModelProviders.of(this, this.viewModeFactory)
            .get(ContactsViewModel::class.java)
        observeContactsListLiveData()
        observeShowLoadingLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setToolbarTitle(getString(R.string.nav_contacts_title))
        setArrowToolbarVisibility(false)
        setToolbarAndBottomNavVisibility(
            toolbarVisibility = View.VISIBLE,
            bottomNavVisibility = View.VISIBLE
        )
        viewModel.getContacts()
    }

    private fun injectDependencies() {
        (rootActivity.application as BaseApplication).appComponent.inject(this)
    }

    private fun observeContactsListLiveData() =
        viewModel.contactsListLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                val listOfToUserChats = mutableListOf<String>()
                val listOfContacts = mutableListOf<ChatUser>()

                response.data.contactsList.forEach {
                    listOfContacts.add(it)
                }
                response.data.chatsList.forEach {
                    listOfToUserChats.add(it)
                }

                val contactsAdapter = ContactsAdapter { pos ->
                    val chatBundle = Bundle()
                    chatBundle.putString(ARG_USER_UID, listOfContacts[pos].uid)
                    chatBundle.putString(ARG_CHAT_ID, listOfToUserChats[pos])
                    rootActivity.navigateTo(ChatWithUserFragment(), chatBundle)
                }
                contactsAdapter.submitList(listOfContacts)
                rv_contacts.adapter = contactsAdapter

            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeShowLoadingLiveData() =
        viewModel.showLoadingLiveData.observe(this, Observer {
            it?.let { it1 -> rootActivity.showLoading(it1) }
        })

    companion object {
        fun newInstance() = ContactsFragment()
    }
}
