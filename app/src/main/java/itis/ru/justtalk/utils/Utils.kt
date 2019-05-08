package itis.ru.justtalk.utils

import android.location.Location
import com.google.firebase.firestore.GeoPoint
import kotlin.math.roundToInt

fun getDistanceFromLocation(geoPoint: GeoPoint, myLocation: GeoPoint): String {
    val pointA = Location("").apply {
        latitude = geoPoint.latitude
        longitude = geoPoint.longitude
    }
    val pointB = Location("").apply {
        latitude = myLocation.latitude
        longitude = myLocation.longitude
    }

    val distanceInMeters = pointB.distanceTo(pointA)

    return if (distanceInMeters < 1000) {
        "${distanceInMeters.roundToInt()} m"
    } else {
        "${(distanceInMeters / 1000).roundToInt()} km"
    }
}