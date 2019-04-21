package itis.ru.justtalk.di.component

import dagger.Component
import itis.ru.justtalk.di.module.AppModule
import itis.ru.justtalk.di.module.RepoModule
import itis.ru.justtalk.di.module.ViewModelFactoryModule
import itis.ru.justtalk.di.module.ViewModelModule
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.ui.login.LoginFragment
import itis.ru.justtalk.ui.myprofile.MyProfileFragment

@Component(modules = [ViewModelFactoryModule::class, ViewModelModule::class, RepoModule::class, AppModule::class])
interface MainComponent {
    fun inject(fragment: LoginFragment)
    fun inject(fragment: MyProfileFragment)
    fun inject(fragment: EditProfileInfoFragment)
    fun inject(activity: MainActivity)
}
