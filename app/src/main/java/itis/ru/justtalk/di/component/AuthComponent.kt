package itis.ru.justtalk.di.component

import dagger.Subcomponent
import itis.ru.justtalk.di.module.AuthModule
import itis.ru.justtalk.di.scope.AuthScope
import itis.ru.justtalk.ui.login.LoginFragment

@AuthScope
@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {
    fun inject(fragment: LoginFragment)
}