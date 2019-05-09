package itis.ru.justtalk.ui.messages

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.utils.ViewModelFactory
import javax.inject.Inject

class ChatWithUserFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: ChatWithUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        viewModel = ViewModelProviders.of(this, this.viewModeFactory)
            .get(ChatWithUserViewModel::class.java)
        viewModel.startChat(arguments)
        observeStartChatSuccessLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_with_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun injectDependencies() {
        (rootActivity.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {

    }

    private fun observeStartChatSuccessLiveData() =
        viewModel.startChatSuccessLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                showSnackbar("Success")
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    companion object {
        fun newInstance(arguments: Bundle?): ChatWithUserFragment {
            val fragment = ChatWithUserFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}
