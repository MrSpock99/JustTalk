package itis.ru.justtalk.models.user

data class ChatUser(
    var uid: String,
    val name: String,
    val chats: MutableMap<String, Boolean>,
    var avatarUrl: String,
    var lastMessage: String
)
