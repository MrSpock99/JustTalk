package itis.ru.justtalk.ui.people

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.GeoPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.PeopleInteractor
import itis.ru.justtalk.models.User
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.ClickEvent
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class PeopleViewModel @Inject constructor(
    private val interactor: PeopleInteractor,
    private val locationProviderClient: FusedLocationProviderClient
) : BaseViewModel() {
    val usersLiveData = MutableLiveData<Response<List<User>>>()
    val myLocationLiveData = MutableLiveData<GeoPoint>()
    val navigateToChat = MutableLiveData<ClickEvent<User?>>()
    val navigateToUserDetails = MutableLiveData<ClickEvent<User?>>()

    @SuppressLint("MissingPermission")
    fun getUsersNearby() {
        locationProviderClient.lastLocation.addOnCompleteListener {
            val currentLocation = it.result as Location
            val locationGeoPoint = GeoPoint(currentLocation.latitude, currentLocation.longitude)
            myLocationLiveData.value = locationGeoPoint
            getUsers(locationGeoPoint, 5)
        }
    }

    private fun getUsers(userLocation: GeoPoint, limit: Long) {
        showLoadingLiveData.value = true
        disposables.add(
            interactor.getUsers(userLocation, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    showLoadingLiveData.value = false
                }
                .subscribe({
                    usersLiveData.value = Response.success(it)
                }, {
                    usersLiveData.value = Response.error(it)
                    it.printStackTrace()
                })
        )
    }

    fun onMessageClick(index: Int) {
        navigateToChat.value = ClickEvent(usersLiveData.value?.data?.get(index))
    }

    fun onUserClicked(user: User) {
        navigateToUserDetails.value = ClickEvent(user)
    }
}
