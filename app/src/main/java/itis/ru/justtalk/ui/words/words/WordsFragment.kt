package itis.ru.justtalk.ui.words.words

import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.*
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.WordsAdapter
import itis.ru.justtalk.models.db.Word
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.ui.base.REQUEST_CODE
import itis.ru.justtalk.ui.words.groups.ARG_GROUP_ID
import itis.ru.justtalk.ui.words.groups.ARG_GROUP_NAME
import itis.ru.justtalk.ui.words.groups.ARG_IMAGE_URL
import itis.ru.justtalk.ui.words.test.TestFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_words.*
import javax.inject.Inject

const val REQ_CODE_ADD_WORD = 1002
const val REQ_CODE_EDIT_WORD = 1003
const val ARG_WORD = "WORD"
const val ARG_TRANSLATION = "TRANSLATION"
const val ARG_WORD_ID = "WORD_ID"
const val ARG_AUTO_PHOTO = "AUTO_PHOTO"

class WordsFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: WordsViewModel
    private var groupId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setHasOptionsMenu(true)
        groupId = arguments?.get(ARG_GROUP_ID) as Long

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory).get(WordsViewModel::class.java)
        observeAddWordLiveData()
        observeAllWordsLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getWords(groupId)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_add -> {
                startActivityForResult(
                    Intent(rootActivity, AddWordActivity::class.java),
                    REQ_CODE_ADD_WORD
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_ADD_WORD -> viewModel.addWord(arguments, data)
                REQ_CODE_EDIT_WORD -> viewModel.editWord(arguments, data)
            }
        }
    }

    private fun injectDependencies() {
        (rootActivity.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {
        setToolbarAndBottomNavVisibility(View.VISIBLE, View.GONE)
        setArrowToolbarVisibility(true)
        setToolbarTitle(arguments?.get(ARG_GROUP_NAME) as String)
        btn_start_test.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong(ARG_GROUP_ID, groupId)
            rootActivity.navigateTo(TestFragment.toString(), bundle)
        }
    }

    private fun observeAddWordLiveData() =
        viewModel.wordOperationsLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                viewModel.getWords(groupId)
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeAllWordsLiveData() =
        viewModel.allWordsLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                val adapter = WordsAdapter({ item ->
                    fillForEditing(item)
                }, { _, item ->
                    showPopup(item)
                })
                adapter.submitList(response.data)
                rv_words.adapter = adapter
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun showPopup(word: Word) {
        val entities = resources.getStringArray(R.array.word_popup_entities)
        val builder = AlertDialog.Builder(context)
        builder.setItems(entities) { _, which ->
            when (which) {
                0 -> fillForEditing(word)
                1 -> viewModel.deleteWord(word)
            }
        }
        builder.show()
    }

    private fun fillForEditing(word: Word) {
        val intent = Intent(rootActivity, AddWordActivity::class.java)
        intent.putExtra(ARG_WORD, word.word)
        intent.putExtra(ARG_TRANSLATION, word.translation)
        intent.putExtra(ARG_GROUP_ID, word.groupId)
        intent.putExtra(ARG_WORD_ID, word.wordId)
        intent.putExtra(ARG_IMAGE_URL, word.imageUrl)
        intent.putExtra(REQUEST_CODE, REQ_CODE_EDIT_WORD)
        startActivityForResult(
            intent,
            REQ_CODE_EDIT_WORD
        )
    }

    companion object {
        fun newInstance(bundle: Bundle?): WordsFragment {
            val fragment = WordsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
