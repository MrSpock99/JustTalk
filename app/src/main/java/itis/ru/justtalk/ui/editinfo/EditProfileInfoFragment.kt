package itis.ru.justtalk.ui.editinfo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import itis.ru.justtalk.R
import itis.ru.justtalk.di.component.DaggerMainComponent
import itis.ru.justtalk.di.module.AppModule
import itis.ru.justtalk.models.User
import itis.ru.justtalk.utils.ViewModelFactory
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_edit_profile_info.*
import kotlinx.android.synthetic.main.spinner_material.*
import javax.inject.Inject

class EditProfileInfoFragment : Fragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var user: User
    private lateinit var viewModel: EditProfileInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injectDependencies()
        return inflater.inflate(R.layout.fragment_edit_profile_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun injectDependencies() {
        val component = DaggerMainComponent.builder()
            .appModule(AppModule())
            .build()
        component.inject(this)
    }

    private fun init() {
        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory)
                .get(EditProfileInfoViewModel::class.java)

        viewModel.getMyProfile(arguments)
        observeProfileLiveData()
    }

    private fun observeProfileLiveData() = viewModel.myProfileLiveData.observe(this, Observer {
        it?.let { user ->
            this.user = user
            setUserInfo()
        }
    })

    private fun setUserInfo() {
        et_age.setText(user.age.toString())
        et_about_me.setText(user.aboutMe)
        setGenderSpinner()
        setUserPhotos()
    }

    private fun setUserPhotos() {
        val transformation = RoundedCornersTransformation(20, 1)

        val requestOptions = RequestOptions()
            .centerCrop()
            .transforms(transformation)

        val thumbnail = Glide.with(this)
            .load(R.drawable.ic_launcher_background)
            .apply(requestOptions)

        Glide.with(this)
            .load(user.avatarUrl)
            .apply(requestOptions)
            .thumbnail(thumbnail)
            .into(iv_user_avatar)

        val imageViewList = arrayListOf(
            iv_user_photo_1,
            iv_user_photo_2,
            iv_user_photo_3,
            iv_user_photo_4,
            iv_user_photo_5
        )

        for (i in 0 until imageViewList.size) {
            Glide.with(this)
                .load(user.photosUrls[i])
                .thumbnail(thumbnail)
                .apply(requestOptions)
                .into(imageViewList[i])
        }
    }

    private fun setGenderSpinner() {
        val aa =
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                activity?.resources?.getStringArray(R.array.spinner_gender_entities)
            )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_genders.adapter = aa

        spinner_genders.setSelection(if (user.gender == User.GENDER_MAN) 0 else 1)
    }

    companion object {
        fun newInstance(arguments: Bundle?): EditProfileInfoFragment {
            val fragment = EditProfileInfoFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}
