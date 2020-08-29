package itis.ru.justtalk.adapters

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import itis.ru.justtalk.R
import kotlinx.android.synthetic.main.item_unsplash_photo.view.*

class PhotoChooseAdapter(private val clickListener: (String) -> Unit) :
    ListAdapter<String, PhotoChooseAdapter.PhotoHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PhotoHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhotoHolder(
            inflater.inflate(
                R.layout.item_unsplash_photo,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoHolder, pos: Int) {
        holder.bind(getItem(pos), clickListener)
    }

    class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(url: String, clickListener: (String) -> Unit) {
            Glide.with(itemView)
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .into(itemView.iv_image)
            itemView.setOnClickListener {
                clickListener(url)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
