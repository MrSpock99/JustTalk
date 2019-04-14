package itis.ru.justtalk.di.module

import dagger.Binds
import dagger.Module
import itis.ru.justtalk.repository.UserRepository
import itis.ru.justtalk.repository.UserRepositoryImpl

@Module
interface RepoModule{
    @Binds
    fun provideRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}