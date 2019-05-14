package itis.ru.justtalk.interactor

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.models.user.User
import itis.ru.justtalk.repository.UserRepository
import javax.inject.Inject

class MyProfileInteractor @Inject constructor(
    private val userRepository: UserRepository
) {
    fun getUserInfo(firebaseUser: FirebaseUser): Single<User> {
        return userRepository.getUserFromDb(firebaseUser)
            .subscribeOn(Schedulers.io())
            .map {
                User(
                    it.uid,
                    it.name,
                    it.age,
                    it.gender,
                    it.avatarUrl,
                    it.photosUrls,
                    it.aboutMe,
                    it.learningLanguage,
                    it.learningLanguageLevel,
                    it.speakingLanguage,
                    it.speakingLanguageLevel,
                    it.location,
                    it.chats
                )
            }
    }

    fun getMyProfile(): Single<User> {
        return userRepository.getMyProfile()
            .subscribeOn(Schedulers.io())
            .map {
                User(
                    it.uid,
                    it.name,
                    it.age,
                    it.gender,
                    it.avatarUrl,
                    it.photosUrls,
                    it.aboutMe,
                    it.learningLanguage,
                    it.learningLanguageLevel,
                    it.speakingLanguage,
                    it.speakingLanguageLevel,
                    it.location,
                    it.chats
                )
            }
    }

    fun getMyUid(): Single<String> {
        return userRepository.getMyProfile()
            .subscribeOn(Schedulers.io())
            .map {
                it.uid
            }
    }

    fun getEmptyUser(): Single<User> {
        return userRepository.getEmptyUser()
            .subscribeOn(Schedulers.io())
            .map {
                User(
                    it.uid,
                    it.name,
                    it.age,
                    it.gender,
                    it.avatarUrl,
                    it.photosUrls,
                    it.aboutMe,
                    it.learningLanguage,
                    it.learningLanguageLevel,
                    it.speakingLanguage,
                    it.speakingLanguageLevel,
                    it.location,
                    it.chats
                )
            }
    }

    fun editUserInfo(user: User): Completable {
        return userRepository.addUserToDb(user)
    }
}
