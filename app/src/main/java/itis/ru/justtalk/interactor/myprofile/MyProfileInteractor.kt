package itis.ru.justtalk.interactor.myprofile

import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.models.User
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
                    it.location
                )
            }
    }

    fun getMyProfile(): Single<User> {
        return userRepository.getMyProfile()
            .subscribeOn(Schedulers.io())
            .map {
                User(
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
                    it.location
                )
            }
    }

    fun getEmptyUser(): Single<User> {
        return userRepository.getEmptyUser()
            .subscribeOn(Schedulers.io())
            .map {
                User(
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
                    it.location
                )
            }
    }

    fun editUserInfo(user: User): Completable {
        return userRepository.addUserToDb(user)
    }
}
