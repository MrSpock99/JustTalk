package itis.ru.justtalk.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import itis.ru.justtalk.R
import itis.ru.justtalk.models.Message
import kotlinx.android.synthetic.main.fragment_chat_with_user.view.*



class MessageAdapter internal constructor(
    private  val uidFrom: String,
    options: FirestoreRecyclerOptions<Message>
) :
    FirestoreRecyclerAdapter<Message, MessageAdapter.MessageViewHolder>(options) {
    private var view: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == R.layout.item_message_to) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_message_to, parent, false)
            MessageViewHolder()
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_from, parent, false)
            MessageViewHolder()
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
        holder.setMessage(model)
    }

    override fun getItemViewType(position: Int): Int {
        return if (uidFrom != getItem(position).fromUid) {
            R.layout.item_message_to
        } else {
            R.layout.item_message_from
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onDataChanged() {
        if (view != null){
            view?.rv_messages?.layoutManager?.scrollToPosition(itemCount - 1)
        }
    }

    inner class MessageViewHolder : RecyclerView.ViewHolder(view!!) {
        internal fun setMessage(message: Message) {
            val textView = view?.findViewById<TextView>(R.id.text_view)
            Log.d("MYLOPG", message.messageText)
            textView?.text = message.messageText
        }
    }
}
