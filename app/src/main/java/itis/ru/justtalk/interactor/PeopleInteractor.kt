package itis.ru.justtalk.interactor

import com.google.firebase.firestore.GeoPoint
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import itis.ru.justtalk.models.User
import itis.ru.justtalk.repository.UserRepository
import javax.inject.Inject

class PeopleInteractor @Inject constructor(
    private val userRepository: UserRepository
) {
    fun getUsers(userLocation: GeoPoint, limit: Long): Single<List<User>> {
        return userRepository.getUsers(userLocation,limit)
            .subscribeOn(Schedulers.io())
    }
}