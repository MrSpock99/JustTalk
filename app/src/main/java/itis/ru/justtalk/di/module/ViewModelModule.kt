package itis.ru.justtalk.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import itis.ru.justtalk.ui.editinfo.EditProfileInfoViewModel
import itis.ru.justtalk.ui.login.LoginViewModel
import itis.ru.justtalk.ui.main.MainViewModel
import itis.ru.justtalk.ui.messages.ChatWithUserViewModel
import itis.ru.justtalk.ui.messages.ContactsViewModel
import itis.ru.justtalk.ui.myprofile.MyProfileViewModel
import itis.ru.justtalk.ui.people.PeopleViewModel
import itis.ru.justtalk.ui.people.UserDetailsViewModel
import itis.ru.justtalk.ui.words.PhotoChooseViewModel
import itis.ru.justtalk.ui.words.groups.CreateGroupViewModel
import itis.ru.justtalk.ui.words.groups.GroupsViewModel
import itis.ru.justtalk.ui.words.test.EndTestViewModel
import itis.ru.justtalk.ui.words.test.TestViewModel
import itis.ru.justtalk.ui.words.words.AddWordViewModel
import itis.ru.justtalk.ui.words.words.WordsViewModel
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyProfileViewModel::class)
    abstract fun bindMyProfileViewModel(myProfileViewModel: MyProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileInfoViewModel::class)
    abstract fun bindEditProfileViewModel(editProfileViewModel: EditProfileInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PeopleViewModel::class)
    abstract fun bindPeopleViewModel(peopleViewModel: PeopleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailsViewModel::class)
    abstract fun bindUserDetailsViewModel(userDetailsViewModel: UserDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatWithUserViewModel::class)
    abstract fun bindChatWithUserViewModel(chatWithUserViewModel: ChatWithUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactsViewModel::class)
    abstract fun bindContactsViewModel(contactsViewModel: ContactsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupsViewModel::class)
    abstract fun bindWordsViewModel(groupsViewModel: GroupsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateGroupViewModel::class)
    abstract fun bindCreateGroupViewModel(createGroupViewModel: CreateGroupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WordsViewModel::class)
    abstract fun bindWordsDetailsViewModel(wordsViewModel: WordsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddWordViewModel::class)
    abstract fun bindAddWordViewModel(addWordViewModel: AddWordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestViewModel::class)
    abstract fun bindTestViewModel(testViewModel: TestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EndTestViewModel::class)
    abstract fun bindEndTestViewModel(endTestViewModel: EndTestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PhotoChooseViewModel::class)
    abstract fun bindPhotoChooseViewModel(photoChooseViewModel: PhotoChooseViewModel): ViewModel
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
