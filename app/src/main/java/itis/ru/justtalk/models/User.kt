package itis.ru.justtalk.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class User(
    val name: String,
    var age: Int,
    var gender: String,
    @get:PropertyName("avatar_url")
    @set:PropertyName("avatar_url")
    var avatarUrl: String,
    @get:PropertyName("photo_urls")
    @set:PropertyName("photo_urls")
    var photosUrls: List<String>,
    @get:PropertyName("about_me")
    @set:PropertyName("about_me")
    var aboutMe: String,
    var location: @RawValue GeoPoint
) : Parcelable {

    constructor() : this("", 0, "", "", arrayListOf("", "", "", "", ""),
        "", GeoPoint(0.0, 0.0))

    constructor(parcel: Parcel): this("", 0, "", "", arrayListOf("", "", "", "", ""),
    "", GeoPoint(parcel.readDouble(), parcel.readDouble()))

    companion object : Parceler<User> {
        const val GENDER_MAN = "man"
        const val GENDER_WOMAN = "woman"
        override fun User.write(parcel: Parcel, flags: Int) {
            parcel.writeDouble(location.latitude)
            parcel.writeDouble(location.longitude)
        }

        override fun create(parcel: Parcel): User = User(parcel)
    }
}
