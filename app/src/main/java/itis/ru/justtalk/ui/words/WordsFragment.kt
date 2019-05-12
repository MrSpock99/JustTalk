package itis.ru.justtalk.ui.words

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.models.db.WordGroup
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.utils.ViewModelFactory
import javax.inject.Inject

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
        viewModel.getGroups()
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_add -> {
                viewModel.addGroup(WordGroup(1, "Test", 0, ""))
                return true
            }
        }
        return false
    }

    private fun injectDependencies() {
        (activity?.application as BaseApplication).appComponent.inject(this)
    }//////

    private fun observeAddGroupSuccessLiveData() =
        viewModel.addGroupSuccessLiveData.observe(this, Observer { response ->
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })


    private fun observeGetAllGroupsLiveData() =
        viewModel.allGroupsLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                response.data.forEach {
                    showSnackbar(it.toString())
                }
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    companion object {
        fun newInstance() = WordsFragment()
    }
}