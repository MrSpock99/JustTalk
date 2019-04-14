package itis.ru.justtalk.di.component

import dagger.Component
import itis.ru.justtalk.di.module.AppModule
import itis.ru.justtalk.di.module.RepoModule
import itis.ru.justtalk.di.module.ViewModelFactoryModule
import itis.ru.justtalk.di.module.ViewModelModule
import itis.ru.justtalk.ui.login.LoginFragment

@Component(modules = [ViewModelFactoryModule::class, ViewModelModule::class, RepoModule::class,AppModule::class])
interface FragmentComponent {
    fun inject(fragment: LoginFragment)
}
