package itis.ru.justtalk.ui.people

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import itis.ru.justtalk.R
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.myprofile.MyProfileFragment.Companion.ARG_USER
import itis.ru.justtalk.utils.getDistanceFromLocation
import kotlinx.android.synthetic.main.fragmet_user_details.*

class UserDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmet_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = (arguments?.getParcelable(ARG_USER) as User)
        Glide.with(view)
            .load(user.avatarUrl)
            .placeholder(R.drawable.image_placeholder)
            .into(iv_user_avatar)
        tv_learning_lang.text = user.learningLanguage
        tv_speaking_lang.text = user.speakingLanguage
        tv_distance.text = getDistanceFromLocation(user.location)
    }

    companion object {
        fun newInstance(arguments: Bundle?): UserDetailsFragment {
            val fragment = UserDetailsFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}