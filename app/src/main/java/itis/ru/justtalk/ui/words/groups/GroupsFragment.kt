package itis.ru.justtalk.ui.words.groups

import android.app.Activity
import android.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.*
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.WordGroupAdapter
import itis.ru.justtalk.models.db.Group
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.ui.base.REQUEST_CODE
import itis.ru.justtalk.ui.words.test.TestFragment
import itis.ru.justtalk.ui.words.words.WordsFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_groups.*
import javax.inject.Inject

const val REQ_CODE_CREATE_GROUP = 1001
const val REQ_CODE_EDIT_GROUP = 1002
const val ARG_GROUP_NAME = "GROUP_NAME"
const val ARG_IMAGE_URL = "IMAGE_URL"
const val ARG_GROUP_ID = "GROUP_ID"

class GroupsFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: GroupsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        injectDependencies()

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(GroupsViewModel::class.java)
        observeAddGroupSuccessLiveData()
        observeGetAllGroupsLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getGroups()
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_add -> {
                startActivityForResult(
                    Intent(rootActivity, CreateGroupActivity::class.java),
                    REQ_CODE_CREATE_GROUP
                )
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_CREATE_GROUP -> viewModel.addGroup(data)
                REQ_CODE_EDIT_GROUP -> viewModel.editGroup(data)
            }
        }
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {
        setToolbarAndBottomNavVisibility(View.VISIBLE, View.VISIBLE)
        setArrowToolbarVisibility(false)
        setToolbarTitle(getString(R.string.nav_words_title))
        btn_start_test.setOnClickListener {
            rootActivity.navigateTo(TestFragment.toString(), null)
        }
    }

    private fun observeAddGroupSuccessLiveData() =
        viewModel.groupOperationsLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                viewModel.getGroups()
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeGetAllGroupsLiveData() =
        viewModel.allGroupsLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                val adapter = WordGroupAdapter(clickListener = { item ->
                    val bundle = Bundle()
                    bundle.putLong(ARG_GROUP_ID, item.id)
                    bundle.putString(ARG_GROUP_NAME, item.name)
                    rootActivity.navigateTo(WordsFragment.toString(), bundle)
                }, longClickListener = { position, item ->
                    showPopup(item)
                })
                adapter.submitList(response.data)
                rv_word_groups.adapter = adapter
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun showPopup(group: Group) {
        val entities = resources.getStringArray(R.array.group_popup_entities)
        val builder = AlertDialog.Builder(context)
        builder.setItems(entities) { _, which ->
            when (which) {
                0 -> {
                    val intent = Intent(rootActivity, CreateGroupActivity::class.java)
                    intent.putExtra(ARG_GROUP_ID, group.id)
                    intent.putExtra(ARG_GROUP_NAME, group.name)
                    intent.putExtra(ARG_IMAGE_URL, group.imageUrl)
                    intent.putExtra(REQUEST_CODE, REQ_CODE_EDIT_GROUP)
                    startActivityForResult(
                        intent,
                        REQ_CODE_EDIT_GROUP
                    )
                }
                1 -> viewModel.deleteGroup(group)
            }
        }
        builder.show()
    }

    companion object {
        fun newInstance() = GroupsFragment()
    }
}
