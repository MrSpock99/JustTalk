package itis.ru.justtalk.adapters

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import itis.ru.justtalk.R
import itis.ru.justtalk.models.user.RemoteChatUser
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactsAdapter(private val clickListener: (Int) -> Unit) :
    ListAdapter<RemoteChatUser, ContactsAdapter.RvItemViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RvItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RvItemViewHolder(inflater.inflate(R.layout.item_contact, parent, false))
    }

    override fun onBindViewHolder(holder: RvItemViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class RvItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: RemoteChatUser, clickListener: (Int) -> Unit) {
            itemView.tv_user_name.text = item.name
            itemView.tv_last_message.text = item.lastMessage
            Glide.with(itemView)
                .load(item.avatarUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(itemView.iv_contact_avatar)
            itemView.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RemoteChatUser>() {
        override fun areItemsTheSame(oldItem: RemoteChatUser, newItem: RemoteChatUser): Boolean {
            return oldItem.lastMessage == newItem.lastMessage
        }

        override fun areContentsTheSame(oldItem: RemoteChatUser, newItem: RemoteChatUser): Boolean {
            return oldItem == newItem
        }
    }
}
