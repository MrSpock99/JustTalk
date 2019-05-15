package itis.ru.justtalk.ui.words.words

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.WordsAdapter
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.ui.words.groups.ARG_GROUP_ID
import itis.ru.justtalk.ui.words.groups.ARG_GROUP_NAME
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_words.*
import javax.inject.Inject

const val REQ_CODE_ADD_WORD = 1002
const val ARG_WORD = "WORD"
const val ARG_TRANSLATION = "TRANSLATION"

class WordsFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: WordsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()

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
        viewModel.getWords(arguments?.get(ARG_GROUP_ID) as Long)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_ADD_WORD) {
            viewModel.addWord(arguments, data)
        }
    }

    private fun injectDependencies() {
        (rootActivity.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {
        setToolbarAndBottomNavVisibility(View.VISIBLE, View.GONE)
        setArrowToolbarVisibility(true)
        setToolbarTitle(arguments?.get(ARG_GROUP_NAME) as String)
        fab_add_word.setOnClickListener {
            startActivityForResult(
                Intent(rootActivity, AddWordActivity::class.java),
                REQ_CODE_ADD_WORD
            )
        }
    }

    private fun observeAddWordLiveData() =
        viewModel.addWordSuccessLiveData.observe(this, Observer { response ->
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeAllWordsLiveData() =
        viewModel.allWordsLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                val adapter = WordsAdapter { item -> }
                adapter.submitList(response.data)
                rv_words.adapter = adapter
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    companion object {
        fun newInstance(bundle: Bundle?): WordsFragment {
            val fragment = WordsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
