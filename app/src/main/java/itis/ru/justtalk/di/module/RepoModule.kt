package itis.ru.justtalk.di.module

import dagger.Binds
import dagger.Module
import itis.ru.justtalk.repository.ChatRepository
import itis.ru.justtalk.repository.ChatRepositoryImpl
import itis.ru.justtalk.repository.UserRepository
import itis.ru.justtalk.repository.UserRepositoryImpl

@Module
interface RepoModule {
    @Binds
    fun provideUsersRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun provideChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository
}
