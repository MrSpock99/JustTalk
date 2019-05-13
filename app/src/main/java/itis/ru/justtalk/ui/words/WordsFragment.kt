package itis.ru.justtalk.ui.words

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.*
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.WordGroupAdapter
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_words.*
import javax.inject.Inject

const val REQ_CODE_CREATE_GROUP = 1001
const val ARG_GROUP_NAME = "GROUP_NAME"
const val ARG_IMAGE_URL = "IMAGE_URL"

class WordsFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: WordsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        injectDependencies()

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(WordsViewModel::class.java)
        observeAddGroupSuccessLiveData()
        observeGetAllGroupsLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getGroups()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_add -> {
                startActivityForResult(
                    Intent(rootActivity, ActivityCreateGroup::class.java),
                    REQ_CODE_CREATE_GROUP
                )
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_CREATE_GROUP) {
            viewModel.addGroup(
                WordGroup(
                    name = data?.getStringExtra(ARG_GROUP_NAME).toString(),
                    imageUrl = data?.getStringExtra(ARG_IMAGE_URL).toString()
                )
            )
        }
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    private fun observeAddGroupSuccessLiveData() =
        viewModel.addGroupSuccessLiveData.observe(this, Observer { response ->
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })


    private fun observeGetAllGroupsLiveData() =
        viewModel.allGroupsLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                val adapter = WordGroupAdapter { item ->
                    /*val chatBundle = Bundle()
                    chatBundle.putString(ARG_USER_UID, listOfContacts[pos].uid)
                    chatBundle.putString(ARG_CHAT_ID, listOfToUserChats[pos])
                    rootActivity.navigateTo(ChatWithUserFragment(), chatBundle)*/
                }
                adapter.submitList(response.data)
                rv_word_groups.adapter = adapter
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    companion object {
        fun newInstance() = WordsFragment()
    }
}