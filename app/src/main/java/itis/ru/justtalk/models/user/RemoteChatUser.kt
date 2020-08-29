package itis.ru.justtalk.models.user

import com.google.firebase.firestore.PropertyName

data class RemoteChatUser(
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
