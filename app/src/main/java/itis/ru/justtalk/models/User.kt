package itis.ru.justtalk.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class User(
    val uid: String,
    val name: String,
    var age: Int,
    var gender: String, var avatarUrl: String,
    var photosUrls: List<String>,
    var aboutMe: String,
    var learningLanguage: String,
    var learningLanguageLevel: String,
    var speakingLanguage: String,
    var speakingLanguageLevel: String,
    var location: @RawValue GeoPoint,
    var chats: Map<String, Boolean>
) : Parcelable {

    constructor(parcel: Parcel): this("","", 0, "", "", arrayListOf("", "", "", "", ""),
        "","","","","",
        GeoPoint(parcel.readDouble(), parcel.readDouble()), mutableMapOf<String,Boolean>())

    companion object : Parceler<User> {
        override fun User.write(parcel: Parcel, flags: Int) {
            parcel.writeDouble(location.latitude)
            parcel.writeDouble(location.longitude)
        }

        override fun create(parcel: Parcel): User = User(parcel)
    }
}
