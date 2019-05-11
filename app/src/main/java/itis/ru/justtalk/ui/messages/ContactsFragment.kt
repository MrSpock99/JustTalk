package itis.ru.justtalk.ui.messages

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
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
        viewModel.getContacts()
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
    }

    private fun injectDependencies() {
        (rootActivity.application as BaseApplication).appComponent.inject(this)
    }

    private fun observeContactsListLiveData() =
        viewModel.contactsListLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                val listOfToUserNames = mutableListOf<String>()
                val listOfToUserChats = mutableListOf<String>()
                response.data.contactsList.forEach {
                    listOfToUserNames.add(it.name)
                }
                response.data.chatsList.forEach {
                    listOfToUserChats.add(it)
                }
                val arrayAdapter =
                    ArrayAdapter(
                        rootActivity,
                        android.R.layout.simple_list_item_1,
                        listOfToUserNames
                    )
                rv_contacts.adapter = arrayAdapter
                rv_contacts.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        val chatBundle = Bundle()
                        chatBundle.putString(ARG_USER_UID, response.data.contactsList[position].uid)
                        chatBundle.putString(ARG_CHAT_ID, listOfToUserChats[position])
                        rootActivity.navigateTo(ChatWithUserFragment(), chatBundle)
                    }
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
