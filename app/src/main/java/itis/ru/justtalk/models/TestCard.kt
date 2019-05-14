package itis.ru.justtalk.models

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mindorks.placeholderview.SwipePlaceHolderView
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import com.mindorks.placeholderview.annotations.swipe.*
import itis.ru.justtalk.R
import itis.ru.justtalk.models.db.Word

@Layout(R.layout.item_test)
class TestCard(
    private val context: android.view.View,
    private val word: Word,
    private val swipeView: SwipePlaceHolderView
) {

    @View(R.id.iv_word_image)
    private lateinit var wordImage: ImageView

    @View(R.id.tv_word)
    private lateinit var wordTv: TextView

    @View(R.id.tv_translation)
    private lateinit var translationTv: TextView

    @Resolve
    private fun onResolved() {
        if (word.imageUrl != "null") {
            Glide.with(context).load(word.imageUrl).into(wordImage)
        }
        wordTv.text = word.word
        translationTv.text = word.translation
    }

    @SwipeOut
    private fun onSwipedOut() {
        swipeView.addView(this)
    }

    @SwipeCancelState
    private fun onSwipeCancelState() {
    }

    @SwipeIn
    private fun onSwipeIn() {
    }

    @SwipeInState
    private fun onSwipeInState() {
    }

    @SwipeOutState
    private fun onSwipeOutState() {
    }
}
