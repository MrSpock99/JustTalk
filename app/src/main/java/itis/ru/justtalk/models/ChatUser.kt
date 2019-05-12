package itis.ru.justtalk.models

import com.google.firebase.firestore.PropertyName

data class ChatUser(
    var uid: String,
    val name: String,
    val chats: MutableMap<String, Boolean>,
    @get:PropertyName("avatar_url")
    @set:PropertyName("avatar_url")
    var avatarUrl: String,
    var lastMessage: String
) {
    constructor() : this("", "", mutableMapOf(), "", "")
}
