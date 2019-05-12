package itis.ru.justtalk.ui.messages

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.adapters.MessageAdapter
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_chat_with_user.*
import javax.inject.Inject

class ChatWithUserFragment : BaseFragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: ChatWithUserViewModel
    private var adapter: MessageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this, this.viewModeFactory)
            .get(ChatWithUserViewModel::class.java)

        viewModel.startChat(arguments)
        observeStartChatSuccessLiveData()
        observeSendMessagesLiveData()
        observeGetMessagesLiveData()
        observeShowLoadingLiveData()
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
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

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    private fun injectDependencies() {
        (rootActivity.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {
        setArrowToolbarVisibility(true)
        setToolbarAndBottomNavVisibility(
            toolbarVisibility = View.VISIBLE,
            bottomNavVisibility = View.GONE
        )
        btn_send_message.setOnClickListener {
            if (et_message.text.toString() != "") {
                viewModel.sendMessage(et_message.text.toString())
            }
            et_message.setText("")
        }
    }

    private fun observeStartChatSuccessLiveData() =
        viewModel.startChatSuccessLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                setToolbarTitle(response.data)
            }
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeSendMessagesLiveData() =
        viewModel.sendMessageSuccessLiveData.observe(this, Observer { response ->
            if (response?.error != null) {
                showSnackbar(getString(R.string.snackbar_error_message))
            }
        })

    private fun observeGetMessagesLiveData() =
        viewModel.getMessagesLiveData.observe(this, Observer { response ->
            if (response?.data != null) {
                adapter = MessageAdapter(response.data.uid, response.data.options)
                rv_messages.adapter = adapter
                adapter?.startListening()
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
        fun newInstance(arguments: Bundle?): ChatWithUserFragment {
            val fragment = ChatWithUserFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}
