package itis.ru.justtalk.models

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class User(
    val name: String,
    val age: Int,
    val gender: String,
    @get:PropertyName("avatar_url")
    @set:PropertyName("avatar_url")
    var avatarUrl: String,
    @get:PropertyName("photo_urls")
    @set:PropertyName("photo_urls")
    var photosUrls: List<String>,
    @get:PropertyName("about_me")
    @set:PropertyName("about_me")
    var aboutMe: String
) : Serializable {
    constructor() : this("", 0, "", "", arrayListOf("", "", "", "", ""), "")

    companion object {
        const val GENDER_MAN = "man"
        const val GENDER_WOMAN = "woman"
    }
}
