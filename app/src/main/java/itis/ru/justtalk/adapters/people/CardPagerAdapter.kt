package itis.ru.justtalk.adapters.people

import android.annotation.SuppressLint
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import itis.ru.justtalk.R
import itis.ru.justtalk.interactor.myprofile.MyProfileInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.repository.UserRepositoryImpl
import itis.ru.justtalk.utils.getDistanceFromLocation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.card_view_tinder_like.view.*
import java.util.*
import kotlin.collections.ArrayList

class CardPagerAdapter: PagerAdapter() {
    val data: MutableList<User> = ArrayList()
    private lateinit var clickListener: (User) -> Unit
    private lateinit var myLocation: GeoPoint
    private var baseElevation: Float = 0.0f

    fun setOnClickListener(clickListener: (User) -> Unit) {
        this.clickListener = clickListener
    }

    fun setMyLocation(myLocation: GeoPoint){
        this.myLocation = myLocation
    }

    fun addCardItemS(userList: List<User>) {
        data.addAll(userList)
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

        if (baseElevation == 0f) {
            baseElevation = cardView.cardElevation
        }

        cardView.maxCardElevation = baseElevation
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    @SuppressLint("CheckResult")
    private fun bind(user: User, view: View) {
        val tvName = view.tv_name_age
        val tvDistance = view.tv_distance
        val ivAvatar = view.iv_user_avatar

        tvName.text = user.name
        tvDistance.text = myLocation?.let { getDistanceFromLocation(user.location, it) }

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
