package itis.ru.justtalk.di.module

import dagger.Binds
import dagger.Module
import itis.ru.justtalk.repository.*

@Module
interface RepoModule {
    @Binds
    fun provideUsersRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun provideChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

    @Binds
    fun provideWordsRepository(wordsRepositoryImpl: WordsRepositoryImpl): WordsRepository
}
