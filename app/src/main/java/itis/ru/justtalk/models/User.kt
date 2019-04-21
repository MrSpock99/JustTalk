package itis.ru.justtalk.models

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

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
    val location: GeoPoint
) : Serializable {

    constructor() : this("", 0, "", "", arrayListOf("", "", "", "", ""),
        "", GeoPoint(0.0, 0.0))

    companion object {
        const val GENDER_MAN = "man"
        const val GENDER_WOMAN = "woman"
    }
}
