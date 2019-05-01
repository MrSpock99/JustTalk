package itis.ru.justtalk

import android.app.Application
import itis.ru.justtalk.di.component.DaggerAppComponent
import itis.ru.justtalk.di.component.AppComponent
import itis.ru.justtalk.di.module.AppModule

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        //injectDependencies()
    }

    fun injectDependencies(){
        appComponent.inject(this)
    }

    lateinit var appComponent: AppComponent
}
