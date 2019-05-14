package itis.ru.justtalk.adapters

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import itis.ru.justtalk.R
import itis.ru.justtalk.models.db.Word
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.card_view_tinder_like.view.*
import kotlinx.android.synthetic.main.card_view_tinder_like.view.cardview
import kotlinx.android.synthetic.main.item_test.view.*

class TestAdapter : PagerAdapter() {
    val data: MutableList<Word> = ArrayList()
    private lateinit var clickListener: (Word) -> Unit

    fun setOnClickListener(clickListener: (Word) -> Unit) {
        this.clickListener = clickListener
    }

    fun addCardItemS(list: List<Word>) {
        data.addAll(list)
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.card_view_tinder_like, container, false)
        container.addView(view)
        bind(data[position], view)
        val cardView = view.cardview

        cardView.setOnClickListener {
            clickListener(data[position])
        }

        /*if (baseElevation == 0f) {
            baseElevation = cardView.cardElevation
        }*/

        //cardView.maxCardElevation = baseElevation
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    private fun bind(word: Word, view: View) {
        view.tv_word.text = word.word
        view.tv_translation.text = word.translation

        val transformation = RoundedCornersTransformation(20, 1)

        val requestOptions = RequestOptions()
            .centerCrop()
            .transforms(transformation)

        val thumbnail = Glide.with(view)
            .load(R.drawable.image_placeholder)
            .apply(requestOptions)

        Glide.with(view)
            .load(word.imageUrl)
            .apply(requestOptions)
            .thumbnail(thumbnail)
            .into(view.iv_user_avatar)
    }
}
