package itis.ru.justtalk.utils

import android.content.res.Resources
import android.graphics.Point
import android.location.Location
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
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

fun getDisplaySize(windowManager: WindowManager): Point {
    return try {
        if (Build.VERSION.SDK_INT > 16) {
            val display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            Point(displayMetrics.widthPixels, displayMetrics.heightPixels)
        } else {
            Point(0, 0)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Point(0, 0)
    }
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}
