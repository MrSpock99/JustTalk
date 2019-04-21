package itis.ru.justtalk.di.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import itis.ru.justtalk.ui.editinfo.EditProfileInfoViewModel
import itis.ru.justtalk.ui.login.LoginViewModel
import itis.ru.justtalk.ui.myprofile.MyProfileViewModel
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyProfileViewModel::class)
    abstract fun bindMyProfileViewModel(myProfileViewModel: MyProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileInfoViewModel::class)
    abstract fun bindEditProfileViewModel(editProfileViewModel: EditProfileInfoViewModel): ViewModel
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
