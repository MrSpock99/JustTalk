package itis.ru.justtalk.models

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.RawValue

data class RemoteUser(
    val uid: String,
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

    @get:PropertyName("learning_language")
    @set:PropertyName("learning_language")
    var learningLanguage: String,

    @get:PropertyName("learning_language_level")
    @set:PropertyName("learning_language_level")
    var learningLanguageLevel: String,

    @get:PropertyName("speaking_language")
    @set:PropertyName("speaking_language")
    var speakingLanguage: String,

    @get:PropertyName("speaking_language_level")
    @set:PropertyName("speaking_language_level")
    var speakingLanguageLevel: String,

    var location: @RawValue GeoPoint
) {
    constructor() : this(
        "",
        "", 0, "", "", arrayListOf("", "", "", "", ""),
        "", "", "", "", "",
        GeoPoint(0.0, 0.0)
    )
}
