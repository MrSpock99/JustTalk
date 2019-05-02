package itis.ru.justtalk.di.component

import dagger.Component
import itis.ru.justtalk.di.module.*
import itis.ru.justtalk.ui.MainActivity
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.ui.login.LoginFragment
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, ViewModelFactoryModule::class, ViewModelModule::class,
        RepoModule::class, AuthModule::class]
)
interface AppComponent {
    fun inject(fragment: LoginFragment)
    fun inject(fragment: MyProfileFragment)
    fun inject(fragment: EditProfileInfoFragment)
    fun inject(activity: MainActivity)
}
