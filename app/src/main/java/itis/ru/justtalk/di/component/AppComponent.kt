package itis.ru.justtalk.di.component

import dagger.Component
import itis.ru.justtalk.di.module.*
import itis.ru.justtalk.ui.editinfo.EditProfileInfoFragment
import itis.ru.justtalk.ui.login.LoginFragment
import itis.ru.justtalk.ui.main.MainActivity
import itis.ru.justtalk.ui.messages.ChatWithUserFragment
import itis.ru.justtalk.ui.messages.ContactsFragment
import itis.ru.justtalk.ui.myprofile.MyProfileFragment
import itis.ru.justtalk.ui.people.PeopleFragment
import itis.ru.justtalk.ui.people.UserDetailsFragment
import itis.ru.justtalk.ui.words.groups.CreateGroupActivity
import itis.ru.justtalk.ui.words.groups.GroupsFragment
import itis.ru.justtalk.ui.words.test.EndTestFragment
import itis.ru.justtalk.ui.words.test.TestFragment
import itis.ru.justtalk.ui.words.words.AddWordActivity
import itis.ru.justtalk.ui.words.words.WordsFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, ViewModelFactoryModule::class, ViewModelModule::class,
        RepoModule::class, AuthModule::class, DatabaseModule::class, NetModule::class]
)
interface AppComponent {
    fun inject(fragment: LoginFragment)
    fun inject(fragment: MyProfileFragment)
    fun inject(fragment: EditProfileInfoFragment)
    fun inject(fragment: PeopleFragment)
    fun inject(fragment: UserDetailsFragment)
    fun inject(fragment: ChatWithUserFragment)
    fun inject(fragment: ContactsFragment)
    fun inject(fragment: GroupsFragment)
    fun inject(fragment: WordsFragment)
    fun inject(fragment: TestFragment)
    fun inject(fragment: EndTestFragment)
    fun inject(activity: MainActivity)
    fun inject(activity: CreateGroupActivity)
    fun inject(activity: AddWordActivity)
}
