package itis.ru.justtalk.di.module

import dagger.Module
import dagger.Provides
import itis.ru.justtalk.adapters.people.CardPagerAdapter

@Module
class AdapterModule {
    @Provides
    fun provideCardPagerAdapter() = CardPagerAdapter()
}
