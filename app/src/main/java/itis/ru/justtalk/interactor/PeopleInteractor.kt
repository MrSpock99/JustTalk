package itis.ru.justtalk.interactor

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.models.user.User
import itis.ru.justtalk.repository.UserRepository
import javax.inject.Inject

class PeopleInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) {
    fun getUsers(
        myChats: Map<String, Boolean>,
        userLocation: GeoPoint,
        limit: Long
    ): Single<List<User>> {
        return userRepository.getUsers(userLocation, limit)
            .subscribeOn(Schedulers.io())
            /*.map { remoteUserList ->
                Log.d("MAP","WAIT WHAT")
                val list = mutableListOf<RemoteUser>()
                remoteUserList.forEach { user ->
                    *//*if (user.chats.isEmpty()) {
                        list.add(user)
                    }*//*
                    Log.d("USER_GET",user.toString())
                    user.chats.keys.forEach { key ->
                        if (!myChats.containsKey(key)) {
                            list.add(user)
                        }
                    }
                }
                list
            }*/
            /* .map { remoteUserList ->
                 val list = mutableListOf<RemoteUser>()
                 remoteUserList.forEach {
                     if (it.uid != firebaseAuth.currentUser?.email) {
                         list.add(it)
                     }
                 }
                 list
             }*/
            .map { remoteUserList ->
                val list = mutableListOf<User>()
                remoteUserList.forEach {
                    list.add(
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
                    )
                }
                list
            }
    }
}
