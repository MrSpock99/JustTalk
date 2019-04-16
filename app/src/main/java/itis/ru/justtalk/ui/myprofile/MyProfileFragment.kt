package itis.ru.justtalk.ui.myprofile

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.justtalk.R
import itis.ru.justtalk.di.component.DaggerMainComponent
import itis.ru.justtalk.di.module.AppModule
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.editinfo.EditInfoFragment
import itis.ru.justtalk.ui.settings.SettingsFragment
import itis.ru.justtalk.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_my_profile.*
import javax.inject.Inject

class MyProfileFragment : Fragment() {
    @Inject
    lateinit var viewModeFactory: ViewModelFactory
    private lateinit var viewModel: MyProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        injectDependencies()
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = ViewModelProviders.of(this, this.viewModeFactory).get(MyProfileViewModel::class.java)

        btn_settings.setOnClickListener {
            (activity as MainActivity).navigateTo(SettingsFragment())
        }
        btn_add_photo.setOnClickListener {
            //(activity as MainActivity).navigateTo(SettingsFragment())
        }
        btn_edit.setOnClickListener {
            (activity as MainActivity).navigateTo(EditInfoFragment())
        }
    }

    private fun injectDependencies() {
        val component = DaggerMainComponent.builder()
            .appModule(AppModule())
            .build()
        component.inject(this)
    }
}
