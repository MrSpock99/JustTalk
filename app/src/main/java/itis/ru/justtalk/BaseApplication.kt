package itis.ru.justtalk

import android.app.Application
import itis.ru.justtalk.di.component.DaggerAppComponent
import itis.ru.justtalk.di.component.AppComponent
import itis.ru.justtalk.di.component.AuthComponent
import itis.ru.justtalk.di.module.AppModule
import itis.ru.justtalk.di.module.AuthModule
import itis.ru.justtalk.ui.login.LoginFragment

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent
    var authComponent: AuthComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    fun initAuthComponent(fragment: LoginFragment): AuthComponent {
        if (authComponent == null) {
            authComponent = appComponent.authComponent(AuthModule())
        }
        return authComponent as AuthComponent
    }

    fun clearAuthComponent() {
        authComponent = null
    }

}
