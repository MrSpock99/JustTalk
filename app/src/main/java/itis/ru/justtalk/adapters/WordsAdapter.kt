package itis.ru.justtalk.adapters

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import itis.ru.justtalk.R
import itis.ru.justtalk.models.db.Word
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_words_group.view.*

class WordsAdapter(
    private val clickListener: (Word) -> Unit,
    private val longClickListener: (Int, Word) -> Unit
) :
    ListAdapter<Word, WordsAdapter.RvItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RvItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = RvItemViewHolder(
            inflater.inflate(
                R.layout.item_words_group,
                parent,
                false
            )
        )
        holder.itemView.pb_word_group.max = 10
        return holder
    }

    override fun onBindViewHolder(holder: RvItemViewHolder, position: Int) {
        holder.bind(position, getItem(position), clickListener, longClickListener)
    }

    class RvItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            position: Int,
            item: Word,
            clickListener: (Word) -> Unit,
            longClickListener: (Int, Word) -> Unit
        ) {
            itemView.tv_word_group_name.text = item.word
            itemView.tv_word_group_count.text = item.translation
            itemView.pb_word_group.progress = item.progress

            val transformation = RoundedCornersTransformation(20, 1)

            val requestOptions = RequestOptions()
                .centerCrop()
                .transforms(transformation)

            val thumbnail = Glide.with(itemView)
                .load(R.drawable.image_placeholder)
                .apply(requestOptions)

            Glide.with(itemView)
                .load(item.imageUrl)
                .apply(requestOptions)
                .thumbnail(thumbnail)
                .into(itemView.iv_word_group)

            itemView.setOnClickListener {
                clickListener(item)
            }

            itemView.setOnLongClickListener {
                longClickListener(position, item)
                return@setOnLongClickListener true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.wordId == newItem.wordId
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }
    }
}
