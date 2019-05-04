package itis.ru.justtalk.ui.messages

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.R

class ChatWithUserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_with_user, container, false)
    }

    companion object {
        fun newInstance(arguments: Bundle?): ChatWithUserFragment {
            val fragment = ChatWithUserFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}
