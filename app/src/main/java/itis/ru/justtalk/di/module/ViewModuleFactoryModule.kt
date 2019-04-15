package itis.ru.justtalk.di.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import itis.ru.justtalk.utils.ViewModelFactory

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
