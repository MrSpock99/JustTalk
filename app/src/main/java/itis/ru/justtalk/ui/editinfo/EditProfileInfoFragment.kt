package itis.ru.justtalk.ui.editinfo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import itis.ru.justtalk.BaseApplication
import itis.ru.justtalk.R
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.base.BaseFragment
import itis.ru.justtalk.utils.ViewModelFactory
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_edit_profile_info.*
import kotlinx.android.synthetic.main.spinner_gender.*
import kotlinx.android.synthetic.main.spinner_learning_level.*
import kotlinx.android.synthetic.main.spinner_speaking_level.*
import javax.inject.Inject

class EditProfileInfoFragment : BaseFragment() {
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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_edit_profile_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_edit_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_done -> {
                editUserInfo()
                return true
            }
        }
        return false
    }

    private fun injectDependencies() {
        /* val component = DaggerAppComponent.builder()
             .appModule(AppModule())
             .build()
         component.inject(this)*/
        (rootActivity.application as BaseApplication).appComponent.inject(this)
    }

    private fun init() {
        setToolbarAndBottomNavVisibility(
            toolbarVisibility = View.VISIBLE,
            bottomNavVisibility = View.GONE
        )
        setToolbarTitle(getString(R.string.fragment_edit_profile_toolbar_title))

        setAutocompleteAdapters()

        viewModel =
            ViewModelProviders.of(this, this.viewModeFactory)
                .get(EditProfileInfoViewModel::class.java)

        viewModel.getMyProfile(arguments)
        observeProfileLiveData()
        observeEditProfileSuccessLiveData()
        observeShowLoadingLiveData()
    }

    private fun setAutocompleteAdapters() {
        et_speaking_language.setAdapter(
            ArrayAdapter<String>(
                rootActivity,
                android.R.layout.simple_dropdown_item_1line,
                rootActivity.resources.getStringArray(R.array.et_language_entities)
            )
        )
        et_learning_language.setAdapter(
            ArrayAdapter<String>(
                rootActivity,
                android.R.layout.simple_dropdown_item_1line,
                rootActivity.resources.getStringArray(R.array.et_language_entities)
            )
        )
    }

    private fun observeProfileLiveData() =
        viewModel.myProfileLiveData.observe(this, Observer {
            it?.let { user ->
                this.user = user
                setUserInfo()
            }
        })

    private fun observeEditProfileSuccessLiveData() =
        viewModel.editProfileSuccessLiveData.observe(this, Observer {
            it?.let { success ->
                if (success) {
                    rootActivity.onBackPressed()
                } else {
                    //show error
                }
            }
        })

    private fun observeShowLoadingLiveData() =
        viewModel.showLoadingLiveData.observe(this, Observer {
            it?.let { show ->
                rootActivity.showLoading(show)
            }
        })

    private fun setUserInfo() {
        et_age.setText(user.age.toString())
        et_about_me.setText(user.aboutMe)
        et_learning_language.setText(user.learningLanguage)
        et_speaking_language.setText(user.speakingLanguage)
        setGenderSpinner()
        setLanguageLevelSpinners()
        setUserPhotos()
    }

    private fun editUserInfo() {
        user.age = et_age.text.toString().toInt()
        user.gender = spinner_genders.selectedItem.toString()
        user.aboutMe = et_about_me.text.toString()

        user.learningLanguage = et_learning_language.text.toString()
        user.learningLanguageLevel = spinner_learning_level.selectedItem.toString()

        user.speakingLanguage = et_speaking_language.text.toString()

        user.speakingLanguageLevel = spinner_speaking_level.selectedItem.toString()

        viewModel.editProfileInfo(user)
    }

    private fun setUserPhotos() {
        val transformation = RoundedCornersTransformation(20, 1)

        val requestOptions = RequestOptions()
            .centerCrop()
            .transforms(transformation)

        val thumbnail = Glide.with(this)
            .load(R.drawable.image_placeholder)
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

    private fun setLanguageLevelSpinners() {
        val levelsArr = activity?.resources?.getStringArray(R.array.spinner_level_entities)
        val aa =
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                levelsArr
            )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_learning_level.adapter = aa
        spinner_speaking_level.adapter = aa

        levelsArr?.indexOf(user.learningLanguageLevel)
            ?.let { spinner_learning_level.setSelection(it) }
        levelsArr?.indexOf(user.speakingLanguageLevel)
            ?.let { spinner_speaking_level.setSelection(it) }
    }

    companion object {
        fun newInstance(arguments: Bundle?): EditProfileInfoFragment {
            val fragment = EditProfileInfoFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}
