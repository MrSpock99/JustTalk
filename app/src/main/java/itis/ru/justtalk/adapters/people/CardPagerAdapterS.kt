package itis.ru.justtalk.adapters.people

import android.annotation.SuppressLint
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import itis.ru.justtalk.R
import itis.ru.justtalk.interactor.myprofile.MyProfileInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.repository.UserRepositoryImpl
import itis.ru.justtalk.utils.getDistanceFromLocation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.util.*
import javax.inject.Inject

class CardPagerAdapterS: PagerAdapter() {
    var myProfileInteractor: MyProfileInteractor = MyProfileInteractor(UserRepositoryImpl(
        FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()))
    private val mViews: MutableList<CardView?>
    val mData: MutableList<User>
    private lateinit var clickListener: (User) -> Unit
    var baseElevation: Float = 0.toFloat()
        private set

    init {
        mData = ArrayList()
        mViews = ArrayList()
    }

    fun setOnClickListener(clickListener: (User) -> Unit) {
        this.clickListener = clickListener
    }

    fun addCardItemS(userList: List<User>) {
        //mViews.add(null)
        mData.addAll(userList)
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

        cardView.setOnClickListener {
            clickListener(mData[position])
        }

        if (baseElevation == 0f) {
            baseElevation = cardView.cardElevation
        }

        cardView.maxCardElevation = baseElevation
        mViews.add(cardView)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        mViews[position] = null
    }

    @SuppressLint("CheckResult")
    private fun bind(user: User, view: View) {
        val tvName = view.findViewById<TextView>(R.id.tv_name_age)
        val tvDistance = view.findViewById<TextView>(R.id.tv_distance)
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_user_avatar)

        tvName.text = user.name
        myProfileInteractor
            .getMyProfile()
            .subscribe({
                tvDistance.text = getDistanceFromLocation(user.location, it.location)
            }, {
                it.printStackTrace()
            })

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
