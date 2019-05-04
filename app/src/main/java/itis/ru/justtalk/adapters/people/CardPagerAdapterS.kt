package itis.ru.justtalk.adapters.people

import android.support.v4.view.PagerAdapter
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import itis.ru.justtalk.R
import itis.ru.justtalk.models.User
import itis.ru.justtalk.utils.getDistanceFromLocation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.util.*

class CardPagerAdapterS : PagerAdapter() {

    private val mViews: MutableList<CardView?>
    private val mData: MutableList<User>
    var baseElevation: Float = 0.toFloat()
        private set

    init {
        mData = ArrayList()
        mViews = ArrayList()
    }

    fun addCardItemS(user: User) {
        mViews.add(null)
        mData.add(user)
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.card_view_tinder_like, container, false)
        container.addView(view)
        bind(mData[position], view)
        val cardView = view.findViewById<View>(R.id.cardview) as CardView

        if (baseElevation == 0f) {
            baseElevation = cardView.cardElevation
        }

        cardView.maxCardElevation = baseElevation
        mViews[position] = cardView
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        mViews.set(position, null)
    }

    private fun bind(user: User, view: View) {
        val tvName = view.findViewById<TextView>(R.id.tv_name_age)
        val tvDistance = view.findViewById<TextView>(R.id.tv_distance)
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_user_avatar)

        tvName.text = user.name
        tvDistance.text = getDistanceFromLocation(user.location)

        val transformation = RoundedCornersTransformation(20, 1)

        val requestOptions = RequestOptions()
            .centerCrop()
            .transforms(transformation)

        val thumbnail = Glide.with(view)
            .load(R.drawable.image_placeholder)
            .apply(requestOptions)

        Glide.with(view)
            .load(user.avatarUrl)
            .apply(requestOptions)
            .thumbnail(thumbnail)
            .into(ivAvatar)
    }
}
